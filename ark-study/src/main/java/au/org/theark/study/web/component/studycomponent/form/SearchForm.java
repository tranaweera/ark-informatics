/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.study.web.component.studycomponent.form;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.model.study.entity.StudyComp;
import au.org.theark.core.security.RoleConstants;
import au.org.theark.core.web.form.*;
import au.org.theark.study.model.vo.StudyCompVo;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;

/**
 * @author nivedann
 *
 */
public class SearchForm extends AbstractSearchForm<StudyCompVo>{

	private TextField<String> studyCompIdTxtFld;
	private TextField<String> compNameTxtFld;
	private TextArea<String> descriptionTxtArea;
	private TextArea<String> keywordTxtArea;
	private PageableListView<StudyComp> listView;
	

	@SpringBean( name = Constants.STUDY_SERVICE)
	private IStudyService studyService;
	protected void addSearchComponentsToForm(){
		add(studyCompIdTxtFld);
		add(compNameTxtFld);
		add(keywordTxtArea);
	}

	protected void initialiseSearchForm(){
		
		studyCompIdTxtFld = new TextField<String>(Constants.STUDY_COMPONENT_ID);
		compNameTxtFld = new TextField<String>(Constants.STUDY_COMPONENT_NAME);
		descriptionTxtArea = new TextArea<String>(Constants.STUDY_COMPONENT_DESCRIPTION);
		keywordTxtArea = new TextArea<String>(Constants.STUDY_COMPONENT_KEYWORD);
	}
	/**
	 * @param id
	 * @param cpmModel
	 */
	public SearchForm(	String id, 
						CompoundPropertyModel<StudyCompVo> cpmModel,
						PageableListView<StudyComp> listView, 
						FeedbackPanel feedBackPanel,
						WebMarkupContainer listContainer,
						WebMarkupContainer searchMarkupContainer,
						WebMarkupContainer detailsContainer,
						WebMarkupContainer detailPanelFormContainer,
						WebMarkupContainer viewButtonContainer,
						WebMarkupContainer editButtonContainer	) {
		
		//super(id, cpmModel);
		super	(id,
				cpmModel,
				detailsContainer,
				detailPanelFormContainer,
				viewButtonContainer,
				editButtonContainer,
				searchMarkupContainer,
				listContainer,
				feedBackPanel);
		
		this.listView = listView;
		initialiseSearchForm();
		addSearchComponentsToForm();
		Long sessionStudyId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		disableSearchForm(sessionStudyId, "There is no study in context. Please select a Study.");
		
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.form.AbstractSearchForm#onNew(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onNew(AjaxRequestTarget target) {
		
		StudyCompVo studyCompVo = new StudyCompVo();
		studyCompVo.setMode(Constants.MODE_NEW);
		setModelObject(studyCompVo);
		//processDetail(target);
		preProcessDetailPanel(target);
		
	}
	
	/* (non-Javadoc)
	 * @see au.org.theark.core.form.AbstractSearchForm#onSearch(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onSearch(AjaxRequestTarget target) {
		
		target.addComponent(feedbackPanel);
		try{
			
			List<StudyComp> resultList = studyService.searchStudyComp(getModelObject().getStudyComponent());
			
			if(resultList != null && resultList.size() == 0){
				this.info("Study Component with the specified criteria does not exist in the system.");
				target.addComponent(feedbackPanel);
			}
			getModelObject().setStudyCompList(resultList);
			listView.removeAll();
			listContainer.setVisible(true);//Make the WebMarkupContainer that houses the search results visible
			target.addComponent(listContainer);//For ajax this is required so 
		}catch(ArkSystemException arkEx){
			this.error("A system error has occured. Please try after sometime.");
		}
		
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractSearchForm#isSecure(java.lang.String)
	 */
	
	protected boolean isSecure(String actionType) {
		SecurityManager securityManager =  ThreadContext.getSecurityManager();
		Subject currentUser = SecurityUtils.getSubject();		
		boolean flag = false;
		
		if(actionType.equalsIgnoreCase(Constants.NEW)){
			if(		
					securityManager.hasRole(currentUser.getPrincipals(), RoleConstants.SUPER_ADMIN) ||
					securityManager.hasRole(currentUser.getPrincipals(), RoleConstants.ARK_SUPER_ADMIN) ||
					securityManager.hasRole(currentUser.getPrincipals(), RoleConstants.STUDY_ADMIN)){
				flag = true;
			}
			
		}else if (actionType.equalsIgnoreCase(Constants.SEARCH)){
			flag = true;
		}else{
			flag = true;
		}
		return flag;
	
	}

}
