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
package au.org.theark.admin.web.component.module.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.admin.model.vo.AdminVO;
import au.org.theark.admin.service.IAdminService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.form.AbstractDetailForm;

public class DetailForm extends AbstractDetailForm<AdminVO> {
	/**
	 * 
	 */
	private static final long		serialVersionUID	= 7359844049561245524L;

	protected transient Logger		log					= LoggerFactory.getLogger(DetailForm.class);

	@SpringBean(name = au.org.theark.admin.service.Constants.ARK_ADMIN_SERVICE)
	private IAdminService<Void>	iAdminService;

	private int							mode;
	private TextField<String>		idTxtFld;
	private TextField<String>		nameTxtFld;
	private TextArea<String>		descriptionTxtAreaFld;

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param crudVO
	 * @param feedbackPanel
	 * @param containerForm
	 */
	public DetailForm(String id, FeedbackPanel feedbackPanel, ContainerForm containerForm, ArkCrudContainerVO arkCrudContainerVo) {
		super(id, feedbackPanel, containerForm, arkCrudContainerVo);
		this.containerForm = containerForm;
		arkCrudContainerVO = arkCrudContainerVo;
		setMultiPart(true);
	}

	public void initialiseDetailForm() {
		idTxtFld = new TextField<String>("arkModule.id");
		idTxtFld.setEnabled(false);

		nameTxtFld = new TextField<String>("arkModule.name");
		descriptionTxtAreaFld = new TextArea<String>("arkModule.description");

		attachValidators();
		addDetailFormComponents();
	}

	@Override
	protected void attachValidators() {
		// Set required field here
		nameTxtFld.setRequired(true);
	}

	private void addDetailFormComponents() {
		arkCrudContainerVO.getDetailPanelFormContainer().add(idTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(nameTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(descriptionTxtAreaFld);

		add(arkCrudContainerVO.getDetailPanelFormContainer());
	}

	protected void onSave(Form<AdminVO> containerForm, AjaxRequestTarget target) {
		// Save or update
		iAdminService.creatOrUpdateArkModule(containerForm.getModelObject());

		this.info("Ark Module: " + containerForm.getModelObject().getArkModule().getName() + " was created/updated successfully.");
		target.addComponent(feedBackPanel);
	}

	protected void onCancel(AjaxRequestTarget target) {
		containerForm.setModelObject(new AdminVO());
	}

	protected void onDeleteConfirmed(AjaxRequestTarget target, String selectionO) {
		// Delete
		iAdminService.deleteArkModule(containerForm.getModelObject());

		this.info("Ark Module: " + containerForm.getModelObject().getArkModule().getName() + " was deleted successfully.");
		editCancelProcess(target, true);
	}

	protected void processErrors(AjaxRequestTarget target) {
		target.addComponent(feedBackPanel);
	}

	protected boolean isNew() {
		if (containerForm.getModelObject().getArkFunction().getId() == null) {
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * @return the mode
	 */
	public int getMode() {
		return mode;
	}

	/**
	 * @param mode
	 *           the mode to set
	 */
	public void setMode(int mode) {
		this.mode = mode;
	}
}
