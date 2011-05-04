package au.org.theark.phenotypic.web.component.fieldUpload;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.util.Date;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.form.AbstractWizardForm;
import au.org.theark.core.web.form.AbstractWizardStepPanel;
import au.org.theark.phenotypic.exception.FileFormatException;
import au.org.theark.phenotypic.exception.PhenotypicSystemException;
import au.org.theark.phenotypic.model.vo.UploadVO;
import au.org.theark.phenotypic.service.Constants;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.util.PhenoDataUploader;
import au.org.theark.phenotypic.util.PhenoUploadReport;
import au.org.theark.phenotypic.web.component.fieldUpload.form.WizardForm;

/**
 * The 4th step of this wizard.
 */
public class FieldUploadStep4 extends AbstractWizardStepPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2788948560672351760L;
	static Logger	log	= LoggerFactory.getLogger(FieldUploadStep4.class);
	private Form<UploadVO>						containerForm;
	private String	validationMessage;
	public java.util.Collection<String> validationMessages = null;
	private WizardForm wizardForm;
	@SpringBean(name = Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService phenotypicService;
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;
	
	/**
	 * Construct.
	 */
	public FieldUploadStep4(String id, Form<UploadVO> containerForm, WizardForm wizardForm)
	{
		super(id, "Step 4/5: Confirm Upload", "Data will now be written to the database, click Next to continue, otherwise click Cancel.");
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
		validationMessage = containerForm.getModelObject().getValidationMessagesAsString();
		addOrReplace(new MultiLineLabel("multiLineLabel", validationMessage));
		
		form.getArkExcelWorkSheetAsGrid().setVisible(false);
		target.addComponent(form.getArkExcelWorkSheetAsGrid());
	}
	
	@Override
	public void onStepOutNext(AbstractWizardForm<?> form, AjaxRequestTarget target)
	{
		if(validationMessage == null || containerForm.getModel().getObject().getOverrideDataValidationChkBox())
		{
			// Filename seems to be lost from model when moving between steps in wizard
			containerForm.getModelObject().getUpload().setFilename(wizardForm.getFileName());
			
			// Perform actual import of data
			containerForm.getModelObject().getUpload().setStartTime(new Date(System.currentTimeMillis()));
			StringBuffer uploadReport = null;
			String fileFormat = containerForm.getModelObject().getUpload().getFileFormat().getName();
			char delimiterChar = containerForm.getModelObject().getUpload().getDelimiterType().getDelimiterCharacter().charAt(0);
			
			Subject currentUser = SecurityUtils.getSubject();
			Long studyId = (Long) currentUser.getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
			Study study = iArkCommonService.getStudy(studyId);
			PhenoDataUploader phenoUploader = new PhenoDataUploader(phenotypicService, study, null, iArkCommonService, fileFormat, delimiterChar);;
			
			try
			{
				log.info("Uploading data dictionary file");
				InputStream inputStream = containerForm.getModelObject().getFileUpload().getInputStream();
				uploadReport = phenoUploader.uploadAndReportMatrixDataDictionaryFile(inputStream, inputStream.toString().length());
				
				// Determined FieldUpload entities
				containerForm.getModelObject().setFieldUploadCollection(phenoUploader.getFieldUploadCollection());
			}
			catch (FileFormatException ffe)
			{
				log.error(Constants.FILE_FORMAT_EXCEPTION + ffe);
			}
			catch (PhenotypicSystemException pse)
			{
				log.error(Constants.PHENOTYPIC_SYSTEM_EXCEPTION + pse);
			}
			catch (IOException e1)
			{
				log.error(e1.getMessage());
			}
			
			// Update the report
			updateUploadReport(uploadReport.toString());
			
			// Save all objects to the database
			save();
		}
	}
	
	public void updateUploadReport(String importReport)
	{
		// Set Upload report
		PhenoUploadReport phenoUploadReport = new PhenoUploadReport();
		phenoUploadReport.appendDetails(containerForm.getModelObject().getUpload());
		phenoUploadReport.append(importReport);
		byte[] bytes = phenoUploadReport.getReport().toString().getBytes();
		Blob uploadReportBlob = Hibernate.createBlob(bytes);
		containerForm.getModelObject().getUpload().setUploadReport(uploadReportBlob);
	}
	
	private void save()
	{
		containerForm.getModelObject().getUpload().setFinishTime(new Date(System.currentTimeMillis()));
		phenotypicService.createUpload(containerForm.getModelObject());
	}
}
