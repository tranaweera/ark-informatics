/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.study.web.component.subject;

import java.util.Collection;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.vo.SubjectVO;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.subject.form.ContainerForm;
import au.org.theark.study.web.component.subject.form.SearchForm;

/**
 * @author nivedann
 *
 */
public class Search extends Panel{

	private WebMarkupContainer listContainer;
	private WebMarkupContainer searchWebMarkupContainer;
	private WebMarkupContainer detailsWebMarkupContainer;
	private ContainerForm containerForm;
	private PageableListView<SubjectVO> pageableListView;
	private FeedbackPanel fbPanel;
	private Details details;
	
	@SpringBean( name = Constants.STUDY_SERVICE)
	private IStudyService studyService;
	
	/**
	 * @param id
	 */
	public Search(	String id,
					FeedbackPanel feedbackpanel,
					WebMarkupContainer searchMarkupContainer,
					PageableListView<SubjectVO> pageableListView,
					WebMarkupContainer resultListContainer,
					WebMarkupContainer detailsContainer,
					Details detailPanel,
					ContainerForm subjectContainerForm) {
		
		super(id);
		this.containerForm = subjectContainerForm;
		this.pageableListView = pageableListView;
		listContainer = resultListContainer;
		searchWebMarkupContainer = searchMarkupContainer;
		detailsWebMarkupContainer = detailsContainer;
		fbPanel = feedbackpanel;
		this.details = detailPanel;
	}
	
	public void processDetail(AjaxRequestTarget target){
		searchWebMarkupContainer.setVisible(false);
		details.getDetailsForm().getSubjectIdTxtFld().setEnabled(false);
		detailsWebMarkupContainer.setVisible(true);
		listContainer.setVisible(false);
		target.addComponent(detailsWebMarkupContainer);
		target.addComponent(searchWebMarkupContainer);
		target.addComponent(listContainer);
	}
	
	public void initialisePanel(){
		
		SearchForm searchForm = new SearchForm("searchForm", (CompoundPropertyModel<SubjectVO>)containerForm.getModel()){
			
			protected  void onSearch(AjaxRequestTarget target){
				
				Long sessionStudyId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
				containerForm.getModelObject().setStudy(studyService.getStudy(sessionStudyId));
				//Refresh the FB panel if there was an old message from previous search result
				target.addComponent(fbPanel);
				Collection<SubjectVO> subjects = studyService.getSubject(containerForm.getModelObject());
				
				if(subjects != null && subjects.size() == 0){
					this.info("There are no subjects with the specified criteria.");
					target.addComponent(fbPanel);
				}
				
				containerForm.getModelObject().setSubjectList(subjects);
				pageableListView.removeAll();
				listContainer.setVisible(true);
				target.addComponent(listContainer);
				
			}
			
			protected  void onNew(AjaxRequestTarget target){
				containerForm.setModelObject(new SubjectVO());
				processDetail(target);
			}
		};
		
		searchForm.initialiseForm();
		add(searchForm);
	}

}
