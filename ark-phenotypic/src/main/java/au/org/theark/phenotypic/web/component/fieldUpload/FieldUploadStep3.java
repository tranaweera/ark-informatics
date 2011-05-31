package au.org.theark.phenotypic.web.component.fieldUpload;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.component.ArkDownloadAjaxButton;
import au.org.theark.core.web.component.ArkDownloadTemplateButton;
import au.org.theark.core.web.component.ArkExcelWorkSheetAsGrid;
import au.org.theark.core.web.component.ArkGridCell;
import au.org.theark.core.web.form.AbstractWizardForm;
import au.org.theark.core.web.form.AbstractWizardStepPanel;
import au.org.theark.phenotypic.model.vo.UploadVO;
import au.org.theark.phenotypic.service.Constants;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.util.PhenotypicValidator;
import au.org.theark.phenotypic.web.component.fieldUpload.form.WizardForm;

/**
 * The first step of this wizard.
 */
public class FieldUploadStep3 extends AbstractWizardStepPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5099768179441679542L;
	static Logger	log	= LoggerFactory.getLogger(FieldUploadStep3.class);
	private Form<UploadVO>						containerForm;
	private String	validationMessage;
	public java.util.Collection<String> validationMessages = null;
	private WizardForm wizardForm;
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;
	@SpringBean(name = Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService iPhenotypicService;
	private WebMarkupContainer 			overrideDataValidationContainer;
	private CheckBox							overrideDataValidationChkBox;
	private WebMarkupContainer 			updateExistingDataContainer;
	private CheckBox							updateChkBox;
	
	private ArkDownloadAjaxButton downloadValMsgButton = new ArkDownloadAjaxButton("downloadValMsg", "ValidationMessage", null, "txt");
	
	
	/**
	 * Construct.
	 */
	public FieldUploadStep3(String id, Form<UploadVO> containerForm, WizardForm wizardForm)
	{
		super(id, "Step 3/5: Data Validation", 
				"The data in the file is now validated, correct any errors and try again, otherwise, click Next to continue.");
		this.containerForm = containerForm;
		this.wizardForm = wizardForm;
		initialiseDetailForm();
	}
	
	@SuppressWarnings("serial")
	private void initialiseDetailForm() 
	{
		setValidationMessage(containerForm.getModelObject().getValidationMessagesAsString());
		addOrReplace(new MultiLineLabel("multiLineLabel", getValidationMessage()));
		
		add(downloadValMsgButton);
		
		updateExistingDataContainer = new WebMarkupContainer("updateExistingDataContainer");
		updateExistingDataContainer.setOutputMarkupId(true);
		updateChkBox = new CheckBox("updateChkBox");
		updateChkBox.setVisible(true);
		
		overrideDataValidationContainer = new WebMarkupContainer("overrideDataValidationContainer");
		overrideDataValidationContainer.setOutputMarkupId(true);
		overrideDataValidationChkBox = new CheckBox("overrideDataValidationChkBox");
		overrideDataValidationChkBox.setOutputMarkupId(false);
		overrideDataValidationContainer.setVisible(false);
		
		containerForm.getModelObject().setOverrideDataValidationChkBox(true);
		containerForm.getModelObject().setUpdateChkBox(false);

		updateChkBox.add(new AjaxFormComponentUpdatingBehavior("onChange")
		{
			/**
			 * 
			 */
			private static final long	serialVersionUID	= -4514605801401294450L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				if (containerForm.getModelObject().getOverrideDataValidationChkBox() && containerForm.getModelObject().getUpdateChkBox())
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
		
		overrideDataValidationChkBox.add(new AjaxFormComponentUpdatingBehavior("onChange")
		{
			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				if (containerForm.getModelObject().getOverrideDataValidationChkBox() && containerForm.getModelObject().getUpdateChkBox())
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
		
		overrideDataValidationContainer.add(overrideDataValidationChkBox);
		add(overrideDataValidationContainer);

		updateExistingDataContainer.add(updateChkBox);
		add(updateExistingDataContainer);
	}

	/**
	 * @param validationMessages the validationMessages to set
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
		if (containerForm.getModelObject().getOverrideDataValidationChkBox())
			containerForm.getModelObject().setValidationMessages(null);
	}
	
	@Override
	public void onStepInNext(AbstractWizardForm<?> form, AjaxRequestTarget target)
	{
		String fileFormat = containerForm.getModelObject().getUpload().getFileFormat().getName();
		char delimChar = containerForm.getModelObject().getUpload().getDelimiterType().getDelimiterCharacter().charAt(0);
		InputStream inputStream;
		try
		{
			PhenotypicValidator phenotypicValidator = new PhenotypicValidator(iArkCommonService, iPhenotypicService, containerForm.getModelObject());
			inputStream = containerForm.getModelObject().getFileUpload().getInputStream();
			validationMessages = phenotypicValidator.validateDataDictionaryFileData(inputStream, fileFormat, delimChar);
		
			HashSet<Integer> insertRows = new HashSet<Integer>();
			HashSet<Integer> updateRows = new HashSet<Integer>();
			HashSet<ArkGridCell> insertCells = new HashSet<ArkGridCell>();
			HashSet<ArkGridCell> updateCells = new HashSet<ArkGridCell>();
			HashSet<ArkGridCell> warningCells = new HashSet<ArkGridCell>();
			HashSet<ArkGridCell> errorCells = new HashSet<ArkGridCell>();
			
			insertRows = phenotypicValidator.getInsertRows();
			updateRows = phenotypicValidator.getUpdateRows();
			insertCells = phenotypicValidator.getInsertCells();
			updateCells = phenotypicValidator.getUpdateCells();
			warningCells = phenotypicValidator.getWarningCells();
			errorCells = phenotypicValidator.getErrorCells();
			inputStream.reset();
			
			// Show file data (and key reference)
			ArkExcelWorkSheetAsGrid arkExcelWorkSheetAsGrid = new ArkExcelWorkSheetAsGrid("gridView", inputStream, fileFormat, delimChar, containerForm.getModelObject().getFileUpload(), insertRows, updateRows, insertCells, updateCells, warningCells, errorCells);
			arkExcelWorkSheetAsGrid.setOutputMarkupId(true);
			arkExcelWorkSheetAsGrid.getWizardDataGridKeyContainer().setVisible(true);
			form.setArkExcelWorkSheetAsGrid(arkExcelWorkSheetAsGrid);
			form.getWizardPanelFormContainer().addOrReplace(arkExcelWorkSheetAsGrid);
			
			// Repaint
			target.addComponent(arkExcelWorkSheetAsGrid.getWizardDataGridKeyContainer());
			target.addComponent(form.getWizardPanelFormContainer());
			
			if(updateCells.isEmpty())
			{
				containerForm.getModelObject().setUpdateChkBox(true);
				updateExistingDataContainer.setVisible(false);
			}
			else
			{
				containerForm.getModelObject().setUpdateChkBox(false);
				updateExistingDataContainer.setVisible(true);
			}
			target.addComponent(updateExistingDataContainer);
			
			if(!errorCells.isEmpty())
			{
				overrideDataValidationContainer.setVisible(false);
				target.addComponent(overrideDataValidationContainer);
				updateExistingDataContainer.setVisible(false);
				target.addComponent(updateExistingDataContainer);
				form.getNextButton().setEnabled(false);
				target.addComponent(form.getWizardButtonContainer());
			}
			
			if(warningCells.isEmpty())
			{
				overrideDataValidationContainer.setVisible(false);
				target.addComponent(overrideDataValidationContainer);
			}
		} catch (IOException ioe){
			validationMessage = "Error attempting to display the file. Please check the file and try again.";
		}
		
		containerForm.getModelObject().setValidationMessages(validationMessages);
		validationMessage = containerForm.getModelObject().getValidationMessagesAsString();
		addOrReplace(new MultiLineLabel("multiLineLabel", validationMessage));
		
		if(validationMessage != null && validationMessage.length() > 0)
		{
			form.getNextButton().setEnabled(false);
			target.addComponent(form.getWizardButtonContainer());
			downloadValMsgButton = new ArkDownloadAjaxButton("downloadValMsg", "ValidationMessage", validationMessage, "txt");
			addOrReplace(downloadValMsgButton);
			target.addComponent(downloadValMsgButton);
		}
	}

	/**
	 * @param updateChkBox the updateChkBox to set
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

	public WebMarkupContainer getUpdateExistingDataContainer()
	{
		return updateExistingDataContainer;
	}

	public void setUpdateExistingDataContainer(WebMarkupContainer updateExistingDataContainer)
	{
		this.updateExistingDataContainer = updateExistingDataContainer;
	}

	/**
	 * @param overrideDataValidationContainer the overrideDataValidationContainer to set
	 */
	public void setOverrideDataValidationContainer(WebMarkupContainer overrideDataValidationContainer)
	{
		this.overrideDataValidationContainer = overrideDataValidationContainer;
	}

	/**
	 * @return the overrideDataValidationContainer
	 */
	public WebMarkupContainer getOverrideDataValidationContainer()
	{
		return overrideDataValidationContainer;
	}
}
