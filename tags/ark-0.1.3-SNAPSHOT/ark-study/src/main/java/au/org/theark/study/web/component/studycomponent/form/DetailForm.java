/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.study.web.component.studycomponent.form;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityCannotBeRemoved;
import au.org.theark.core.exception.EntityExistsException;
import au.org.theark.core.exception.UnAuthorizedOperation;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.behavior.ArkDefaultFormFocusBehavior;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.study.model.vo.StudyCompVo;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;


/**
 * @author nivedann
 *
 */
public class DetailForm extends AbstractDetailForm<StudyCompVo>{

	@SpringBean( name =  au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;
	
	@SpringBean(name ="studyService")
	private IStudyService studyService;
	private Study study;
	
	private TextField<String> componentIdTxtFld;
	private TextField<String> componentNameTxtFld;
	private TextArea<String> componentDescription;
	private TextArea<String> keywordTxtArea;

	/**
	 * @param id
	 * @param resultListContainer
	 * @param detailPanelContainer
	 */
	public DetailForm(	String id,
						FeedbackPanel feedBackPanel,
						WebMarkupContainer resultListContainer,
						WebMarkupContainer detailPanelContainer,
						WebMarkupContainer detailPanelFormContainer,
						WebMarkupContainer searchPanelContainer,
						WebMarkupContainer viewButtonContainer,
						WebMarkupContainer editButtonContainer,
						ContainerForm containerForm				) {
		
	
		super(	id, feedBackPanel,	
				resultListContainer, detailPanelContainer, 
				detailPanelFormContainer, searchPanelContainer,
				viewButtonContainer, editButtonContainer,
				containerForm);
		
	}
	
	public void initialiseDetailForm(){
		
		componentIdTxtFld = new TextField<String>(Constants.STUDY_COMPONENT_ID);
		componentIdTxtFld.setEnabled(false);
		componentNameTxtFld = new TextField<String>(Constants.STUDY_COMPONENT_NAME);
		componentNameTxtFld.add(new ArkDefaultFormFocusBehavior());
		componentDescription = new TextArea<String>(Constants.STUDY_COMPONENT_DESCRIPTION);
		keywordTxtArea = new TextArea<String>(Constants.STUDY_COMPONENT_KEYWORD);
		addDetailFormComponents();
		attachValidators();
	}
	
	public void addDetailFormComponents(){

		detailPanelFormContainer.add(componentIdTxtFld);
		detailPanelFormContainer.add(componentIdTxtFld);
		detailPanelFormContainer.add(componentNameTxtFld);
		detailPanelFormContainer.add(componentDescription);
		detailPanelFormContainer.add(keywordTxtArea);
		
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractDetailForm#attachValidators()
	 */
	@Override
	protected void attachValidators() {
		componentNameTxtFld.setRequired(true).setLabel(new StringResourceModel("error.study.component.name.required", componentNameTxtFld, new Model<String>("Study Component Name")));
		componentNameTxtFld.add(StringValidator.lengthBetween(3, 100)).setLabel(new StringResourceModel("error.study.component.name.length", componentNameTxtFld, new Model<String>("Study Component Name")));
		componentDescription.add(StringValidator.lengthBetween(5, 500)).setLabel(new StringResourceModel("error.study.component.description.length", null, new Model<String>("Description")));
		keywordTxtArea.add(StringValidator.lengthBetween(1,255)).setLabel(new StringResourceModel("error.study.component.keywords.length", null, new Model<String>("Keywords")));
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onCancel(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onCancel(AjaxRequestTarget target) {
		
		StudyCompVo studyCompVo = new StudyCompVo();
		containerForm.setModelObject(studyCompVo);
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onSave(org.apache.wicket.markup.html.form.Form, org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onSave(Form<StudyCompVo> containerForm,	AjaxRequestTarget target) {
		target.addComponent(detailPanelContainer);
		try {

			Long studyId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
			study =	iArkCommonService.getStudy(studyId);	
			containerForm.getModelObject().getStudyComponent().setStudy(study);
			
			if(containerForm.getModelObject().getStudyComponent().getId() == null){
				
				studyService.create(containerForm.getModelObject().getStudyComponent());
				this.info("Study Component " + containerForm.getModelObject().getStudyComponent().getName() + " was created successfully" );
				processErrors(target);
			
			}else{
			
				studyService.update(containerForm.getModelObject().getStudyComponent());
				this.info("Study Component " + containerForm.getModelObject().getStudyComponent().getName() + " was updated successfully" );
				processErrors(target);
				
			}
			onSavePostProcess(target);
			
		}catch (EntityExistsException e) {
			this.error("A Study Component with the same name already exists for this study." );
			processErrors(target);
		}catch (UnAuthorizedOperation e) {
			 this.error("You are not authorised to manage study components for the given study " + study.getName());
			 processErrors(target);
		} catch (ArkSystemException e) {
			this.error("A System error occured, we will have someone contact you.");
			processErrors(target);
		}
		
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractDetailForm#processErrors(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void processErrors(AjaxRequestTarget target) {
		target.addComponent(feedBackPanel);
		
	}


	protected void onDeleteConfirmed(AjaxRequestTarget target,String selection, ModalWindow selectModalWindow) {
		try {
			studyService.delete(containerForm.getModelObject().getStudyComponent());
			StudyCompVo studyCompVo = new StudyCompVo();
			containerForm.setModelObject(studyCompVo);
			selectModalWindow.close(target);
			containerForm.info("The Study Component was deleted successfully.");
			editCancelProcess(target);
		}catch(UnAuthorizedOperation unAuthorisedexception){
			containerForm.error("You are not authorised to delete this study component.");
			processErrors(target);
		}
		catch(EntityCannotBeRemoved cannotRemoveException){
			containerForm.error("Cannot Delete this Study Component. This component is associated with a Subject");
			processErrors(target);
		} 
		catch (ArkSystemException e) {
			containerForm.error("A System Error has occured please contact support.");
			processErrors(target);
		}
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractDetailForm#isNew()
	 */
	@Override
	protected boolean isNew() {
		if(containerForm.getModelObject().getStudyComponent().getId() == null){
			return true;
		}else{
			return false;	
		}
		
	}

}
