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
package au.org.theark.lims.web.component.inventory.panel.box.display;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.Loop;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.file.File;
import org.apache.wicket.util.io.ByteArrayOutputStream;
import org.apache.wicket.util.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.model.lims.entity.Biospecimen;
import au.org.theark.core.model.lims.entity.InvBox;
import au.org.theark.core.model.lims.entity.InvCell;
import au.org.theark.lims.service.IInventoryService;
import au.org.theark.lims.web.Constants;

/**
 * 
 * @author cellis
 *
 */
public class GridBoxPanel extends Panel {
	/**
	 * 
	 */
	private static final long					serialVersionUID		= -4769477913855966069L;
	private byte[]									workBookAsBytes;
	private WebMarkupContainer					gridBoxKeyContainer	= new WebMarkupContainer("gridBoxKeyContainer");
	public String									exportXlsFileName;

	private static final ResourceReference	EMPTY_CELL_ICON		= new ResourceReference(GridBoxPanel.class, "emptyCell.gif");
	private static final ResourceReference	USED_CELL_ICON			= new ResourceReference(GridBoxPanel.class, "usedCell.gif");
	private static final ResourceReference	BARCODE_CELL_ICON		= new ResourceReference(GridBoxPanel.class, "barcodeCell.gif");

	private static final Logger				log						= LoggerFactory.getLogger(GridBoxPanel.class);

	@SpringBean(name = Constants.LIMS_INVENTORY_SERVICE)
	private IInventoryService					iInventoryService;

	public GridBoxPanel(String id, InvBox invBox, String exportXlsfileName) {
		super(id);
		this.exportXlsFileName = exportXlsfileName;
		if (this.exportXlsFileName == null) {
			this.exportXlsFileName = "GridBoxData";
		}
		initialiseGrid(invBox);
	}

	/**
	 * Initialises the grid to display for the InvBox in question
	 * @param invBox
	 */
	private void initialiseGrid(InvBox invBox) {
		invBox = iInventoryService.getInvBox(invBox.getId());
		List<InvCell> invCellList = new ArrayList<InvCell>(0);
		invCellList = iInventoryService.getCellAndBiospecimenListByBox(invBox);
		
		// Handle for no cells in InvCell table!
		int cells = invBox.getNoofcol() * invBox.getNoofrow();
		if (invCellList.size() != cells) {
			log.error("InvCell table is missing data for invBox.id " + invBox.getId());
			this.error("InvCell table is missing data for invBox.id " + invBox.getId());
			this.setVisible(false);
			add(createHeadings(new InvBox()));
			add(createMainGrid(new InvBox(), invCellList));
		}
		else {
			add(createHeadings(invBox));
			add(createMainGrid(invBox, invCellList));
			initialiseGridKey(invCellList);
		}
	}

