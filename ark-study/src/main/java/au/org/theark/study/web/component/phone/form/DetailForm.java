/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.study.web.component.phone.form;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.ArkUniqueException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.PhoneType;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.PhoneVO;
import au.org.theark.core.web.behavior.ArkDefaultFormFocusBehavior;
import au.org.theark.core.web.form.AbstractContainerForm;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;

/**
 * @author nivedann
 *
 */
public class DetailForm extends AbstractDetailForm<PhoneVO>{

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;

	@SpringBean( name = Constants.STUDY_SERVICE)
	private IStudyService studyService;
	
	private TextField<String> phoneIdTxtFld;
	private TextField<String> areaCodeTxtFld;
	private TextField<String> phoneNumberTxtFld;
	private DropDownChoice<PhoneType> phoneTypeChoice;
	
	/**
	 * @param id
	 * @param feedBackPanel
	 * @param resultListContainer
	 * @param detailPanelContainer
	 * @param detailPanelFormContainer
	 * @param searchPanelContainer
	 * @param viewButtonContainer
	 * @param editButtonContainer
	 * @param containerForm
	 */
	public DetailForm(String id, FeedbackPanel feedBackPanel,
			WebMarkupContainer resultListContainer,
			WebMarkupContainer detailPanelContainer,
			WebMarkupContainer detailPanelFormContainer,
			WebMarkupContainer searchPanelContainer,
			WebMarkupContainer viewButtonContainer,
			WebMarkupContainer editButtonContainer, AbstractContainerForm<PhoneVO> containerForm) {
		
		
			super(id, 
					feedBackPanel, 
					resultListContainer, 
					detailPanelContainer,
					detailPanelFormContainer, 
					searchPanelContainer, 
					viewButtonContainer,
					editButtonContainer, 
					containerForm);

	}
	
	
	public void initialiseDetailForm(){
		phoneIdTxtFld = new TextField<String>("phone.id");
		areaCodeTxtFld = new TextField<String>("phone.areaCode");
		phoneNumberTxtFld = new TextField<String>("phone.phoneNumber");
		List<PhoneType> phoneTypeList = iArkCommonService.getListOfPhoneType();
		ChoiceRenderer defaultChoiceRenderer = new ChoiceRenderer(Constants.NAME, Constants.ID);
		phoneTypeChoice = new DropDownChoice<PhoneType>("phone.phoneType",phoneTypeList,defaultChoiceRenderer);
		phoneTypeChoice.add(new ArkDefaultFormFocusBehavior());
		addDetailFormComponents();
		attachValidators();
	}

	
	public void addDetailFormComponents(){
		detailPanelFormContainer.add(phoneIdTxtFld.setEnabled(false));
		detailPanelFormContainer.add(areaCodeTxtFld);
		detailPanelFormContainer.add(phoneNumberTxtFld);
		detailPanelFormContainer.add(phoneTypeChoice);
	}


	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractDetailForm#attachValidators()
	 */
	@Override
	protected void attachValidators() {
		phoneNumberTxtFld.setRequired(true);
		phoneNumberTxtFld.add(StringValidator.maximumLength(10));
//		areaCodeTxtFld.setRequired(true);	// Removed due to ARK-73:: May be option depending on phone type
		areaCodeTxtFld.add(StringValidator.maximumLength(10));
		phoneTypeChoice.setRequired(true);
	}


	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onCancel(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onCancel(AjaxRequestTarget target) {
		PhoneVO phoneVO = new PhoneVO();
		containerForm.setModelObject(phoneVO);
	}
	

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onDeleteConfirmed(org.apache.wicket.ajax.AjaxRequestTarget, java.lang.String, org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow)
	 */
	@Override
	protected void onDeleteConfirmed(AjaxRequestTarget target,	String selection, ModalWindow selectModalWindow) {
		selectModalWindow.close(target);
		try {
			studyService.delete(containerForm.getModelObject().getPhone());
			containerForm.info("The Phone record was deleted successfully.");
			editCancelProcess(target);
		} catch (ArkSystemException e) {
			this.error("An error occured while processing your delete. Please contact Support");
			//TODO Need to work out more on how user will contact support (Level 1..etc) a generic message with contact info plus logs to be emailed to admin
		}
		onCancel(target);
	}


	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onSave(org.apache.wicket.markup.html.form.Form, org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onSave(Form<PhoneVO> containerForm, AjaxRequestTarget target) {
		//Persist the phone number to the backend, associate the person in context with the phone object
		Long personSessionId =(Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
		//Get the person and set it on the Phone object.
		try {
			Person person = studyService.getPerson(personSessionId);
			containerForm.getModelObject().getPhone().setPerson(person);
			// Make the area code mandatory only for landline phone (home/work) entries
			String phType = containerForm.getModelObject().getPhone().getPhoneType().getName().toLowerCase();
			boolean saveOk = true;
			
			if (!phType.equals("mobile")) {
				//must be landline
				if (containerForm.getModelObject().getPhone().getAreaCode() == null 
						|| containerForm.getModelObject().getPhone().getAreaCode().length() < 1) {
					this.error("An area code must be entered for landline numbers");
					saveOk = false;
				}
			}
			if (saveOk) {
				// Ok to save...
				LinkSubjectStudy subjectInContext = iArkCommonService.getSubject(personSessionId);
				if(containerForm.getModelObject().getPhone().getId() == null ){
					studyService.create(containerForm.getModelObject().getPhone());
					this.info("Phone number was added and linked to Subject UID: " + subjectInContext.getSubjectUID() );
					processErrors(target);
					//Call the create
				}else{
					studyService.update(containerForm.getModelObject().getPhone());
					this.info("Phone number was updated and linked to Subject UID: " + subjectInContext.getSubjectUID());
					processErrors(target);
					//Update 
				}
				onSavePostProcess(target);
			}
			//Invoke backend to persist the phone
		} catch (ArkUniqueException aue) {
			this.error(aue.getMessage());
		} catch (EntityNotFoundException e) {
			this.error("The Subject/Participant is not available in the system anymore");
		} catch (ArkSystemException e) {
			this.error("A System Exception has occured please contact Support.");
		}
	}


	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractDetailForm#processErrors(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void processErrors(AjaxRequestTarget target) {
		target.addComponent(feedBackPanel);
	}


	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractDetailForm#getMode()
	 */
	@Override
	protected boolean isNew() {
		if(containerForm.getModelObject().getPhone().getId() == null){
			return true;
		}else{
			return false;
		}
		
	}
}
