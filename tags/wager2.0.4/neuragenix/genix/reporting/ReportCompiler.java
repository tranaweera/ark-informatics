/*

 * ============================================================================

 *                   The JasperReports License, Version 1.0

 * ============================================================================

 * 

 * Copyright (C) 2001-2002 Teodor Danciu (teodord@hotmail.com). All rights reserved.

 * 

 * Redistribution and use in source and binary forms, with or without modification,

 * are permitted provided that the following conditions are met:

 * 

 * 1. Redistributions of source code must retain the above copyright notice,

 *    this list of conditions and the following disclaimer.

 * 

 * 2. Redistributions in binary form must reproduce the above copyright notice,

 *    this list of conditions and the following disclaimer in the documentation

 *    and/or other materials provided with the distribution.

 * 

 * 3. The end-user documentation included with the redistribution, if any, must

 *    include the following acknowledgment: "This product includes software

 *    developed by Teodor Danciu (http://jasperreports.sourceforge.net)."

 *    Alternately, this acknowledgment may appear in the software itself, if

 *    and wherever such third-party acknowledgments normally appear.

 * 

 * 4. The name "JasperReports" must not be used to endorse or promote products 

 *    derived from this software without prior written permission. For written 

 *    permission, please contact teodord@hotmail.com.

 * 

 * 5. Products derived from this software may not be called "JasperReports", nor 

 *    may "JasperReports" appear in their name, without prior written permission

 *    of Teodor Danciu.

 * 

 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,

 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND

 * FITNESS  FOR A PARTICULAR  PURPOSE ARE  DISCLAIMED.  IN NO  EVENT SHALL  THE

 * APACHE SOFTWARE  FOUNDATION  OR ITS CONTRIBUTORS  BE LIABLE FOR  ANY DIRECT,

 * INDIRECT, INCIDENTAL, SPECIAL,  EXEMPLARY, OR CONSEQUENTIAL  DAMAGES (INCLU-

 * DING, BUT NOT LIMITED TO, PROCUREMENT  OF SUBSTITUTE GOODS OR SERVICES; LOSS

 * OF USE, DATA, OR  PROFITS; OR BUSINESS  INTERRUPTION)  HOWEVER CAUSED AND ON

 * ANY  THEORY OF LIABILITY,  WHETHER  IN CONTRACT,  STRICT LIABILITY,  OR TORT

 * (INCLUDING  NEGLIGENCE OR  OTHERWISE) ARISING IN  ANY WAY OUT OF THE  USE OF

 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 */



/*

 * ============================================================================

 *                   GNU Lesser General Public License

 * ============================================================================

 *

 * JasperReports - Free Java report-generating library.

 * Copyright (C) 2001-2002 Teodor Danciu teodord@hotmail.com

 * 

 * This library is free software; you can redistribute it and/or

 * modify it under the terms of the GNU Lesser General Public

 * License as published by the Free Software Foundation; either

 * version 2.1 of the License, or (at your option) any later version.

 * 

 * This library is distributed in the hope that it will be useful,

 * but WITHOUT ANY WARRANTY; without even the implied warranty of

 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU

 * Lesser General Public License for more details.

 * 

 * You should have received a copy of the GNU Lesser General Public

 * License along with this library; if not, write to the Free Software

 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.

 * 

 * Teodor Danciu

 * 173, Calea Calarasilor, Bl. 42, Sc. 1, Ap. 18

 * Postal code 741181, Sector 3

 * Bucharest, ROMANIA

 * Email: teodord@hotmail.com

 */

package neuragenix.genix.reporting;



import net.sf.jasperreports.engine.*;

import net.sf.jasperreports.engine.data.*;

import net.sf.jasperreports.engine.export.*;

import net.sf.jasperreports.engine.util.*;

import net.sf.jasperreports.view.*;

import java.sql.*;

import java.util.*;

import java.io.*;





/**

 *

 */

public class ReportCompiler

{





	/**

	 *

	 */

	private static final String TASK_COMPILE = "compile";

	private static final String TASK_FILL = "fill";

	private static final String TASK_PRINT = "print";

	private static final String TASK_PDF = "pdf";

	private static final String TASK_XML = "xml";

	private static final String TASK_XML_EMBED = "xmlEmbed";

	private static final String TASK_HTML = "html";

	private static final String TASK_XLS = "xls";

	private static final String TASK_CSV = "csv";

	private static final String TASK_RUN = "run";

	

	

	/**

	 *

	 */

	public static void main(String[] args)

