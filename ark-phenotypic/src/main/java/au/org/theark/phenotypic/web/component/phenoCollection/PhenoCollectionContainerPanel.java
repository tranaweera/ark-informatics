package au.org.theark.phenotypic.web.component.phenoCollection;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.component.AbstractContainerPanel;
import au.org.theark.phenotypic.model.entity.Field;
import au.org.theark.phenotypic.model.entity.PhenoCollection;
import au.org.theark.phenotypic.model.vo.PhenoCollectionVO;
import au.org.theark.phenotypic.service.Constants;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.web.component.phenoCollection.Detail;
import au.org.theark.phenotypic.web.component.phenoCollection.Search;
import au.org.theark.phenotypic.web.component.phenoCollection.SearchResultList;
import au.org.theark.phenotypic.web.component.phenoCollection.form.ContainerForm;

public class PhenoCollectionContainerPanel extends AbstractContainerPanel<PhenoCollectionVO>
{
//	private static final long					serialVersionUID	= 1L;
//
//	private FeedbackPanel						feedBackPanel;
//
//	// Panels
//	private Search									searchComponentPanel;
//	private SearchResultList					searchResultPanel;
//	private Detail									detailPanel;
//
//	private CompoundPropertyModel<PhenoCollectionVO>	fieldCpm;
//
//	private IModel<Object>						iModel;
//	private PageableListView<PhenoCollection>			listView;
//
//	// Mark-up Containers
//	private WebMarkupContainer					searchPanelContainer;
//	private WebMarkupContainer					resultListContainer;
//	private WebMarkupContainer					detailPanelContainer;
//	private WebMarkupContainer					detailPanelFormContainer;
//	
//	private WebMarkupContainer viewButtonContainer;
//	private WebMarkupContainer editButtonContainer;
//	
//
//	private ContainerForm						containerForm;
//
//	@SpringBean(name = Constants.PHENOTYPIC_SERVICE)
//	private IPhenotypicService					phenotypicService;
//
//	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
//	private IArkCommonService					iArkCommonService;
	private static final long					serialVersionUID	= 1L;

	// Panels
	private Search									searchComponentPanel;
	private SearchResultList					searchResultPanel;
	private Detail									detailPanel;
	private PageableListView<PhenoCollection>			listView;

	private ContainerForm						containerForm;

	@SpringBean(name = Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService					phenotypicService;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService					iArkCommonService;

	public PhenoCollectionContainerPanel(String id)
	{
		super(id);

		/* Initialise the CPM */
		cpModel = new CompoundPropertyModel<PhenoCollectionVO>(new PhenoCollectionVO());

		initialiseMarkupContainers();

		/* Bind the CPM to the Form */
		containerForm = new ContainerForm("containerForm", cpModel);
		containerForm.add(initialiseFeedBackPanel());
		containerForm.add(initialiseDetailPanel());
		containerForm.add(initialiseSearchResults());
		containerForm.add(initialiseSearchPanel());

		add(containerForm);
	}

	protected WebMarkupContainer initialiseSearchResults()
	{

		searchResultPanel = new SearchResultList("searchResults", detailPanelContainer, searchPanelContainer, containerForm, searchResultPanelContainer, detailPanel,
				viewButtonContainer,
				editButtonContainer,
				detailPanelFormContainer);

		iModel = new LoadableDetachableModel<Object>()
		{
			private static final long	serialVersionUID	= 1L;

			@Override
			protected Object load()
			{
				return containerForm.getModelObject().getPhenoCollectionCollection();
			}
		};

		listView = searchResultPanel.buildPageableListView(iModel);
		listView.setReuseItems(true);
		PagingNavigator pageNavigator = new PagingNavigator("navigator", listView);
		searchResultPanel.add(pageNavigator);
		searchResultPanel.add(listView);
		searchResultPanelContainer.add(searchResultPanel);
		return searchResultPanelContainer;
	}

	protected WebMarkupContainer initialiseDetailPanel()
	{
		detailPanel = new Detail("detailPanel", searchResultPanelContainer, feedBackPanel, detailPanelContainer, searchPanelContainer, containerForm,
				viewButtonContainer,
				editButtonContainer,
				detailPanelFormContainer);
		detailPanel.initialisePanel();
		detailPanelContainer.add(detailPanel);
		return detailPanelContainer;
	}

	protected WebMarkupContainer initialiseSearchPanel()
	{
		// Get a collection of fields for the study in context by default
		Collection<PhenoCollection> phenoCollectionCol = new ArrayList<PhenoCollection>();
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);

		if (sessionStudyId != null && sessionStudyId > 0)
		{
			Study study = iArkCommonService.getStudy(sessionStudyId);
			containerForm.getModelObject().getPhenoCollection().setStudy(study);
			phenoCollectionCol = phenotypicService.searchPhenotypicCollection(containerForm.getModelObject().getPhenoCollection());
		}

		containerForm.getModelObject().setPhenoCollectionCollection(phenoCollectionCol);

		searchComponentPanel = new Search("searchPanel", feedBackPanel, searchPanelContainer, listView, searchResultPanelContainer, detailPanelContainer, detailPanel, containerForm, viewButtonContainer, editButtonContainer, detailPanelFormContainer);
		searchComponentPanel.initialisePanel();
		
		searchPanelContainer.add(searchComponentPanel);
		return searchPanelContainer;
	}
}