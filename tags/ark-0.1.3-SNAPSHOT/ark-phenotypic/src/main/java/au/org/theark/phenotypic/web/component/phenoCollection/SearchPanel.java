package au.org.theark.phenotypic.web.component.phenoCollection;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.model.pheno.entity.PhenoCollection;
import au.org.theark.phenotypic.model.vo.PhenoCollectionVO;
import au.org.theark.phenotypic.web.component.phenoCollection.form.ContainerForm;
import au.org.theark.phenotypic.web.component.phenoCollection.form.SearchForm;

/**
 * @author cellis
 * 
 */
@SuppressWarnings("serial")
public class SearchPanel extends Panel
{
	private FeedbackPanel				feedBackPanel;
	private WebMarkupContainer			searchMarkupContainer;
	private WebMarkupContainer			listContainer;
	private WebMarkupContainer			detailContainer;
	private PageableListView<PhenoCollection>	listView;
	private ContainerForm				containerForm;
	private DetailPanel							detailPanel;
	private WebMarkupContainer viewButtonContainer;
	private WebMarkupContainer editButtonContainer;
	private WebMarkupContainer detailPanelFormContainer;
	private WebMarkupContainer arkContextMarkup;

	/* Constructor */
	public SearchPanel(	String id, 
					FeedbackPanel feedBackPanel, 
					WebMarkupContainer searchMarkupContainer, 
					PageableListView<PhenoCollection> listView, 
					WebMarkupContainer resultListContainer,
					WebMarkupContainer detailPanelContainer, 
					DetailPanel detail, 
					ContainerForm containerForm,
					WebMarkupContainer viewButtonContainer,
					WebMarkupContainer editButtonContainer,
					WebMarkupContainer detailPanelFormContainer,
					WebMarkupContainer arkContextMarkup)
	{
		super(id);
		this.searchMarkupContainer = searchMarkupContainer;
		this.listView = listView;
		this.feedBackPanel = feedBackPanel;
		this.detailContainer = detailPanelContainer;
		this.detailPanel = detail;
		this.containerForm = containerForm;
		this.viewButtonContainer = viewButtonContainer;
		this.editButtonContainer = editButtonContainer;
		this.detailPanelFormContainer = detailPanelFormContainer;
		this.arkContextMarkup = arkContextMarkup;
		listContainer = resultListContainer;
	}


	public void initialisePanel()
	{
		SearchForm searchForm = new SearchForm(	au.org.theark.core.Constants.SEARCH_FORM, 
												(CompoundPropertyModel<PhenoCollectionVO>) containerForm.getModel(),
												listView,
												feedBackPanel,
												detailPanel,
												listContainer,
												searchMarkupContainer,
												detailContainer,
												detailPanelFormContainer,
												viewButtonContainer,
												editButtonContainer,
												arkContextMarkup
												);
		
		add(searchForm);
	}
}