	{

		String fileName = null;

		String taskName = TASK_COMPILE;



		if(args.length == 0)

		{

			usage();

			return;

		}

				

		int k = 0;

		while ( args.length > k )

		{

			if ( args[k].startsWith("-T") )

				taskName = args[k].substring(2);

			if ( args[k].startsWith("-F") )

				fileName = args[k].substring(2);

			

			k++;	

		}



		try

		{

			long start = System.currentTimeMillis();

			if (TASK_COMPILE.equals(taskName))

			{

				JasperCompileManager.compileReportToFile(fileName);

				System.err.println("Compile time : " + (System.currentTimeMillis() - start));

				System.exit(0);

			}

			else if (TASK_FILL.equals(taskName))

			{

				//Preparing parameters

				Map parameters = new HashMap();

				parameters.put("ReportTitle", "Patients");



				JasperFillManager.fillReportToFile(fileName, parameters, getConnection());

				System.err.println("Filling time : " + (System.currentTimeMillis() - start));

				System.exit(0);

			}

			else if (TASK_PRINT.equals(taskName))

			{

				JasperPrintManager.printReport(fileName, true);

				System.err.println("Printing time : " + (System.currentTimeMillis() - start));

				System.exit(0);

			}

			else if (TASK_PDF.equals(taskName))

			{

				JasperExportManager.exportReportToPdfFile(fileName);

				System.err.println("PDF creation time : " + (System.currentTimeMillis() - start));

				System.exit(0);

			}

			else if (TASK_XML.equals(taskName))

			{

				JasperExportManager.exportReportToXmlFile(fileName, false);

				System.err.println("XML creation time : " + (System.currentTimeMillis() - start));

				System.exit(0);

			}

			else if (TASK_XML_EMBED.equals(taskName))

			{

				JasperExportManager.exportReportToXmlFile(fileName, true);

				System.err.println("XML creation time : " + (System.currentTimeMillis() - start));

				System.exit(0);

			}

			else if (TASK_HTML.equals(taskName))

			{

				JasperExportManager.exportReportToHtmlFile(fileName);

				System.err.println("HTML creation time : " + (System.currentTimeMillis() - start));

				System.exit(0);

			}

			else if (TASK_XLS.equals(taskName))

			{

				File sourceFile = new File(fileName);

		

				JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);

		

				File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".xls");

				

				JRXlsExporter exporter = new JRXlsExporter();

				

				exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);

				exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());

				exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);

				

				exporter.exportReport();



				System.err.println("XLS creation time : " + (System.currentTimeMillis() - start));

				System.exit(0);

			}

			else if (TASK_CSV.equals(taskName))

			{

				File sourceFile = new File(fileName);

		

				JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);

		

				File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".csv");

				

				JRCsvExporter exporter = new JRCsvExporter();

				

				exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);

				exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());

				

				exporter.exportReport();



				System.err.println("CSV creation time : " + (System.currentTimeMillis() - start));

				System.exit(0);

			}

			else if (TASK_RUN.equals(taskName))

			{

				//Preparing parameters

				Map parameters = new HashMap();

				parameters.put("ReportTitle", "Address Report");

				parameters.put("FilterClause", "'Boston', 'Chicago'");

				parameters.put("OrderClause", "City,");

				

				JasperRunManager.runReportToPdfFile(fileName, parameters, getConnection());

				System.err.println("PDF running time : " + (System.currentTimeMillis() - start));

				System.exit(0);

			}

			else

			{

				usage();

				System.exit(0);

			}

		}

		catch (JRException e)

		{

			e.printStackTrace();

			System.exit(1);

		}

		catch (Exception e)

		{

			e.printStackTrace();

			System.exit(1);

		}

	}





	/**

	 *

	 */

	private static void usage()

	{

		System.out.println( "ReportCompiler usage:" );

		System.out.println( "\tjava neuragenix.reporting.ReportCompiler -Ttask -Ffile" );

		System.out.println( "\tTasks : compile | fill1 | fill2 | fill3 | fill4 | print | pdf | xml | xmlEmbed | html | xls | csv | run" );

	}





	/**

	 *

	 */

	private static Connection getConnection() throws ClassNotFoundException, SQLException, Exception

	{

		//Change these settings according to your local configuration

        

        	Properties props = new Properties();

		props.load(PdfServlet.class.getResourceAsStream("/properties/rdbm.properties"));

		String driver = props.getProperty("jdbcDriver");

		String connectString = props.getProperty("jdbcUrl");

		String user = props.getProperty("jdbcUser");

		String password = props.getProperty("jdbcPassword");





		Class.forName(driver);

		Connection conn = DriverManager.getConnection(connectString, user, password);

		return conn;

	}





}

