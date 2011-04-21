package au.org.theark.study.web.component.subjectUpload;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.vo.UploadVO;
import au.org.theark.core.web.component.ArkErrorCell;
import au.org.theark.core.web.component.ArkExcelWorkSheetAsGrid;
import au.org.theark.core.web.form.AbstractWizardForm;
import au.org.theark.core.web.form.AbstractWizardStepPanel;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.component.subjectUpload.form.WizardForm;

/**
 * The first step of this wizard.
 */
public class SubjectUploadStep3 extends AbstractWizardStepPanel
{
	/**
	 * 
	 */
	private static final long				serialVersionUID		= 2987959815074138750L;
	private Form<UploadVO>					containerForm;
	private String								validationMessage;
	public java.util.Collection<String>	validationMessages	= null;
	private WizardForm						wizardForm;
	private WebMarkupContainer 			updateExistingDataContainer;
	private CheckBox							updateChkBox;

	@SpringBean(name = au.org.theark.core.Constants.STUDY_SERVICE)
	private IStudyService					studyService;

	/**
	 * Construct.
	 */
	public SubjectUploadStep3(String id, Form<UploadVO> containerForm, WizardForm wizardForm)
	{
		super(id, "Step 3/5: Data Validation", "The data in the file is now validated, correct any errors and try again, otherwise, click Next to continue.");
		this.containerForm = containerForm;
		this.wizardForm = wizardForm;
		initialiseDetailForm();
	}

	private void initialiseDetailForm()
	{
		setValidationMessage(containerForm.getModelObject().getValidationMessagesAsString());
		addOrReplace(new MultiLineLabel("multiLineLabel", getValidationMessage()));

		updateExistingDataContainer = new WebMarkupContainer("updateExistingDataContainer");
		updateExistingDataContainer.setOutputMarkupId(true);
		updateChkBox = new CheckBox("updateChkBox");
		updateChkBox.setVisible(true);

		updateChkBox.add(new AjaxFormComponentUpdatingBehavior("onChange")
		{
			/**
			 * 
			 */
			private static final long	serialVersionUID	= -4514605801401294450L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				if (containerForm.getModelObject().getUpdateChkBox())
				{
					wizardForm.getNextButton().setEnabled(true);
				}
				else
				{
					wizardForm.getNextButton().setEnabled(false);
				}
				target.addComponent(wizardForm.getWizardButtonContainer());
			}
		});

		updateExistingDataContainer.add(updateChkBox);
		add(updateExistingDataContainer);
	}

	/**
	 * @param validationMessages
	 *           the validationMessages to set
	 */
	public void setValidationMessage(String validationMessage)
	{
		this.validationMessage = validationMessage;
	}

	/**
	 * @return the validationMessages
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
	public void onStepOutNext(AbstractWizardForm<?> form, AjaxRequestTarget target)
	{
		if (containerForm.getModelObject().getUpdateChkBox())
			containerForm.getModelObject().setValidationMessages(null);
	}

	@Override
	public void onStepInNext(AbstractWizardForm<?> form, AjaxRequestTarget target)
	{
		try
		{
			String fileFormat = containerForm.getModelObject().getUpload().getFileFormat().getName();
			char delimiterChar = containerForm.getModelObject().getUpload().getDelimiterType().getDelimiterCharacter().charAt(0);
			InputStream inputStream = containerForm.getModelObject().getFileUpload().getInputStream();
			validationMessages = studyService.validateSubjectFileData(inputStream, fileFormat, delimiterChar);
			this.containerForm.getModelObject().setValidationMessages(validationMessages);
			validationMessage = containerForm.getModelObject().getValidationMessagesAsString();
			addOrReplace(new MultiLineLabel("multiLineLabel", validationMessage));

			HashSet<Integer> insertRows = new HashSet<Integer>();
			HashSet<Integer> updateRows = new HashSet<Integer>();
			HashSet<ArkErrorCell> errorCells = new HashSet<ArkErrorCell>();
			
			insertRows = studyService.getSubjectUploadInsertRows();
			updateRows = studyService.getSubjectUploadUpdateRows();
			errorCells = studyService.getSubjectUploadErrorCells();
			inputStream.reset();
			
			// Show file data (and key reference)
			ArkExcelWorkSheetAsGrid arkExcelWorkSheetAsGrid = new ArkExcelWorkSheetAsGrid("gridView", inputStream, fileFormat, delimiterChar, containerForm.getModelObject().getFileUpload(), insertRows, updateRows, errorCells);
			arkExcelWorkSheetAsGrid.setOutputMarkupId(true);
			arkExcelWorkSheetAsGrid.getWizardDataGridKeyContainer().setVisible(true);
			form.setArkExcelWorkSheetAsGrid(arkExcelWorkSheetAsGrid);
			form.getWizardPanelFormContainer().addOrReplace(arkExcelWorkSheetAsGrid);
			
			// Repaint
			target.addComponent(arkExcelWorkSheetAsGrid.getWizardDataGridKeyContainer());
			target.addComponent(form.getWizardPanelFormContainer());
			
			if(!errorCells.isEmpty())
			{
				updateExistingDataContainer.setVisible(false);
				target.addComponent(updateExistingDataContainer);
				form.getNextButton().setEnabled(false);
				target.addComponent(form.getWizardButtonContainer());
			}
		}
		catch (IOException e)
		{
			System.out.println("Failed to display the uploaded file: " + e);
		}
		
		if (validationMessage != null && validationMessage.length() > 0)
		{
			form.getNextButton().setEnabled(false);
			target.addComponent(form.getWizardButtonContainer());	
		}
	}

	/**
	 * @param updateChkBox
	 *           the updateChkBox to set
	 */
	public void setUpdateChkBox(CheckBox updateChkBox)
	{
		this.updateChkBox = updateChkBox;
	}

	/**
	 * @return the updateChkBox
	 */
	public CheckBox getUpdateChkBox()
	{
		return updateChkBox;
	}

	/**
	 * @param updateExistingDataContainer the updateExistingDataContainer to set
	 */
	public void setUpdateExistingDataContainer(WebMarkupContainer updateExistingDataContainer)
	{
		this.updateExistingDataContainer = updateExistingDataContainer;
	}

	/**
	 * @return the updateExistingDataContainer
	 */
	public WebMarkupContainer getUpdateExistingDataContainer()
	{
		return updateExistingDataContainer;
	}
}
