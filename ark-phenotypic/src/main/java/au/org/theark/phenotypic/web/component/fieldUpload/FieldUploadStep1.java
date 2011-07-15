package au.org.theark.phenotypic.web.component.fieldUpload;

import java.io.IOException;
import java.sql.Blob;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.hibernate.Hibernate;

import au.org.theark.core.model.pheno.entity.DelimiterType;
import au.org.theark.core.model.pheno.entity.FileFormat;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.form.AbstractWizardForm;
import au.org.theark.core.web.form.AbstractWizardStepPanel;
import au.org.theark.phenotypic.model.vo.UploadVO;
import au.org.theark.phenotypic.service.Constants;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.web.component.fieldUpload.form.WizardForm;

/**
 * The first step of this wizard.
 */
public class FieldUploadStep1 extends AbstractWizardStepPanel {
	/**
	 * 
	 */
	private static final long					serialVersionUID		= 4272918747277155957L;

	public java.util.Collection<String>		validationMessages	= null;

	@SpringBean(name = Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService					iPhenotypicService;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>			iArkCommonService;

	private Form<UploadVO>						containerForm;

	private FileUploadField						fileUploadField;
	// private UploadProgressBar uploadProgressBar;
	private DropDownChoice<DelimiterType>	delimiterTypeDdc;
	private WizardForm							wizardForm;

	public FieldUploadStep1(String id) {
		super(id);
		initialiseDetailForm();
	}

	public FieldUploadStep1(String id, Form<UploadVO> containerForm, WizardForm wizardForm) {
		super(id, "Step 1/5: Select data file to upload", "Select the file containing data, the file type and the specified delimiter, click Next to continue.");

		this.containerForm = containerForm;
		this.setWizardForm(wizardForm);
		initialiseDetailForm();
	}

	@SuppressWarnings({ "unchecked" })
	private void initialiseDropDownChoices() {
		// Initialise Drop Down Choices
		java.util.Collection<DelimiterType> delimiterTypeCollection = iPhenotypicService.getDelimiterTypes();
		ChoiceRenderer delimiterTypeRenderer = new ChoiceRenderer(au.org.theark.phenotypic.web.Constants.DELIMITER_TYPE_NAME, au.org.theark.phenotypic.web.Constants.DELIMITER_TYPE_ID);
		delimiterTypeDdc = new DropDownChoice<DelimiterType>(au.org.theark.phenotypic.web.Constants.UPLOADVO_UPLOAD_DELIMITER_TYPE, (List) delimiterTypeCollection, delimiterTypeRenderer);
		// Set to default delimiterType
		containerForm.getModelObject().getUpload().setDelimiterType(iPhenotypicService.getDelimiterType(new Long(1)));
	}

	public void initialiseDetailForm() {
		// Set up field on form here

		// progress bar for upload
		// uploadProgressBar = new UploadProgressBar("progress",
		// ajaxSimpleUploadForm);

		// fileUpload for payload
		fileUploadField = new FileUploadField(au.org.theark.phenotypic.web.Constants.UPLOADVO_UPLOAD_FILENAME);

		// Initialise Drop Down Choices
		initialiseDropDownChoices();

		attachValidators();
		addComponents();
	}

	protected void attachValidators() {
		// Field validation here
		fileUploadField.setRequired(true).setLabel(new StringResourceModel("error.filename.required", this, new Model<String>("Filename")));
		delimiterTypeDdc.setRequired(true).setLabel(new StringResourceModel("error.delimiterType.required", this, new Model<String>("Delimiter")));
	}

	private void addComponents() {
		// Add components here
		add(fileUploadField);
		add(delimiterTypeDdc);
	}

	@Override
	public void handleWizardState(AbstractWizardForm<?> form, AjaxRequestTarget target) {
		// log.info("Validating Pheno upload file!");
	}

	@Override
	public void onStepOutNext(AbstractWizardForm<?> form, AjaxRequestTarget target) {
		saveFileInMemory();
	}

	public void setWizardForm(WizardForm wizardForm) {
		this.wizardForm = wizardForm;
	}

	public WizardForm getWizardForm() {
		return wizardForm;
	}

	private void saveFileInMemory() {
		// Set study in context
		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(studyId);

		// Retrieve file and store as Blob in database
		// TODO: AJAX-ified and asynchronous and hit database
		FileUpload fileUpload = fileUploadField.getFileUpload();
		containerForm.getModelObject().setFileUpload(fileUpload);

		try {
			// Copy file to BLOB object
			Blob payload = Hibernate.createBlob(fileUpload.getInputStream());
			containerForm.getModelObject().getUpload().setPayload(payload);
		}
		catch (IOException ioe) {
			log.error("Failed to save the uploaded file: " + ioe);
		}

		// Set details of Upload object
		containerForm.getModelObject().getUpload().setStudy(study);
		String filename = containerForm.getModelObject().getFileUpload().getClientFileName();
		String fileFormatName = filename.substring(filename.lastIndexOf('.') + 1).toUpperCase();
		FileFormat fileFormat = new FileFormat();
		fileFormat = iPhenotypicService.getFileFormatByName(fileFormatName);
		containerForm.getModelObject().getUpload().setFileFormat(fileFormat);

		byte[] byteArray = fileUpload.getMD5();
		String checksum = getHex(byteArray);
		containerForm.getModelObject().getUpload().setChecksum(checksum);
		containerForm.getModelObject().getUpload().setFilename(fileUpload.getClientFileName());
		wizardForm.setFileName(fileUpload.getClientFileName());
	}
}