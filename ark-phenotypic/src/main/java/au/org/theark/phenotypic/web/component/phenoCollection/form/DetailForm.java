package au.org.theark.phenotypic.web.component.phenoCollection.form;

import java.util.Collection;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.markup.html.form.palette.Palette;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.pheno.entity.Field;
import au.org.theark.core.model.pheno.entity.PhenoCollection;
import au.org.theark.core.model.pheno.entity.Status;
import au.org.theark.core.util.ContextHelper;
import au.org.theark.core.web.behavior.ArkDefaultFormFocusBehavior;
import au.org.theark.core.web.component.AjaxDeleteButton;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.core.web.form.AbstractContainerForm;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.phenotypic.model.vo.PhenoCollectionVO;
import au.org.theark.phenotypic.service.Constants;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.web.component.field.form.ContainerForm;
import au.org.theark.phenotypic.web.component.phenoCollection.DetailPanel;

/**
 * @author nivedann
 * 
 */
@SuppressWarnings({ "serial", "unchecked", "unused" })
public class DetailForm extends AbstractDetailForm<PhenoCollectionVO> {
	@SpringBean(name = Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService			iPhenotypicService;

	private ContainerForm				fieldContainerForm;

	private int								mode;

	private TextField<String>			idTxtFld;
	private TextField<String>			nameTxtFld;
	private DropDownChoice<Status>	statusDdc;
	private TextArea<String>			descriptionTxtAreaFld;
	private DateTextField				startDateTxtFld;
	private DateTextField				endDateTxtFld;

	// Field selection Palette
	private Palette						fieldPalette;

	private AjaxButton					clearCollectionButton;

	private WebMarkupContainer			arkContextMarkup;

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
	public DetailForm(String id, FeedbackPanel feedBackPanel, DetailPanel detailPanel, WebMarkupContainer listContainer, WebMarkupContainer detailsContainer,
			AbstractContainerForm<PhenoCollectionVO> containerForm, WebMarkupContainer viewButtonContainer, WebMarkupContainer editButtonContainer, WebMarkupContainer detailFormContainer,
			WebMarkupContainer searchPanelContainer, WebMarkupContainer arkContextMarkup) {

		super(id, feedBackPanel, listContainer, detailsContainer, detailFormContainer, searchPanelContainer, viewButtonContainer, editButtonContainer, containerForm);

		this.arkContextMarkup = arkContextMarkup;
	}

	private void initStatusDdc() {
		java.util.Collection<Status> statusCollection = iPhenotypicService.getStatus();
		ChoiceRenderer statusRenderer = new ChoiceRenderer(au.org.theark.phenotypic.web.Constants.STATUS_NAME, au.org.theark.phenotypic.web.Constants.STATUS_ID);
		statusDdc = new DropDownChoice<Status>(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTIONVO_PHENO_COLLECTION_STATUS, (List) statusCollection, statusRenderer);
	}

	public void initialiseDetailForm() {
		idTxtFld = new TextField<String>(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTIONVO_PHENO_COLLECTION_ID);
		nameTxtFld = new TextField<String>(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTIONVO_PHENO_COLLECTION_NAME);
		nameTxtFld.add(new ArkDefaultFormFocusBehavior());
		descriptionTxtAreaFld = new TextArea<String>(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTIONVO_PHENO_COLLECTION_DESCRIPTION);
		startDateTxtFld = new DateTextField(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTIONVO_PHENO_COLLECTION_START_DATE, au.org.theark.core.Constants.DD_MM_YYYY);
		endDateTxtFld = new DateTextField(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTIONVO_PHENO_COLLECTION_END_DATE, au.org.theark.core.Constants.DD_MM_YYYY);

		ArkDatePicker startDatePicker = new ArkDatePicker();
		startDatePicker.bind(startDateTxtFld);
		startDateTxtFld.add(startDatePicker);

		ArkDatePicker endDatePicker = new ArkDatePicker();
		endDatePicker.bind(endDateTxtFld);
		endDateTxtFld.add(endDatePicker);

		clearCollectionButton = new AjaxDeleteButton("clearCollection", new StringResourceModel("button.clearCollection.confirm", this, null), new StringResourceModel("button.clearCollection", this,
				null)) {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				iPhenotypicService.clearPhenoCollection(containerForm.getModelObject().getPhenoCollection());
				this.info("Phenotypic collection " + containerForm.getModelObject().getPhenoCollection().getName() + " was cleared successfully");
				processErrors(target);
			}

			@Override
			public boolean isVisible() {
				// TODO NN Uncomment after User Management UI is completed
				// return isActionPermitted(Constants.DELETE);
				return (true);
			}
		};

		// Initialise Drop Down Choices
		initStatusDdc();

		// Initialise Field Palette
		initFieldPalette();

		attachValidators();
		addComponents();
	}

