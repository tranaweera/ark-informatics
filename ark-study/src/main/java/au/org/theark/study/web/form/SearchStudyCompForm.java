package au.org.theark.study.web.form;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.odlabs.wiquery.ui.themes.ThemeUiHelper;

import au.org.theark.core.security.RoleConstants;
import au.org.theark.study.model.entity.Person;
import au.org.theark.study.model.vo.StudyCompVo;
import au.org.theark.study.web.Constants;

public class SearchStudyCompForm extends Form<StudyCompVo>{
	
	private AjaxButton searchButton;
	private AjaxButton newButton;
	private Button resetButton;
	
	private TextField<String> studyCompIdTxtFld;
	private TextField<String> compNameTxtFld;
	private TextArea descriptionTxtArea;
	
	//Field for uploading  a file

	public SearchStudyCompForm(String id,CompoundPropertyModel<StudyCompVo> model) {
		
		super(id,model);
		
		studyCompIdTxtFld = new TextField<String>("studyComponent.studyCompKey");
		compNameTxtFld = new TextField<String>("studyComponent.name");
		descriptionTxtArea = new TextArea("studyComponent.description");
		
		newButton = new AjaxButton(Constants.NEW){
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				//Make the details panel visible
				onNew(target);
			}
			
			@Override
			public boolean isVisible(){
				
				SecurityManager securityManager =  ThreadContext.getSecurityManager();
				Subject currentUser = SecurityUtils.getSubject();		
				boolean flag = false;
				if(		securityManager.hasRole(currentUser.getPrincipals(), RoleConstants.ARK_SUPER_ADMIN) ||
						securityManager.hasRole(currentUser.getPrincipals(), RoleConstants.STUDY_ADMIN)){
					flag = true;
				}
				//if it is a Super or Study admin then make the new available
				return flag;
			}
		};
		
		searchButton = new AjaxButton(Constants.SEARCH){
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				//Make the details panel visible
				onSearch(target);
			}
		};
		
		resetButton = new Button(Constants.RESET){
			public void onSubmit(){
				onReset();
			}
		};
		decorateComponents();
		addComponentsToForm();
	}
	
	private void decorateComponents(){
		ThemeUiHelper.componentRounded(studyCompIdTxtFld);
		ThemeUiHelper.componentRounded(compNameTxtFld);
		ThemeUiHelper.componentRounded(descriptionTxtArea);
		ThemeUiHelper.componentRounded(searchButton);
		ThemeUiHelper.componentRounded(newButton);
		ThemeUiHelper.componentRounded(resetButton);
	}
	
	private void addComponentsToForm(){
		add(studyCompIdTxtFld);
		add(compNameTxtFld);
		add(descriptionTxtArea);
		add(searchButton);
		add(resetButton);
		add(newButton);
	}
	
	protected void onSearch(AjaxRequestTarget target){}
	
	protected void onNew(AjaxRequestTarget target){}
	
	// A non-ajax function
	protected void onReset(){
		clearInput();
		updateFormComponentModels();
		
	}


}
