/*******************************************************************************
 * Copyright (c) 2011  University of Western Australia. All rights reserved.
 * 
 * This file is part of The Ark.
 * 
 * The Ark is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * 
 * The Ark is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package au.org.theark.study.web.component.subjectFile.form;

import java.io.IOException;
import java.sql.Blob;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.file.File;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.StudyComp;
import au.org.theark.core.model.study.entity.StudyCompStatus;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.SubjectVO;
import au.org.theark.core.web.behavior.ArkDefaultFormFocusBehavior;
import au.org.theark.core.web.form.AbstractContainerForm;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;

/**
 * @author cellis
 * 
 */
@SuppressWarnings({ "serial", "unused" })
public class DetailForm extends AbstractDetailForm<SubjectVO> {
	private transient Logger						log	= LoggerFactory.getLogger(DetailForm.class);
	@SpringBean(name = au.org.theark.study.web.Constants.STUDY_SERVICE)
	private IStudyService							studyService;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService						iArkCommonService;

	private int											mode;

	private TextField<String>						subjectFileIdTxtFld;
	private TextField<String>						consentFileFilenameTxtFld;
	private FileUploadField							fileSubjectFileField;
	private DropDownChoice<StudyCompStatus>	studyComponentChoice;
	private TextArea<String>						commentsTxtArea;

	// private ConsentFileProgressBar uploadProgressBar;
	
	
	public DetailForm(String id, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVO,AbstractContainerForm<SubjectVO> containerForm){
		
		super(id,feedBackPanel,containerForm,arkCrudContainerVO);
	}

	@SuppressWarnings("unchecked")
	private void initialiseDropDownChoices() {
		// Initialise Drop Down Choices
		List<StudyComp> studyCompList = iArkCommonService.getStudyComponent();
		ChoiceRenderer<StudyComp> defaultChoiceRenderer = new ChoiceRenderer<StudyComp>(Constants.NAME, Constants.ID);
		studyComponentChoice = new DropDownChoice(Constants.SUBJECT_FILE_STUDY_COMP, studyCompList, defaultChoiceRenderer);
	}

	public void initialiseDetailForm() {
		// Set up field on form here
		subjectFileIdTxtFld = new TextField<String>(au.org.theark.study.web.Constants.SUBJECT_FILE_ID);

		// progress bar for upload
		// uploadProgressBar = new ConsentFileProgressBar("progress", ajaxSimpleConsentFileForm);

		// fileSubjectFile for payload (attached to filename key)
		fileSubjectFileField = new FileUploadField(au.org.theark.study.web.Constants.SUBJECT_FILE_FILENAME);
		fileSubjectFileField.setRequired(true);
		fileSubjectFileField.add(new ArkDefaultFormFocusBehavior());

		// Initialise Drop Down Choices
		initialiseDropDownChoices();

		commentsTxtArea = new TextArea<String>(au.org.theark.study.web.Constants.SUBJECT_FILE_COMMENTS);

		attachValidators();
		addComponents();
	}

	protected void attachValidators() {
		// Field validation here
		fileSubjectFileField.setRequired(true).setLabel(new StringResourceModel("subjectFile.filename.required", this, null));
	}

