package au.org.theark.phenotypic.web.component.phenoUpload;

import java.io.IOException;
import java.io.InputStream;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.web.component.ArkExcelWorkSheetAsGrid;
import au.org.theark.core.web.form.AbstractWizardForm;
import au.org.theark.core.web.form.AbstractWizardStepPanel;
import au.org.theark.phenotypic.model.vo.UploadVO;
import au.org.theark.phenotypic.service.Constants;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.web.component.phenoUpload.form.WizardForm;

/**
 * The first step of this wizard.
 */
public class PhenoUploadStep2 extends AbstractWizardStepPanel
{
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -6923277221441497110L;
	private Form<UploadVO>						containerForm;
	private String	validationMessage;
	public java.util.Collection<String> validationMessages = null;
	private WizardForm wizardForm;
	
	@SpringBean(name = Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService phenotypicService;
	
	public PhenoUploadStep2(String id) {
		super(id);
		initialiseDetailForm();
	}
	
	public PhenoUploadStep2(String id, Form<UploadVO> containerForm, WizardForm wizardForm) {
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
		InputStream inputStream;
		try
		{
			inputStream = containerForm.getModelObject().getFileUpload().getInputStream();
			validationMessages = phenotypicService.validateMatrixPhenoFileFormat(inputStream, fileFormat, delimChar);
		}
		catch (IOException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		containerForm.getModelObject().setValidationMessages(validationMessages);
		validationMessage = containerForm.getModelObject().getValidationMessagesAsString();
		addOrReplace(new MultiLineLabel("multiLineLabel", validationMessage));
		
		if(validationMessage != null && validationMessage.length() > 0)
		{
			form.getNextButton().setEnabled(false);
			target.addComponent(form.getWizardButtonContainer());
		}
		
		// Show file data
		try
		{
			inputStream = containerForm.getModelObject().getFileUpload().getInputStream();
			inputStream.reset();
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
	}
	
	@Override
	public void onStepOutNext(AbstractWizardForm<?> form, AjaxRequestTarget target)
	{
	}
}