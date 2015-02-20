package au.org.theark.genomics.web.component.microservice;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.spark.entity.MicroService;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.ArkCRUDHelper;
import au.org.theark.core.web.component.link.ArkBusyAjaxLink;
import au.org.theark.genomics.model.vo.MicroServiceVo;
import au.org.theark.genomics.util.Constants;
import au.org.theark.genomics.web.component.microservice.form.ContainerForm;

public class SearchResultListPanel extends Panel {

	private static final long	serialVersionUID	= 1L;

	private ContainerForm		containerForm;
	private ArkCrudContainerVO	arkCrudContainerVO;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService		iArkCommonService;
	
	public SearchResultListPanel(String id, ArkCrudContainerVO crudContainerVO, ContainerForm studyCompContainerForm) {
		super(id);
		arkCrudContainerVO = crudContainerVO;
		containerForm = studyCompContainerForm;
	}

	public PageableListView<MicroService> buildPageableListView(IModel iModel) {

		PageableListView<MicroService> sitePageableListView = new PageableListView<MicroService>("microServiceList", iModel, iArkCommonService.getRowsPerPage()) {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void populateItem(final ListItem<MicroService> item) {

				MicroService microService = item.getModelObject();
				
				if (microService.getId() != null) {
					item.add(new Label(Constants.MICRO_SERVICE_ID, microService.getId().toString()));
				}
				else {
					item.add(new Label(Constants.MICRO_SERVICE_ID, ""));
				}
				
				item.add(buildLink(microService));
				
				if (microService.getDescription() != null) {
					item.add(new Label(Constants.MICRO_SERVICE_DESCRIPTION, microService.getDescription()));
				}
				else {
					item.add(new Label(Constants.MICRO_SERVICE_DESCRIPTION, ""));
				}			
				
				if (microService.getServiceUrl() != null) {
					item.add(new Label(Constants.MICRO_SERVICE_URL, microService.getServiceUrl()));
				}
				else {
					item.add(new Label(Constants.MICRO_SERVICE_URL, ""));
				}
				
				if (microService.getStatus()!= null) {
					item.add(new Label(Constants.MICRO_SERVICE_STATUS, microService.getStatus()));
				}
				else {
					item.add(new Label(Constants.MICRO_SERVICE_STATUS, ""));
				}
				
				item.add(new AttributeModifier("class", new AbstractReadOnlyModel<String>() {
					private static final long	serialVersionUID	= 1L;

					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));

			}
		};
		return sitePageableListView;
	}

	@SuppressWarnings( { "serial" })
	private AjaxLink buildLink(final MicroService microService) {

		ArkBusyAjaxLink link = new ArkBusyAjaxLink(Constants.MICRO_SERVICE_NAME) {

			@Override
			public void onClick(AjaxRequestTarget target) {

				MicroServiceVo microServiceVo = containerForm.getModelObject();
				microServiceVo.setMode(Constants.MODE_EDIT);
				microServiceVo.setMicroService(microService);
				ArkCRUDHelper.preProcessDetailPanelOnSearchResults(target, arkCrudContainerVO);
			}
		};
		
		Label nameLinkLabel = new Label("nameLbl", microService.getName());
		link.add(nameLinkLabel);
		return link;
	}

}
