package au.org.theark.study.web.component.site;

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

import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.site.form.ContainerForm;


@SuppressWarnings("serial")
public class SearchResultList extends Panel{
	
	
	private WebMarkupContainer detailPanelContainer;
	private WebMarkupContainer searchPanelContainer;
	private ContainerForm containerForm;
	
	public SearchResultList(String id, WebMarkupContainer  detailPanelContainer, WebMarkupContainer searchPanelContainer, ContainerForm siteContainerForm){
		super(id);
		this.detailPanelContainer = detailPanelContainer;
		this.searchPanelContainer = searchPanelContainer;
		this.containerForm = siteContainerForm;
	}

	
	
	public PageableListView<SiteVo> buildPageableListView(IModel iModel){
		
		PageableListView<SiteVo> sitePageableListView = new PageableListView<SiteVo>("siteVoList", iModel, 10) {
			@Override
			protected void populateItem(final ListItem<SiteVo> item) {
				
				SiteVo site = item.getModelObject();

				item.add(buildLink(site));
				
				if(site.getSiteDescription() != null){
					item.add(new Label("siteDescription", site.getSiteDescription()));//the ID here must match the ones in mark-up	
				}else{
					item.add(new Label("siteDescription", ""));//the ID here must match the ones in mark-up
				}

				item.add(new AttributeModifier("class", true, new AbstractReadOnlyModel() {
					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));
				
			}
		};
		return sitePageableListView;
	}
	
	
	@SuppressWarnings({ "unchecked", "serial" })
	private AjaxLink buildLink(final SiteVo site) {
		
		AjaxLink link = new AjaxLink("siteVo.siteName") {

			@Override
			public void onClick(AjaxRequestTarget target) {

				containerForm.getModelObject().setMode(Constants.MODE_EDIT);
				containerForm.getModelObject().setSiteVo(site);
				detailPanelContainer.setVisible(true);
				searchPanelContainer.setVisible(false);
		
				target.addComponent(detailPanelContainer);
				target.addComponent(searchPanelContainer);
			}
		};
		
		//Add the label for the link
		Label nameLinkLabel = new Label("siteNameLbl", site.getSiteName());
		link.add(nameLinkLabel);
		return link;

	}
	

}
