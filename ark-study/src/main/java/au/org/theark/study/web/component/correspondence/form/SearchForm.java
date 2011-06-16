package au.org.theark.study.web.component.correspondence.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.CorrespondenceDirectionType;
import au.org.theark.core.model.study.entity.CorrespondenceModeType;
import au.org.theark.core.model.study.entity.CorrespondenceOutcomeType;
import au.org.theark.core.model.study.entity.CorrespondenceStatusType;
import au.org.theark.core.model.study.entity.Correspondences;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.CorrespondenceVO;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.core.web.form.AbstractSearchForm;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.address.DetailPanel;

public class SearchForm extends AbstractSearchForm<CorrespondenceVO> {

	@SpringBean(name = Constants.STUDY_SERVICE)
	private IStudyService studyService;

	@SpringBean( name =  au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;
	
	private DetailPanel detailPanel;
	private PageableListView<Correspondences> pageableListView;
	
	private DropDownChoice<CorrespondenceStatusType> statusTypeChoice;
	private DropDownChoice<ArkUser> operatorChoice;
	private DateTextField dateFld;
	private TextField<String> timeTxtFld;
	private TextArea<String> reasonTxtArea;
	private DropDownChoice<CorrespondenceModeType> modeTypeChoice;
	private DropDownChoice<CorrespondenceDirectionType> directionTypeChoice;
	private DropDownChoice<CorrespondenceOutcomeType> outcomeTypeChoice;
	private TextArea<String> detailsTxtArea;
	
	
	public SearchForm(String id,
			CompoundPropertyModel<CorrespondenceVO> model,
			PageableListView<Correspondences> listView,
			FeedbackPanel feedbackPanel,
			WebMarkupContainer listContainer,
			WebMarkupContainer searchMarkupContainer,
			WebMarkupContainer detailContainer,
			WebMarkupContainer detailPanelFormContainer,
			WebMarkupContainer viewButtonContainer,
			WebMarkupContainer editButtonContainer)	{
		
		super(id, model, detailContainer, detailPanelFormContainer, viewButtonContainer, editButtonContainer, searchMarkupContainer, listContainer, feedbackPanel);
		this.pageableListView = listView;
		initialiseSearchForm();
		Long sessionPersonId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
		disableSearchForm(sessionPersonId, "There is no subject or contact in context. Please select a subject or contact.");
		
	}


	private void initialiseSearchForm() {

		initialiseStatusTypeDropDown();
		initialiseOperatorDropDown();
		dateFld = new DateTextField("correspondence.date", au.org.theark.core.Constants.DD_MM_YYYY);
		ArkDatePicker datePicker = new ArkDatePicker();
		datePicker.bind(dateFld);
		dateFld.add(datePicker);
		// create new DateTextField and assign date format
		timeTxtFld = new TextField<String>("correspondence.time");
		reasonTxtArea = new TextArea<String>("correspondence.reason");
		initialiseModeTypeDropDown();
		initialiseDirectionTypeDropDown();
		initialiseOutcomeTypeDropDown();
		detailsTxtArea = new TextArea<String>("correspondence.details");
		
		addSearchComponentsToForm();
	}
	
	
	private void initialiseOperatorDropDown() {

		Long sessionStudyId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(sessionStudyId);
		
		Collection<ArkUser> coll = studyService.lookupArkUser(study);
		List<ArkUser> list = new ArrayList<ArkUser>(coll);
		
		ChoiceRenderer<ArkUser> defaultRenderer = new ChoiceRenderer<ArkUser>("ldapUserName", "id");
		operatorChoice = new DropDownChoice<ArkUser>("correspondence.operator", list, defaultRenderer); 
	}

	private void initialiseStatusTypeDropDown() {
		
		List<CorrespondenceStatusType> list = studyService.getCorrespondenceStatusTypes();
		ChoiceRenderer<CorrespondenceStatusType> defaultRenderer = new ChoiceRenderer<CorrespondenceStatusType>("name", "id");
		statusTypeChoice = new DropDownChoice<CorrespondenceStatusType>("correspondence.correspondenceStatusType", list, defaultRenderer); 
	}

	
	private void initialiseModeTypeDropDown() {
		
		List<CorrespondenceModeType> list = studyService.getCorrespondenceModeTypes();
		ChoiceRenderer<CorrespondenceModeType> defaultRenderer = new ChoiceRenderer<CorrespondenceModeType>("name", "id");
		modeTypeChoice = new DropDownChoice<CorrespondenceModeType>("correspondence.correspondenceModeType", list, defaultRenderer);
	}
	
	
	private void initialiseDirectionTypeDropDown() {
		
		List<CorrespondenceDirectionType> list = studyService.getCorrespondenceDirectionTypes();
		ChoiceRenderer<CorrespondenceDirectionType> defaultRenderer = new ChoiceRenderer<CorrespondenceDirectionType>("name", "id");
		directionTypeChoice = new DropDownChoice<CorrespondenceDirectionType>("correspondence.correspondenceDirectionType", list, defaultRenderer);
	}
	
	
	private void initialiseOutcomeTypeDropDown() {
		
		List<CorrespondenceOutcomeType> list = studyService.getCorrespondenceOutcomeTypes();
		ChoiceRenderer<CorrespondenceOutcomeType> defaultRenderer = new ChoiceRenderer<CorrespondenceOutcomeType>("name", "id");
		outcomeTypeChoice = new DropDownChoice<CorrespondenceOutcomeType>("correspondence.correspondenceOutcomeType", list, defaultRenderer);
	}
	
	
	private void addSearchComponentsToForm() {

		add(statusTypeChoice);
		add(operatorChoice);
		add(dateFld);
		add(timeTxtFld);
		add(reasonTxtArea);
		add(modeTypeChoice);
		add(directionTypeChoice);
		add(outcomeTypeChoice);
		add(detailsTxtArea);	
	}
	
	
	@Override
	protected boolean isSecure(String actionType) {
		return true;
	}

	
	@Override
	protected void onNew(AjaxRequestTarget target) {
		
		setModelObject(new CorrespondenceVO());
		preProcessDetailPanel(target);
	}

	
	@Override
	protected void onSearch(AjaxRequestTarget target) {
		
		target.addComponent(feedbackPanel);
		Long sessionStudyId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Long sessionPersonId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
		
		try {
			Correspondences correspondence = getModelObject().getCorrespondence();
			correspondence.setPerson(studyService.getPerson(sessionPersonId));
			Collection<Correspondences> correspondenceList = studyService.getPersonCorrespondenceList(sessionPersonId, correspondence);
			if(correspondenceList != null && correspondenceList.size() == 0) {
				this.info("Fields with the specified criteria do not exist in the system.");
				target.addComponent(feedbackPanel);
			}
			
			getModelObject().setCorrespondenceList(correspondenceList);
			pageableListView.removeAll();
			listContainer.setVisible(true);
			target.addComponent(listContainer);
			
		}catch(EntityNotFoundException ex) {
			this.warn("There are no correspondences available for the specified criteria.");
			target.addComponent(feedbackPanel);
		}catch(ArkSystemException ex) {
			this.error("The Ark application has encountered a system error.");
			target.addComponent(feedbackPanel);
		}
	}

}
