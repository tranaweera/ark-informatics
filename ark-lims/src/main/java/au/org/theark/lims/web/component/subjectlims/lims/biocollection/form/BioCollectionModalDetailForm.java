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
package au.org.theark.lims.web.component.subjectlims.lims.biocollection.form;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.lims.entity.BioCollection;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.behavior.ArkDefaultFormFocusBehavior;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.core.web.form.AbstractModalDetailForm;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.service.ILimsService;
import au.org.theark.lims.web.Constants;
import au.org.theark.lims.web.component.biocollectioncustomdata.BioCollectionCustomDataContainerPanel;

/**
 * @author cellis
 * 
 */
@SuppressWarnings ("unused")
public class BioCollectionModalDetailForm extends AbstractModalDetailForm<LimsVO> {
	/**
	 * 
	 */
	private static final long			serialVersionUID	= 2926069852602563767L;
	private static final Logger		log					= LoggerFactory.getLogger(BioCollectionModalDetailForm.class);
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>	iArkCommonService;

	@SpringBean(name = Constants.LIMS_SERVICE)
	private ILimsService					iLimsService;

	private int								mode;

	private TextField<String>			idTxtFld;
	private TextField<String>			nameTxtFld;
	private TextField<String>			collectionIdTxtFld;
	private TextArea<String>			commentsTxtAreaFld;
	private DateTextField				collectionDateTxtFld;
	private DateTextField				surgeryDateTxtFld;
	private ModalWindow					modalWindow;
	private WebMarkupContainer			arkContextMarkup;
	private WebMarkupContainer 		bioCollectionCFDataEntryWMC;
	private Panel 							bioCollectionCFDataEntryPanel;

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param feedBackPanel
	 * @param arkCrudContainerVo
	 * @param modalWindow
	 * @param containerForm
	 * @param detailPanelContainer
	 */
	public BioCollectionModalDetailForm(String id, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVo, ModalWindow modalWindow, CompoundPropertyModel<LimsVO> cpModel) {
		super(id, feedBackPanel, arkCrudContainerVo, cpModel);
		this.modalWindow = modalWindow;
		refreshEntityFromBackend();
	}

	protected void refreshEntityFromBackend() {
		// Get BioCollection entity fresh from backend
		BioCollection bioCollection = cpModel.getObject().getBioCollection();
		if (bioCollection.getId() != null) {
			try {
				cpModel.getObject().setBioCollection(iLimsService.getBioCollection(bioCollection.getId()));
			}
			catch (EntityNotFoundException e) {
				this.error("Can not edit this record - it has been invalidated (e.g. deleted)");
				log.error(e.getMessage());
			}
		}		
	}

