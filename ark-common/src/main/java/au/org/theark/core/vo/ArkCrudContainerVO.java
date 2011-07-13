/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.core.vo;

import java.io.Serializable;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;

/**
 * The base container VO object that holds all WebMarkupContainers (panels and forms) of the generic search/detail/results pattern
 * @author nivedann
 * 
 */
public class ArkCrudContainerVO implements Serializable
{
	/**
	 * 
	 */
	private static final long		serialVersionUID	= -4796975897895746053L;
	protected WebMarkupContainer	searchPanelContainer;
	protected WebMarkupContainer	searchResultPanelContainer;
	protected WebMarkupContainer	detailPanelContainer;
	protected WebMarkupContainer	detailPanelFormContainer;
	protected WebMarkupContainer	viewButtonContainer;
	protected WebMarkupContainer	editButtonContainer;
	protected WebMarkupContainer	wizardPanelContainer;
	protected WebMarkupContainer	wizardPanelFormContainer;
	protected WebMarkupContainer	wmcForarkUserAccountPanel;
	protected PageableListView<?> myListView;

	/**
	 * Default constructor. Initialises all objects and sets default visibility
	 */
	public ArkCrudContainerVO()
	{
		initialisePanels();
		setDefaultVisibility();
	}
	
	/**
	 * General method to initialise all panels within the VO
	 */
	public void initialisePanels()
	{
		// Web markup to hold the Search panel
		searchPanelContainer = new WebMarkupContainer("searchContainer");
		searchPanelContainer.setOutputMarkupPlaceholderTag(true);
		
		// Web markup to hold the SearchResults panel
		searchResultPanelContainer = new WebMarkupContainer("resultListContainer");
		searchResultPanelContainer.setOutputMarkupPlaceholderTag(true);

		// Web markup to hold the Detail panel
		detailPanelContainer = new WebMarkupContainer("detailContainer");
		detailPanelContainer.setOutputMarkupPlaceholderTag(true);
		
		// Contains the controls of the Detail form
		detailPanelFormContainer = new WebMarkupContainer("detailFormContainer");
		detailPanelFormContainer.setOutputMarkupPlaceholderTag(true);

		// Web markup to hold the buttons visible when in "View" mode
		viewButtonContainer = new WebMarkupContainer("viewButtonContainer");
		viewButtonContainer.setOutputMarkupPlaceholderTag(true);

		// Web markup to hold the buttons visible when in "Edit" mode
		editButtonContainer = new WebMarkupContainer("editButtonContainer");
		editButtonContainer.setOutputMarkupPlaceholderTag(true);

		// Web markup to hold the Wizard panel
		wizardPanelContainer = new WebMarkupContainer("wizardContainer");
		wizardPanelContainer.setOutputMarkupPlaceholderTag(true);

		// Contains the controls of the Wizard form
		wizardPanelFormContainer = new WebMarkupContainer("wizardFormContainer");
		wizardPanelFormContainer.setOutputMarkupPlaceholderTag(true);

		// Will contain User Management related Modules and Roles Panel
		wmcForarkUserAccountPanel = new WebMarkupContainer("arkUserAccountPanelcontainer");
		wmcForarkUserAccountPanel.setOutputMarkupPlaceholderTag(true);
	}
	
	/**
	 * General method that sets the default visibility of the panels 
	 */
	public void setDefaultVisibility()
	{
		searchPanelContainer.setVisible(true);
		detailPanelContainer.setVisible(false);
		detailPanelFormContainer.setEnabled(false);
		searchResultPanelContainer.setVisible(true);
		viewButtonContainer.setVisible(false);
		editButtonContainer.setVisible(false);
		wizardPanelContainer.setVisible(true);
		wizardPanelFormContainer.setEnabled(true);
	}
	
	/**
	 * General method that hides search and searchResults, and displays detail panels in view mode
	 * @param target
	 */
	public void showDetailPanelInViewMode(AjaxRequestTarget target)
	{
		getSearchResultPanelContainer().setVisible(false);
		getSearchPanelContainer().setVisible(false);
		getDetailPanelContainer().setVisible(true);
		getDetailPanelFormContainer().setEnabled(false);
		getViewButtonContainer().setVisible(true);
		getViewButtonContainer().setEnabled(true);
		getEditButtonContainer().setVisible(false);

		// Refresh the markup containers
		target.addComponent(getSearchResultPanelContainer());
		target.addComponent(getDetailPanelContainer());
		target.addComponent(getDetailPanelFormContainer());
		target.addComponent(getSearchPanelContainer());
		target.addComponent(getViewButtonContainer());
		target.addComponent(getEditButtonContainer());
	}
	
	/**
	 * General method that hides search and searchResults, and displays detail panels in edit mode
	 * @param target
	 */
	public void showDetailPanelInEditMode(AjaxRequestTarget target)
	{
		getSearchResultPanelContainer().setVisible(false);
		getSearchPanelContainer().setVisible(false);
		getDetailPanelContainer().setVisible(true);
		getDetailPanelFormContainer().setEnabled(true);
		getViewButtonContainer().setVisible(false);
		getEditButtonContainer().setVisible(true);

		// Refresh the markup containers
		target.addComponent(getSearchResultPanelContainer());
		target.addComponent(getDetailPanelContainer());
		target.addComponent(getDetailPanelFormContainer());
		target.addComponent(getSearchPanelContainer());
		target.addComponent(getViewButtonContainer());
		target.addComponent(getEditButtonContainer());
	}
	
