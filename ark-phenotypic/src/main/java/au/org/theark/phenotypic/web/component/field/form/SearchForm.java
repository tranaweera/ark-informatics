package au.org.theark.phenotypic.web.component.field.form;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.security.RoleConstants;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.form.AbstractSearchForm;
import au.org.theark.phenotypic.model.entity.Field;
import au.org.theark.phenotypic.model.entity.FieldType;
import au.org.theark.phenotypic.model.vo.FieldVO;
import au.org.theark.phenotypic.service.Constants;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.web.component.field.Detail;

/**
 * @author cellis
 * 
 */
@SuppressWarnings({"serial", "unchecked"})
public class SearchForm extends AbstractSearchForm<FieldVO>
{
	@SpringBean(name = Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService phenotypicService;
	
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService			iArkCommonService;
	
	private PageableListView<Field>	listView;
	private CompoundPropertyModel<FieldVO>	cpmModel;
	private TextField<String>					fieldIdTxtFld;
	private TextField<String>					fieldNameTxtFld;
	private DropDownChoice<FieldType>		fieldTypeDdc;
	private TextArea<String>					fieldDescriptionTxtAreaFld;
	private TextField<String>					fieldUnitsTxtFld;
	private TextField<String>					fieldMinValueTxtFld;
	private TextField<String>					fieldMaxValueTxtFld;
	private Detail detailPanel;
	
	/**
	 * @param id
	 */
	public SearchForm(	String id, 
						CompoundPropertyModel<FieldVO> model,
						PageableListView<Field> listView, 
						FeedbackPanel feedBackPanel,
						Detail detailPanel,
						WebMarkupContainer listContainer,
						WebMarkupContainer searchMarkupContainer,
						WebMarkupContainer detailContainer,
						WebMarkupContainer detailPanelFormContainer,
						WebMarkupContainer viewButtonContainer,
						WebMarkupContainer editButtonContainer)
	{
		
		super(	id,
				model,
				detailContainer,
				detailPanelFormContainer,
				viewButtonContainer,
				editButtonContainer,
				searchMarkupContainer,
				listContainer,
				feedBackPanel);
		
		this.cpmModel = model;
		this.listView = listView;
		this.detailPanel = detailPanel;
		initialiseFieldForm();
	}

	 private void initFieldTypeDdc()
	 {
		 java.util.Collection<FieldType> fieldTypeCollection = phenotypicService.getFieldTypes();
		 CompoundPropertyModel<FieldVO> fieldCpm = cpmModel;
		 PropertyModel<Field> fieldPm = new PropertyModel<Field>(fieldCpm, au.org.theark.phenotypic.web.Constants.FIELD);
		 PropertyModel<FieldType> fieldTypePm = new PropertyModel<FieldType>(fieldPm, au.org.theark.phenotypic.web.Constants.FIELD_TYPE);
		 ChoiceRenderer fieldTypeRenderer = new ChoiceRenderer(
				 au.org.theark.phenotypic.web.Constants.FIELD_TYPE_NAME, 
				 au.org.theark.phenotypic.web.Constants.FIELD_TYPE_ID);
		 fieldTypeDdc = new DropDownChoice<FieldType>(au.org.theark.phenotypic.web.Constants.FIELD_TYPE, fieldTypePm, (List) fieldTypeCollection, fieldTypeRenderer);
	 }

	public void initialiseFieldForm()
	{
		fieldIdTxtFld = new TextField<String>(au.org.theark.phenotypic.web.Constants.FIELDVO_FIELD_ID);
		fieldNameTxtFld = new TextField<String>(au.org.theark.phenotypic.web.Constants.FIELDVO_FIELD_NAME);
		fieldDescriptionTxtAreaFld = new TextArea<String>(au.org.theark.phenotypic.web.Constants.FIELDVO_FIELD_DESCRIPTION);
		fieldUnitsTxtFld = new TextField<String>(au.org.theark.phenotypic.web.Constants.FIELDVO_FIELD_UNITS);
		fieldMinValueTxtFld = new TextField<String>(au.org.theark.phenotypic.web.Constants.FIELDVO_FIELD_MIN_VALUE);
		fieldMaxValueTxtFld = new TextField<String>(au.org.theark.phenotypic.web.Constants.FIELDVO_FIELD_MAX_VALUE);
		initFieldTypeDdc();
		addFieldComponents();
	}

	private void addFieldComponents()
	{
		add(fieldIdTxtFld);
		add(fieldNameTxtFld);
		add(fieldTypeDdc);
		add(fieldDescriptionTxtAreaFld);
		add(fieldUnitsTxtFld);
		add(fieldMinValueTxtFld);
		add(fieldMaxValueTxtFld);
	}

	@Override
	protected void onNew(AjaxRequestTarget target)
	{
		FieldVO fieldVo = new FieldVO();
		fieldVo.setMode(au.org.theark.core.Constants.MODE_NEW);
		// Set study for the new field
		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study	study = iArkCommonService.getStudy(studyId);
		fieldVo.getField().setStudy(study);
		setModelObject(fieldVo);
		preProcessDetailPanel(target);
		//// Hide Delete button on New
		detailPanel.getDetailForm().getDeleteButton().setVisible(false);
	}

	@Override
	protected void onSearch(AjaxRequestTarget target)
	{
		
		target.addComponent(feedbackPanel);
		final Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		// Get a list of all Fields for the Study in context
		Study study = iArkCommonService.getStudy(sessionStudyId);
		Field searchField = getModelObject().getField();
		searchField.setStudy(study);

		java.util.Collection<Field> fieldCollection = phenotypicService.searchField(searchField);

		if (fieldCollection != null && fieldCollection.size() == 0)
		{
			this.info("Fields with the specified criteria does not exist in the system.");
			target.addComponent(feedbackPanel);
		}
		getModelObject().setFieldCollection(fieldCollection);
		listView.removeAll();
		listContainer.setVisible(true);// Make the WebMarkupContainer that houses the search results visible
		target.addComponent(listContainer);// For ajax this is required so
		
	}
	
	protected boolean isSecure() {
		SecurityManager securityManager =  ThreadContext.getSecurityManager();
		Subject currentUser = SecurityUtils.getSubject();		
		boolean flag = false;
		if(		securityManager.hasRole(currentUser.getPrincipals(), RoleConstants.ARK_SUPER_ADMIN) ||
				securityManager.hasRole(currentUser.getPrincipals(), RoleConstants.STUDY_ADMIN)){
			flag = true;
		}
		//if it is a Super or Study admin then make the new available
		return flag;
	}
}