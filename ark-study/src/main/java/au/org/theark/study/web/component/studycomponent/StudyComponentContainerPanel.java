package au.org.theark.study.web.component.studycomponent;

import org.apache.wicket.markup.html.panel.Panel;

public class StudyComponentContainerPanel extends Panel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Search searchComponentPanel;
	public StudyComponentContainerPanel(String id) {
		
		super(id);
		searchComponentPanel = new Search("searchComponentPanel");
		searchComponentPanel.initialise();
		add(searchComponentPanel);
	}
	

}
