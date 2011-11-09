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
package au.org.theark.report.web.component.viewReport.phenoFieldDetails;

import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.report.model.vo.CustomFieldDetailsReportVO;
import au.org.theark.report.web.component.viewReport.phenoFieldDetails.filterForm.FieldDetailsFilterForm;

public class ReportFilterPanel extends Panel {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	AjaxButton						generateButton;

	public ReportFilterPanel(String id) {
		super(id);
		// TODO Auto-generated constructor stub
	}

	public void initialisePanel(CompoundPropertyModel<CustomFieldDetailsReportVO> cpModel, FeedbackPanel feedbackPanel, au.org.theark.report.web.component.viewReport.ReportOutputPanel reportOutputPanel) {
		FieldDetailsFilterForm fieldDetailsFilterForm = new FieldDetailsFilterForm("filterForm", cpModel);
		fieldDetailsFilterForm.initialiseFilterForm(feedbackPanel, reportOutputPanel);
		add(fieldDetailsFilterForm);
	}
}