	/**
	 * General method that hides the detail panel, and displays search and searchResults panels accordingly
	 * @param target
	 */
	public void showSearchPanels(AjaxRequestTarget target)
	{
		getSearchResultPanelContainer().setVisible(true);
		getSearchPanelContainer().setVisible(true);
		getDetailPanelContainer().setVisible(false);
		getDetailPanelFormContainer().setEnabled(false);
		getViewButtonContainer().setVisible(false);
		getViewButtonContainer().setEnabled(true);
		getEditButtonContainer().setVisible(false);

		// Refresh the markup containers
		target.addComponent(getSearchResultPanelContainer());
		target.addComponent(getDetailPanelContainer());
		target.addComponent(getDetailPanelFormContainer());
		target.addComponent(getSearchPanelContainer());
		target.addComponent(getViewButtonContainer());
		target.addComponent(getEditButtonContainer());
	}

	/**
	 * @return the searchPanelContainer
	 */
	public WebMarkupContainer getSearchPanelContainer()
	{
		return searchPanelContainer;
	}

	/**
	 * @param searchPanelContainer the searchPanelContainer to set
	 */
	public void setSearchPanelContainer(WebMarkupContainer searchPanelContainer)
	{
		this.searchPanelContainer = searchPanelContainer;
	}

	/**
	 * @return the searchResultPanelContainer
	 */
	public WebMarkupContainer getSearchResultPanelContainer()
	{
		return searchResultPanelContainer;
	}

	/**
	 * @param searchResultPanelContainer the searchResultPanelContainer to set
	 */
	public void setSearchResultPanelContainer(WebMarkupContainer searchResultPanelContainer)
	{
		this.searchResultPanelContainer = searchResultPanelContainer;
	}

	/**
	 * @return the detailPanelContainer
	 */
	public WebMarkupContainer getDetailPanelContainer()
	{
		return detailPanelContainer;
	}

	/**
	 * @param detailPanelContainer the detailPanelContainer to set
	 */
	public void setDetailPanelContainer(WebMarkupContainer detailPanelContainer)
	{
		this.detailPanelContainer = detailPanelContainer;
	}

	/**
	 * @return the detailPanelFormContainer
	 */
	public WebMarkupContainer getDetailPanelFormContainer()
	{
		return detailPanelFormContainer;
	}

	/**
	 * @param detailPanelFormContainer the detailPanelFormContainer to set
	 */
	public void setDetailPanelFormContainer(WebMarkupContainer detailPanelFormContainer)
	{
		this.detailPanelFormContainer = detailPanelFormContainer;
	}

	/**
	 * @return the viewButtonContainer
	 */
	public WebMarkupContainer getViewButtonContainer()
	{
		return viewButtonContainer;
	}

	/**
	 * @param viewButtonContainer the viewButtonContainer to set
	 */
	public void setViewButtonContainer(WebMarkupContainer viewButtonContainer)
	{
		this.viewButtonContainer = viewButtonContainer;
	}

	/**
	 * @return the editButtonContainer
	 */
	public WebMarkupContainer getEditButtonContainer()
	{
		return editButtonContainer;
	}

	/**
	 * @param editButtonContainer the editButtonContainer to set
	 */
	public void setEditButtonContainer(WebMarkupContainer editButtonContainer)
	{
		this.editButtonContainer = editButtonContainer;
	}

	/**
	 * @return the wizardPanelContainer
	 */
	public WebMarkupContainer getWizardPanelContainer()
	{
		return wizardPanelContainer;
	}

	/**
	 * @param wizardPanelContainer the wizardPanelContainer to set
	 */
	public void setWizardPanelContainer(WebMarkupContainer wizardPanelContainer)
	{
		this.wizardPanelContainer = wizardPanelContainer;
	}

	/**
	 * @return the wizardPanelFormContainer
	 */
	public WebMarkupContainer getWizardPanelFormContainer()
	{
		return wizardPanelFormContainer;
	}

	/**
	 * @param wizardPanelFormContainer the wizardPanelFormContainer to set
	 */
	public void setWizardPanelFormContainer(WebMarkupContainer wizardPanelFormContainer)
	{
		this.wizardPanelFormContainer = wizardPanelFormContainer;
	}

	/**
	 * @return the wmcForarkUserAccountPanel
	 */
	public WebMarkupContainer getWmcForarkUserAccountPanel()
	{
		return wmcForarkUserAccountPanel;
	}

	/**
	 * @param wmcForarkUserAccountPanel the wmcForarkUserAccountPanel to set
	 */
	public void setWmcForarkUserAccountPanel(WebMarkupContainer wmcForarkUserAccountPanel)
	{
		this.wmcForarkUserAccountPanel = wmcForarkUserAccountPanel;
	}

	/**
	 * @return the pageableListView
	 */
	public PageableListView<?> getMyListView()
	{
		return myListView;
	}

	/**
	 * @param pageableListView the pageableListView to set
	 */
	public void setMyListView(PageableListView<?> pageableListView)
	{
		this.myListView = pageableListView;
	}
}