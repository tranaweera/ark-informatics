package au.org.theark.study.web.component.manageuser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.model.study.entity.ArkModuleRole;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.core.web.component.AbstractContainerPanel;
import au.org.theark.study.service.IUserService;
import au.org.theark.study.web.component.manageuser.form.ContainerForm;

@SuppressWarnings("serial")
public class UserContainerPanel extends AbstractContainerPanel<ArkUserVO>{


	/** 
	 * Private members
	 **/
	
	private ContainerForm containerForm;
	private DetailPanel detailsPanel;
	private SearchPanel searchPanel;
	private SearchResultListPanel searchResultListPanel;
	private PageableListView<ArkUserVO> pageableListView;	
	
	/* Spring Beans to Access Service Layer */
	
	@SpringBean( name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;
	
	@SpringBean( name = "userService")
	private IUserService userService;
	
	
	
	public UserContainerPanel(String id) {

		super(id, true);
		
		cpModel = new CompoundPropertyModel<ArkUserVO>(new ArkUserVO());
		containerForm = new ContainerForm("containerForm", cpModel);
		initCrudContainerVO();//THe CRUD Container VO that will have all the WebMarkupContainers.This will be passed around to various panels
		containerForm.add(initialiseFeedBackPanel());
		containerForm.add(initialiseDetailPanel());
		containerForm.add(initialiseSearchResults());
		containerForm.add(initialiseSearchPanel());
		
		add(containerForm);
	}

	@Override
	protected WebMarkupContainer initialiseSearchResults() {
		ArkUserVO arkUserVO = new ArkUserVO();
		Long sessionStudyId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		SearchResultListPanel searchResultListPanel = new SearchResultListPanel("searchResults", arkCrudContainerVO, containerForm);
		
		Collection<ArkModuleRole> listOfModulRoles = iArkCommonService.getArkModuleAndLinkedRoles();
		List<ArkUserVO> userResultList = new ArrayList<ArkUserVO>();
		
		try{
			if(sessionStudyId != null && sessionStudyId > 0){
				 userResultList = userService.searchUser(arkUserVO);	
			}
		}catch(ArkSystemException arkException){
					
		}
		
		containerForm.getModelObject().setUserList(userResultList);
	
		iModel = new LoadableDetachableModel<Object>() {
			private static final long serialVersionUID = 1L;
			@Override
			protected Object load() {
				List<ArkUserVO> arkUserList = new ArrayList<ArkUserVO>();
				
				try {
					pageableListView.removeAll();
					arkUserList =  userService.searchUser(containerForm.getModelObject());
				} catch (ArkSystemException e) {
					feedBackPanel.error("A System Error has occured. Please contact support.");
				}
				return arkUserList;
			}
		};
	
		pageableListView = searchResultListPanel.buildPageableListView(iModel, arkCrudContainerVO.getSearchResultPanelContainer());
		pageableListView.setReuseItems(true);
		PagingNavigator pageNavigator = new PagingNavigator("navigator",pageableListView);
		searchResultListPanel.add(pageNavigator);
		searchResultListPanel.add(pageableListView);
		arkCrudContainerVO.getSearchResultPanelContainer().add(searchResultListPanel);
		return arkCrudContainerVO.getSearchResultPanelContainer();
	}

	@Override
	protected WebMarkupContainer initialiseDetailPanel() {
		detailsPanel = new DetailPanel("detailsPanel",feedBackPanel,arkCrudContainerVO,containerForm);
		detailsPanel.initialisePanel();
		arkCrudContainerVO.getDetailPanelContainer().add(detailsPanel);
		return arkCrudContainerVO.getDetailPanelContainer();
	}

	@Override
	protected WebMarkupContainer initialiseSearchPanel() {
		
		ArkUserVO arkUserVO = new ArkUserVO();
		Long sessionStudyId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		List<ArkUserVO> userResultList = new ArrayList<ArkUserVO>();
		
		try{
			if(sessionStudyId != null && sessionStudyId > 0){
				 userResultList = userService.searchUser(arkUserVO);	
			}
		}catch(ArkSystemException arkException){
					
		}
		
		containerForm.getModelObject().setUserList(userResultList);
		searchPanel = new SearchPanel("searchPanel",arkCrudContainerVO,feedBackPanel,containerForm, pageableListView);
		searchPanel.initialisePanel(cpModel);
		arkCrudContainerVO.getSearchPanelContainer().add(searchPanel);
		return arkCrudContainerVO.getSearchPanelContainer();
	}

}
