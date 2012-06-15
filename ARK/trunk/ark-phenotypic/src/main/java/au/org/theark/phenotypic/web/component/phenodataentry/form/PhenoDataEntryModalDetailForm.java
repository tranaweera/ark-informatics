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
package au.org.theark.phenotypic.web.component.phenodataentry.form;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityCannotBeRemoved;
import au.org.theark.core.model.pheno.entity.PhenoCollection;
import au.org.theark.core.model.pheno.entity.QuestionnaireStatus;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.CustomFieldGroup;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.PhenoDataCollectionVO;
import au.org.theark.core.web.behavior.ArkDefaultFormFocusBehavior;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.core.web.form.AbstractModalDetailForm;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.web.Constants;
import au.org.theark.phenotypic.web.component.phenodataentry.PhenoDataDataViewPanel;

/**
 * Detail form for Phenotypic Collection, as displayed within a modal window
 * 
 * @author elam
 */
public class PhenoDataEntryModalDetailForm extends AbstractModalDetailForm<PhenoDataCollectionVO> {

	private static final long					serialVersionUID	= 2727419197330261916L;
	@SuppressWarnings("unused")
	private static final Logger				log					= LoggerFactory.getLogger(PhenoDataEntryModalDetailForm.class);

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>			iArkCommonService;

