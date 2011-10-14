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
package au.org.theark.web.component.applet;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * @author cellis
 * 
 */
public class PrintAppletPanel extends Panel {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 3901317600541352403L;

	public PrintAppletPanel(String id) {
		super(id);
		setOutputMarkupPlaceholderTag(true);

		WebMarkupContainer applet = new WebMarkupContainer("applet") {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

		};
		applet.setOutputMarkupPlaceholderTag(true);
		
		//String codebase = new String();
		// codebase="http://hostname/ark/classes"
		//HttpServletRequest req = (HttpServletRequest)((WebRequest)RequestCycle.get().getRequest()).getContainerRequest();
		//codebase = RequestUtils.toAbsolutePath(req.getRequestURL().toString(), "classes");
		//applet.add(new AttributeModifier("codebase", codebase));
		add(applet);
	}
}
