package au.org.theark.phenotypic.web.component.fieldUpload;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.model.pheno.entity.FieldUpload;
import au.org.theark.core.model.pheno.entity.PhenoCollectionUpload;
import au.org.theark.core.model.pheno.entity.PhenoUpload;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.component.AbstractContainerPanel;
import au.org.theark.phenotypic.model.vo.UploadVO;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.web.component.fieldUpload.form.ContainerForm;

@SuppressWarnings({ "serial", "unused" })
public class FieldUploadContainerPanel extends AbstractContainerPanel<UploadVO> {
	private static final long					serialVersionUID				= 1L;

	// Panels
	private SearchPanel							searchComponentPanel;
	private SearchResultListPanel				searchResultPanel;
	private DetailPanel							detailPanel;
	private WizardPanel							wizardPanel;
	private PageableListView<PhenoUpload>	listView;
	private ContainerForm						containerForm;

	@SpringBean(name = "phenotypicService")
	private IPhenotypicService					serviceInterface;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService					iArkCommonService;

	private transient Logger					log								= LoggerFactory.getLogger(FieldUploadContainerPanel.class);
	private boolean								phenoCollectionInContext	= false;

	public FieldUploadContainerPanel(String id) {
		super(id);

		/* Initialise the CPM */
		cpModel = new CompoundPropertyModel<UploadVO>(new UploadVO());

		initialiseMarkupContainers();

		/* Bind the CPM to the Form */
		containerForm = new ContainerForm("containerForm", cpModel);
		containerForm.add(initialiseFeedBackPanel());
		// containerForm.add(initialiseDetailPanel());
		containerForm.add(initialiseWizardPanel());
		containerForm.add(initialiseSearchResults());
		// containerForm.add(initialiseSearchPanel());

		// initialiseTestButtons();

		add(containerForm);
	}

	private WebMarkupContainer initialiseWizardPanel() {
		wizardPanel = new WizardPanel("wizardPanel", searchResultPanelContainer, feedBackPanel, wizardPanelContainer, searchPanelContainer, wizardPanelFormContainer, containerForm);
		wizardPanel.initialisePanel();
		wizardPanelContainer.setVisible(true);
		wizardPanelContainer.add(wizardPanel);
		return wizardPanelContainer;
	}

	protected WebMarkupContainer initialiseSearchResults() {
		searchResultPanel = new SearchResultListPanel("searchResults", feedBackPanel, detailPanelContainer, searchPanelContainer, containerForm, searchResultPanelContainer, detailPanel,
				viewButtonContainer, editButtonContainer, detailPanelFormContainer);

		iModel = new LoadableDetachableModel<Object>() {
			private static final long	serialVersionUID	= 1L;

			@Override
			protected Object load() {
				// Return all Uploads for the study in context
				Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);

				listView.removeAll();

				if (sessionStudyId != null) {
					PhenoUpload searchPhenoUpload = new PhenoUpload();
					Study study = iArkCommonService.getStudy(sessionStudyId);
					searchPhenoUpload.setStudy(study);

					java.util.Collection<PhenoUpload> phenoUploads = serviceInterface.searchFieldUpload(searchPhenoUpload);
					return phenoUploads;
				}
				else {
					return null;
				}
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

	protected WebMarkupContainer initialiseDetailPanel() {
		detailPanel = new DetailPanel("detailPanel", searchResultPanelContainer, feedBackPanel, detailPanelContainer, searchPanelContainer, containerForm, viewButtonContainer, editButtonContainer,
				detailPanelFormContainer);
		detailPanel.initialisePanel();
		detailPanelContainer.add(detailPanel);
		return detailPanelContainer;
	}

	protected WebMarkupContainer initialiseSearchPanel() {
		searchComponentPanel = new SearchPanel("searchPanel", feedBackPanel, searchPanelContainer, listView, searchResultPanelContainer, wizardPanelContainer, wizardPanel, containerForm,
				viewButtonContainer, editButtonContainer, wizardPanelFormContainer);
		searchComponentPanel.initialisePanel();
		searchComponentPanel.setVisible(false);
		searchPanelContainer.add(searchComponentPanel);
		return searchPanelContainer;
	}
}