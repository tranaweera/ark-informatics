/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.study.web.component.consent;

import java.text.SimpleDateFormat;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.model.study.entity.Consent;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.consent.form.ContainerForm;
import au.org.theark.study.web.component.consent.form.FormHelper;


/**
 * @author nivedann
 *
 */
public class SearchResultListPanel extends Panel{

	@SpringBean( name = Constants.STUDY_SERVICE)
	protected IStudyService studyService;
	private WebMarkupContainer detailPanelContainer;
	private WebMarkupContainer detailPanelFormContainer;
	private WebMarkupContainer searchPanelContainer;
	private WebMarkupContainer searchResultContainer;
	private WebMarkupContainer viewButtonContainer;
	private WebMarkupContainer editButtonContainer;
	private ContainerForm containerForm;
	
	/**
	 * @param id
	 */
	public SearchResultListPanel(String id, 
			WebMarkupContainer  detailPanelContainer,
			WebMarkupContainer  detailPanelFormContainer, 
			WebMarkupContainer searchPanelContainer,
			WebMarkupContainer searchResultContainer,
			WebMarkupContainer viewButtonContainer,
			WebMarkupContainer editButtonContainer,
			ContainerForm containerForm) {
		
		super(id);
	 	this.detailPanelContainer = detailPanelContainer;
		this.searchPanelContainer = searchPanelContainer;
		this.searchResultContainer = searchResultContainer;
		this.viewButtonContainer = viewButtonContainer;
		this.editButtonContainer = editButtonContainer;
		this.detailPanelFormContainer = detailPanelFormContainer;
		this.containerForm = containerForm;
	}
	
	public PageableListView<Consent> buildPageableListView(IModel iModel){
		
		PageableListView<Consent> pageableListView = new PageableListView<Consent>(Constants.CONSENT_LIST,iModel,5) {

			@Override
			protected void populateItem(final ListItem<Consent> item) {
				Consent consent = item.getModelObject();
				
				item.add(buildLink(consent));
				if(consent.getStudyComponentStatus() != null){
					item.add(new Label("studyComponentStatus.name", consent.getStudyComponentStatus().getName()));
				}else{
					item.add(new Label("studyComponentStatus.name", " "));
				}
				
				if(consent.getConsentStatus() != null){
					item.add(new Label("consentStatus.name",consent.getConsentStatus().getName()));
				}else{
					item.add(new Label("consentStatus.name"," "));
				}
				
				if(consent.getConsentType() != null){
					item.add( new Label("consentType.name", consent.getConsentType().getName()));
				}else{
					item.add( new Label("consentType.name", ""));
				}
				
				if(consent.getConsentedBy() != null){
					item.add( new Label("consentedBy", consent.getConsentedBy()));
				}else{
					item.add( new Label("consentedBy",""));
				}
				
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
				String consentDate ="";
				
				if(consent.getConsentDate() != null){
					item.add( new Label("consentDate", simpleDateFormat.format(consent.getConsentDate())));
				}else{
					item.add( new Label("consentDate",consentDate));
				}
				
				item.add(new AttributeModifier("class", true, new AbstractReadOnlyModel() {
					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));
			}
		};
		return pageableListView;
		
	}
	
	private AjaxLink buildLink(final Consent consent){
		
		AjaxLink link = new AjaxLink("studyComp.name") {

			@Override
			public void onClick(AjaxRequestTarget target) {
				Long id  = consent.getId();
				
				try {
					Consent consentFromBackend = studyService.getConsent(id);
					containerForm.getModelObject().setConsent(consentFromBackend);
					// Add consentId into context (for use with consentFile(s))
					SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_CONSENT_ID, consentFromBackend.getId()); 
					
					
					WebMarkupContainer wmcPlain  = (WebMarkupContainer) detailPanelFormContainer.get(Constants.WMC_PLAIN);
					WebMarkupContainer wmcRequested  = (WebMarkupContainer) detailPanelFormContainer.get(Constants.WMC_REQUESTED);
					WebMarkupContainer wmcRecieved  = (WebMarkupContainer) detailPanelFormContainer.get(Constants.WMC_RECIEVED);
					WebMarkupContainer wmcCompleted  = (WebMarkupContainer) detailPanelFormContainer.get(Constants.WMC_COMPLETED);
					
					
					new FormHelper().updateStudyCompStatusDates(target, consentFromBackend.getStudyComponentStatus().getName(), wmcPlain, wmcRequested, wmcRecieved, wmcCompleted);
					
					detailPanelContainer.setVisible(true);
					viewButtonContainer.setVisible(true);
					viewButtonContainer.setEnabled(true);
					detailPanelFormContainer.setEnabled(false);
					searchResultContainer.setVisible(false);
					searchPanelContainer.setVisible(false);
					editButtonContainer.setVisible(false);
					
					target.addComponent(searchResultContainer);
					target.addComponent(detailPanelContainer);
					target.addComponent(detailPanelFormContainer);
					target.addComponent(searchPanelContainer);
					target.addComponent(viewButtonContainer);
					target.addComponent(editButtonContainer);
					
				} catch (ArkSystemException e) {
					containerForm.error("A System Error has occured please contact Support");
				}
			}
		};
		Label nameLinkLabel = new Label(Constants.CONSENT_COMPONENT_LABEL,consent.getStudyComp().getName());
		link.add(nameLinkLabel);
		return link;
	}
	

}
