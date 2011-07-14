package au.org.theark.study.web.component.subjectUpload;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.util.Date;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.hibernate.Hibernate;

import au.org.theark.core.vo.UploadVO;
import au.org.theark.core.web.form.AbstractWizardForm;
import au.org.theark.core.web.form.AbstractWizardStepPanel;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.util.SubjectUploadReport;
import au.org.theark.study.web.component.subjectUpload.form.WizardForm;

/**
 * The 4th step of this wizard.
 */
public class SubjectUploadStep4 extends AbstractWizardStepPanel
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 2971945948091031160L;
	private Form<UploadVO>						containerForm;
	private WizardForm wizardForm;
	@SpringBean(name = au.org.theark.core.Constants.STUDY_SERVICE)
	private IStudyService					iStudyService;
	
	/**
	 * Construct.
	 */
	public SubjectUploadStep4(String id, Form<UploadVO> containerForm, WizardForm wizardForm)
	{
		super(id, "Step 4/5: Confirm Upload", "Data will now be written to the database, click Next to continue, otherwise click Cancel.");
		this.containerForm = containerForm;
		this.wizardForm = wizardForm;
		initialiseDetailForm();
	}
	
	private void initialiseDetailForm() 
	{
	}

	@Override
	public void handleWizardState(AbstractWizardForm<?> form, AjaxRequestTarget target)
	{
	}
	
	@Override
	public void onStepInNext(AbstractWizardForm<?> form, AjaxRequestTarget target)
	{
		form.getNextButton().setEnabled(true);
		target.addComponent(form.getNextButton());
		
		form.getArkExcelWorkSheetAsGrid().setVisible(false);
		target.addComponent(form.getArkExcelWorkSheetAsGrid());
	}
	
	@Override
	public void onStepOutNext(AbstractWizardForm<?> form, AjaxRequestTarget target)
	{
		form.getNextButton().setEnabled(false);
		target.addComponent(form.getNextButton());
		
		// Filename seems to be lost from model when moving between steps in wizard
		containerForm.getModelObject().getUpload().setFilename(wizardForm.getFileName());
		
		// Perform actual import of data
		containerForm.getModelObject().getUpload().setStartTime(new Date(System.currentTimeMillis()));
		
		String fileFormat = containerForm.getModelObject().getUpload().getFileFormat().getName();
		char delimiterChar = containerForm.getModelObject().getUpload().getDelimiterType().getDelimiterCharacter();
		StringBuffer uploadReport = null;
		try 
		{
			InputStream inputStream = containerForm.getModelObject().getFileUpload().getInputStream();
			uploadReport = iStudyService.uploadAndReportMatrixSubjectFile(inputStream, containerForm.getModelObject().getFileUpload().getSize(), fileFormat, delimiterChar);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		
		// Update the report
		updateUploadReport(uploadReport.toString());
		
		// Save all objects to the database
		save();
	}
	
	public void updateUploadReport(String importReport)
	{
		// Set Upload report
		SubjectUploadReport subjectUploadReport = new SubjectUploadReport();
		subjectUploadReport.appendDetails(containerForm.getModelObject().getUpload());
		subjectUploadReport.append(importReport);
		byte[] bytes = subjectUploadReport.getReport().toString().getBytes();
		Blob uploadReportBlob = Hibernate.createBlob(bytes);
		containerForm.getModelObject().getUpload().setUploadReport(uploadReportBlob);
	}
	
	private void save()
	{
		containerForm.getModelObject().getUpload().setFinishTime(new Date(System.currentTimeMillis()));
		iStudyService.createUpload(containerForm.getModelObject().getUpload());
	}
}