	private void addComponents() {
		// Add components
		subjectFileIdTxtFld.setEnabled(false);
		subjectFileIdTxtFld.setVisible(true);
		
		arkCrudContainerVO.getDetailPanelFormContainer().add(subjectFileIdTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(fileSubjectFileField);
		arkCrudContainerVO.getDetailPanelFormContainer().add(studyComponentChoice);
		arkCrudContainerVO.getDetailPanelFormContainer().add(commentsTxtArea);

		// TODO: AJAXify the form to show progress bar
		// ajaxSimpleConsentFileForm.add(new ConsentFileProgressBar("progress", ajaxSimpleConsentFileForm));
		// add(ajaxSimpleConsentFileForm);
		add(arkCrudContainerVO.getDetailPanelFormContainer());
	}

	private void createDirectoryIfNeeded(String directoryName) {
		File theDir = new File(directoryName);

		// if the directory does not exist, create it
		if (!theDir.exists()) {
			log.debug("Creating directory: " + directoryName);
			theDir.mkdir();
		}
	}

	@Override
	protected void onSave(Form<SubjectVO> containerForm, AjaxRequestTarget target) {
		LinkSubjectStudy linkSubjectStudy = null;
		Long sessionPersonId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
		try {
			linkSubjectStudy = iArkCommonService.getSubject(sessionPersonId);
			containerForm.getModelObject().getSubjectFile().setLinkSubjectStudy(linkSubjectStudy);

			// Implement Save/Update
			if (containerForm.getModelObject().getSubjectFile().getId() == null) {
				// required for file uploads
				setMultiPart(true);

				// Retrieve file and store as Blob in database
				// TODO: AJAX-ified and asynchronous and hit database
				FileUpload fileSubjectFile = fileSubjectFileField.getFileUpload();

				try {
					// Copy file to BLOB object
					Blob payload = Hibernate.createBlob(fileSubjectFile.getInputStream());
					containerForm.getModelObject().getSubjectFile().setPayload(payload);
				}
				catch (IOException ioe) {
					log.error("Failed to save the uploaded file: " + ioe);
				}

				byte[] byteArray = fileSubjectFile.getMD5();
				String checksum = getHex(byteArray);

				// Set details of ConsentFile object
				containerForm.getModelObject().getSubjectFile().setChecksum(checksum);
				containerForm.getModelObject().getSubjectFile().setFilename(fileSubjectFile.getClientFileName());

				// Save
				studyService.create(containerForm.getModelObject().getSubjectFile());
				this.info("Attachment " + containerForm.getModelObject().getSubjectFile().getFilename() + " was created successfully");
				processErrors(target);
			}
			else {
				// Update
				studyService.update(containerForm.getModelObject().getSubjectFile());
				this.info("Attachment " + containerForm.getModelObject().getSubjectFile().getFilename() + " was updated successfully");
				processErrors(target);
			}

			onSavePostProcess(target,arkCrudContainerVO);
			AjaxButton editButton = (AjaxButton) arkCrudContainerVO.getViewButtonContainer().get("edit");
			editButton.setEnabled(false);
			
		}
		catch (EntityNotFoundException e) {
			this.error("The record you tried to update is no longer available in the system");
			processErrors(target);
		}
		catch (ArkSystemException e) {
			this.error(e.getMessage());
			processErrors(target);
		}
	}

	static final String	HEXES	= "0123456789ABCDEF";

	public static String getHex(byte[] raw) {
		if (raw == null) {
			return null;
		}
		final StringBuilder hex = new StringBuilder(2 * raw.length);
		for (final byte b : raw) {
			hex.append(HEXES.charAt((b & 0xF0) >> 4)).append(HEXES.charAt((b & 0x0F)));
		}
		return hex.toString();
	}

	protected void onCancel(AjaxRequestTarget target) {
		SubjectVO subjectVo = new SubjectVO();
		LinkSubjectStudy linkSubjectStudy = null;
		Long sessionPersonId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
		try {
			linkSubjectStudy = iArkCommonService.getSubject(sessionPersonId);
		}
		catch (EntityNotFoundException e) {
			this.error("The Person/Subject in context does not exist in the system. Please contact support.");
			processErrors(target);
		}
		subjectVo.setLinkSubjectStudy(linkSubjectStudy);
		containerForm.setModelObject(subjectVo);
	}

	@Override
	protected void processErrors(AjaxRequestTarget target) {
		target.add(feedBackPanel);
	}

	public AjaxButton getDeleteButton() {
		return deleteButton;
	}

	public void setDeleteButton(AjaxButton deleteButton) {
		this.deleteButton = deleteButton;
	}

	/**
	 * 
	 */
	protected void onDeleteConfirmed(AjaxRequestTarget target, String selection) {
		// required for file uploads
		setMultiPart(true);

		// TODO:(CE) To handle Business and System Exceptions here
		// studyService.deleteConsentFile(containerForm.getModelObject().getSubjectFile());
		// this.info("Consent file " + containerForm.getModelObject().getSubjectFile().getFilename() + " was deleted successfully");

		// Display delete confirmation message
		target.add(feedBackPanel);
		// TODO Implement Exceptions in PhentoypicService
		// } catch (UnAuthorizedOperation e) { this.error("You are not authorised to manage study components for the given study " +
		// study.getName()); processFeedback(target); } catch (ArkSystemException e) {
		// this.error("A System error occurred, we will have someone contact you."); processFeedback(target); }

		// Move focus back to Search form
		SubjectVO subjectVO = new SubjectVO();
		setModelObject(subjectVO);
		onCancel(target);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#isNew()
	 */
	@Override
	protected boolean isNew() {
		if (containerForm.getModelObject().getSubjectFile().getId() == null) {
			return true;
		}
		else {
			return false;
		}

	}
}
