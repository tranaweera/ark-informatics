package au.org.theark.phenotypic.web.component.customfieldgroup.form;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.Constants;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.CustomFieldGroupVO;
import au.org.theark.core.web.form.AbstractSearchForm;
import au.org.theark.phenotypic.web.component.customfieldgroup.CustomFieldDisplayListPanel;
import au.org.theark.phenotypic.web.component.customfieldgroup.CustomFieldGroupDetailPanel;

/**
 * @author nivedann
 *
 */
public class SearchForm extends AbstractSearchForm<CustomFieldGroupVO>{

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;
	
	private ArkCrudContainerVO	arkCrudContainerVO;
	private TextField<String> groupNameTxtFld;
	private CheckBox publishedStatusCb;	
	
	
	/**
	 * @param id
	 * @param cpmModel
	 */
	public SearchForm(String id,CompoundPropertyModel<CustomFieldGroupVO> cpmModel, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVO) {
		super(id, cpmModel,feedBackPanel,arkCrudContainerVO);
		this.feedbackPanel = feedBackPanel;
		this.arkCrudContainerVO = arkCrudContainerVO;
		initialiseSearchForm();
		addSearchComponentsToForm();
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractSearchForm#onNew(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onNew(AjaxRequestTarget target) {
		
		CustomField customFieldCriteria = new CustomField();
		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(Constants.STUDY_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(studyId);
		
		ArkFunction arkFunction  =iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_DATA_DICTIONARY);
		customFieldCriteria.setStudy(study);
		customFieldCriteria.setArkFunction(arkFunction);
		
		List<CustomField> availableListOfFields = iArkCommonService.getCustomFieldList(customFieldCriteria);
		
		
		//Show Detail Form with Custom Field Group  Name and Status and a Palette with Custom Fields for the study and function
		//Save creates Custom Field Group and links the custom Fields to the Custom Field Display.
		CompoundPropertyModel<CustomFieldGroupVO> newModel = new CompoundPropertyModel<CustomFieldGroupVO>(new CustomFieldGroupVO());
		
		//Copy over any details user may have typed in the search form and carry it to the Detail Form
		newModel.getObject().getCustomFieldGroup().setName(getModelObject().getCustomFieldGroup().getName());
		newModel.getObject().getCustomFieldGroup().setDescription(getModelObject().getCustomFieldGroup().getDescription());
		newModel.getObject().setAvailableCustomFields(availableListOfFields);
		CustomFieldGroupDetailPanel detailPanel = new CustomFieldGroupDetailPanel("detailsPanel", feedbackPanel, arkCrudContainerVO,newModel,false);
		arkCrudContainerVO.getDetailPanelContainer().addOrReplace(detailPanel);
		preProcessDetailPanel(target);
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractSearchForm#onSearch(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onSearch(AjaxRequestTarget target) {
		target.add(feedbackPanel);
		final Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(sessionStudyId);
		getModelObject().getCustomFieldGroup().setStudy(study);
		
		int count = iArkCommonService.getCustomFieldGroupCount(getModelObject().getCustomFieldGroup());

		if (count <= 0) {
			this.info("No records match the specified criteria.");
			target.add(feedbackPanel);
		}
		arkCrudContainerVO.getSearchResultPanelContainer().setVisible(true);// Make the WebMarkupContainer that houses the search results visible
		target.add(arkCrudContainerVO.getSearchResultPanelContainer());
	}
	
	protected void initialiseSearchForm(){
		groupNameTxtFld = new TextField<String>("customFieldGroup.name");
		publishedStatusCb = new CheckBox("customFieldGroup.published");
		
	}
	
	protected void addSearchComponentsToForm() {
		add(groupNameTxtFld);
		add(publishedStatusCb);
	}

}