	/**
	 * Creates the header of the table for the represented InvBox in question
	 * @param invBox
	 * @return
	 */
	@SuppressWarnings( { "unchecked" })
	private Loop createHeadings(final InvBox invBox) {
		// Outer Loop instance, using a PropertyModel to bind the Loop iteration to invBox "noofcol" value
		return new Loop("heading", new PropertyModel(invBox, "noofcol")) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= -7027878243061138904L;

			public void populateItem(LoopItem item) {
				final int col = item.getIteration();

				IModel<String> colModel = new Model() {
					/**
					 * 
					 */
					private static final long	serialVersionUID	= 1144128566137457199L;

					@Override
					public Serializable getObject() {
						String label = new String();
						String colNoType = invBox.getColnotype().getName();
						if (colNoType.equalsIgnoreCase("ALPHABET")) {
							char character = (char) (col + 65);
							label = new Character(character).toString();
						}
						else {
							label = new Integer(col+1).toString();
						}
						return label;
					}
				};
				
				// Create the col number/label
				Label colLabel = new Label("cellHead", colModel);
				item.add(colLabel);
			}
		};
	}
	
	/**
	 * Creates the table data that represents the cells of the InvBox in question
	 * @param invBox
	 * @param invCellList
	 * @return
	 */
	@SuppressWarnings( { "serial", "unchecked" })
	private Loop createMainGrid(final InvBox invBox, final List<InvCell> invCellList) {
		// Outer Loop instance, using a PropertyModel to bind the Loop iteration to invBox "noofrow" value
		Loop loop = new Loop("rows", new PropertyModel(invBox, "noofrow")) {
			public void populateItem(LoopItem item) {
				final int row = item.getIteration();
				
				// Create the row number/label
				String label = new String();
				String rowNoType = invBox.getRownotype().getName();
				if (rowNoType.equalsIgnoreCase("ALPHABET")) {
					char character = (char) (row + 65);
					label = new Character(character).toString();
				}
				else {
					label = new Integer(row+1).toString();
				}

				Label rowLabel = new Label("rowNo", new Model(label));
				rowLabel.add(new AbstractBehavior() {
					public void onComponentTag(Component component, ComponentTag tag) {
						super.onComponentTag(component, tag);
						tag.put("style", "background: none repeat scroll 0 0 #FFFFFF; color: black; font-weight: bold; padding: 1px;");
					};
				});
				item.add(rowLabel);

				// We create an inner Loop instance and uses PropertyModel to bind the Loop iteration to invBox "noofcol" value
				item.add(new Loop("cols", new PropertyModel(invBox, "noofcol")) {
					public void populateItem(LoopItem item) {
						final int col = item.getIteration();
						
						final int index = (row * invBox.getNoofcol()) + col;
						
						InvCell invCell = invCellList.get(index);
						IModel<Biospecimen> biospecimenModel = new PropertyModel<Biospecimen>(invCell, "biospecimen");
						
						// Cell used/empty icon
						Component cellIconComponent = cellIconComponent("cellIcon", biospecimenModel);

						// tooltip of cell
						StringBuffer stringBuffer = new StringBuffer();
						stringBuffer.append("Column: ");
						stringBuffer.append(col);
						stringBuffer.append("\t");
						stringBuffer.append("Row: ");
						stringBuffer.append(row);
						stringBuffer.append("\t");
						stringBuffer.append("Status: ");

						if (biospecimenModel.getObject() != null) {
							stringBuffer.append("Used");
							stringBuffer.append("\t");
							stringBuffer.append("BiospecimenUID: ");
							stringBuffer.append(biospecimenModel.getObject().getBiospecimenUid());
							stringBuffer.append("\t");
							stringBuffer.append("Sample Type: ");
							stringBuffer.append(biospecimenModel.getObject().getSampleType().getName());
							stringBuffer.append("\t");
							stringBuffer.append("Quantity: ");
							stringBuffer.append(biospecimenModel.getObject().getQuantity());
						}
						else {
							stringBuffer.append("Empty");
						}

						String toolTip = stringBuffer.toString();
						cellIconComponent.add(new AttributeModifier("showtooltip", true, new Model("true")));
						cellIconComponent.add(new AttributeModifier("title", true, new Model(toolTip)));
						cellIconComponent.add(new AbstractBehavior() {
							public void onComponentTag(Component component, ComponentTag tag) {
								super.onComponentTag(component, tag);
								tag.put("style", "padding: 1px;");
							};
						});
						item.add(cellIconComponent);
					}
					// }
				});
			}
		};
		return loop;
	}

	/**
	 * Creates the icon dislplayed at the cell in question
	 * @param componentId
	 * @param biospecimenModel
	 * @return
	 */
	protected Component cellIconComponent(String componentId, final IModel<Biospecimen> biospecimenModel) {
		return new Image(componentId) {
			private static final long	serialVersionUID	= 1L;

			@Override
			protected ResourceReference getImageResourceReference() {
				return getIconResourceReference(biospecimenModel.getObject());
			}

		};
	}

	/**
	 * Determine what icon to display on the cell
	 * 
	 * @param object
	 *           the referece object of the node
	 * @return resourceReference to the icon for the cell in question
	 */
	private ResourceReference getIconResourceReference(Object object) {
		ResourceReference resourceReference = null;
		if (object == null) {
			resourceReference = EMPTY_CELL_ICON;
		}
		else {
			resourceReference = USED_CELL_ICON;
		}
		return resourceReference;
	}

	private void initialiseGridKey(List<InvCell> invCellList) {
		gridBoxKeyContainer.setOutputMarkupId(true);

		gridBoxKeyContainer.add(new Image("emptyCellIcon", EMPTY_CELL_ICON));
		gridBoxKeyContainer.add(new Image("usedCellIcon", USED_CELL_ICON));
		gridBoxKeyContainer.add(new Image("barcodeCellIcon", BARCODE_CELL_ICON));

		// Download file link button
		gridBoxKeyContainer.add(buildDownloadButton(invCellList));
		add(gridBoxKeyContainer);
	}
	
	/**
	 * Initialise a WorkBook object representing the Box from the database and store as byte array
	 * 
	 * @param invCellList
	 * @return WorkBook as a byte array
	 */
	private byte[] createWorkBookAsByteArray(List<InvCell> invCellList) {
		byte[] bytes = null;
		try {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			WritableWorkbook writableWorkBook = Workbook.createWorkbook(output);
			WritableSheet writableSheet = writableWorkBook.createSheet("Sheet", 0);

			int col = 0;
			int row = 0;
			int cellCount = 0;

			for (Iterator<InvCell> iterator = invCellList.iterator(); iterator.hasNext();) {
				InvCell invCell = (InvCell) iterator.next();
				row = invCell.getRowno().intValue();
				col = invCell.getColno().intValue();

				Biospecimen biospecimen = new Biospecimen();
				biospecimen = invCell.getBiospecimen();
				jxl.write.Label label = null;

				if (biospecimen != null) {
					label = new jxl.write.Label(col, row, biospecimen.getBiospecimenUid());
				}
				else {
					label = new jxl.write.Label(col, row, "");
				}
				writableSheet.addCell(label);
				cellCount = cellCount + 1;
			}

			writableWorkBook.write();
			writableWorkBook.close();
			bytes = output.toByteArray();
			output.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return bytes;
	}

	private AjaxButton buildDownloadButton(final List<InvCell> invCellList) {
		AjaxButton ajaxButton = new AjaxButton("downloadGridBoxData", new StringResourceModel("downloadGridBoxData", this, null)) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 2409955824467683966L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				log.info("Downloading grid as XLS");
				byte[] data = createWorkBookAsByteArray(invCellList);
				String filename = exportXlsFileName + ".xls";
				if (data != null) {
					log.info("Writing out XLS to client");
					au.org.theark.core.util.ByteDataRequestTarget requestTarget = new au.org.theark.core.util.ByteDataRequestTarget("application/vnd.ms-excel", data, filename);
					getRequestCycle().setRequestTarget(requestTarget);
				}

				//TODO: Remove when working
				InputStream inputStream = new ByteArrayInputStream(data);
				OutputStream outputStream;
				try {
					java.io.File file = File.createTempFile("tmpBoxData_", ".xls");
					log.info("Writing out temp file to: " + file.getCanonicalPath());
					outputStream = new FileOutputStream(file);
					IOUtils.copy(inputStream, outputStream);
				}
				catch (FileNotFoundException e) {
					log.error(e.getMessage());
				}
				catch (IOException e) {
					log.error(e.getMessage());
				}
			};
		};

		ajaxButton.setVisible(true);
		ajaxButton.setDefaultFormProcessing(false);
		return ajaxButton;
	}

	/**
	 * @param workBookAsBytes
	 *           the workBookAsBytes to set
	 */
	public void setWorkBookAsBytes(byte[] workBookAsBytes) {
		this.workBookAsBytes = workBookAsBytes;
	}

	/**
	 * @return the workBookAsBytes
	 */
	public byte[] getWorkBookAsBytes() {
		return workBookAsBytes;
	}
}