package au.org.theark.core.web.component;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

import au.org.theark.core.vo.ArkCrudContainerVO;

/**
 * <p>
 * Abstract class for the Container panels that contains Search,SearchResult and Detail panels.
 * Defines the WebMarkupContainers and initialises them.It also defines the Model CompoundPropertyModel
 * and provides methods that the sub-classes must implement such as initialiseSearchResults,initialiseDetailPanel
 * and initialiseSearchPanel()
 * </p>
 * @author nivedann
 *
 */
@SuppressWarnings("serial")
public abstract class AbstractContainerPanel<T> extends Panel{
	protected FeedbackPanel feedBackPanel;
	
	protected ArkCrudContainerVO arkCrudContainerVO;
	/*Web Markup Containers */
	protected WebMarkupContainer searchPanelContainer;
	protected WebMarkupContainer searchResultPanelContainer;
	protected WebMarkupContainer detailPanelContainer;
	protected WebMarkupContainer detailPanelFormContainer;
	
	protected WebMarkupContainer wizardPanelContainer;
	protected WebMarkupContainer wizardPanelFormContainer;
	
	protected WebMarkupContainer viewButtonContainer;
	protected WebMarkupContainer editButtonContainer;

	protected IModel<Object> iModel;
	protected CompoundPropertyModel<T> cpModel;
	
	protected PageableListView<T> myListView;
	/**
	 * @param id
	 */
	public AbstractContainerPanel(String id) {

		super(id);
		initialiseMarkupContainers();
	}
	
	public AbstractContainerPanel(String id, boolean flag){
		super(id);
		initCrudContainerVO();
	}
	
	public void initCrudContainerVO(){
		
		arkCrudContainerVO = new ArkCrudContainerVO();
		
	}
	public void initialiseMarkupContainers(){
		
		searchPanelContainer = new WebMarkupContainer("searchContainer");
		searchPanelContainer.setOutputMarkupPlaceholderTag(true);
		
		detailPanelContainer = new WebMarkupContainer("detailContainer");
		detailPanelContainer.setOutputMarkupPlaceholderTag(true);
		detailPanelContainer.setVisible(false);

		//Contains the controls of the details
		detailPanelFormContainer = new WebMarkupContainer("detailFormContainer");
		detailPanelFormContainer.setOutputMarkupPlaceholderTag(true);
		detailPanelFormContainer.setEnabled(false);
		
		wizardPanelContainer = new WebMarkupContainer("wizardContainer");
		wizardPanelContainer.setOutputMarkupPlaceholderTag(true);
		wizardPanelContainer.setVisible(false);

		//Contains the controls of the Wizard
		wizardPanelFormContainer = new WebMarkupContainer("wizardFormContainer");
		wizardPanelFormContainer.setOutputMarkupPlaceholderTag(true);
		wizardPanelFormContainer.setEnabled(false);
		
		//The wrapper for ResultsList panel that will contain a ListView
		searchResultPanelContainer = new WebMarkupContainer("resultListContainer");
		searchResultPanelContainer.setOutputMarkupPlaceholderTag(true);
		searchResultPanelContainer.setVisible(true);
		
		/* Defines a Read-Only Mode */
		viewButtonContainer = new WebMarkupContainer("viewButtonContainer");
		viewButtonContainer.setOutputMarkupPlaceholderTag(true);
		viewButtonContainer.setVisible(false);
		
		/* Defines a edit mode */
		editButtonContainer = new WebMarkupContainer("editButtonContainer");
		editButtonContainer.setOutputMarkupPlaceholderTag(true);
		editButtonContainer.setVisible(false);
		
	}
	
	protected WebMarkupContainer initialiseFeedBackPanel(){
		/* Feedback Panel */
		feedBackPanel= new FeedbackPanel("feedbackMessage");
		feedBackPanel.setOutputMarkupId(true);
		return feedBackPanel;
	}
	
	
	protected abstract WebMarkupContainer initialiseSearchResults();
	
	protected abstract WebMarkupContainer initialiseDetailPanel();
	
	//protected abstract WebMarkupContainer initialiseWizardPanel();
	
	protected abstract WebMarkupContainer initialiseSearchPanel();
	
	
}
