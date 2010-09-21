package au.org.theark.study.web.component.study;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.vo.ModuleVO;
import au.org.theark.study.model.entity.Study;
import au.org.theark.study.model.entity.StudyStatus;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.service.IUserService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.form.ModuleVo;
import au.org.theark.study.web.form.SearchStudyForm;

@SuppressWarnings("serial")
public class Search extends Panel {
	
	/**
	 * Constructor
	 * @param id
	 */
	public Search(String id) {
		super(id);
	}
	
	private FeedbackPanel feedBackPanel;
	
	private SearchResultList searchResults;
	private PageableListView<Study> listView;
	private Details detailsPanel;
	private CompoundPropertyModel<StudyModel> cpm;
	private WebMarkupContainer listContainer;
	private WebMarkupContainer detailsContainer;
	private IModel<Object> iModel;

	@SpringBean( name = Constants.STUDY_SERVICE)
	private IStudyService studyService;
	
	@SpringBean( name = "userService")
	private IUserService userService;
	
	private void initialiseSearchResults(){
		
		//searchResults = new SearchResultList("searchResults",detailsPanel);
		searchResults = new SearchResultList("searchResults",detailsContainer,this);
		
		//Set the Model reference into the results panel
		searchResults.setCpm(cpm);
		
		//Build a Pageable List
		listView  = searchResults.buildPageableListView(iModel, listContainer);
		listView.setReuseItems(true);
		PagingNavigator pageNavigator = new PagingNavigator("navigator", listView);
		searchResults.add(pageNavigator);
		searchResults.add(listView);
		listContainer.add(searchResults);
		
	}
	
	public void processSearch(AjaxRequestTarget target){
		listContainer.setVisible(true);
		target.addComponent(listContainer);
	}
	
	public void processDetail(AjaxRequestTarget target, int mode){
		
		detailsPanel.getStudyForm().getStudyNameTxtFld().setEnabled(true);
		detailsPanel.setCpm(cpm);
		detailsContainer.setVisible(true);
		listContainer.setVisible(false);
		target.addComponent(detailsContainer);
		target.addComponent(listContainer);
	}
	
	
	public void initialise(String id){
		 feedBackPanel= new FeedbackPanel("feedbackMessage");
		 feedBackPanel.setOutputMarkupId(true);
		//The Model is defined here
		cpm = new CompoundPropertyModel<StudyModel>(new StudyModel());
		//The wrapper for ResultsList panel that will contain a ListView
		listContainer = new WebMarkupContainer("resultListContainer");
		listContainer.setOutputMarkupPlaceholderTag(true);
		listContainer.setVisible(true);
		
		
		detailsContainer = new WebMarkupContainer("detailsContainer");
		detailsContainer.setOutputMarkupPlaceholderTag(true);
		detailsContainer.setVisible(false);
		
		//Initialise the Details Panel	
		detailsPanel = new Details("detailsPanel", listContainer,feedBackPanel,detailsContainer);
		detailsPanel.setCpm(cpm);
		detailsPanel.initialiseForm();
		detailsContainer.add(detailsPanel);
		iModel = new LoadableDetachableModel<Object>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected Object load() {
				return cpm.getObject().getStudyList();
			}
		};
		
		//Create the Search Results Panel
		initialiseSearchResults();
		
		List<StudyStatus> studyStatusList  = studyService.getListOfStudyStatus();
		
		//Instantiate the Form and pass in the reference to the model
		SearchStudyForm searchForm = new SearchStudyForm(Constants.SEARCH_FORM, cpm, studyStatusList){
			
			/*When user has clicked on the Search Button*/
			protected  void onSearch(AjaxRequestTarget target){
				
				List<Study> resultList = studyService.getStudy(cpm.getObject().getStudy());
				if(resultList != null && resultList.size() == 0){
					this.info("Study with the specified criteria does not exist in the system.");	
				}
				
				this.setModelObject(new StudyModel());
				cpm = (CompoundPropertyModel<StudyModel>)this.getModel();////reset the original one
				cpm.getObject().setStudyList(resultList);//Place the results into the model
				listView.removeAll();
				listContainer.setVisible(true);//Make the WebMarkupContainer that houses the search results visible
				target.addComponent(listContainer);//For ajax this is required so it knows which element on the page must be refreshed/repainted.
			}
			
			protected void onNew(AjaxRequestTarget target){
				
				this.setModelObject(new StudyModel());
				cpm = (CompoundPropertyModel<StudyModel>)this.getModel();//reset the original one.Turn it on if its really needed
				
				List<ModuleVO> modules;
				List<ModuleVo> moduleVoList = new ArrayList<ModuleVo>();
				try {
					modules = userService.getModules(true);//source this from a static list or on application startup 
					for (ModuleVO moduleVO : modules) {
						ModuleVo moduleVo = new ModuleVo();
						moduleVo.setModuleName(moduleVO.getModule());
						moduleVoList.add(moduleVo);
					}
				} catch (ArkSystemException e) {
					//log the error message and notify sys admin to take appropriate action
					this.error("A system error has occured. Please try after some time.");
				}
				cpm.getObject().setModulesAvailable(moduleVoList);
				detailsPanel.setCpm(cpm);
				//If the selected side has items then its re-using the first object
				processDetail(target, Constants.MODE_NEW);
			}
			
		};

		//Add the Form to the Panel. The Form object that will contain the child or UI components that will be part of the search or be affected by the search.
		searchForm.add(listContainer);
		searchForm.add(detailsContainer);
		add(searchForm);
		add(feedBackPanel);
	}

}
