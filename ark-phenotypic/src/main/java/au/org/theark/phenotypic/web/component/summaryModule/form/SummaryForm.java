package au.org.theark.phenotypic.web.component.summaryModule.form;

import java.awt.Color;
import java.util.Iterator;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.security.ArkPermissionHelper;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.util.BarChartResult;
import au.org.theark.core.web.component.chart.JFreeChartImage;
import au.org.theark.phenotypic.model.vo.PhenoCollectionVO;
import au.org.theark.phenotypic.service.IPhenotypicService;

/**
 * @author cellis
 * 
 */
public class SummaryForm extends Form<PhenoCollectionVO> {
	/**
	 * 
	 */
	private static final long			serialVersionUID	= 7554016167563013219L;
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>	iArkCommonService;

	@SpringBean(name = au.org.theark.phenotypic.service.Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService			iPhenotypicService;

	private JFreeChart					jFreeChart;
	private DefaultPieDataset			defaultPieDataset;

	/**
	 * @param id
	 */
	public SummaryForm(String id, FeedbackPanel feedBackPanel, WebMarkupContainer searchMarkupContainer, WebMarkupContainer resultListContainer) {
		super(id);

		initialiseFieldForm();

		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		
		if (ArkPermissionHelper.isModuleFunctionAccessPermitted()) {
			if (sessionStudyId == null) {
				searchMarkupContainer.setEnabled(false);
				this.error("There is no study in context. Please select a study");
			}
			else {
				searchMarkupContainer.setEnabled(true);
			}
		}
		else {
			searchMarkupContainer.setEnabled(false);
			resultListContainer.setVisible(false);
			this.error(au.org.theark.core.Constants.MODULE_NOT_ACCESSIBLE_MESSAGE);
		}
	}

	public void initialiseFieldForm() {
		// Force versioning to force the refresh of the images (ignoring cache)
		setVersioned(true);

		// Study in context
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);

		if (sessionStudyId != null) {
			Study study = iArkCommonService.getStudy(sessionStudyId);

			defaultPieDataset = new DefaultPieDataset();
			defaultPieDataset.setValue("Fields with Data", new Integer(iPhenotypicService.getCountOfFieldsWithDataInStudy(study)));
			defaultPieDataset.setValue("Fields without Data", new Integer(iPhenotypicService.getCountOfFieldsInStudy(study) - iPhenotypicService.getCountOfFieldsWithDataInStudy(study)));

			jFreeChart = ChartFactory.createPieChart("Phenotypic Field Summary", defaultPieDataset, true, // Show legend
					true, // Show tooltips
					true); // Show urls
			jFreeChart.setBackgroundPaint(Color.white);
			jFreeChart.setBorderVisible(false);
			addOrReplace(new JFreeChartImage("phenoFieldSummaryImage", jFreeChart, 400, 400).setVersioned(true));

			defaultPieDataset = new DefaultPieDataset();
			int intValue = iPhenotypicService.getCountOfCollectionsWithDataInStudy(study);

			defaultPieDataset.setValue("Collections with Data", new Integer(intValue));

			intValue = iPhenotypicService.getCountOfCollectionsInStudy(study) - iPhenotypicService.getCountOfCollectionsWithDataInStudy(study);
			defaultPieDataset.setValue("Collections without Data", new Integer(intValue));

			jFreeChart = ChartFactory.createPieChart("Phenotypic Collection Summary", defaultPieDataset, true, // Show legend
					true, // Show tooltips
					true); // Show urls
			jFreeChart.setBackgroundPaint(Color.white);
			jFreeChart.setBorderVisible(false);
			addOrReplace(new JFreeChartImage("phenoPhenoCollectionSummaryImage", jFreeChart, 400, 400).setVersioned(true));

			DefaultCategoryDataset dataset = new DefaultCategoryDataset();

			List<BarChartResult> resultList = iPhenotypicService.getFieldsWithDataResults(study);
			for (Iterator<BarChartResult> iterator = resultList.iterator(); iterator.hasNext();) {
				BarChartResult barChartResult = (BarChartResult) iterator.next();
				dataset.setValue(barChartResult.getValue(), barChartResult.getRowKey(), barChartResult.getColumnKey());
			}

			jFreeChart = ChartFactory.createBarChart("Fields With Data", // Title
					"Collection Name", // categoryAxisLabel
					"Fields", // valueAxisLabel
					dataset, // Dataset
					PlotOrientation.VERTICAL, // Orientation
					false, // Show legend
					true, // Show tooltips
					false // Show urls
					);
			addOrReplace(new JFreeChartImage("phenoCollectionBarChartSummaryImage", jFreeChart, 800, 400).setVersioned(true));
		}
		else {
			defaultPieDataset = new DefaultPieDataset();
			defaultPieDataset.setValue("Fields with Data", new Integer(0));
			defaultPieDataset.setValue("Fields without Data", new Integer(0));

			jFreeChart = ChartFactory.createPieChart("Phenotypic Field Summary", defaultPieDataset, true, // Show legend
					true, // Show tooltips
					true); // Show urls
			jFreeChart.setBackgroundPaint(Color.white);
			jFreeChart.setBorderVisible(false);
			addOrReplace(new JFreeChartImage("phenoFieldSummaryImage", jFreeChart, 0, 0).setVisible(false));

			jFreeChart = ChartFactory.createPieChart("Phenotypic Collection Summary", defaultPieDataset, true, // Show legend
					true, // Show tooltips
					true); // Show urls
			jFreeChart.setBackgroundPaint(Color.white);
			jFreeChart.setBorderVisible(false);
			addOrReplace(new JFreeChartImage("phenoPhenoCollectionSummaryImage", jFreeChart, 0, 0).setVisible(false));
			addOrReplace(new JFreeChartImage("phenoCollectionBarChartSummaryImage", jFreeChart, 800, 400).setVisible(false));
		}
	}
}