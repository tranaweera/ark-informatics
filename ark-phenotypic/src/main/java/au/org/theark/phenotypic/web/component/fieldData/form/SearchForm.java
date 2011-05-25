package au.org.theark.phenotypic.web.component.fieldData.form;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.security.RoleConstants;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.form.AbstractSearchForm;
import au.org.theark.phenotypic.model.entity.Field;
import au.org.theark.phenotypic.model.entity.FieldData;
import au.org.theark.phenotypic.model.entity.FieldType;
import au.org.theark.phenotypic.model.entity.PhenoCollection;
import au.org.theark.phenotypic.model.entity.Status;
import au.org.theark.phenotypic.model.vo.PhenoCollectionVO;
import au.org.theark.phenotypic.service.Constants;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.web.component.fieldData.DetailPanel;

/**
 * @author cellis
 * 
 */
@SuppressWarnings( { "serial", "unused" })
public class SearchForm extends AbstractSearchForm<PhenoCollectionVO>
{
	@SpringBean(name = Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService									iPhenotypicService;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService									iArkCommonService;

	private PageableListView<FieldData>						listView;
	private CompoundPropertyModel<PhenoCollectionVO>	cpmModel;
	private TextField<String>									fieldDataIdTxtFld;
	private DropDownChoice<PhenoCollection>				fieldDataCollectionDdc;
	private DropDownChoice<PhenoCollection>				fieldDataFieldDdc;
	private TextField<String>									fieldDataSubjectUIDTxtFld;
	
	private DetailPanel											detailPanel;
	private Long sessionStudyId;

	/**
	 * @param id
	 */
	public SearchForm(String id, CompoundPropertyModel<PhenoCollectionVO> model, PageableListView<FieldData> listView, FeedbackPanel feedBackPanel, DetailPanel detailPanel,
			WebMarkupContainer listContainer, WebMarkupContainer searchMarkupContainer, WebMarkupContainer detailContainer, WebMarkupContainer detailPanelFormContainer,
			WebMarkupContainer viewButtonContainer, WebMarkupContainer editButtonContainer)
	{

		super(id, model, detailContainer, detailPanelFormContainer, viewButtonContainer, editButtonContainer, searchMarkupContainer, listContainer, feedBackPanel);

		this.cpmModel = model;
		this.listView = listView;
		this.detailPanel = detailPanel;
		initialiseFieldForm();

		sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		if (sessionStudyId != null && sessionStudyId > 0)
		{
			getModelObject().setStudy(iArkCommonService.getStudy(sessionStudyId));
		}
		disableSearchForm(sessionStudyId, "There is no study in context. Please select a study");
	}

	@SuppressWarnings("unchecked")
	private void initFieldDataCollectionDdc()
	{
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = new Study();
		java.util.Collection<PhenoCollection> fieldDataCollection = null;
		PhenoCollection phenotypicCollection = new PhenoCollection();
		
		if (sessionStudyId != null && sessionStudyId > 0)
		{
			study = iArkCommonService.getStudy(sessionStudyId);
			fieldDataCollection = iPhenotypicService.getPhenoCollectionByStudy(study);
		}
		else
		{
			fieldDataCollection = iPhenotypicService.searchPhenotypicCollection(phenotypicCollection);
		}
		
		ChoiceRenderer fieldDataCollRenderer = new ChoiceRenderer(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTION_NAME, au.org.theark.phenotypic.web.Constants.PHENO_COLLECTION_ID);
		fieldDataCollectionDdc = new DropDownChoice<PhenoCollection>(au.org.theark.phenotypic.web.Constants.FIELD_DATAVO_FIELD_DATA_COLLECTION, (List) fieldDataCollection, fieldDataCollRenderer);
	}
	
	@SuppressWarnings("unchecked")
	private void initFieldDataFieldDdc()
	{
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = new Study();
		Field field = new Field();
		
		if (sessionStudyId != null && sessionStudyId > 0)
		{
			study = iArkCommonService.getStudy(sessionStudyId);
			field.setStudy(study);
		}
		
		java.util.Collection<Field> fieldDataField = iPhenotypicService.searchField(field);
		ChoiceRenderer fieldDataFieldRenderer = new ChoiceRenderer(au.org.theark.phenotypic.web.Constants.FIELD_NAME, au.org.theark.phenotypic.web.Constants.FIELD_ID);
		fieldDataFieldDdc = new DropDownChoice<PhenoCollection>(au.org.theark.phenotypic.web.Constants.FIELD_DATAVO_FIELD_DATA_FIELD, (List) fieldDataField, fieldDataFieldRenderer);
	}

	public void initialiseFieldForm()
	{
		fieldDataIdTxtFld = new TextField<String>(au.org.theark.phenotypic.web.Constants.FIELD_DATAVO_FIELD_DATA_ID);
		fieldDataSubjectUIDTxtFld = new TextField<String>(au.org.theark.phenotypic.web.Constants.FIELD_DATAVO_FIELD_DATA_SUBJECTUID);
		initFieldDataCollectionDdc();
		initFieldDataFieldDdc();
		addFieldComponents();
	}

	private void addFieldComponents()
	{
		add(fieldDataIdTxtFld);
		add(fieldDataCollectionDdc);
		add(fieldDataFieldDdc);
		add(fieldDataSubjectUIDTxtFld);
	}

	@Override
	protected void onSearch(AjaxRequestTarget target)
	{
		FieldData searchFieldData = getModelObject().getFieldData();
		/*
		target.addComponent(feedbackPanel);
		FieldData searchFieldData = getModelObject().getFieldData();

		java.util.Collection<FieldData> fieldDataCollection = phenotypicService.searchFieldData(searchFieldData);

		if (fieldDataCollection != null && fieldDataCollection.size() == 0)
		{
			this.info("Field data with the specified criteria does not exist in the system.");
			target.addComponent(feedbackPanel);
		}
		getModelObject().setFieldDataCollection(fieldDataCollection);
		listView.removeAll();
		listContainer.setVisible(true);// Make the WebMarkupContainer that houses the search results visible
		target.addComponent(listContainer);
		*/
		
		target.addComponent(feedbackPanel);
		Long sessionStudyId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		getModelObject().setStudy(iArkCommonService.getStudy(sessionStudyId)); 
		
		int count = iPhenotypicService.getStudyFieldDataCount(getModelObject());
		if(count == 0){
			this.info("There are no field data records with the specified criteria.");
			target.addComponent(feedbackPanel);
		}
		listContainer.setVisible(true);//Make the WebMarkupContainer that houses the search results visible
		target.addComponent(listContainer);//For ajax this is required so
	}

	// Reset button implemented in AbstractSearcForm

	@Override
	protected void onNew(AjaxRequestTarget target)
	{
		//NB: Should not be possible to get here (not allowed to create a new FieldData via GUI)
		// Due to ARK-108 :: No longer reset the VO onNew(..)
		PhenoCollectionVO phenoCollectionVo = getModelObject();
		phenoCollectionVo.setMode(au.org.theark.core.Constants.MODE_NEW);
		phenoCollectionVo.getFieldData().setId(null);	//must ensure Id is blank onNew
		
		// Set study for the new field of the fieldData
		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(studyId);
		Field field = new Field();
		field.setStudy(study);
		phenoCollectionVo.setField(field);
		
		setModelObject(phenoCollectionVo);
		preProcessDetailPanel(target);
		// Hide Delete button on New
		detailPanel.getDetailForm().getDeleteButton().setVisible(false);
	}
	
	protected boolean isSecure(String actionType)
	{
		boolean flag = false;
		if (actionType.equalsIgnoreCase(au.org.theark.core.Constants.NEW))
		{
			flag = false;
		}
		else{
			flag = true;
		}
		return flag;
	}
}