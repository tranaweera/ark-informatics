package au.org.theark.phenotypic.web.component.summaryModule.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.web.form.AbstractSearchForm;
import au.org.theark.phenotypic.model.entity.PhenoCollection;
import au.org.theark.phenotypic.model.vo.PhenoCollectionVO;
import au.org.theark.phenotypic.web.component.summaryModule.DetailPanel;

/**
 * @author cellis
 * 
 */
@SuppressWarnings({"serial", "unused"})
public class SearchForm extends AbstractSearchForm<PhenoCollectionVO>
{
	private PageableListView<PhenoCollection>				listView;
	private CompoundPropertyModel<PhenoCollectionVO>	cpmModel;
	private DetailPanel													detailPanel;

	/**
	 * @param id
	 */
	public SearchForm(String id, CompoundPropertyModel<PhenoCollectionVO> model, PageableListView<PhenoCollection> listView, FeedbackPanel feedBackPanel, DetailPanel detailPanel,
			WebMarkupContainer listContainer, WebMarkupContainer searchMarkupContainer, WebMarkupContainer detailContainer, WebMarkupContainer detailPanelFormContainer,
			WebMarkupContainer viewButtonContainer, WebMarkupContainer editButtonContainer)
	{

		super(id, model, detailContainer, detailPanelFormContainer, viewButtonContainer, editButtonContainer, searchMarkupContainer, listContainer, feedBackPanel);

		this.cpmModel = model;
		this.listView = listView;
		this.detailPanel = detailPanel;
		initialiseFieldForm();
	}

	/**
	 * @param id
	 */
	public SearchForm(String id, CompoundPropertyModel<PhenoCollectionVO> compoundPropertyModel)
	{
		super(id, compoundPropertyModel);
		this.cpmModel = compoundPropertyModel;
		initialiseFieldForm();
		
		// For SummaryModule, disable buttons
		this.viewButtonContainer.setVisible(false);
		this.editButtonContainer.setVisible(false);
	}

	private void initDropDownChoice()
	{
		// Initialise any drop-downs
	}

	public void initialiseFieldForm()
	{
		// Set up fields on the form
		initDropDownChoice();
		addFieldComponents();
	}

	private void addFieldComponents()
	{
		// Add the field components
	}

	@Override
	protected void onNew(AjaxRequestTarget target)
	{
		// What to do on New button click
	}

	@Override
	protected void onSearch(AjaxRequestTarget target)
	{
		// What to do on Search button click
	}
}