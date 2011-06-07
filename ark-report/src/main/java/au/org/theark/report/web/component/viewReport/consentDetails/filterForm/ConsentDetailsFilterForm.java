package au.org.theark.report.web.component.viewReport.consentDetails.filterForm;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.protocol.http.WebApplication;
import org.wicketstuff.jasperreports.JRConcreteResource;
import org.wicketstuff.jasperreports.JRResource;
import org.wicketstuff.jasperreports.handlers.CsvResourceHandler;
import org.wicketstuff.jasperreports.handlers.PdfResourceHandler;

import au.org.theark.core.model.study.entity.ConsentStatus;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyComp;
import au.org.theark.core.model.study.entity.SubjectStatus;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.report.model.entity.ReportOutputFormat;
import au.org.theark.report.model.entity.ReportTemplate;
import au.org.theark.report.model.vo.ConsentDetailsReportVO;
import au.org.theark.report.web.Constants;
import au.org.theark.report.web.component.viewReport.consentDetails.ConsentDetailsReportDataSource;
import au.org.theark.report.web.component.viewReport.form.AbstractReportFilterForm;

/**
 * @author elam
 *
 */
@SuppressWarnings("serial")
public class ConsentDetailsFilterForm extends AbstractReportFilterForm<ConsentDetailsReportVO>{
	
	protected TextField<String> tfSubjectUID;
	protected DropDownChoice<SubjectStatus> ddcSubjectStatus;
	protected DropDownChoice<ConsentStatus> ddcConsentStatus;
	protected DateTextField dtfConsentDate;
	protected DropDownChoice<StudyComp>	ddcStudyComp;
	
	public ConsentDetailsFilterForm(String id, CompoundPropertyModel<ConsentDetailsReportVO> model) {
		super(id, model);
		this.cpModel = model;
	}

	protected void onGenerateProcess(AjaxRequestTarget target) {
		
		Long sessionStudyId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(sessionStudyId);
		cpModel.getObject().getLinkSubjectStudy().setStudy(study);

		String reportTitle = study.getName() + " - Study Component Consent Details Report";
		if (cpModel.getObject().getStudyComp() != null) {
			String studyComponent = cpModel.getObject().getStudyComp().getName();
			reportTitle += " - " + studyComponent;
		}
		
		ReportTemplate reportTemplate = cpModel.getObject().getSelectedReportTemplate();
		ReportOutputFormat reportOutputFormat = cpModel.getObject().getSelectedOutputFormat();

		// show report
		ServletContext context = ((WebApplication)getApplication()).getServletContext();
		File reportFile = null;

		reportFile = new File(context.getRealPath("/reportTemplates/" + reportTemplate.getTemplatePath()));
		JasperDesign design = null;
		JasperReport report = null;
		try {
			design = JRXmlLoader.load(reportFile);
//			System.out.println(" design -- created " );
			if (design != null) {
				design.setName(reportTitle);	//set the output file name to match report title
				if (reportOutputFormat.getName().equals(au.org.theark.report.service.Constants.CSV_REPORT_FORMAT)) {
					design.setIgnorePagination(true);	//don't paginate CSVs
				}
				report = JasperCompileManager.compileReport(design);
//				System.out.println(" design -- compiled " );
			}
		} catch (JRException e) {
			reportFile = null;
			e.printStackTrace();
		}
//		templateIS = getClass().getResourceAsStream("/reportTemplates/WebappReport.jrxml");
		final Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("BaseDir", new File(context.getRealPath("/reportTemplates")));
		parameters.put("ReportTitle", reportTitle);
		Subject currentUser = SecurityUtils.getSubject();
		String userName = "(unknown)";
		if(currentUser.getPrincipal() != null)
		{
			userName = (String) currentUser.getPrincipal();
		}
		parameters.put("UserName", userName);
		ConsentDetailsReportDataSource reportDS = new ConsentDetailsReportDataSource(reportService, cpModel.getObject());
		
		JRResource reportResource = null;
		if (reportOutputFormat.getName().equals(au.org.theark.report.service.Constants.PDF_REPORT_FORMAT)) {
			final JRResource pdfResource = new JRConcreteResource<PdfResourceHandler>(new PdfResourceHandler());
			pdfResource.setJasperReport(report);
			pdfResource.setReportParameters(parameters).setReportDataSource(reportDS);
			// This code would emulate a file download as if clicked the user 
			// clicked on the download link, but unfortunately it seems to 
			// stuff up the Indicator (not hidden upon completion).
//			ResourceReference ref = new ResourceReference(study.getName() + "/" + report.getName() + "." + reportOutputFormat.getName()) {
//					protected Resource newResource() {
//						return pdfResource;
//					}
//			};
//			String url = getRequestCycle().urlFor(ref).toString();
//			getRequestCycle().setRequestTarget(new RedirectRequestTarget(url));
//			add(new ResourceLink<Void>("linkToPdf", pdfResource));		
			reportResource = pdfResource;
		}
		else if (reportOutputFormat.getName().equals(au.org.theark.report.service.Constants.CSV_REPORT_FORMAT)) {
			final JRResource csvResource = new JRConcreteResource<CsvResourceHandler>(new CsvResourceHandler());
			csvResource.setJasperReport(report);
			csvResource.setReportParameters(parameters).setReportDataSource(reportDS);
			// This code would emulate a file download as if clicked the user 
			// clicked on the download link, but unfortunately it seems to 
			// stuff up the Indicator (not hidden upon completion).
//			ResourceReference ref = new ResourceReference(study.getName() + "/" + report.getName() + "." + reportOutputFormat.getName()) {
//				protected Resource newResource() {
//					return csvResource;
//				}
//			};
//			String url = getRequestCycle().urlFor(ref).toString();
//			getRequestCycle().setRequestTarget(new RedirectRequestTarget(url));
//			add(new ResourceLink<Void>("linkToCsv", csvResource));
			reportResource = csvResource;
		}
		if (reportResource != null) {
			reportOutputPanel.setReportResource(reportResource);
			reportOutputPanel.setVisible(true);
			target.addComponent(reportOutputPanel);
		}
	}

