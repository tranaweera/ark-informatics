package au.org.theark.phenotypic.web.component.phenoUpload;

import java.io.IOException;
import java.sql.Blob;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.form.AbstractWizardForm;
import au.org.theark.core.web.form.AbstractWizardStepPanel;
import au.org.theark.phenotypic.model.entity.DelimiterType;
import au.org.theark.phenotypic.model.entity.FileFormat;
import au.org.theark.phenotypic.model.entity.PhenoCollectionUpload;
import au.org.theark.phenotypic.model.vo.UploadVO;
import au.org.theark.phenotypic.service.Constants;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.web.component.phenoUpload.form.WizardForm;

/**
 * The first step of this wizard.
 */
public class PhenoUploadStep1 extends AbstractWizardStepPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4272918747277155957L;

	public java.util.Collection<String> validationMessages = null;

	@SpringBean(name = Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService phenotypicService;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;

	private transient Logger log = LoggerFactory.getLogger(PhenoUploadContainerPanel.class);

	private Form<UploadVO> containerForm;

	private TextField<String> uploadIdTxtFld;
	private DropDownChoice<FileFormat> fileFormatDdc;
	private FileUploadField fileUploadField;
	// private UploadProgressBar uploadProgressBar;
	private DropDownChoice<DelimiterType> delimiterTypeDdc;
	private WizardForm wizardForm;
	static final String HEXES = "0123456789ABCDEF";

	public PhenoUploadStep1(String id) {
		super(id);
		initialiseDetailForm();
	}

	public PhenoUploadStep1(String id, Form<UploadVO> containerForm, WizardForm wizardForm) {
		super(id,
			"Step 1/5: Select data file to upload",
			"Select the file containing data, the file type and the specified delimiter, click Next to continue.");

		this.containerForm = containerForm;
		this.setWizardForm(wizardForm);
		initialiseDetailForm();
	}

	@SuppressWarnings({ "unchecked"})
	private void initialiseDropDownChoices() {
		// Initialise Drop Down Choices
		java.util.Collection<FileFormat> fieldFormatCollection = phenotypicService.getFileFormats();
		ChoiceRenderer fieldFormatRenderer = new ChoiceRenderer(
				au.org.theark.phenotypic.web.Constants.FILE_FORMAT_NAME,
				au.org.theark.phenotypic.web.Constants.FILE_FORMAT_ID);
		fileFormatDdc = new DropDownChoice<FileFormat>(
				au.org.theark.phenotypic.web.Constants.UPLOADVO_UPLOAD_FILE_FORMAT,
				(List) fieldFormatCollection, fieldFormatRenderer);

		java.util.Collection<DelimiterType> delimiterTypeCollection = phenotypicService
				.getDelimiterTypes();
		ChoiceRenderer delimiterTypeRenderer = new ChoiceRenderer(
				au.org.theark.phenotypic.web.Constants.DELIMITER_TYPE_NAME,
				au.org.theark.phenotypic.web.Constants.DELIMITER_TYPE_ID);
		delimiterTypeDdc = new DropDownChoice<DelimiterType>(
				au.org.theark.phenotypic.web.Constants.UPLOADVO_UPLOAD_DELIMITER_TYPE,
				(List) delimiterTypeCollection, delimiterTypeRenderer);
	}

	public void initialiseDetailForm() {
		// Set up field on form here
		uploadIdTxtFld = new TextField<String>(au.org.theark.phenotypic.web.Constants.UPLOADVO_UPLOAD_ID);
		// uploadFilenameTxtFld = new
		// TextField<String>(au.org.theark.phenotypic.web.Constants.UPLOADVO_UPLOAD_FILENAME);

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
		fileUploadField.setRequired(true).setLabel(
				new StringResourceModel("error.filename.required", this,
						new Model<String>("Filename")));
		fileFormatDdc.setRequired(true).setLabel(
				new StringResourceModel("error.fileFormat.required", this,
						new Model<String>("File Format")));
		delimiterTypeDdc.setRequired(true).setLabel(
				new StringResourceModel("error.delimiterType.required", this,
						new Model<String>("Delimiter")));
	}

	private void addComponents() {
		// Add components here
		add(uploadIdTxtFld.setEnabled(false));
		add(fileUploadField);
		add(fileFormatDdc);
		add(delimiterTypeDdc);
	}
	
	@Override
	public void handleWizardState(AbstractWizardForm<?> form, AjaxRequestTarget target) {
		log.info("Validating Pheno upload file!");
	}
	
	@Override
	public void onStepOutNext(AbstractWizardForm<?> form, AjaxRequestTarget target)
	{
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

		// Get Collection in context
		Long sessionPhenoCollectionId = (Long) SecurityUtils
				.getSubject()
				.getSession()
				.getAttribute(au.org.theark.phenotypic.web.Constants.SESSION_PHENO_COLLECTION_ID);

		// Retrieve file and store as Blob in database
		// TODO: AJAX-ified and asynchronous and hit database
		FileUpload fileUpload = fileUploadField.getFileUpload();
		containerForm.getModelObject().setFileUpload(fileUpload);

		try {
			// Copy file to BLOB object
			Blob payload = Hibernate.createBlob(fileUpload.getInputStream());
			containerForm.getModelObject().getUpload().setPayload(payload);
		} catch (IOException ioe) {
			System.out.println("Failed to save the uploaded file: " + ioe);
		}

		// Set details of Upload object
		containerForm.getModelObject().getUpload().setStudy(study);

		byte[] byteArray = fileUpload.getMD5();
		String checksum = getHex(byteArray);
		containerForm.getModelObject().getUpload().setChecksum(checksum);
		containerForm.getModelObject().getUpload().setFilename(fileUpload.getClientFileName());
		wizardForm.setFileName(fileUpload.getClientFileName());

		containerForm.getModelObject().setPhenoCollection(phenotypicService.getPhenoCollection(sessionPhenoCollectionId));

		// Set details of link table object
		PhenoCollectionUpload phenoCollectionUpload = new PhenoCollectionUpload();
		phenoCollectionUpload.setCollection(phenotypicService.getPhenoCollection(sessionPhenoCollectionId));
		phenoCollectionUpload.setUpload(containerForm.getModelObject().getUpload());
		containerForm.getModelObject().setPhenoCollectionUpload(phenoCollectionUpload);
	}
	
	public static String getHex(byte[] raw) 
	{
		if (raw == null) {
			return null;
		}
		final StringBuilder hex = new StringBuilder(2 * raw.length);
		for (final byte b : raw) {
			hex.append(HEXES.charAt((b & 0xF0) >> 4)).append(
					HEXES.charAt((b & 0x0F)));
		}
		return hex.toString();
	}
}