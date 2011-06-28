
package au.org.theark.lims.web.component.subject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.SubjectVO;
import au.org.theark.core.web.component.AbstractContainerPanel;
import au.org.theark.core.web.component.ArkDataProvider;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.service.ILimsService;
import au.org.theark.lims.web.Constants;
import au.org.theark.lims.web.component.subject.bioCollection.ListDetailPanel;
import au.org.theark.lims.web.component.subject.bioCollection.form.ListDetailForm;
import au.org.theark.lims.web.component.subject.form.ContainerForm;

/**
 * @author nivedann
 *
 */
@SuppressWarnings("unchecked")
public class SubjectContainerPanel extends AbstractContainerPanel<SubjectVO> 
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -2956968644138345497L;
	private SearchPanel searchPanel;
	private SearchResultListPanel searchResultsPanel;
	private DetailPanel detailsPanel;
	private PageableListView<SubjectVO> pageableListView;
	private ContainerForm containerForm;
	
	private WebMarkupContainer arkContextMarkup;
	
	@SpringBean( name =  au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;
	
	@SpringBean( name = Constants.LIMS_SERVICE)
	private ILimsService iLimsService;
	
	private DataView<SubjectVO> dataView;
	private ArkDataProvider<SubjectVO, IArkCommonService> subjectProvider;
	
	/**
	 * @param id
	 */
	public SubjectContainerPanel(String id,WebMarkupContainer arkContextMarkup) {
		
		super(id);
		this.arkContextMarkup = arkContextMarkup;
		/*Initialise the CPM */
		cpModel = new CompoundPropertyModel<SubjectVO>(new SubjectVO());
		containerForm = new ContainerForm("containerForm", cpModel);
		
		containerForm.add(initialiseFeedBackPanel());
		containerForm.add(initialiseDetailPanel());
		containerForm.add(initialiseSearchResults());
		containerForm.add(initialiseSearchPanel());
		
		prerenderContextCheck();

		add(containerForm);
	}
	
	protected void prerenderContextCheck() {		
		//Get the Person in Context and determine the Person Type
		Long sessionStudyId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Long sessionPersonId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);

		if ((sessionStudyId != null) && (sessionPersonId != null)) {
			String sessionPersonType = (String)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_TYPE);
			if (sessionPersonType.equals(au.org.theark.core.Constants.PERSON_CONTEXT_TYPE_SUBJECT))
			{
				Person person;
				boolean contextLoaded = false;
				try {
					person = iLimsService.getPerson(sessionPersonId);
					SubjectVO subjectVO = new SubjectVO();
					subjectVO.getLinkSubjectStudy().setPerson(person);	//must have Person id
					subjectVO.getLinkSubjectStudy().setStudy(iArkCommonService.getStudy(sessionStudyId));	//must have Study id
					List<SubjectVO> subjectList = (List<SubjectVO>) iArkCommonService.getSubject(subjectVO);
					containerForm.setModelObject(subjectList.get(0));
					contextLoaded = true;
				} catch (EntityNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ArkSystemException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if (contextLoaded) {
					// Set up BioCollections listDetail
					DetailPanel details = (DetailPanel) detailPanelContainer.get("detailsPanel");
					LimsVO limsVo = new LimsVO();
					limsVo.setLinkSubjectStudy(containerForm.getModelObject().getLinkSubjectStudy());
					limsVo.getBioCollection().setLinkSubjectStudy(containerForm.getModelObject().getLinkSubjectStudy());
					
					ListDetailPanel listDetailPanel = (ListDetailPanel) details.get("listDetailPanel");
					ListDetailForm listDetailForm = (ListDetailForm) listDetailPanel.get("listDetailForm");
					
					listDetailForm.setModelObject(limsVo);
					//listDetailForm.initialiseList();
					listDetailForm.initialiseForm();
					listDetailForm.setLinkSubjectStudy(containerForm.getModelObject().getLinkSubjectStudy());
					
					// Put into Detail View mode
					searchPanelContainer.setVisible(false);
					searchResultPanelContainer.setVisible(false);
					detailPanelContainer.setVisible(true);
					detailPanelFormContainer.setEnabled(false);
					viewButtonContainer.setVisible(true);
					editButtonContainer.setVisible(false);
				}
			}			
		}
	}
	
	protected WebMarkupContainer initialiseSearchPanel(){
		Long sessionStudyId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		if(sessionStudyId != null && sessionStudyId > 0){
			containerForm.getModelObject().getLinkSubjectStudy().setStudy(iArkCommonService.getStudy(sessionStudyId));
		}

		searchPanel = new SearchPanel("searchComponentPanel",
									feedBackPanel,
									searchPanelContainer,
									pageableListView,
									searchResultPanelContainer,
									detailPanelContainer,
									detailPanelFormContainer,
									viewButtonContainer,
									editButtonContainer,
									detailsPanel,
									containerForm);

		searchPanel.initialisePanel(cpModel);
		searchPanelContainer.add(searchPanel);
		return searchPanelContainer;
	}
	
	
	protected WebMarkupContainer initialiseDetailPanel(){
		
		detailsPanel = new DetailPanel("detailsPanel",feedBackPanel,searchResultPanelContainer,detailPanelContainer,detailPanelFormContainer,searchPanelContainer,viewButtonContainer,editButtonContainer,arkContextMarkup,containerForm);
		detailsPanel.initialisePanel();
		detailPanelContainer.add(detailsPanel);
		return detailPanelContainer;
	}
	
	protected WebMarkupContainer initialiseSearchResults(){
		
		searchResultsPanel = new SearchResultListPanel("searchResults",detailPanelContainer,detailPanelFormContainer,searchPanelContainer,searchResultPanelContainer,viewButtonContainer,editButtonContainer,arkContextMarkup,containerForm);
		
		// Restrict to subjects in current study in session
		Long sessionStudyId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);		
		if(sessionStudyId != null){
			Study study = iArkCommonService.getStudy(sessionStudyId);
			LinkSubjectStudy linkSubjectStudy = new LinkSubjectStudy();
			linkSubjectStudy.setStudy(study);
			containerForm.getModelObject().setLinkSubjectStudy(linkSubjectStudy);
		}
		
		// Data providor to paginate resultList
		subjectProvider = new ArkDataProvider<SubjectVO, IArkCommonService>(iArkCommonService) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 5965748714329690977L;


			public int size() {
				return service.getStudySubjectCount(compoundPropertyModel.getObject());
			}
			
			
			public Iterator<SubjectVO> iterator(int first, int count) {
				List<SubjectVO> listSubjects = new ArrayList<SubjectVO>();
				listSubjects = iArkCommonService.searchPageableSubjects(compoundPropertyModel.getObject(), first, count);
				return listSubjects.iterator();
			}
		};
		subjectProvider.setCompoundPropertyModel(this.cpModel);

		dataView = searchResultsPanel.buildDataView(subjectProvider);
		dataView.setItemsPerPage(au.org.theark.core.Constants.ROWS_PER_PAGE);
				
		PagingNavigator pageNavigator = new PagingNavigator("navigator", dataView);
		searchResultsPanel.add(pageNavigator);
		searchResultsPanel.add(dataView);
		searchResultPanelContainer.add(searchResultsPanel);
		return searchResultPanelContainer;
	}
	
	public void resetDataProvider()
	{
	}

}
