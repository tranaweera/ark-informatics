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
package au.org.theark.study.web.component.consentFile;

import java.sql.SQLException;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.Constants;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.ConsentFile;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.button.AjaxDeleteButton;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.component.consentFile.form.ContainerForm;

@SuppressWarnings({ "serial", "unchecked", "unused", "rawtypes" })
public class SearchResultListPanel extends Panel {
	@SpringBean(name = au.org.theark.study.web.Constants.STUDY_SERVICE)
	private IStudyService		studyService;

	private transient Logger	log	= LoggerFactory.getLogger(SearchResultListPanel.class);
	private ContainerForm		containerForm;
	private ArkCrudContainerVO	arkCrudContainerVO;

	/**
	 * @param id
	 */
	public SearchResultListPanel(String id,  ArkCrudContainerVO arkCrudContainerVO, ContainerForm containerForm) {
		super(id);
		this.containerForm = containerForm;
		this.arkCrudContainerVO = arkCrudContainerVO;
	}

	/**
	 * 
	 * @param iModel
	 * @return the pageableListView of Upload
	 */
	public PageableListView<ConsentFile> buildPageableListView(IModel iModel) {
		PageableListView<ConsentFile> sitePageableListView = new PageableListView<ConsentFile>(Constants.RESULT_LIST, iModel, Constants.ROWS_PER_PAGE) {
			@Override
			protected void populateItem(final ListItem<ConsentFile> item) {
				ConsentFile consentFile = item.getModelObject();
				// The ID
				if (consentFile.getId() != null) {
					// Add the id component here
					item.add(new Label(au.org.theark.study.web.Constants.CONSENT_FILE_ID, consentFile.getId().toString()));
				}
				else {
					item.add(new Label(au.org.theark.study.web.Constants.CONSENT_FILE_ID, ""));
				}

				// / The filename
				if (consentFile.getFilename() != null) {
					// Add the id component here
					item.add(new Label(au.org.theark.study.web.Constants.CONSENT_FILE_FILENAME, consentFile.getFilename()));
				}
				else {
					item.add(new Label(au.org.theark.study.web.Constants.CONSENT_FILE_FILENAME, ""));
				}

				// TODO when displaying text escape any special characters
				// UserId
				if (consentFile.getUserId() != null) {
					item.add(new Label(au.org.theark.study.web.Constants.CONSENT_FILE_USER_ID, consentFile.getUserId()));// the ID here must match the
					// ones in
					// mark-up
				}
				else {
					item.add(new Label(au.org.theark.study.web.Constants.CONSENT_FILE_USER_ID, ""));// the ID here must match the ones in mark-up
				}

				// Download file link button
				item.add(buildDownloadButton(consentFile));

				// Delete the upload file
				item.add(buildDeleteButton(consentFile));

				// For the alternative stripes
				item.add(new AttributeModifier("class", new AbstractReadOnlyModel() {
					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));
			}
		};
		return sitePageableListView;
	}

	private Link buildDownloadLink(final ConsentFile consentFile) {
		Link link = new Link(au.org.theark.study.web.Constants.DOWNLOAD_FILE) {
			@Override
			public void onClick() {
				// Attempt to download the Blob as an array of bytes
				byte[] data = null;
				try {
					data = consentFile.getPayload().getBytes(1, (int) consentFile.getPayload().length());
				}
				catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				getRequestCycle().scheduleRequestHandlerAfterCurrent(new au.org.theark.core.util.ByteDataResourceRequestHandler("text/plain", data, consentFile.getFilename()));

			};
		};

		// Add the label for the link
		// TODO when displaying text escape any special characters
		Label nameLinkLabel = new Label("downloadFileLbl", "Download File");
		link.add(nameLinkLabel);
		return link;
	}

	private AjaxButton buildDownloadButton(final ConsentFile consentFile) {
		AjaxButton ajaxButton = new AjaxButton(au.org.theark.study.web.Constants.DOWNLOAD_FILE, new StringResourceModel("downloadKey", this, null)) {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// Attempt to download the Blob as an array of bytes
				byte[] data = null;
				try {
					data = consentFile.getPayload().getBytes(1, (int) consentFile.getPayload().length());
				}
				catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				getRequestCycle().scheduleRequestHandlerAfterCurrent((new au.org.theark.core.util.ByteDataResourceRequestHandler("text/plain", data, consentFile.getFilename())));
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub
				
			};
		};

		ajaxButton.setVisible(true);
		ajaxButton.setDefaultFormProcessing(false);

		if (consentFile.getPayload() == null)
			ajaxButton.setVisible(false);

		return ajaxButton;
	}

	private AjaxDeleteButton buildDeleteButton(final ConsentFile consentFile) {
		DeleteButton ajaxButton = new DeleteButton(consentFile, SearchResultListPanel.this) {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// Attempt to delete upload
				if (consentFile.getId() != null) {
					try {
						studyService.delete(consentFile);
					}
					catch (ArkSystemException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					catch (EntityNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				containerForm.info("Consent file " + consentFile.getFilename() + " was deleted successfully.");

				// Update the result panel
				//target.add(searchResultContainer);
				target.add(arkCrudContainerVO.getSearchResultPanelContainer());
				target.add(containerForm);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				this.error("An error occured whil deleteting the consent file" );
				
			}
		};

		// TODO: Check permissions for delete
		ajaxButton.setVisible(true);
		ajaxButton.setDefaultFormProcessing(false);

		return ajaxButton;
	}
}
