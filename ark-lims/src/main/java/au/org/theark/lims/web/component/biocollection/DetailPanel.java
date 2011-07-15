package au.org.theark.lims.web.component.biocollection;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;

import au.org.theark.lims.web.component.biocollection.form.ContainerForm;
import au.org.theark.lims.web.component.biocollection.form.DetailForm;

@SuppressWarnings("serial")
public class DetailPanel extends Panel {
	private DetailForm			detailForm;
	private FeedbackPanel		feedBackPanel;
	private WebMarkupContainer	listContainer;
	private WebMarkupContainer	detailsContainer;
	private WebMarkupContainer	searchPanelContainer;
	private WebMarkupContainer	detailPanelFormContainer;
	private WebMarkupContainer	viewButtonContainer;
	private WebMarkupContainer	editButtonContainer;
	private WebMarkupContainer	arkContextMarkup;

	private ContainerForm		containerForm;

	public DetailPanel(String id, final WebMarkupContainer listContainer, FeedbackPanel feedBackPanel, WebMarkupContainer detailsContainer, WebMarkupContainer searchPanelContainer,
			ContainerForm containerForm, WebMarkupContainer viewButtonContainer, WebMarkupContainer editButtonContainer, WebMarkupContainer detailPanelFormContainer, WebMarkupContainer arkContextMarkup) {
		super(id);
		this.feedBackPanel = feedBackPanel;
		this.listContainer = listContainer;
		this.detailsContainer = detailsContainer;
		this.containerForm = containerForm;
		this.searchPanelContainer = searchPanelContainer;
		this.viewButtonContainer = viewButtonContainer;
		this.editButtonContainer = editButtonContainer;
		this.detailPanelFormContainer = detailPanelFormContainer;
		this.arkContextMarkup = arkContextMarkup;
	}

	public void initialisePanel() {
		detailForm = new DetailForm("detailForm", feedBackPanel, this, listContainer, detailsContainer, containerForm, viewButtonContainer, editButtonContainer, detailPanelFormContainer,
				searchPanelContainer, arkContextMarkup);

		detailForm.initialiseDetailForm();
		add(detailForm);
	}

	public DetailForm getDetailForm() {
		return detailForm;
	}

	public void setDetailForm(DetailForm detailsForm) {
		this.detailForm = detailsForm;
	}
}