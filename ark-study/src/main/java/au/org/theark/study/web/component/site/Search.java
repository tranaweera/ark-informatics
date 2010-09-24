package au.org.theark.study.web.component.site;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.study.model.entity.Person;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.site.form.ContainerForm;
import au.org.theark.study.web.component.site.form.SearchSiteForm;
public class Search extends Panel{

	private FeedbackPanel fbPanel;
	private WebMarkupContainer searchMarkupContainer;
	
	private CompoundPropertyModel<SiteModel> cpm;
	private PageableListView<SiteVo> listView;
	
	
	//The container to wrap the Search Result List
	private WebMarkupContainer listContainer;
	//The Container to wrap the details panel
	private WebMarkupContainer detailsContainer;
	private Details detailsPanel;
	private ContainerForm containerForm;
	
	@SpringBean( name = Constants.STUDY_SERVICE)
	private IStudyService studyService;
	
	
	
	/*Constructor*/
	public Search(String id, FeedbackPanel feedBackPanel, WebMarkupContainer searchMarkupContainer,PageableListView<SiteVo> listView,  
						 WebMarkupContainer resultListContainer, WebMarkupContainer detailPanelContainer, ContainerForm siteContainerForm) {
		super(id);
		this.searchMarkupContainer =  searchMarkupContainer;
		this.listView = listView;
		fbPanel = feedBackPanel;
		listContainer = resultListContainer;
		detailsContainer = detailPanelContainer;
		containerForm = siteContainerForm;
	}
	
	
	public void initialisePanel(CompoundPropertyModel<SiteModel> siteModelCpm){
		
		
		//Get the study id from the session and get the study
		Long sessionStudyId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		List<Person> availablePersons = new ArrayList<Person>();
		
		SearchSiteForm searchSiteForm = new SearchSiteForm(Constants.SEARCH_FORM, siteModelCpm,availablePersons){
			
			protected  void onSearch(AjaxRequestTarget target){
				
				//Refresh the FB panel if there was an old message from previous search result
				target.addComponent(fbPanel);
				List<SiteVo> resultList = studyService.getSite(containerForm.getModelObject().getSiteVo());
				if(resultList != null && resultList.size() == 0){
					this.info("Site with the specified criteria does not exist in the system.");
					target.addComponent(fbPanel);
				}else{
					containerForm.getModelObject().setSiteVoList(resultList);
					listView.removeAll();
					listContainer.setVisible(true);//Make the WebMarkupContainer that houses the search results visible
					target.addComponent(listContainer);//For ajax this is required so 
				}
			}
			
			protected void onNew(AjaxRequestTarget target){
				// Show the details panel name and description
				SiteModel siteModel = new SiteModel();
				siteModel.setMode(Constants.MODE_NEW);
				containerForm.setModelObject(siteModel);
				processDetail(target, Constants.MODE_NEW);
			}
		};
		
		add(searchSiteForm);
	}
	
	public void processDetail(AjaxRequestTarget target, int mode){
		
		detailsContainer.setVisible(true);
		listContainer.setVisible(false);
		searchMarkupContainer.setVisible(false);
		target.addComponent(detailsContainer);
		target.addComponent(listContainer);
		target.addComponent(searchMarkupContainer);
	}

}