	@Override
	protected void initialiseCustomFilterComponents() {
		tfSubjectUID = new TextField<String>(Constants.LINKSUBJECTSTUDY_SUBJECTUID);
		add(tfSubjectUID);
		initialiseConsentDatePicker();	
		initialiseSubjectStatusDropDown();
		initialiseConsentStatusDropDown();
		initialiseConsentCompDropDown();
	}

	protected void initialiseConsentDatePicker() {
		dtfConsentDate = new DateTextField(Constants.CONSENT_DATE, au.org.theark.core.Constants.DD_MM_YYYY);
		ArkDatePicker datePicker = new ArkDatePicker();
		datePicker.bind(dtfConsentDate);
		dtfConsentDate.add(datePicker);
		add(dtfConsentDate);
	}
	
	protected void initialiseSubjectStatusDropDown() {
		List<SubjectStatus> subjectStatusList = iArkCommonService.getSubjectStatus();
		ChoiceRenderer<SubjectStatus> defaultChoiceRenderer = new ChoiceRenderer<SubjectStatus>("name", "id");
		ddcSubjectStatus  = new DropDownChoice<SubjectStatus>(Constants.LINKSUBJECTSTUDY_SUBJECTSTATUS, subjectStatusList, defaultChoiceRenderer);
		add(ddcSubjectStatus);
	}
	
	protected void initialiseConsentStatusDropDown() {
		List<ConsentStatus> consentStatusList = iArkCommonService.getConsentStatus();
		ChoiceRenderer<ConsentStatus> defaultChoiceRenderer = new ChoiceRenderer<ConsentStatus>("name", "id");
		ddcConsentStatus  = new DropDownChoice<ConsentStatus>(Constants.CONSENT_STATUS, consentStatusList, defaultChoiceRenderer);
		add(ddcConsentStatus);
	}
	
	protected void initialiseConsentCompDropDown() {
		Long sessionStudyId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(sessionStudyId);

		List<StudyComp> consentStatusList = new ArrayList<StudyComp>(study.getStudyComps());
		ChoiceRenderer<StudyComp> defaultChoiceRenderer = new ChoiceRenderer<StudyComp>("name", "id");
		ddcStudyComp = new DropDownChoice<StudyComp>(Constants.STUDY_COMP, consentStatusList, defaultChoiceRenderer);
		ddcStudyComp.setRequired(true);
		add(ddcStudyComp);
	}

}
