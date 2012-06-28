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
package au.org.theark.study.web.component.subjectUpload.form;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.DelimiterType;
import au.org.theark.core.model.study.entity.FileFormat;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.UploadVO;
import au.org.theark.core.web.behavior.ArkDefaultFormFocusBehavior;
import au.org.theark.core.web.form.AbstractDetailForm;

/**
 * @author cellis
 * 
 */
public class DetailForm extends AbstractDetailForm<UploadVO> {

	private static final long					serialVersionUID	= 1L;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService					iArkCommonService;

	// REMOVED BY TRAV private int mode;

	private TextField<String>					uploadIdTxtFld;
//	private TextField<String> uploadFilenameTxtFld;
	private DropDownChoice<FileFormat>		fileFormatDdc;
	private FileUploadField						fileUploadField;
	// private UploadProgressBar uploadProgressBar;
	private DropDownChoice<DelimiterType>	delimiterTypeDdc;
	@SuppressWarnings("unused")
	private ArkFunction							arkFunction;

	public DetailForm(String id, FeedbackPanel feedBackPanel, ContainerForm containerForm, ArkCrudContainerVO arkCrudContainerVO, ArkFunction arkFunction) {
		super(id, feedBackPanel, containerForm, arkCrudContainerVO);
		this.arkFunction = arkFunction;
	}

	@SuppressWarnings("unchecked")
	private void initialiseDropDownChoices() {
		// Initialise Drop Down Choices
		java.util.Collection<FileFormat> fieldFormatCollection = iArkCommonService.getFileFormats();
		ChoiceRenderer fieldFormatRenderer = new ChoiceRenderer(au.org.theark.study.web.Constants.FILE_FORMAT_NAME, au.org.theark.study.web.Constants.FILE_FORMAT_ID);
		fileFormatDdc = new DropDownChoice<FileFormat>(au.org.theark.study.web.Constants.UPLOADVO_UPLOAD_FILE_FORMAT, (List) fieldFormatCollection, fieldFormatRenderer);

		java.util.Collection<DelimiterType> delimiterTypeCollection = iArkCommonService.getDelimiterTypes();
		ChoiceRenderer delimiterTypeRenderer = new ChoiceRenderer(au.org.theark.study.web.Constants.DELIMITER_TYPE_NAME, au.org.theark.study.web.Constants.DELIMITER_TYPE_ID);
		delimiterTypeDdc = new DropDownChoice<DelimiterType>(au.org.theark.study.web.Constants.UPLOADVO_UPLOAD_DELIMITER_TYPE, (List) delimiterTypeCollection, delimiterTypeRenderer);

		java.util.Collection<String> uploadTypeCollection = iArkCommonService.getUploadTypes();
		ChoiceRenderer uploadTypeRenderer = new ChoiceRenderer(au.org.theark.study.web.Constants.DELIMITER_TYPE_NAME, au.org.theark.study.web.Constants.DELIMITER_TYPE_ID);
		delimiterTypeDdc = new DropDownChoice<DelimiterType>(au.org.theark.study.web.Constants.UPLOADVO_UPLOAD_DELIMITER_TYPE, (List) delimiterTypeCollection, delimiterTypeRenderer);
	}

	public void initialiseDetailForm() {
		// Do not allow delete for upload - see to do in onDeleteConfirmed(..)
		deleteButton.setVisible(false);

		// Set up field on form here
		uploadIdTxtFld = new TextField<String>(au.org.theark.study.web.Constants.UPLOADVO_UPLOAD_ID);
//		uploadFilenameTxtFld = new TextField<String>(au.org.theark.study.web.Constants.UPLOADVO_UPLOAD_FILENAME);

		// progress bar for upload
		// uploadProgressBar = new UploadProgressBar("progress", ajaxSimpleUploadForm);

		// fileUpload for payload
		fileUploadField = new FileUploadField(au.org.theark.study.web.Constants.UPLOADVO_UPLOAD_FILENAME);
		fileUploadField.add(new ArkDefaultFormFocusBehavior());

		// Initialise Drop Down Choices
		initialiseDropDownChoices();

		attachValidators();
		addDetailFormComponents();
	}

	protected void attachValidators() {
		// Field validation here
		fileUploadField.setRequired(true).setLabel(new StringResourceModel("error.filename.required", this, new Model<String>("Filename")));
		fileFormatDdc.setRequired(true).setLabel(new StringResourceModel("error.fileFormat.required", this, new Model<String>("File Format")));
		delimiterTypeDdc.setRequired(true).setLabel(new StringResourceModel("error.delimiterType.required", this, new Model<String>("Delimiter")));
	}

	@Override
	protected void onSave(Form<UploadVO> containerForm, AjaxRequestTarget target) {
		System.err.println("*********************************************************************************" +
				"******************************************************************************************************************" +
				"WHY ARE WE SAVING HERE TOO???? ARE WE?????");
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
		// Implement Cancel
		UploadVO uploadVo = new UploadVO();
		containerForm.setModelObject(uploadVo);
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


	protected void onDeleteConfirmed(AjaxRequestTarget target, String selection) {
		/*
		 * TO DO: DELETE of uploaded file is not supported till we can verify whether all subjects within the upload have also been deleted. At present,
		 * there is no linking table clearly indicating which subjects came from which upload (i.e. will need to be looked at 1st).
		 */
		// setMultiPart(true); // multipart required for file uploads
		// 
		// iArkCommonService.deleteUpload(containerForm.getModelObject().getUpload());
		// this.info("Subject upload " + containerForm.getModelObject().getUpload().getFilename() + " was deleted successfully");
		//
		// // Display delete confirmation message
		// target.add(feedBackPanel);

		// // } catch (UnAuthorizedOperation e) { this.error("You are not authorised to manage study components for the given study " +
		// // study.getName()); processFeedback(target); } catch (ArkSystemException e) {
		// // this.error("A System error occured, we will have someone contact you."); processFeedback(target); }
		// // Move focus back to Search form
		// UploadVO uploadVo = new UploadVO();
		// setModelObject(uploadVo);
		// onCancel(target);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#isNew()
	 */
	@Override
	protected boolean isNew() {
		if (containerForm.getModelObject().getUpload().getId() == null) {
			return true;
		}
		else {
			return false;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#addDetailFormComponents()
	 */
	@Override
	protected void addDetailFormComponents() {
		// Add components here eg:
		arkCrudContainerVO.getDetailPanelFormContainer().add(uploadIdTxtFld.setEnabled(false));
		arkCrudContainerVO.getDetailPanelFormContainer().add(fileUploadField);
		arkCrudContainerVO.getDetailPanelFormContainer().add(fileFormatDdc);
		arkCrudContainerVO.getDetailPanelFormContainer().add(delimiterTypeDdc);

		add(arkCrudContainerVO.getDetailPanelFormContainer());

	}
}
