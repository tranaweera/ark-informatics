/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.phenotypic.web.component.fieldData.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.behavior.ArkDefaultFormFocusBehavior;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.phenotypic.model.vo.PhenoCollectionVO;
import au.org.theark.phenotypic.service.Constants;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.web.component.fieldData.DetailPanel;

/**
 * @author nivedann
 * 
 */
@SuppressWarnings( { "serial", "unused" })
public class DetailForm extends AbstractDetailForm<PhenoCollectionVO>
{
	@SpringBean(name = Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService				phenotypicService;

	private ContainerForm					fieldContainerForm;

	private int									mode;

	private TextField<String>				fieldDataIdTxtFld;
	private TextField<String>				fieldDataCollectionTxtFld;
	private TextField<String>				fieldDataSubjectUidTxtFld;
	private DateTextField					fieldDataDateCollectedDteFld;
	private TextField<String>				fieldDataFieldTxtFld;
	private TextField<String>				fieldDataValueTxtFld;
	
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService				iArkCommonService;

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param feedBackPanel
	 * @param detailPanel
	 * @param listContainer
	 * @param detailsContainer
	 * @param containerForm
	 * @param viewButtonContainer
	 * @param editButtonContainer
	 * @param detailFormContainer
	 * @param searchPanelContainer
	 */
	public DetailForm(String id, FeedbackPanel feedBackPanel, DetailPanel detailPanel, WebMarkupContainer listContainer, WebMarkupContainer detailsContainer, ContainerForm containerForm,
			WebMarkupContainer viewButtonContainer, WebMarkupContainer editButtonContainer, WebMarkupContainer detailFormContainer, WebMarkupContainer searchPanelContainer)
	{

		super(id, feedBackPanel, listContainer, detailsContainer, detailFormContainer, searchPanelContainer, viewButtonContainer, editButtonContainer, containerForm);
	}

	public void initialiseDetailForm()
	{
		fieldDataIdTxtFld = new TextField<String>(au.org.theark.phenotypic.web.Constants.FIELD_DATAVO_FIELD_DATA_ID);
		fieldDataCollectionTxtFld = new TextField<String>(au.org.theark.phenotypic.web.Constants.FIELD_DATAVO_FIELD_DATA_COLLECTION_NAME);
		fieldDataSubjectUidTxtFld = new TextField<String>(au.org.theark.phenotypic.web.Constants.FIELD_DATAVO_FIELD_DATA_SUBJECTUID);
		fieldDataDateCollectedDteFld = new DateTextField(au.org.theark.phenotypic.web.Constants.FIELD_DATAVO_FIELD_DATA_DATE_COLLECTED, au.org.theark.core.Constants.DD_MM_YYYY);
		fieldDataFieldTxtFld = new TextField<String>(au.org.theark.phenotypic.web.Constants.FIELD_DATAVO_FIELD_DATA_FIELD_NAME);
		fieldDataValueTxtFld = new TextField<String>(au.org.theark.phenotypic.web.Constants.FIELD_DATAVO_FIELD_DATA_VALUE);
		fieldDataValueTxtFld.add(new ArkDefaultFormFocusBehavior());

		ArkDatePicker datePicker = new ArkDatePicker();
		datePicker.bind(fieldDataDateCollectedDteFld);
		fieldDataDateCollectedDteFld.add(datePicker);

		attachValidators();
		addComponents();
	}

	protected void attachValidators()
	{
		
	}

	private void addComponents()
	{
		detailPanelFormContainer.add(fieldDataIdTxtFld.setEnabled(false));
		detailPanelFormContainer.add(fieldDataCollectionTxtFld.setEnabled(false));
		detailPanelFormContainer.add(fieldDataSubjectUidTxtFld.setEnabled(false));
		detailPanelFormContainer.add(fieldDataDateCollectedDteFld.setEnabled(false));
		detailPanelFormContainer.add(fieldDataFieldTxtFld.setEnabled(false));
		detailPanelFormContainer.add(fieldDataValueTxtFld);

		add(detailPanelFormContainer);
	}

	@Override
	protected void onSave(Form<PhenoCollectionVO> containerForm, AjaxRequestTarget target)
	{

		if (containerForm.getModelObject().getFieldData().getId() == null)
		{
			// Save the Field data
			phenotypicService.createFieldData(containerForm.getModelObject().getFieldData());
			this.info("Field Data " + containerForm.getModelObject().getFieldData().getId() + " was created successfully");
			processErrors(target);
		}
		else
		{
			// Update the Field data
			phenotypicService.updateFieldData(containerForm.getModelObject().getFieldData());
			this.info("Field Data " + containerForm.getModelObject().getFieldData().getId() + " was updated successfully");
			processErrors(target);
		}

		onSavePostProcess(target);
		// TODO:(CE) To handle Business and System Exceptions here
	}

	protected void onCancel(AjaxRequestTarget target)
	{
		PhenoCollectionVO phenoCollectionVo = new PhenoCollectionVO();
		containerForm.setModelObject(phenoCollectionVo);
	}

	@Override
	protected void processErrors(AjaxRequestTarget target)
	{
		target.addComponent(feedBackPanel);
	}

	public AjaxButton getDeleteButton()
	{
		return deleteButton;
	}

	public void setDeleteButton(AjaxButton deleteButton)
	{
		this.deleteButton = deleteButton;
	}

	/**
	 * 
	 */
	protected void onDeleteConfirmed(AjaxRequestTarget target, String selection, ModalWindow selectModalWindow)
	{
		// TODO:(CE) To handle Business and System Exceptions here
		phenotypicService.deleteFieldData(containerForm.getModelObject().getFieldData());
		this.info("Field data " + containerForm.getModelObject().getFieldData().getId() + " was deleted successfully");

		// Display delete confirmation message
		target.addComponent(feedBackPanel);
		// TODO Implement Exceptions in PhentoypicService
		// } catch (UnAuthorizedOperation e) { this.error("You are not authorised to manage study components for the given study " +
		// study.getName()); processFeedback(target); } catch (ArkSystemException e) {
		// this.error("A System error occured, we will have someone contact you."); processFeedback(target); }

		// Close the confirm modal window
		selectModalWindow.close(target);
		// Move focus back to Search form
		PhenoCollectionVO phenoCollectionVo = new PhenoCollectionVO();
		containerForm.setModelObject(phenoCollectionVo);
		onCancel(target);
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractDetailForm#isNew()
	 */
	@Override
	protected boolean isNew() {
		if(containerForm.getModelObject().getFieldData().getId() == null){
			return true;
		}else{
			return false;	
		}
		
	}
}