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
package au.org.theark.lims.web.component.inventory.form;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.tree.BaseTree;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.convert.converter.IntegerConverter;
import org.apache.wicket.validation.validator.MinimumValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.model.lims.entity.InvSite;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.behavior.ArkDefaultFormFocusBehavior;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.core.web.form.AbstractContainerForm;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.service.IInventoryService;
import au.org.theark.lims.web.Constants;

/**
 * @author cellis
 * 
 */
@SuppressWarnings( { "unused" })
public class FreezerDetailForm extends AbstractInventoryDetailForm<LimsVO> {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -6404585686220567379L;
	private static Logger				log	= LoggerFactory.getLogger(FreezerDetailForm.class);
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>	iArkCommonService;

	@SpringBean(name = Constants.LIMS_INVENTORY_SERVICE)
	private IInventoryService			iInventoryService;

	private ContainerForm				fieldContainerForm;

	private int								mode;

	private TextField<String>			idTxtFld;
	private TextField<String>			nameTxtFld;
	private TextField<Integer>			capacityTxtFld;
	private TextField<Integer>			availableTxtFld;
	private TextArea<String>			lastservicenoteTxtAreaFld;
	private DateTextField				decommissiondateDateTxtFld;
	private TextArea<String>			descriptionTxtAreaFld;
	private DropDownChoice<InvSite>	invSiteDdc;

	/**
	 * 
	 * @param id
	 * @param feedBackPanel
	 * @param detailContainer
	 * @param containerForm
	 * @param tree
	 * @param node 
	 */
	public FreezerDetailForm(String id, FeedbackPanel feedBackPanel, WebMarkupContainer detailContainer, AbstractContainerForm<LimsVO> containerForm, BaseTree tree, DefaultMutableTreeNode node) {

		super(id, feedBackPanel, detailContainer, containerForm, tree, node);
	}

	public void initialiseDetailForm() {
		idTxtFld = new TextField<String>("invFreezer.id");
		nameTxtFld = new TextField<String>("invFreezer.name");
		capacityTxtFld = new TextField<Integer>("invFreezer.capacity"){
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@SuppressWarnings("unchecked")
			@Override
			public <C> IConverter<C> getConverter(Class<C> type) {
				IntegerConverter integerConverter = new IntegerConverter();
				return (IConverter<C>) integerConverter;
			}
		};
		capacityTxtFld.setEnabled(isNew());
		capacityTxtFld.add(new AjaxFormComponentUpdatingBehavior("onChange") {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				Integer capacity = containerForm.getModelObject().getInvFreezer().getCapacity();
				containerForm.getModelObject().getInvFreezer().setAvailable(capacity);
				target.add(availableTxtFld);
			}
		});
		availableTxtFld = new TextField<Integer>("invFreezer.available");
		availableTxtFld.setEnabled(false);
		lastservicenoteTxtAreaFld = new TextArea<String>("invFreezer.lastservicenote");
		decommissiondateDateTxtFld = new DateTextField("invFreezer.decommissiondate");
		descriptionTxtAreaFld = new TextArea<String>("invFreezer.description");

		ArkDatePicker arkDatePicker = new ArkDatePicker();
		arkDatePicker.bind(decommissiondateDateTxtFld);
		decommissiondateDateTxtFld.add(arkDatePicker);

		initSiteDdc();
		
		attachValidators();
		addComponents();

		// Focus on Name
		nameTxtFld.add(new ArkDefaultFormFocusBehavior());
	}
	
	private void initSiteDdc() {
		List<InvSite> invSiteList = new ArrayList<InvSite>(0);
		InvSite invSite = new InvSite();

		try {
			invSiteList = iInventoryService.searchInvSite(invSite);
		}
		catch (ArkSystemException e) {
			log.error(e.getMessage());
		}
		ChoiceRenderer<InvSite> choiceRenderer = new ChoiceRenderer<InvSite>(Constants.NAME, Constants.ID);
		invSiteDdc = new DropDownChoice<InvSite>("invFreezer.invSite", (List<InvSite>) invSiteList, choiceRenderer);
	}

	protected void attachValidators() {
		idTxtFld.setRequired(true);
		nameTxtFld.setRequired(true).setLabel(new StringResourceModel("error.name.required", this, new Model<String>("Name")));
		invSiteDdc.setRequired(true).setLabel(new StringResourceModel("error.site.required", this, new Model<String>("Site")));
		capacityTxtFld.setRequired(true).setLabel(new StringResourceModel("error.capacity.required", this, new Model<String>("Capacity")));
		MinimumValidator<Integer> minValue = new MinimumValidator<Integer>(new Integer(0));
		capacityTxtFld.add(minValue);
	}

	private void addComponents() {
		detailFormContainer.add(idTxtFld.setEnabled(false));
		detailFormContainer.add(nameTxtFld);
		detailFormContainer.add(capacityTxtFld);
		detailFormContainer.add(availableTxtFld);
		detailFormContainer.add(lastservicenoteTxtAreaFld);
		detailFormContainer.add(decommissiondateDateTxtFld);
		detailFormContainer.add(descriptionTxtAreaFld);
		detailFormContainer.add(invSiteDdc);
		add(detailFormContainer);
	}

	@Override
	protected void onSave(Form<LimsVO> containerForm, AjaxRequestTarget target) {
		if (containerForm.getModelObject().getInvFreezer().getId() == null) {
			// Save
			iInventoryService.createInvFreezer(containerForm.getModelObject());
			this.info("Freezer " + containerForm.getModelObject().getInvFreezer().getName() + " was created successfully");
			processErrors(target);
		}
		else {
			// Update
			iInventoryService.updateInvFreezer(containerForm.getModelObject());
			this.info("Freezer " + containerForm.getModelObject().getInvFreezer().getName() + " was updated successfully");
			processErrors(target);
		}

		onSavePostProcess(target);
	}

	protected void onCancel(AjaxRequestTarget target) {
		LimsVO limsVo = new LimsVO();
		containerForm.setModelObject(limsVo);
	}

	@Override
	protected void processErrors(AjaxRequestTarget target) {
		target.add(feedbackPanel);
	}

	public AjaxButton getDeleteButton() {
		return deleteButton;
	}

	public void setDeleteButton(AjaxButton deleteButton) {
		this.deleteButton = deleteButton;
	}

	protected void onDeleteConfirmed(AjaxRequestTarget target) {
		iInventoryService.deleteInvFreezer(containerForm.getModelObject());
		this.info("Freezer " + containerForm.getModelObject().getInvFreezer().getName() + " was deleted successfully");

		// Display delete confirmation message
		target.add(feedbackPanel);

		// Move focus back to Search form
		LimsVO limsVo = new LimsVO();
		containerForm.setModelObject(limsVo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#isNew()
	 */
	@Override
	protected boolean isNew() {
		if (containerForm.getModelObject().getInvFreezer().getId() == null) {
			return true;
		}
		else {
			return false;
		}
	}
}