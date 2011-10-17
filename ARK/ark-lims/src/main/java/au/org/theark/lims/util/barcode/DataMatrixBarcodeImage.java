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
package au.org.theark.lims.util.barcode;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.markup.html.image.resource.BufferedDynamicImageResource;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;
import org.krysalis.barcode4j.impl.datamatrix.DataMatrixBean;
import org.krysalis.barcode4j.impl.datamatrix.SymbolShapeHint;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.output.bitmap.BitmapEncoder;
import org.krysalis.barcode4j.output.bitmap.BitmapEncoderRegistry;
import org.krysalis.barcode4j.tools.UnitConv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class creates a 2D DataMatrix barcode bitmap, enhanced by custom elements.
 * 
 * @author cellis
 */
public class DataMatrixBarcodeImage extends NonCachingImage {
	/**
	 * 
	 */
	private static final long		serialVersionUID	= 3235603243807828600L;
	private static final Logger	log					= LoggerFactory.getLogger(DataMatrixBarcodeImage.class);
	private static int				dpi					= 200;
	private boolean					antiAlias			= true;
	private int							orientation			= 0;

	/**
	 * Create a new DataMatrixBarcode of the specified barcodeString
	 * 
	 * @param id
	 * @param barcodeString
	 */
	public DataMatrixBarcodeImage(String id, final String barcodeString) {
		super(id);
		setOutputMarkupPlaceholderTag(true);
		setImageResourceReference(getDataMatrixBarcodeImageResource(barcodeString));
	}

	/**
	 * Create a new DataMatrixBarcode of the specified barcodeString with specified parameters
	 * 
	 * @param id
	 * @param barcodeString
	 * @param dpi
	 * @param anitAlias
	 * @param orientation
	 */
	public DataMatrixBarcodeImage(String id, final String barcodeString, int dpi, boolean anitAlias, int orientation) {
		super(id);
		setOutputMarkupPlaceholderTag(true);
		DataMatrixBarcodeImage.dpi = dpi;
		this.antiAlias = anitAlias;
		this.orientation = orientation;
	}

	/**
	 * @return Gets shared image component
	 */
	public static ResourceReference getDataMatrixBarcodeImageResource(final String barcodeString) {
		return new ResourceReference(DataMatrixBarcodeImage.class, "dataMatrixBarcodeImage") {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			public IResource getResource() {
				final BufferedDynamicImageResource resource = new BufferedDynamicImageResource();
				final BufferedImage image = generateBufferedImage(barcodeString);
				resource.setImage(image);
				return resource;
			}
		};
	}

	/**
	 * Creates a BufferedImage of the specified string as a 2D DataMatrix barcode
	 * 
	 * @param msg
	 * @return A Bufferedimage of the 2D DataMatrix barcode
	 * @throws IOException
	 */
	public static BufferedImage generateBufferedImage(String barcodeString) {
		// Create the barcode bean
		DataMatrixBean bean = new DataMatrixBean();

		// Configure the barcode generator
		bean.setModuleWidth(UnitConv.in2mm(10.0f / dpi)); // makes a dot/module exactly ten pixels
		bean.doQuietZone(false);
		bean.setShape(SymbolShapeHint.FORCE_SQUARE);

		boolean antiAlias = true;
		int orientation = 0;
		// Set up the canvas provider to create a monochrome bitmap
		BitmapCanvasProvider canvas = new BitmapCanvasProvider(dpi, BufferedImage.TYPE_BYTE_BINARY, antiAlias, orientation);

		// Generate the barcode
		bean.generateBarcode(canvas, barcodeString);

		// Signal end of generation
		try {
			canvas.finish();
		}
		catch (IOException e) {
			log.error(e.getMessage());
		}

		// Generate the bufferedImage
		BufferedImage bufferedImage = canvas.getBufferedImage();
		return bufferedImage;
	}

	/**
	 * Creates a File of the specified string as a 2D DataMatrix barcode
	 * 
	 * @param barcodeString
	 * @param outputFile
	 * @throws IOException
	 */
	public void generateToFile(String barcodeString, File outputFile) {
		String[] paramArr = new String[] { "Information 1", "Information 2", "Barcode4J is cool!" };
		// Create the barcode bean
		DataMatrixBean bean = new DataMatrixBean();

		// Configure the barcode generator
		bean.setModuleWidth(UnitConv.in2mm(10.0f / dpi)); // makes a dot/module exactly ten pixels
		bean.doQuietZone(false);
		bean.setShape(SymbolShapeHint.FORCE_SQUARE);

		// Set up the canvas provider to create a monochrome bitmap
		BitmapCanvasProvider canvas = new BitmapCanvasProvider(dpi, BufferedImage.TYPE_BYTE_BINARY, antiAlias, orientation);

		// Generate the barcode
		bean.generateBarcode(canvas, barcodeString);

		// Signal end of generation
		try {
			canvas.finish();
		}
		catch (IOException e) {
			log.error(e.getMessage());
		}

		// Get generated bitmap
		BufferedImage symbol = canvas.getBufferedImage();

		int fontSize = 10; // pixels
		int lineHeight = (int) (fontSize * 1.2);
		Font font = new Font("Arial", Font.PLAIN, fontSize);
		int width = symbol.getWidth();
		int height = symbol.getHeight();
		FontRenderContext frc = new FontRenderContext(new AffineTransform(), antiAlias, true);
		for (int i = 0; i < paramArr.length; i++) {
			String line = paramArr[i];
			Rectangle2D bounds = font.getStringBounds(line, frc);
			width = (int) Math.ceil(Math.max(width, bounds.getWidth()));
			height += lineHeight;
		}

		// Add padding
		int padding = 1;
		width += 2 * padding;
		height += 3 * padding;

		BufferedImage bitmap = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
		Graphics2D g2d = (Graphics2D) bitmap.getGraphics();
		g2d.setBackground(Color.white);
		g2d.setColor(Color.black);
		g2d.clearRect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		g2d.setFont(font);

		// Place the barcode symbol
		AffineTransform symbolPlacement = new AffineTransform();
		symbolPlacement.translate(padding, padding);
		g2d.drawRenderedImage(symbol, symbolPlacement);

		// Add text lines (or anything else you might want to add)
		int y = padding + symbol.getHeight() + padding;
		for (int i = 0; i < paramArr.length; i++) {
			String line = paramArr[i];
			y += lineHeight;
			g2d.drawString(line, padding, y);
		}
		g2d.dispose();

		// Encode bitmap as file
		String mime = "image/png";
		OutputStream out = null;
		try {
			out = new FileOutputStream(outputFile);
			final BitmapEncoder encoder = BitmapEncoderRegistry.getInstance(mime);
			encoder.encode(bitmap, out, mime, dpi);
			out.close();
		}
		catch (IOException e) {
			log.error(e.getMessage());
		}
	}
}