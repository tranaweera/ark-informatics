/**
 * 
 */
package au.org.theark.core.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.wicket.markup.html.form.upload.FileUpload;

import au.org.theark.core.model.study.entity.FileFormat;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyUpload;

/**
 * @author cellis
 * 
 */
@SuppressWarnings("serial")
public class UploadVO implements Serializable {
	private StudyUpload								upload;
	private FileFormat								fileFormat;
	private FileUpload								fileUpload;
	private Study										study;
	private java.util.Collection<StudyUpload>	uploadCollection;
	private int											mode;
	private java.util.Collection<String>		validationMessages;
	private Boolean									updateChkBox;

	public UploadVO() {
		upload = new StudyUpload();
		setUploadCollection(new ArrayList<StudyUpload>());
	}

	/**
	 * @return the upload
	 */
	public StudyUpload getUpload() {
		return upload;
	}

	/**
	 * @param field
	 *           the field to set
	 */
	public void setUpload(StudyUpload upload) {
		this.upload = upload;
	}

	/**
	 * @param uploadCollection
	 *           the uploadCollection to set
	 */
	public void setUploadCollection(java.util.Collection<StudyUpload> uploadCollection) {
		this.uploadCollection = uploadCollection;
	}

	/**
	 * @return the uploadCollection
	 */
	public java.util.Collection<StudyUpload> getUploadCollection() {
		return uploadCollection;
	}

	/**
	 * @param fileFormat
	 *           the fileFormat to set
	 */
	public void setFileFormat(FileFormat fileFormat) {
		this.fileFormat = fileFormat;
	}

	/**
	 * @return the fileFormat
	 */
	public FileFormat getFileFormat() {
		return fileFormat;
	}

	/**
	 * @param mode
	 *           the mode to set
	 */
	public void setMode(int mode) {
		this.mode = mode;
	}

	/**
	 * @return the mode
	 */
	public int getMode() {
		return mode;
	}

	/**
	 * @param validationMessages
	 *           the validationMessages to set
	 */
	public void setValidationMessages(java.util.Collection<String> validationMessages) {
		this.validationMessages = validationMessages;
	}

	/**
	 * @return the validationMessages
	 */
	public java.util.Collection<String> getValidationMessages() {
		return validationMessages;
	}

	/**
	 * @return the validationMessages
	 */
	public String getValidationMessagesAsString() {
		StringBuffer stringBuffer = new StringBuffer("");
		java.util.Collection<String> msgs = getValidationMessages();

		if (getValidationMessages() != null) {
			for (Iterator<String> iterator = msgs.iterator(); iterator.hasNext();) {
				String string = (String) iterator.next();
				stringBuffer.append(string);
				stringBuffer.append("\n");
			}
		}
		return stringBuffer.toString();
	}

	/**
	 * @param study
	 *           the study to set
	 */
	public void setStudy(Study study) {
		this.study = study;
	}

	/**
	 * @return the study
	 */
	public Study getStudy() {
		return study;
	}

	/**
	 * @param updateChkBox
	 *           the updateChkBox to set
	 */
	public void setUpdateChkBox(Boolean updateChkBox) {
		this.updateChkBox = updateChkBox;
	}

	/**
	 * @return the updateChkBox
	 */
	public Boolean getUpdateChkBox() {
		return updateChkBox;
	}

	/**
	 * @param fileUpload
	 *           the fileUpload to set
	 */
	public void setFileUpload(FileUpload fileUpload) {
		this.fileUpload = fileUpload;
	}

	/**
	 * @return the fileUpload
	 */
	public FileUpload getFileUpload() {
		return fileUpload;
	}
}
