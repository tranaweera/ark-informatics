package au.org.theark.admin.web.component.rolePolicy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.admin.model.vo.AdminVO;
import au.org.theark.admin.service.IAdminService;
import au.org.theark.admin.web.component.rolePolicy.form.ContainerForm;
import au.org.theark.core.model.study.entity.ArkRolePolicyTemplate;
import au.org.theark.core.security.ArkPermissionHelper;
import au.org.theark.core.web.component.AbstractContainerPanel;
import au.org.theark.core.web.component.ArkDataProvider;

/**
 * @author cellis
 * 
 */
public class RolePolicyContainerPanel extends AbstractContainerPanel<AdminVO> {
	/**
	 * 
	 */
	private static final long								serialVersionUID	= 442185554812824590L;
	private ContainerForm									containerForm;
	private SearchPanel										searchPanel;
	private DetailPanel										detailPanel;
	private SearchResultsPanel								searchResultsPanel;
	private DataView<ArkRolePolicyTemplate>									dataView;
	private ArkDataProvider<ArkRolePolicyTemplate, IAdminService>	dataProvider;

	@SpringBean(name = au.org.theark.admin.service.Constants.ARK_ADMIN_SERVICE)
	private IAdminService<Void>							iAdminService;

	/**
	 * @param id
	 */
	public RolePolicyContainerPanel(String id) {
		super(id, true);
		/* Initialise the CPM */
		cpModel = new CompoundPropertyModel<AdminVO>(new AdminVO());

		initCrudContainerVO();

		/* Bind the CPM to the Form */
		containerForm = new ContainerForm("containerForm", cpModel);
		containerForm.add(initialiseFeedBackPanel());
		containerForm.add(initialiseDetailPanel());
		containerForm.add(initialiseSearchPanel());
		containerForm.add(initialiseSearchResults());

		add(containerForm);
	}

	@Override
	protected WebMarkupContainer initialiseDetailPanel() {
		detailPanel = new DetailPanel("detailPanel", feedBackPanel, containerForm, arkCrudContainerVO);
		detailPanel.initialisePanel();
		arkCrudContainerVO.getDetailPanelContainer().add(detailPanel);
		return arkCrudContainerVO.getDetailPanelContainer();
	}

	@Override
	protected WebMarkupContainer initialiseSearchPanel() {
		searchPanel = new SearchPanel("searchPanel", feedBackPanel, containerForm, cpModel, arkCrudContainerVO);
		searchPanel.initialisePanel();
		arkCrudContainerVO.getSearchPanelContainer().add(searchPanel);
		return arkCrudContainerVO.getSearchPanelContainer();
	}

	@Override
	protected WebMarkupContainer initialiseSearchResults() {
		searchResultsPanel = new SearchResultsPanel("searchResultsPanel", containerForm, arkCrudContainerVO);
		initialiseDataView();
		dataView = searchResultsPanel.buildDataView(dataProvider);
		dataView.setItemsPerPage(au.org.theark.core.Constants.ROWS_PER_PAGE);
		PagingNavigator pageNavigator = new PagingNavigator("navigator", dataView);
		searchResultsPanel.add(pageNavigator);
		searchResultsPanel.add(dataView);
		arkCrudContainerVO.getSearchResultPanelContainer().add(searchResultsPanel);
		return arkCrudContainerVO.getSearchResultPanelContainer();
	}

	@SuppressWarnings( { "unchecked", "serial" })
	private void initialiseDataView() {
		// Data provider to paginate resultList
		dataProvider = new ArkDataProvider<ArkRolePolicyTemplate, IAdminService>(iAdminService) {
			public int size() {
				return service.getArkRolePolicyTemplateCount(model.getObject());
			}

			public Iterator<ArkRolePolicyTemplate> iterator(int first, int count) {
				List<ArkRolePolicyTemplate> listCollection = new ArrayList<ArkRolePolicyTemplate>();
				if (ArkPermissionHelper.isActionPermitted(au.org.theark.core.Constants.SEARCH)) {
					listCollection = service.searchPageableArkRolePolicyTemplates(model.getObject(), first, count);
				}
				return listCollection.iterator();
			}
		};
		// Set the criteria into the data provider's model
		dataProvider.setModel(new LoadableDetachableModel<ArkRolePolicyTemplate>() {
			@Override
			protected ArkRolePolicyTemplate load() {
				return cpModel.getObject().getArkRolePolicyTemplate();
			}
		});
	}
}