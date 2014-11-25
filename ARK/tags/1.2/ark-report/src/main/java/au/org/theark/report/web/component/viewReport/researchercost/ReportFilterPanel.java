package au.org.theark.report.web.component.viewReport.researchercost;

import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.report.model.vo.ResearcherCostResportVO;
import au.org.theark.report.web.component.viewReport.ReportOutputPanel;
import au.org.theark.report.web.component.viewReport.researchercost.filterform.ResearcherCostFilterForm;

public class ReportFilterPanel extends Panel {
	
	private static final long	serialVersionUID	= 1L;

	AjaxButton						generateButton;

	public ReportFilterPanel(String id) {
		super(id);
	}

	public void initialisePanel(CompoundPropertyModel<ResearcherCostResportVO> cpModel, FeedbackPanel feedbackPanel, ReportOutputPanel reportOutputPanel) {
		ResearcherCostFilterForm researcherCostFilterForm = new ResearcherCostFilterForm("filterForm", cpModel);
		researcherCostFilterForm.initialiseFilterForm(feedbackPanel, reportOutputPanel);
		add(researcherCostFilterForm);
	}
}
