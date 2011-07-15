package au.org.theark.admin.web.component.module;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.admin.service.IAdminService;
import au.org.theark.admin.web.component.module.form.ContainerForm;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.ArkBusyAjaxLink;
import au.org.theark.core.web.component.ArkDataProvider;

public class SearchResultsPanel extends Panel {
	/**
	 * 
	 */
	private static final long		serialVersionUID	= 5237384531161620862L;
	protected transient Logger		log					= LoggerFactory.getLogger(SearchResultsPanel.class);

	@SpringBean(name = au.org.theark.admin.service.Constants.ARK_ADMIN_SERVICE)
	private IAdminService<Void>	iAdminService;

	private ContainerForm			containerForm;

	private ArkCrudContainerVO		arkCrudContainerVo;

	public SearchResultsPanel(String id, ContainerForm containerForm, ArkCrudContainerVO arkCrudContainerVo) {
		super(id);
		this.containerForm = containerForm;
		this.arkCrudContainerVo = arkCrudContainerVo;
	}

	@SuppressWarnings("unchecked")
	public DataView<ArkModule> buildDataView(ArkDataProvider<ArkModule, IAdminService> dataProvider) {
		DataView<ArkModule> dataView = new DataView<ArkModule>("arkModuleList", dataProvider) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 2981419595326128410L;

			@Override
			protected void populateItem(final Item<ArkModule> item) {
				ArkModule arkModule = item.getModelObject();

				item.add(buildLink(arkModule));

				if (arkModule.getName() != null) {
					// the ID here must match the ones in mark-up
					item.add(new Label("arkModule.name", arkModule.getName()));
				}
				else {
					item.add(new Label("arkModule.name", ""));
				}

				if (arkModule.getDescription() != null) {
					// the ID here must match the ones in mark-up
					item.add(new Label("arkModule.description", arkModule.getDescription()));
				}
				else {
					item.add(new Label("arkModule.description", ""));
				}

				item.add(new AttributeModifier("class", true, new AbstractReadOnlyModel() {
					/**
					 * 
					 */
					private static final long	serialVersionUID	= 5761909841047153853L;

					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));
			}
		};
		return dataView;
	}

	@SuppressWarnings("unchecked")
	public PageableListView<ArkModule> buildPageableListView(IModel iModel, final WebMarkupContainer searchResultsContainer) {
		PageableListView<ArkModule> pageableListView = new PageableListView<ArkModule>("arkModuleList", iModel, au.org.theark.core.Constants.ROWS_PER_PAGE) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 3350183112731574263L;

			@Override
			protected void populateItem(final ListItem<ArkModule> item) {
				ArkModule arkModule = item.getModelObject();

				item.add(buildLink(arkModule));

				if (arkModule.getName() != null) {
					// the ID here must match the ones in mark-up
					item.add(new Label("arkModule.name", arkModule.getName()));
				}
				else {
					item.add(new Label("arkModule.name", ""));
				}

				if (arkModule.getDescription() != null) {
					// the ID here must match the ones in mark-up
					item.add(new Label("arkModule.description", arkModule.getDescription()));
				}
				else {
					item.add(new Label("arkModule.description", ""));
				}

				item.add(new AttributeModifier("class", true, new AbstractReadOnlyModel() {
					/**
					 * 
					 */
					private static final long	serialVersionUID	= 1938679383897533820L;

					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));

			}
		};
		return pageableListView;
	}

	@SuppressWarnings( { "unchecked", "serial" })
	private AjaxLink buildLink(final ArkModule arkModule) {
		ArkBusyAjaxLink link = new ArkBusyAjaxLink("link") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				Long id = arkModule.getId();
				ArkModule arkModule = iAdminService.getArkModule(id);
				containerForm.getModelObject().setArkModule(arkModule);

				arkCrudContainerVo.getSearchResultPanelContainer().setVisible(false);
				arkCrudContainerVo.getSearchPanelContainer().setVisible(false);
				arkCrudContainerVo.getDetailPanelContainer().setVisible(true);
				arkCrudContainerVo.getDetailPanelFormContainer().setEnabled(false);
				arkCrudContainerVo.getViewButtonContainer().setVisible(true);
				arkCrudContainerVo.getViewButtonContainer().setEnabled(true);
				arkCrudContainerVo.getEditButtonContainer().setVisible(false);

				// Refresh the markup containers
				target.addComponent(arkCrudContainerVo.getSearchResultPanelContainer());
				target.addComponent(arkCrudContainerVo.getDetailPanelContainer());
				target.addComponent(arkCrudContainerVo.getDetailPanelFormContainer());
				target.addComponent(arkCrudContainerVo.getSearchPanelContainer());
				target.addComponent(arkCrudContainerVo.getViewButtonContainer());
				target.addComponent(arkCrudContainerVo.getEditButtonContainer());

				// Refresh base container form to remove any feedBack messages
				target.addComponent(containerForm);
			}
		};

		// Add the label for the link
		Label linkLabel = new Label("arkModule.id", arkModule.getId().toString());
		link.add(linkLabel);
		return link;
	}
}