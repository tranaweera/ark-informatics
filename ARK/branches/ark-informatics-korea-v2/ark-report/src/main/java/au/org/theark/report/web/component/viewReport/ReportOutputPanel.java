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
package au.org.theark.report.web.component.viewReport;

import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.wicketstuff.jasperreports.JRResource;

public class ReportOutputPanel extends Panel {


	private static final long		serialVersionUID	= 1L;

	public AbstractLink				downloadReportLink;
	public AbstractLink				otherIDLink;

	public ReportOutputPanel(String id) {
		super(id);
	}

	public void initialisePanel() {
		downloadReportLink = new ExternalLink("linkToReport", "", "");
		downloadReportLink.setOutputMarkupPlaceholderTag(true); // allow link to be replaced even when invisible
		add(downloadReportLink);

		otherIDLink = new ExternalLink("otherIDLink", "", "");
		otherIDLink.setOutputMarkupPlaceholderTag(true);
		add(otherIDLink);
		
		this.setVisible(false); // start off invisible
	}

	public void setReportResource(JRResource reportResource) {
		if (reportResource != null) {
			ResourceLink<Void> newLink = new ResourceLink<Void>("linkToReport", reportResource);
			newLink.setOutputMarkupPlaceholderTag(true); // allow link to be replaced even when invisible
			addOrReplace(newLink);
			downloadReportLink = newLink;
		}
		else {
			if (!downloadReportLink.getClass().equals(ExternalLink.class)) {
				ExternalLink newLink = new ExternalLink("linkToReport", "", "");
				newLink.setOutputMarkupPlaceholderTag(true); // allow link to be replaced even when invisible
				addOrReplace(newLink);
				downloadReportLink = newLink;
			}
		}
	}
	
	public void setOtherIDReportResource(JRResource resource) {
		if(resource != null) {
			ResourceLink<Void> newLink = new ResourceLink<Void>("otherIDLink", resource);
			newLink.setOutputMarkupPlaceholderTag(true);
			addOrReplace(newLink);
			otherIDLink = newLink;
		} else {
			if(!otherIDLink.getClass().equals(ExternalLink.class)){
				ExternalLink newLink = new ExternalLink("otherIDLink", "", "");
				newLink.setOutputMarkupPlaceholderTag(true); // allow link to be replaced even when invisible
				addOrReplace(newLink);
				otherIDLink = newLink;
			}
		}
	}

	@Override
	public boolean isVersioned() {
		return false;
	}
}
