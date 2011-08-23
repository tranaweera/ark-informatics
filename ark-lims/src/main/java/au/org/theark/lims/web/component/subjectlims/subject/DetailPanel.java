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
package au.org.theark.lims.web.component.subjectlims.subject;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;

import au.org.theark.lims.web.component.subjectlims.subject.form.ContainerForm;
import au.org.theark.lims.web.component.subjectlims.subject.form.DetailForm;

/**
 * @author cellis
 * 
 */
public class DetailPanel extends Panel {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 858762052753650329L;
	private DetailForm			detailsForm;
	private FeedbackPanel		feedBackPanel;
	private WebMarkupContainer	searchResultPanelContainer;
	private WebMarkupContainer	detailPanelContainer;
	private WebMarkupContainer	detailPanelFormContainer;
	private WebMarkupContainer	searchPanelContainer;
	private WebMarkupContainer	viewButtonContainer;
	private WebMarkupContainer	editButtonContainer;
	private WebMarkupContainer	arkContextContainer;
	private ContainerForm		containerForm;

	// private WebMarkupContainer subContainerWebMarkupContainer;
	// private SubjectSubContainerPanel subContainerPanel;

	public DetailPanel(String id, FeedbackPanel feedBackPanel, WebMarkupContainer searchResultPanelContainer, WebMarkupContainer detailPanelContainer, WebMarkupContainer detailPanelFormContainer,
			WebMarkupContainer searchPanelContainer, WebMarkupContainer viewButtonContainer, WebMarkupContainer editButtonContainer, WebMarkupContainer arkContextContainer, ContainerForm containerForm) {

		super(id);
		this.feedBackPanel = feedBackPanel;
		this.searchResultPanelContainer = searchResultPanelContainer;
		this.detailPanelContainer = detailPanelContainer;
		this.detailPanelFormContainer = detailPanelFormContainer;
		this.searchPanelContainer = searchPanelContainer;
		this.viewButtonContainer = viewButtonContainer;
		this.editButtonContainer = editButtonContainer;
		this.arkContextContainer = arkContextContainer;
		this.containerForm = containerForm;

	}

	public void initialisePanel() {
		detailsForm = new DetailForm("detailsForm", feedBackPanel, searchResultPanelContainer, detailPanelContainer, detailPanelFormContainer, searchPanelContainer, viewButtonContainer,
				editButtonContainer, arkContextContainer, containerForm);

		detailsForm.initialiseDetailForm();
		add(detailsForm);

		// subContainerWebMarkupContainer = new WebMarkupContainer("subContainerWebMarkupContainer");
		// subContainerWebMarkupContainer.setOutputMarkupPlaceholderTag(true);
		// // SubContainer with child details
		// subContainerPanel = new SubjectSubContainerPanel("subContainerPanel", arkContextContainer, containerForm, containerForm.getModelObject());
		// subContainerWebMarkupContainer.add(subContainerPanel);
		// add(subContainerWebMarkupContainer);
	}

	public DetailForm getDetailsForm() {
		return detailsForm;
	}

	public void setDetailsForm(DetailForm detailsForm) {
		this.detailsForm = detailsForm;
	}
	//
	// /**
	// * @return the subContainerPanel
	// */
	// public SubjectSubContainerPanel getSubContainerPanel()
	// {
	// return subContainerPanel;
	// }
	//
	// /**
	// * @param subContainerPanel the subContainerPanel to set
	// */
	// public void setSubContainerPanel(SubjectSubContainerPanel subContainerPanel)
	// {
	// this.subContainerPanel = subContainerPanel;
	// }

}
