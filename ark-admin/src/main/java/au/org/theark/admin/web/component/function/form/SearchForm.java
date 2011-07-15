package au.org.theark.admin.web.component.function.form;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.admin.model.vo.AdminVO;
import au.org.theark.admin.service.IAdminService;
import au.org.theark.core.model.study.entity.ArkFunctionType;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.form.AbstractSearchForm;

public class SearchForm extends AbstractSearchForm<AdminVO> {
	/**
	 * 
	 */
	private static final long						serialVersionUID	= -204010204180506704L;

	@SpringBean(name = au.org.theark.admin.service.Constants.ARK_ADMIN_SERVICE)
	private IAdminService<Void>					iAdminService;

	private CompoundPropertyModel<AdminVO>		cpmModel;
	private ArkCrudContainerVO						arkCrudContainerVo;
	private ContainerForm							containerForm;
	private FeedbackPanel							feedbackPanel;
	private TextField<String>						idTxtFld;
	private TextField<String>						nameTxtFld;
	private DropDownChoice<ArkFunctionType>	arkFunctionTypeDropDown;

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param model
	 * @param ArkCrudContainerVO
	 * @param containerForm
	 */
	public SearchForm(String id, CompoundPropertyModel<AdminVO> cpmModel, ArkCrudContainerVO arkCrudContainerVo, FeedbackPanel feedbackPanel, ContainerForm containerForm) {
		super(id, cpmModel, feedbackPanel, arkCrudContainerVo);

		this.containerForm = containerForm;
		this.arkCrudContainerVo = arkCrudContainerVo;
		this.feedbackPanel = feedbackPanel;
		setMultiPart(true);

		this.setCpmModel(cpmModel);

		initialiseSearchForm();
		addSearchComponentsToForm();
	}

	protected void initialiseSearchForm() {
		idTxtFld = new TextField<String>("arkFunction.id");
		nameTxtFld = new TextField<String>("arkFunction.name");

		// Type selection
		initArkFunctionTypeDropDown();
	}

	@SuppressWarnings("unchecked")
	private void initArkFunctionTypeDropDown() {
		List<ArkFunctionType> arkFunctionTypeList = iAdminService.getArkFunctionTypeList();
		ChoiceRenderer<ArkFunctionType> defaultChoiceRenderer = new ChoiceRenderer<ArkFunctionType>("name", "id");
		arkFunctionTypeDropDown = new DropDownChoice("arkFunction.arkFunctionType", arkFunctionTypeList, defaultChoiceRenderer);
		arkFunctionTypeDropDown.add(new AjaxFormComponentUpdatingBehavior("onChange") {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 5591846326218931210L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {

			}
		});
	}

	protected void onSearch(AjaxRequestTarget target) {
		target.addComponent(feedbackPanel);
		int count = iAdminService.getArkFunctionCount(containerForm.getModelObject().getArkFunction());
		if (count == 0) {
			this.info("There are no records that matched your query. Please modify your filter");
			target.addComponent(feedbackPanel);
		}

		arkCrudContainerVo.getSearchResultPanelContainer().setVisible(true);
		target.addComponent(arkCrudContainerVo.getSearchResultPanelContainer());
	}

	private void addSearchComponentsToForm() {
		add(idTxtFld);
		add(nameTxtFld);
		add(arkFunctionTypeDropDown);
	}

	protected void onNew(AjaxRequestTarget target) {
		target.addComponent(feedbackPanel);
		containerForm.setModelObject(new AdminVO());
		arkCrudContainerVo.getSearchResultPanelContainer().setVisible(false);
		arkCrudContainerVo.getSearchPanelContainer().setVisible(false);
		arkCrudContainerVo.getDetailPanelContainer().setVisible(true);
		arkCrudContainerVo.getDetailPanelFormContainer().setEnabled(true);
		arkCrudContainerVo.getViewButtonContainer().setVisible(false);
		arkCrudContainerVo.getEditButtonContainer().setVisible(true);

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

	/**
	 * @param cpmModel
	 *           the cpmModel to set
	 */
	public void setCpmModel(CompoundPropertyModel<AdminVO> cpmModel) {
		this.cpmModel = cpmModel;
	}

	/**
	 * @return the cpmModel
	 */
	public CompoundPropertyModel<AdminVO> getCpmModel() {
		return cpmModel;
	}
}