	private void initFieldPalette() {
		CompoundPropertyModel<PhenoCollectionVO> cpm = (CompoundPropertyModel<PhenoCollectionVO>) containerForm.getModel();
		IChoiceRenderer<String> renderer = new ChoiceRenderer<String>("name", "id");
		PropertyModel<Collection<Field>> selectedModPm = new PropertyModel<Collection<Field>>(cpm, "fieldsSelected");
		PropertyModel<Collection<Field>> availableModPm = new PropertyModel<Collection<Field>>(cpm, "fieldsAvailable");

		fieldPalette = new Palette(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTIONVO_FIELD_PALETTE, selectedModPm, availableModPm, renderer, au.org.theark.core.Constants.ROWS_PER_PAGE, false) {
			@Override
			public ResourceReference getCSS() {
				return null;
			}

			// TODO: ARK-177 implement disabling of selected fields if fieldData exist
		};
	}

	protected void attachValidators() {
		nameTxtFld.setRequired(true).setLabel(new StringResourceModel("error.phenoCollection.name.required", this, new Model<String>("Name")));
		statusDdc.setRequired(true).setLabel(new StringResourceModel("error.phenoCollection.status.required", this, new Model<String>("Status")));
	}

	private void addComponents() {
		detailPanelFormContainer.add(idTxtFld.setEnabled(false));
		detailPanelFormContainer.add(nameTxtFld);
		detailPanelFormContainer.add(descriptionTxtAreaFld);
		detailPanelFormContainer.add(statusDdc);
		detailPanelFormContainer.add(startDateTxtFld);
		detailPanelFormContainer.add(endDateTxtFld);
		detailPanelFormContainer.add(fieldPalette);

		// Custom clear collection button
		editButtonContainer.add(clearCollectionButton);

		add(detailPanelFormContainer);
	}

	@Override
	protected void onSave(Form<PhenoCollectionVO> containerForm, AjaxRequestTarget target) {

		if (containerForm.getModelObject().getPhenoCollection().getId() == null) {
			// Save
			iPhenotypicService.createCollection(containerForm.getModelObject());
			this.info("Phenotypic collection " + containerForm.getModelObject().getPhenoCollection().getName() + " was created successfully");
			processErrors(target);
		}
		else {
			// Update
			iPhenotypicService.updateCollection(containerForm.getModelObject());
			this.info("Phenotypic collection " + containerForm.getModelObject().getPhenoCollection().getName() + " was updated successfully");
			processErrors(target);
		}

		// Reset context item
		ContextHelper contextHelper = new ContextHelper();
		SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.phenotypic.web.Constants.SESSION_PHENO_COLLECTION_ID, containerForm.getModelObject().getPhenoCollection().getId());
		contextHelper.setPhenoContextLabel(target, containerForm.getModelObject().getPhenoCollection().getName(), arkContextMarkup);

		onSavePostProcess(target);
	}

	protected void onCancel(AjaxRequestTarget target) {
		PhenoCollectionVO phenoCollectionVo = new PhenoCollectionVO();
		containerForm.setModelObject(phenoCollectionVo);

		java.util.Collection<PhenoCollection> phenoCollectionCollection = iPhenotypicService.searchPhenotypicCollection(phenoCollectionVo.getPhenoCollection());
		containerForm.getModelObject().setPhenoCollectionCollection(phenoCollectionCollection);
	}

	@Override
	protected void processErrors(AjaxRequestTarget target) {
		target.addComponent(feedBackPanel);
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
	protected void onDeleteConfirmed(AjaxRequestTarget target, String selection, ModalWindow selectModalWindow) {
		// TODO:(CE) To handle Business and System Exceptions here
		iPhenotypicService.deleteCollection(containerForm.getModelObject());
		this.info("Phenotypic collection " + containerForm.getModelObject().getPhenoCollection().getName() + " was deleted successfully");

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
		editCancelProcess(target);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#isNew()
	 */
	@Override
	protected boolean isNew() {
		if (containerForm.getModelObject().getPhenoCollection().getId() == null) {
			return true;
		}
		else {
			return false;
		}
	}

	public AjaxButton getClearCollectionButton() {
		return clearCollectionButton;
	}

	public void setClearCollectionButton(AjaxButton clearCollectionButton) {
		this.clearCollectionButton = clearCollectionButton;
	}
}