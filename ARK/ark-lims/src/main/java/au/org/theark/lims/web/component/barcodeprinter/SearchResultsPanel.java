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
package au.org.theark.lims.web.component.barcodeprinter;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.model.lims.entity.BarcodePrinter;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.ArkDataProvider;
import au.org.theark.core.web.component.link.ArkBusyAjaxLink;
import au.org.theark.lims.service.IBarcodeService;
import au.org.theark.lims.web.component.barcodeprinter.form.ContainerForm;
import au.org.theark.lims.web.component.panel.applet.PrintAppletPanel;

public class SearchResultsPanel extends Panel {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -5165716102918554398L;

	protected transient Logger	log					= LoggerFactory.getLogger(SearchResultsPanel.class);

	@SpringBean(name = au.org.theark.lims.web.Constants.LIMS_BARCODE_SERVICE)
	private IBarcodeService		iBarcodeService;

	private ContainerForm		containerForm;
	private ArkCrudContainerVO	arkCrudContainerVo;

	public SearchResultsPanel(String id, ContainerForm containerForm, ArkCrudContainerVO arkCrudContainerVo) {
		super(id);
		this.containerForm = containerForm;
		this.arkCrudContainerVo = arkCrudContainerVo;
		
		// Applet for printing barcodes/checking printers
		PrintAppletPanel printAppletPanel = new PrintAppletPanel("printAppletPanel", "zebra");
		add(printAppletPanel);
	}

	public DataView<BarcodePrinter> buildDataView(ArkDataProvider<BarcodePrinter, IBarcodeService> dataProvider) {
		DataView<BarcodePrinter> dataView = new DataView<BarcodePrinter>("resultList", dataProvider) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 2981419595326128410L;

			@Override
			protected void populateItem(final Item<BarcodePrinter> item) {
				final BarcodePrinter barcodePrinter = item.getModelObject();

				item.add(buildLink(barcodePrinter));

				if (barcodePrinter.getName() != null) {
					// the ID here must match the ones in mark-up
					item.add(new Label("name", barcodePrinter.getName()));
				}
				else {
					item.add(new Label("name", ""));
				}

				if (barcodePrinter.getDescription() != null) {
					// the ID here must match the ones in mark-up
					item.add(new Label("description", barcodePrinter.getDescription()));
				}
				else {
					item.add(new Label("description", ""));
				}
				
				
				
				AjaxButton checkPrinter = new AjaxButton("checkPrinter") {
					/**
					 * 
					 */
					private static final long	serialVersionUID	= 1L;

					@Override
					protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
						target.appendJavaScript("checkPrinter(\"" + barcodePrinter.getName() + "\");");
					}
					
					@Override
					protected void onError(AjaxRequestTarget target, Form<?> form) {
						log.error("Error on checkPrinter button click");
					}
				};
				item.add(checkPrinter);

				item.add(new AttributeModifier("class", new AbstractReadOnlyModel<String>() {
					/**
					 * 
					 */
					private static final long	serialVersionUID	= 1L;

					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));
			}
		};
		return dataView;
	}

	@SuppressWarnings("unchecked")
	public PageableListView<BarcodePrinter> buildPageableListView(IModel iModel, final WebMarkupContainer searchResultsContainer) {
		PageableListView<BarcodePrinter> pageableListView = new PageableListView<BarcodePrinter>("resultList", iModel, au.org.theark.core.Constants.ROWS_PER_PAGE) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void populateItem(final ListItem<BarcodePrinter> item) {
				BarcodePrinter barcodePrinter = item.getModelObject();

				item.add(buildLink(barcodePrinter));

				if (barcodePrinter.getName() != null) {
					// the ID here must match the ones in mark-up
					item.add(new Label("name", barcodePrinter.getName()));
				}
				else {
					item.add(new Label("name", ""));
				}

				if (barcodePrinter.getDescription() != null) {
					// the ID here must match the ones in mark-up
					item.add(new Label("description", barcodePrinter.getDescription()));
				}
				else {
					item.add(new Label("description", ""));
				}

				item.add(new AttributeModifier("class", new AbstractReadOnlyModel<String>() {
					/**
					 * 
					 */
					private static final long	serialVersionUID	= 1L;

					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));

			}
		};
		return pageableListView;
	}

	@SuppressWarnings( { "unchecked", "serial" })
	private AjaxLink buildLink(final BarcodePrinter barcodePrinter) {
		ArkBusyAjaxLink link = new ArkBusyAjaxLink("link") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				BarcodePrinter barcodePrinterFromDb = iBarcodeService.searchBarcodePrinter(barcodePrinter);
				containerForm.setModelObject(barcodePrinterFromDb);

				arkCrudContainerVo.getSearchResultPanelContainer().setVisible(false);
				arkCrudContainerVo.getSearchPanelContainer().setVisible(false);
				arkCrudContainerVo.getDetailPanelContainer().setVisible(true);
				arkCrudContainerVo.getDetailPanelFormContainer().setEnabled(false);
				arkCrudContainerVo.getViewButtonContainer().setVisible(true);
				arkCrudContainerVo.getViewButtonContainer().setEnabled(true);
				arkCrudContainerVo.getEditButtonContainer().setVisible(false);

				// Refresh the markup containers
				target.add(arkCrudContainerVo.getSearchResultPanelContainer());
				target.add(arkCrudContainerVo.getDetailPanelContainer());
				target.add(arkCrudContainerVo.getDetailPanelFormContainer());
				target.add(arkCrudContainerVo.getSearchPanelContainer());
				target.add(arkCrudContainerVo.getViewButtonContainer());
				target.add(arkCrudContainerVo.getEditButtonContainer());

				// Refresh base container form to remove any feedBack messages
				target.add(containerForm);
			}
		};

		// Add the label for the link
		Label linkLabel = new Label("id", barcodePrinter.getId().toString());
		link.add(linkLabel);
		return link;
	}
}
