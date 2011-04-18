package au.org.theark.study.web.component.subjectUpload;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.Constants;
import au.org.theark.core.vo.UploadVO;
import au.org.theark.core.web.component.ArkExcelWorkSheetAsGrid;
import au.org.theark.core.web.form.AbstractWizardForm;
import au.org.theark.core.web.form.AbstractWizardStepPanel;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.component.subjectUpload.form.WizardForm;

/**
 * The first step of this wizard.
 */
public class SubjectUploadStep2 extends AbstractWizardStepPanel
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -4070515786803720370L;
	private Form<UploadVO>						containerForm;
	private String	validationMessage;
	public java.util.Collection<String> validationMessages = null;
	private WizardForm wizardForm;
	
	@SpringBean(name = Constants.STUDY_SERVICE)
	private IStudyService studyService;
	
	public SubjectUploadStep2(String id) {
		super(id);
		initialiseDetailForm();
	}
	
	public SubjectUploadStep2(String id, Form<UploadVO> containerForm, WizardForm wizardForm) {
		super(id, "Step 2/5: File Validation", "The file has been validated. If there are no errors, click Next to continue.");

		this.containerForm = containerForm;
		this.wizardForm = wizardForm;
		
		initialiseDetailForm();
	}
	
	private void initialiseDetailForm() 
	{
		setValidationMessage(containerForm.getModelObject().getValidationMessagesAsString());
		addOrReplace(new MultiLineLabel("multiLineLabel", getValidationMessage()));
	}

	/**
	 * @param validationMessage the validationMessages to set
	 */
	public void setValidationMessage(String validationMessage)
	{
		this.validationMessage = validationMessage;
	}

	/**
	 * @return the validationMessage
	 */
	public String getValidationMessage()
	{
		return validationMessage;
	}

	@Override
	public void handleWizardState(AbstractWizardForm<?> form, AjaxRequestTarget target) 
	{
	
	}
	
	@Override
	public void onStepInNext(AbstractWizardForm<?> form, AjaxRequestTarget target)
	{
		String fileFormat = containerForm.getModelObject().getUpload().getFileFormat().getName();
		char delimChar = containerForm.getModelObject().getUpload().getDelimiterType().getDelimiterCharacter().charAt(0);
		validationMessages = studyService.validateSubjectFileFormat(wizardForm.getFile(), fileFormat, delimChar);
		containerForm.getModelObject().setValidationMessages(validationMessages);
		validationMessage = containerForm.getModelObject().getValidationMessagesAsString();
		addOrReplace(new MultiLineLabel("multiLineLabel", validationMessage));
		
		// Show file data
		try
		{
			InputStream inputStream = containerForm.getModelObject().getFileUpload().getInputStream();
			ArkExcelWorkSheetAsGrid arkExcelWorkSheetAsGrid = new ArkExcelWorkSheetAsGrid("gridView", inputStream, delimChar);
			arkExcelWorkSheetAsGrid.setOutputMarkupId(true);
			form.setArkExcelWorkSheetAsGrid(arkExcelWorkSheetAsGrid);
			form.getWizardPanelFormContainer().addOrReplace(arkExcelWorkSheetAsGrid);
			target.addComponent(form.getWizardPanelFormContainer());
		}
		catch (IOException e)
		{
			System.out.println("Failed to display the uploaded file: " + e);
		}
			
		if(validationMessage != null && validationMessage.length() > 0)
		{
			form.getNextButton().setEnabled(false);
			target.addComponent(form.getWizardButtonContainer());
		}
	}
	
	@Override
	public void onStepOutNext(AbstractWizardForm<?> form, AjaxRequestTarget target)
	{
	}
}