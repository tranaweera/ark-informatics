package au.org.theark.phenotypic.web.component.phenoUpload;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.web.component.AbstractContainerPanel;
import au.org.theark.phenotypic.model.entity.PhenoCollection;
import au.org.theark.phenotypic.model.entity.PhenoCollectionUpload;
import au.org.theark.phenotypic.model.vo.UploadVO;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.web.Constants;
import au.org.theark.phenotypic.web.component.phenoUpload.form.ContainerForm;

@SuppressWarnings( { "serial", "unused" })
public class PhenoUploadContainer extends AbstractContainerPanel<UploadVO>
{
	private static final long					serialVersionUID				= 1L;

	// Panels
	private SearchPanel							searchComponentPanel;
	private SearchResultListPanel				searchResultPanel;
	private DetailPanel							detailPanel;
	private WizardPanel							wizardPanel;
	private PageableListView<PhenoCollectionUpload>	listView;
	private ContainerForm						containerForm;

	@SpringBean(name = "phenotypicService")
	private IPhenotypicService					serviceInterface;

	private transient Logger					log								= LoggerFactory.getLogger(PhenoUploadContainer.class);
	private boolean								phenoCollectionInContext	= false;

	public PhenoUploadContainer(String id)
	{
		super(id);

		/* Initialise the CPM */
		cpModel = new CompoundPropertyModel<UploadVO>(new UploadVO());

		initialiseMarkupContainers();

		/* Bind the CPM to the Form */
		containerForm = new ContainerForm("containerForm", cpModel);
		containerForm.add(initialiseFeedBackPanel());
		//containerForm.add(initialiseDetailPanel());
		containerForm.add(initialiseWizardPanel());
		containerForm.add(initialiseSearchResults());
		containerForm.add(initialiseSearchPanel());

		// initialiseTestButtons();

		add(containerForm);
	}

	private WebMarkupContainer initialiseWizardPanel()
	{
		wizardPanel = new WizardPanel("wizardPanel", 
												searchResultPanelContainer, 
												feedBackPanel, 
												wizardPanelContainer, 
												searchPanelContainer, 
												wizardPanelFormContainer,
												containerForm);
		wizardPanel.initialisePanel();
		wizardPanelContainer.setVisible(true);
		wizardPanelContainer.add(wizardPanel);
		return wizardPanelContainer;
	}

	public void initialiseTestButtons()
	{

		// Test button to validate pheno data file
		containerForm.add(new Button(au.org.theark.phenotypic.web.Constants.VALIDATE_PHENOTYPIC_DATA_FILE, new StringResourceModel("page.validatePhenotypicDataFile", this, null))
		{
			public void onSubmit()
			{
				log.info("Validate Phenotypic Data File");

				java.util.Collection<String> validationMessages = null;
				// TODO Add placeholder to store the validation messages
				validationMessages = serviceInterface.validatePhenotypicDataFile();
			}

			public boolean isVisible()
			{
				boolean flag = false;
				Long sessionCollectionId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.phenotypic.web.Constants.SESSION_PHENO_COLLECTION_ID);

				if (sessionCollectionId != null && sessionCollectionId.longValue() > 0)
				{
					flag = true;
					phenoCollectionInContext = true;
				}
				return flag;
			}
		});

		// Test button to import pheno data file
		containerForm.add(new Button(au.org.theark.phenotypic.web.Constants.IMPORT_PHENOTYPIC_DATA_FILE, new StringResourceModel("page.importPhenotypicDataFile", this, null))
		{
			public void onSubmit()
			{
				log.info("Import Phenotypic Data File");
				serviceInterface.importPhenotypicDataFile();
			}

			public boolean isVisible()
			{
				boolean flag = false;
				Long sessionCollectionId = (Long) SecurityUtils.getSubject().getSession().getAttribute(Constants.SESSION_PHENO_COLLECTION_ID);

				if (sessionCollectionId != null && sessionCollectionId.longValue() > 0)
				{
					flag = true;
					phenoCollectionInContext = true;
				}
				return flag;
			}
		});

		add(containerForm);
	}

	protected WebMarkupContainer initialiseSearchResults()
	{
		searchResultPanel = new SearchResultListPanel("searchResults", detailPanelContainer, searchPanelContainer, containerForm, searchResultPanelContainer, detailPanel, viewButtonContainer,
				editButtonContainer, detailPanelFormContainer);

		iModel = new LoadableDetachableModel<Object>()
		{
			private static final long	serialVersionUID	= 1L;

			@Override
			protected Object load()
			{
				// Return all Uploads for the PhenoCollection in context
				Long sessionCollectionId = (Long) SecurityUtils.getSubject().getSession().getAttribute(Constants.SESSION_PHENO_COLLECTION_ID);
				PhenoCollection phenoCollection = serviceInterface.getPhenoCollection(sessionCollectionId);
				PhenoCollectionUpload phenoCollectionUpload = new PhenoCollectionUpload();
				
				if (sessionCollectionId != null)
					phenoCollectionUpload.setCollection(phenoCollection);
				
				listView.removeAll();
				java.util.Collection<PhenoCollectionUpload> phenoCollectionUploads = serviceInterface.searchPhenoCollectionUpload(phenoCollectionUpload); 
				return  phenoCollectionUploads;
			}
		};

		listView = searchResultPanel.buildPageableListView(iModel);
		listView.setReuseItems(true);
		PagingNavigator pageNavigator = new PagingNavigator("navigator", listView);
		searchResultPanel.add(pageNavigator);
		searchResultPanel.add(listView);
		searchResultPanelContainer.add(searchResultPanel);
		searchResultPanel.setVisible(true);

		return searchResultPanelContainer;
	}

	protected WebMarkupContainer initialiseDetailPanel()
	{
		detailPanel = new DetailPanel("detailPanel", searchResultPanelContainer, feedBackPanel, detailPanelContainer, searchPanelContainer, containerForm, viewButtonContainer, editButtonContainer,
				detailPanelFormContainer);
		detailPanel.initialisePanel();
		detailPanelContainer.add(detailPanel);
		return detailPanelContainer;
	}

	protected WebMarkupContainer initialiseSearchPanel()
	{
		searchComponentPanel = new SearchPanel("searchPanel", feedBackPanel, searchPanelContainer, listView, searchResultPanelContainer, wizardPanelContainer, wizardPanel, containerForm,
				viewButtonContainer, editButtonContainer, wizardPanelFormContainer);
		searchComponentPanel.initialisePanel();
		searchComponentPanel.setVisible(false);
		searchPanelContainer.add(searchComponentPanel);
		return searchPanelContainer;
	}
}