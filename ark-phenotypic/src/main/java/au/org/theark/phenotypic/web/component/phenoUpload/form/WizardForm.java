package au.org.theark.phenotypic.web.component.phenoUpload.form;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.file.File;

import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.form.AbstractWizardForm;
import au.org.theark.phenotypic.model.vo.UploadVO;
import au.org.theark.phenotypic.service.Constants;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.web.component.field.form.ContainerForm;
import au.org.theark.phenotypic.web.component.phenoUpload.PhenoUploadStep1;
import au.org.theark.phenotypic.web.component.phenoUpload.PhenoUploadStep2;
import au.org.theark.phenotypic.web.component.phenoUpload.PhenoUploadStep3;
import au.org.theark.phenotypic.web.component.phenoUpload.PhenoUploadStep4;
import au.org.theark.phenotypic.web.component.phenoUpload.PhenoUploadStep5;
import au.org.theark.phenotypic.web.component.phenoUpload.WizardPanel;

/**
 * @author cellis
 * 
 */
@SuppressWarnings( { "serial", "unused" })
public class WizardForm extends AbstractWizardForm<UploadVO>
{
	@SpringBean(name = Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService					phenotypicService;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService					iArkCommonService;

	private ContainerForm						fieldContainerForm;

	private static final String	HEXES	= "0123456789ABCDEF";
	
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
		
		Long sessionPhenoCollectionId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.phenotypic.web.Constants.SESSION_PHENO_COLLECTION_ID);
		disableWizardForm(sessionPhenoCollectionId, "There is no Phenotypic Collection in context. Please select a Phenotypic Collection");
	}

	public void initialiseDetailForm()
	{
		initialiseSteps();
		addComponents();
	}
	
	public void initialiseSteps()
	{
		PhenoUploadStep1 step1 = new PhenoUploadStep1("step", containerForm, this);
		PhenoUploadStep2 step2 = new PhenoUploadStep2("step", containerForm, this);
		PhenoUploadStep3 step3 = new PhenoUploadStep3("step", containerForm, this);
		PhenoUploadStep4 step4 = new PhenoUploadStep4("step", containerForm, this);
		PhenoUploadStep5 step5 = new PhenoUploadStep5("step", containerForm);
		
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