	@SpringBean(name = au.org.theark.phenotypic.service.Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService					iPhenotypicService;

	private TextField<String>					idTxtFld;
	private TextField<String>					nameTxtFld;
	private DropDownChoice<CustomFieldGroup>	questionnaireDdc;
	private DropDownChoice<QuestionnaireStatus>	statusDdc;
	private TextArea<String>					descriptionTxtAreaFld;
	private DateTextField						recordDateTxtFld;
	private DropDownChoice<ArkUser>			reviewedByDdc;
	private DateTextField						reviewedDateTxtFld;

	private Panel									phenoCollectionDataEntryPanel;
	private ModalWindow							modalWindow;
	private AjaxPagingNavigator				dataEntryNavigator;
	private WebMarkupContainer					dataEntryWMC;
	protected Label								jQueryLabel;

	/**
	 * Constructor
	 * @param id
	 * @param feedBackPanel
	 * @param arkCrudContainerVo
	 * @param modalWindow
	 * @param cpModel
	 * @param jQueryLabel
	 */
	public PhenoDataEntryModalDetailForm(String id, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVo, ModalWindow modalWindow, CompoundPropertyModel<PhenoDataCollectionVO> cpModel, Label jQueryLabel) {
		super(id, feedBackPanel, arkCrudContainerVo, cpModel);
		this.modalWindow = modalWindow;
		refreshEntityFromBackend();
		this.jQueryLabel = jQueryLabel;
	}

	protected void refreshEntityFromBackend() {
		// Get the Biospecimen entity fresh from backend
		PhenoCollection pc = cpModel.getObject().getPhenoCollection();

		if (pc.getId() != null) {
			pc = iPhenotypicService.getPhenoCollection(pc.getId());
			cpModel.getObject().setPhenoCollection(pc);
			if (pc == null) {
				this.error("Can not edit this record - it has been invalidated (e.g. deleted)");
			}
		}
	}

	private boolean initialisePhenoCollectionDataEntry() {
		boolean replacePanel = false;
		PhenoCollection pc = cpModel.getObject().getPhenoCollection();
		if (!(phenoCollectionDataEntryPanel instanceof PhenoDataDataViewPanel)) {
			CompoundPropertyModel<PhenoDataCollectionVO> phenoDataCpModel = new CompoundPropertyModel<PhenoDataCollectionVO>(new PhenoDataCollectionVO());
			phenoDataCpModel.getObject().setPhenoCollection(pc);
			phenoDataCpModel.getObject().setArkFunction(cpModel.getObject().getArkFunction());
			PhenoDataDataViewPanel phenoCFDataEntryPanel = new PhenoDataDataViewPanel("phenoCFDataEntryPanel", phenoDataCpModel).initialisePanel(au.org.theark.core.Constants.ROWS_PER_PAGE);
			
			dataEntryNavigator = new AjaxPagingNavigator("dataEntryNavigator", phenoCFDataEntryPanel.getDataView()) {
				/**
				 * 
				 */
				private static final long	serialVersionUID	= 1L;

				@Override
				protected void onAjaxEvent(AjaxRequestTarget target) {
					target.add(dataEntryWMC);
				}
			};
			
			
			//dataEntryNavigator = new ArkAjaxPagingNavigator("dataEntryNavigator", phenoCFDataEntryPanel.getDataView(), dataEntryWMC, jQueryLabel);
			phenoCollectionDataEntryPanel = phenoCFDataEntryPanel;
			replacePanel = true;
		}
		return replacePanel;
	}
	
	public void initialiseDetailForm() {
		idTxtFld = new TextField<String>("PhenoCollection.id");
		idTxtFld.setEnabled(false);	// automatically generated
		
		nameTxtFld = new TextField<String>("PhenoCollection.name");
		descriptionTxtAreaFld = new TextArea<String>("PhenoCollection.description");
		recordDateTxtFld = new DateTextField("PhenoCollection.recordDate", au.org.theark.core.Constants.DD_MM_YYYY);
		reviewedDateTxtFld = new DateTextField("PhenoCollection.reviewedDate", au.org.theark.core.Constants.DD_MM_YYYY);

		ArkDatePicker recordDatePicker = new ArkDatePicker();
		recordDatePicker.bind(recordDateTxtFld);
		recordDateTxtFld.add(recordDatePicker);

		ArkDatePicker reviewedDatePicker = new ArkDatePicker();
		reviewedDatePicker.bind(reviewedDateTxtFld);
		reviewedDateTxtFld.add(reviewedDatePicker);
		
		initQuestionnaireDdc();
		initStatusDdc();
		initReviewedByDdc();
		
		dataEntryWMC = new WebMarkupContainer("dataEntryWMC");
		dataEntryWMC.setOutputMarkupId(true);
		
		initialisePhenoCollectionDataEntry();

		attachValidators();
		addComponents();

		// Focus on Questionnaire
		questionnaireDdc.add(new ArkDefaultFormFocusBehavior());
	}

	private void initQuestionnaireDdc() {
		// Get a list of questionnaires for the subject in context by default
		CustomFieldGroup cfgForStudyCriteria = cpModel.getObject().getCustomFieldGroup();
		// NB: Assumes that CustomFieldGroup will always be used for criteria (not a true entity)
		cfgForStudyCriteria.setArkFunction(cpModel.getObject().getArkFunction());
		cfgForStudyCriteria.setPublished(true);	//make sure that we don't return non-published Questionnaires
		
		List<CustomFieldGroup> questionnaireList = iArkCommonService.getCustomFieldGroups(cfgForStudyCriteria, 0, Integer.MAX_VALUE);
		ChoiceRenderer<CustomFieldGroup> choiceRenderer = new ChoiceRenderer<CustomFieldGroup>(Constants.PHENO_COLLECTION_NAME, Constants.PHENO_COLLECTION_ID);
		questionnaireDdc = new DropDownChoice<CustomFieldGroup>("PhenoCollection.questionnaire", (List<CustomFieldGroup>) questionnaireList, choiceRenderer);
		questionnaireDdc.setNullValid(false);
		if (!isNew()) {
			questionnaireDdc.setEnabled(false);	//can't change questionnaire after creating the phenoCollection
		}
	}

	private void initStatusDdc() {
		// Get a list of status
		List<QuestionnaireStatus> statusList = iPhenotypicService.getPhenoCollectionStatusList();
		ChoiceRenderer<QuestionnaireStatus> choiceRenderer = new ChoiceRenderer<QuestionnaireStatus>("name", "id");
		statusDdc = new DropDownChoice<QuestionnaireStatus>("PhenoCollection.status", statusList, choiceRenderer);
		statusDdc.setNullValid(false);
	}

	private void initReviewedByDdc() {
		List<ArkUser> arkUserList = new ArrayList<ArkUser>(0);
		arkUserList = iArkCommonService.getArkUserListByStudy(cpModel.getObject().getPhenoCollection().getLinkSubjectStudy().getStudy());
		ChoiceRenderer<ArkUser> choiceRenderer = new ChoiceRenderer<ArkUser>("ldapUserName", "id");
		reviewedByDdc = new DropDownChoice<ArkUser>("PhenoCollection.reviewedBy", arkUserList, choiceRenderer);
	}

	protected void attachValidators() {
		questionnaireDdc.setRequired(true);
		recordDateTxtFld.setRequired(true);
		statusDdc.setRequired(true);
	}

	private void addComponents() {
		arkCrudContainerVo.getDetailPanelFormContainer().add(idTxtFld);
		arkCrudContainerVo.getDetailPanelFormContainer().add(questionnaireDdc);
		arkCrudContainerVo.getDetailPanelFormContainer().add(recordDateTxtFld);
		arkCrudContainerVo.getDetailPanelFormContainer().add(nameTxtFld);
		arkCrudContainerVo.getDetailPanelFormContainer().add(descriptionTxtAreaFld);
		arkCrudContainerVo.getDetailPanelFormContainer().add(statusDdc);
		arkCrudContainerVo.getDetailPanelFormContainer().add(reviewedByDdc);
		arkCrudContainerVo.getDetailPanelFormContainer().add(reviewedDateTxtFld);

		dataEntryWMC.add(phenoCollectionDataEntryPanel);
		dataEntryWMC.add(dataEntryNavigator);
		arkCrudContainerVo.getDetailPanelFormContainer().add(dataEntryWMC);

		add(arkCrudContainerVo.getDetailPanelFormContainer());
	}

	@Override
	protected void onSave(AjaxRequestTarget target) {
		if (cpModel.getObject().getPhenoCollection().getId() == null) {
			// Save
			iPhenotypicService.createCollection(cpModel.getObject().getPhenoCollection());
			this.info("Phenotypic Collection " + cpModel.getObject().getPhenoCollection().getId() + " was created successfully");
			processErrors(target);

		}
		else {
			// Update
			iPhenotypicService.updateCollection(cpModel.getObject().getPhenoCollection());
			this.info("Phenotypic Collection " + cpModel.getObject().getPhenoCollection().getId() + " was updated successfully");
			processErrors(target);
			
		}
		// Allow the PheotyocCollection data to be saved any time save is performed
		if (phenoCollectionDataEntryPanel instanceof PhenoDataDataViewPanel) {
			((PhenoDataDataViewPanel) phenoCollectionDataEntryPanel).saveCustomData();
		}
		// refresh the CF data entry panel (if necessary)
		if (initialisePhenoCollectionDataEntry() == true) {
			dataEntryWMC.addOrReplace(phenoCollectionDataEntryPanel);
			dataEntryWMC.addOrReplace(dataEntryNavigator);
		}

		onSavePostProcess(target);
	}

	@Override
	protected void onClose(AjaxRequestTarget target) {
		target.add(feedbackPanel);
		modalWindow.close(target);
	}

	@Override
	protected void onDeleteConfirmed(AjaxRequestTarget target, Form<?> form) {
		
		try {
			iPhenotypicService.deletePhenoCollection(cpModel.getObject().getPhenoCollection());
		}
		catch (ArkSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (EntityCannotBeRemoved e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.info("Phenotypic collection " + cpModel.getObject().getPhenoCollection().getId() + " was deleted successfully");
		processErrors(target);

		onClose(target);
	}

	@Override
	protected void processErrors(AjaxRequestTarget target) {
		target.add(feedbackPanel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#isNew()
	 */
	@Override
	protected boolean isNew() {
		if (cpModel.getObject().getPhenoCollection().getId() == null) {
			return true;
		}
		else {
			return false;
		}
	}
}