	private void initialiseBioCollectionCFDataEntry() {
		BioCollection bioCollection = cpModel.getObject().getBioCollection();
		if (bioCollection.getId() == null) {
			// New BioCollection record, so BioCollection CF data entry is disallowed
			bioCollectionCFDataEntryPanel = new EmptyPanel("bioCollectionCFDataEntryPanel");
		}
		else {
			// Editing an existing record, CF data entry is ok
			// Place the selected bioCollection in session context (CF data entry is relies on this context to work)
			SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.lims.web.Constants.BIO_COLLECTION, bioCollection.getId());
			bioCollectionCFDataEntryPanel = new BioCollectionCustomDataContainerPanel("bioCollectionCFDataEntryPanel").initialisePanel();
		}
		bioCollectionCFDataEntryWMC.addOrReplace(bioCollectionCFDataEntryPanel);
	}

	public void initialiseDetailForm() {
		idTxtFld = new TextField<String>("bioCollection.id");
		
		// bioCollection.name auto-generated, this read only
		nameTxtFld = new TextField<String>("bioCollection.name");
		nameTxtFld.setEnabled(false);
		
		commentsTxtAreaFld = new TextArea<String>("bioCollection.comments");
		collectionDateTxtFld = new DateTextField("bioCollection.collectionDate", au.org.theark.core.Constants.DD_MM_YYYY);
		surgeryDateTxtFld = new DateTextField("bioCollection.surgeryDate", au.org.theark.core.Constants.DD_MM_YYYY);

		ArkDatePicker startDatePicker = new ArkDatePicker();
		startDatePicker.bind(collectionDateTxtFld);
		collectionDateTxtFld.add(startDatePicker);

		ArkDatePicker endDatePicker = new ArkDatePicker();
		endDatePicker.bind(surgeryDateTxtFld);
		surgeryDateTxtFld.add(endDatePicker);

		bioCollectionCFDataEntryWMC = new WebMarkupContainer("bioCollectionCFDataEntryWMC");
		bioCollectionCFDataEntryWMC.setOutputMarkupId(true);
		initialiseBioCollectionCFDataEntry();

		attachValidators();
		addComponents();
		
		// Focus on Collection Date
		collectionDateTxtFld.add(new ArkDefaultFormFocusBehavior());
	}

	protected void attachValidators() {
		idTxtFld.setRequired(true);
		nameTxtFld.setRequired(true).setLabel(new StringResourceModel("error.bioCollection.name.required", this, new Model<String>("Name")));
	}

	private void addComponents() {
		arkCrudContainerVo.getDetailPanelFormContainer().add(idTxtFld.setEnabled(false));
		arkCrudContainerVo.getDetailPanelFormContainer().add(nameTxtFld);
		arkCrudContainerVo.getDetailPanelFormContainer().add(commentsTxtAreaFld);
		arkCrudContainerVo.getDetailPanelFormContainer().add(collectionDateTxtFld);
		arkCrudContainerVo.getDetailPanelFormContainer().add(surgeryDateTxtFld);
		
		add(arkCrudContainerVo.getDetailPanelFormContainer());
		add(bioCollectionCFDataEntryWMC);
	}

	@Override
	protected void onSave(AjaxRequestTarget target) {
		if (cpModel.getObject().getBioCollection().getId() == null) {
			// Save
			iLimsService.createBioCollection(cpModel.getObject());
			this.info("Biospecimen collection " + cpModel.getObject().getBioCollection().getName() + " was created successfully");
			if (target != null) {
				processErrors(target);
			}
		}
		else {
			// Update
			iLimsService.updateBioCollection(cpModel.getObject());
			this.info("Biospecimen collection " + cpModel.getObject().getBioCollection().getName() + " was updated successfully");
			if (target != null) {
				processErrors(target);
			}
		}
		// refresh the CF data entry panel
		initialiseBioCollectionCFDataEntry();

		if (target != null) {
			target.addComponent(bioCollectionCFDataEntryWMC);
			onSavePostProcess(target);
		}
	}

	@Override
	protected void onClose(AjaxRequestTarget target) {
		// Reset now handled for in BioCollectionListPanel.onBeforeRender()
		// // Reset the BioCollection (for criteria) in LimsVO
		// BioCollection resetBioCollection = new BioCollection();
		// resetBioCollection.setLinkSubjectStudy(cpModel.getObject().getLinkSubjectStudy());
		// resetBioCollection.setStudy(cpModel.getObject().getLinkSubjectStudy().getStudy());
		// cpModel.getObject().setBioCollection(resetBioCollection);

		target.addComponent(feedbackPanel);
		modalWindow.close(target);
	}

	@Override
	protected void onDeleteConfirmed(AjaxRequestTarget target, Form<?> form) {
		if (!iLimsService.hasBiospecimens(cpModel.getObject().getBioCollection())) {

			iLimsService.deleteBioCollection(cpModel.getObject());
			this.info("Biospecimen collection " + cpModel.getObject().getBioCollection().getName() + " was deleted successfully");
			onClose(target);
		}
		else {
			this.error("Biospecimen collection " + cpModel.getObject().getBioCollection().getName() + " can not be deleted because there are biospecimens attached");
			target.addComponent(feedbackPanel);
		}
	}

	@Override
	protected void processErrors(AjaxRequestTarget target) {
		target.addComponent(feedbackPanel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#isNew()
	 */
	@Override
	protected boolean isNew() {
		if (cpModel.getObject().getBioCollection().getId() == null) {
			return true;
		}
		else {
			return false;
		}
	}

}
