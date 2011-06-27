
package au.org.theark.study.web.component.address;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.Address;
import au.org.theark.core.vo.AddressVO;
import au.org.theark.core.web.component.AbstractContainerPanel;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.address.form.ContainerForm;



/**
 * @author nivedann
 *
 */
public class AddressContainerPanel extends  AbstractContainerPanel<AddressVO>{

	@SpringBean( name = Constants.STUDY_SERVICE)
	private IStudyService studyService;
	
	private ContainerForm containerForm;
	
	//Panels
	private SearchPanel searchPanel;
	private SearchResultListPanel searchResultListPanel;
	private DetailPanel detailPanel;
	private PageableListView<Address> pageableListView;
	
	/**
	 * @param id
	 */
	public AddressContainerPanel(String id) {
		super(id);
		cpModel = new CompoundPropertyModel<AddressVO>( new AddressVO());
		containerForm = new ContainerForm("containerForm",cpModel);
		containerForm.add(initialiseFeedBackPanel());
		containerForm.add(initialiseDetailPanel());
		containerForm.add(initialiseSearchResults());
		containerForm.add(initialiseSearchPanel());
		add(containerForm);
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.component.AbstractContainerPanel#initialiseDetailPanel()
	 */
	@Override
	protected WebMarkupContainer initialiseDetailPanel() {
		
		detailPanel = new DetailPanel("detailsPanel",
										feedBackPanel,
										searchResultPanelContainer, 
										detailPanelContainer,
										detailPanelFormContainer,
										searchPanelContainer,
										viewButtonContainer,
										editButtonContainer,
										containerForm);
		detailPanel.initialisePanel();
		detailPanelContainer.add(detailPanel);
		return detailPanelContainer;
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.component.AbstractContainerPanel#initialiseSearchPanel()
	 */
	@Override
	protected WebMarkupContainer initialiseSearchPanel() {
		Long sessionPersonId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
		String sessionPersonType = (String)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_TYPE);//Subject or Contact: Denotes if it was 
		//Set the person who this address should be associated with 
		Collection<Address> addressList = new ArrayList<Address>();
		
		try {
			if(sessionPersonId != null){
				containerForm.getModelObject().getAddress().setPerson(studyService.getPerson(sessionPersonId));
				addressList = studyService.getPersonAddressList(sessionPersonId, containerForm.getModelObject().getAddress());
			}

			cpModel.getObject().setAddresses(addressList);
			searchPanel = new SearchPanel("searchComponentPanel", 
					feedBackPanel,
					searchPanelContainer,
					pageableListView,
					searchResultPanelContainer,
					detailPanelContainer,
					detailPanel,
					containerForm,
					viewButtonContainer,
					editButtonContainer,
					detailPanelFormContainer);
			
			searchPanel.initialisePanel(cpModel);
			searchPanelContainer.add(searchPanel);
		} catch (EntityNotFoundException e) {
			//Report this to the user
		} catch (ArkSystemException e) {
			//Logged by the back end. Report this to the user
		}
		return searchPanelContainer;
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.component.AbstractContainerPanel#initialiseSearchResults()
	 */
	@Override
	protected WebMarkupContainer initialiseSearchResults() {
		
		SearchResultListPanel searchResultPanel = new SearchResultListPanel("searchResults",
					detailPanelContainer,
					detailPanelFormContainer,
					searchPanelContainer,
					searchResultPanelContainer,
					viewButtonContainer,
					editButtonContainer,
					containerForm);
		
		iModel = new LoadableDetachableModel<Object>() {
	
			private static final long serialVersionUID = 1L;
	
			@Override
			protected Object load() {
				//Get the PersonId from session and get the phoneList from backend
				Collection<Address> addressList = new ArrayList<Address>();
				Long sessionPersonId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
				try {
					if(isActionPermitted()){
						if(sessionPersonId != null){
							addressList = studyService.getPersonAddressList(sessionPersonId,containerForm.getModelObject().getAddress());
						}
					}
				} catch (EntityNotFoundException e) {
					containerForm.error("The specified Address is not found in the system.");
				} catch (ArkSystemException e) {
					containerForm.error("A System Exception has occured please contact support.");
				}
			pageableListView.removeAll();
			return addressList;
			}
		};
	
		pageableListView  = searchResultPanel.buildPageableListView(iModel);
		pageableListView.setReuseItems(true);
		PagingNavigator pageNavigator = new PagingNavigator("addressNavigator", pageableListView);
		searchResultPanel.add(pageNavigator);
		searchResultPanel.add(pageableListView);
		searchResultPanelContainer.add(searchResultPanel);
		return searchResultPanelContainer;
		
	}

}
