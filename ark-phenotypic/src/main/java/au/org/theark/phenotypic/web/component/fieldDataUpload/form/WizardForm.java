package au.org.theark.phenotypic.web.component.fieldDataUpload.form;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.file.File;

import au.org.theark.core.model.pheno.entity.PhenoCollection;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.form.AbstractWizardForm;
import au.org.theark.phenotypic.model.vo.UploadVO;
import au.org.theark.phenotypic.service.Constants;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.web.component.fieldDataUpload.FieldDataUploadStep1;
import au.org.theark.phenotypic.web.component.fieldDataUpload.FieldDataUploadStep2;
import au.org.theark.phenotypic.web.component.fieldDataUpload.FieldDataUploadStep3;
import au.org.theark.phenotypic.web.component.fieldDataUpload.FieldDataUploadStep4;
import au.org.theark.phenotypic.web.component.fieldDataUpload.FieldDataUploadStep5;
import au.org.theark.phenotypic.web.component.fieldDataUpload.WizardPanel;

/**
 * @author cellis
 * 
 */
@SuppressWarnings( { "serial" })
public class WizardForm extends AbstractWizardForm<UploadVO>
{
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>					iArkCommonService;

	@SpringBean(name = Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService iPhenotypicService;
	
	private File file;
	private String fileName;

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param feedBackPanel
	 * @param wizardStep
	 * @param listContainer
	 * @param wizardContainer
	 * @param containerForm
	 * @param wizardButtonContainer
	 * @param wizardFormContainer
	 * @param searchPanelContainer
	 */
	public WizardForm(String id, FeedbackPanel feedBackPanel, WizardPanel wizardPanel, WebMarkupContainer listContainer, WebMarkupContainer wizardContainer, Form<UploadVO> containerForm,
			WebMarkupContainer wizardButtonContainer, WebMarkupContainer wizardFormContainer, WebMarkupContainer searchPanelContainer)
	{
		super(id, feedBackPanel, listContainer, wizardContainer, wizardFormContainer, searchPanelContainer, containerForm);
		
		// Set study in context
		Study study = new Study();
		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		
		if(studyId != null)
		{
			study = iArkCommonService.getStudy(studyId);
			PhenoCollection phenoCollection = new PhenoCollection();
			phenoCollection.setStudy(study);
		
			java.util.Collection<PhenoCollection> phenoCollections = iPhenotypicService.searchPhenotypicCollection(phenoCollection);
			
			if(phenoCollections.size() == 0)
			{
				disableWizardForm(null, "There is no Phenotypic Collections defined for this study. Please create a Phenotypic Collection");
			}
		}
	}

	public void initialiseDetailForm()
	{
		initialiseSteps();
		addComponents();
	}
	
	public void initialiseSteps()
	{
		FieldDataUploadStep1 step1 = new FieldDataUploadStep1("step", containerForm, this);
		FieldDataUploadStep2 step2 = new FieldDataUploadStep2("step", containerForm, this);
		FieldDataUploadStep3 step3 = new FieldDataUploadStep3("step", containerForm, this);
		FieldDataUploadStep4 step4 = new FieldDataUploadStep4("step", containerForm, this);
		FieldDataUploadStep5 step5 = new FieldDataUploadStep5("step", containerForm);
		
		step1.setNextStep(step2);
		step2.setNextStep(step3);
		step3.setPreviousStep(step2);
		step3.setNextStep(step4);
		step4.setPreviousStep(step3);
		step4.setNextStep(step5);
		
		wizardPanelFormContainer.addOrReplace(step1);
	}

	private void addComponents()
	{
		add(wizardPanelFormContainer);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void onFinish(AjaxRequestTarget target, Form form)
	{
		this.info("Data upload of file: " + containerForm.getModelObject().getUpload().getFilename() + " was uploaded successfully");
		onCancel(target);
	}

	@Override
	protected void onCancel(AjaxRequestTarget target)
	{
		// Implement Cancel
		UploadVO uploadVO = new UploadVO();
		containerForm.setModelObject(uploadVO);
		initialiseSteps();
		onCancelPostProcess(target);
	}

	@Override
	protected void processErrors(AjaxRequestTarget target)
	{
		target.addComponent(feedBackPanel);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void onError(AjaxRequestTarget target, Form form)
	{
		processErrors(target);	
	}	
	
	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}