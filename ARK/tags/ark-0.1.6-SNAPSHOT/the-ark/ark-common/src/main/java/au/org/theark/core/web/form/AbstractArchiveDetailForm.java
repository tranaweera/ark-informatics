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
package au.org.theark.core.web.form;

import java.util.Iterator;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

import au.org.theark.core.Constants;
import au.org.theark.core.security.ArkPermissionHelper;
import au.org.theark.core.vo.ArkCrudContainerVO;

/**
 * @author nivedann
 * 
 */
public abstract class AbstractArchiveDetailForm<T> extends Form<T> {

	private static final long		serialVersionUID	= 1L;
	protected FeedbackPanel			feedBackPanel;
	protected Form<T>					containerForm;
	protected AjaxButton				saveButton;
	protected AjaxButton				cancelButton;
	protected AjaxButton				editButton;
	protected AjaxButton				editCancelButton;
	protected ArkCrudContainerVO	crudVO;

	// Add a visitor class for required field marking/validation/highlighting
	ArkFormVisitor	formVisitor = new ArkFormVisitor();

	public void onBeforeRender() {
		super.onBeforeRender();
		visitChildren(formVisitor);
	}

	/**
	 * @param id
	 */
	public AbstractArchiveDetailForm(String id) {
		super(id);
		initialiseForm();
	}
	
	protected void onCancelPostProcess(AjaxRequestTarget target) {

		crudVO.getViewButtonContainer().setVisible(true);
		crudVO.getViewButtonContainer().setEnabled(true);
		crudVO.getDetailPanelContainer().setVisible(true);
		crudVO.getDetailPanelFormContainer().setEnabled(false);
		crudVO.getSearchResultPanelContainer().setVisible(false);
		crudVO.getSearchPanelContainer().setVisible(false);
		crudVO.getEditButtonContainer().setVisible(false);

		target.add(feedBackPanel);
		target.add(crudVO.getSearchPanelContainer());
		target.add(crudVO.getSearchResultPanelContainer());
		target.add(crudVO.getDetailPanelContainer());
		target.add(crudVO.getDetailPanelFormContainer());

		target.add(crudVO.getViewButtonContainer());
		target.add(crudVO.getEditButtonContainer());
	}

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param feedBackPanel
	 * @param crudVO
	 * @param containerForm
	 */
	public AbstractArchiveDetailForm(String id, FeedbackPanel feedBackPanel, ArkCrudContainerVO crudVO, Form<T> containerForm) {

		super(id);
		this.crudVO = crudVO;
		this.containerForm = containerForm;
		this.feedBackPanel = feedBackPanel;
		initialiseForm();
	}

	protected void initialiseForm() {

		cancelButton = new AjaxButton(Constants.CANCEL) {

			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				if (isNew()) {
					editCancelProcess(target);
				}
				else {
					crudVO.getSearchResultPanelContainer().setVisible(false);// Hide the Search Result List Panel via the WebMarkupContainer
					crudVO.getDetailPanelContainer().setVisible(false);// Hide the Detail Panle via the WebMarkupContainer
					target.add(crudVO.getDetailPanelContainer());
					target.add(crudVO.getSearchResultPanelContainer());// Attach the resultListContainer WebMarkupContainer to be re-rendered
					onCancelPostProcess(target);
				}
			}

			@Override
			protected void onError(AjaxRequestTarget arg0, Form<?> arg1) {
				
			}
		};

		saveButton = new AjaxButton(Constants.SAVE) {

			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			public boolean isVisible() {
				return ArkPermissionHelper.isActionPermitted(Constants.SAVE);
			}

			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onSave(containerForm, target);
				target.add(crudVO.getDetailPanelContainer());
			}

			@SuppressWarnings("unchecked")
			public void onError(AjaxRequestTarget target, Form<?> form) {
				boolean setFocusError = false;
				WebMarkupContainer wmc = (WebMarkupContainer) form.get("detailFormContainer");
				for (Iterator iterator = wmc.iterator(); iterator.hasNext();) {
					Component component = (Component) iterator.next();
					if (component instanceof FormComponent) {
						FormComponent formComponent = (FormComponent) component;

						if (!formComponent.isValid()) {
							if (!setFocusError) {
								// Place focus on field in error (for the first field in error)
								target.focusComponent(formComponent);
								setFocusError = true;
							}
						}
					}
				}

				processErrors(target);
			}
		};

		editButton = new AjaxButton("edit") {

			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			public boolean isVisible() {
				return ArkPermissionHelper.isActionPermitted(Constants.SAVE);
			}

			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				crudVO.getViewButtonContainer().setVisible(false);
				crudVO.getEditButtonContainer().setVisible(true);
				crudVO.getDetailPanelFormContainer().setEnabled(true);

				target.add(crudVO.getViewButtonContainer());
				target.add(crudVO.getEditButtonContainer());
				target.add(crudVO.getDetailPanelFormContainer());
			}

			public void onError(AjaxRequestTarget target, Form<?> form) {
				processErrors(target);
			}
		};

		editCancelButton = new AjaxButton("editCancel") {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				editCancelProcess(target);
			}

			public void onError(AjaxRequestTarget target, Form<?> form) {
				processErrors(target);
			}
		};

		addComponentsToForm();
	}

	protected void addComponentsToForm() {
		crudVO.getEditButtonContainer().add(saveButton);
		crudVO.getEditButtonContainer().add(cancelButton.setDefaultFormProcessing(false));
		crudVO.getViewButtonContainer().add(editButton);
		crudVO.getViewButtonContainer().add(editCancelButton.setDefaultFormProcessing(false));

		add(crudVO.getDetailPanelFormContainer());
		add(crudVO.getEditButtonContainer());
		add(crudVO.getViewButtonContainer());
	}

	protected void editCancelProcess(AjaxRequestTarget target) {
		crudVO.getSearchResultPanelContainer().setVisible(true);// Hide the Search Result List Panel via the WebMarkupContainer
		crudVO.getDetailPanelContainer().setVisible(false);// Hide the Detail Panle via the WebMarkupContainer
		crudVO.getSearchPanelContainer().setVisible(true);

		target.add(feedBackPanel);
		target.add(crudVO.getSearchPanelContainer());
		target.add(crudVO.getDetailPanelContainer());
		target.add(crudVO.getSearchResultPanelContainer());
		onCancel(target);
	}

	/**
	 * A helper method that will allow the toggle of panels and buttons. This method can be invoked by sub-classes as part of the onSave()
	 * implementation.Once the user has pressed Save either to create a new entity or update, invoking this method will place the new/edited record
	 * panel in View/Read only mode.
	 * 
	 * @param target
	 */
	protected void onSavePostProcess(AjaxRequestTarget target, ArkCrudContainerVO crudVO) {
		// Visibility
		crudVO.getDetailPanelContainer().setVisible(true);
		crudVO.getViewButtonContainer().setVisible(true);
		crudVO.getSearchResultPanelContainer().setVisible(false);
		crudVO.getSearchPanelContainer().setVisible(false);
		crudVO.getEditButtonContainer().setVisible(false);

		// Enable
		crudVO.getDetailPanelFormContainer().setEnabled(false);
		crudVO.getViewButtonContainer().setEnabled(true);

		target.add(crudVO.getSearchResultPanelContainer());
		target.add(crudVO.getDetailPanelContainer());
		target.add(crudVO.getDetailPanelFormContainer());
		target.add(crudVO.getSearchPanelContainer());
		target.add(crudVO.getViewButtonContainer());
		target.add(crudVO.getEditButtonContainer());

		target.add(feedBackPanel);
	}
	
	abstract protected void attachValidators();

	abstract protected void onCancel(AjaxRequestTarget target);

	abstract protected void onSave(Form<T> containerForm, AjaxRequestTarget target);

	abstract protected void processErrors(AjaxRequestTarget target);

	abstract protected boolean isNew();

}
