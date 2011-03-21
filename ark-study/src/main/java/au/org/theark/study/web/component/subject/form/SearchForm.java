/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.study.web.component.subject.form;

import java.util.Collection;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.study.entity.Country;
import au.org.theark.core.model.study.entity.CountryState;
import au.org.theark.core.model.study.entity.GenderType;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.SubjectStatus;
import au.org.theark.core.model.study.entity.VitalStatus;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.SubjectVO;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.core.web.form.AbstractSearchForm;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;

/**
 * @author nivedann
 *
 */
public class SearchForm extends AbstractSearchForm<SubjectVO>{


	@SpringBean( name = Constants.STUDY_SERVICE)
	private IStudyService studyService;
	
	@SpringBean( name =  au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;

	private TextField<String> subjectUIDTxtFld;
	private TextField<String> firstNameTxtFld;
	private TextField<String> middleNameTxtFld;
	private TextField<String> lastNameTxtFld;
	private DropDownChoice<VitalStatus> vitalStatusDdc;
	private DropDownChoice<GenderType> genderTypeDdc;
	private DropDownChoice<SubjectStatus> subjectStatusDdc;
	private DateTextField dateOfBirthTxtFld;
	private PageableListView<SubjectVO> listView;
	private CompoundPropertyModel<SubjectVO> cpmModel;
	
	/**
	 * @param id
	 * @param cpmModel
	 */
	public SearchForm(	String id, 
						CompoundPropertyModel<SubjectVO> cpmModel,
						PageableListView<SubjectVO> listView, 
						FeedbackPanel feedBackPanel,
						WebMarkupContainer listContainer,
						WebMarkupContainer searchMarkupContainer,
						WebMarkupContainer detailsContainer,
						WebMarkupContainer detailPanelFormContainer,
						WebMarkupContainer viewButtonContainer,
						WebMarkupContainer editButtonContainer	) {
		
		//super(id, cpmModel);
		super	(id,
				cpmModel,
				detailsContainer,
				detailPanelFormContainer,
				viewButtonContainer,
				editButtonContainer,
				searchMarkupContainer,
				listContainer,
				feedBackPanel);
		
		this.cpmModel = cpmModel;
		this.listView = listView;
		initialiseSearchForm();
		addSearchComponentsToForm();
		Long sessionStudyId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		disableSearchButtons(sessionStudyId, "There is no study in context. Please select a study");
	}
	
	protected void addSearchComponentsToForm(){
		add(subjectUIDTxtFld);
		add(firstNameTxtFld);
		add(middleNameTxtFld);
		add(lastNameTxtFld);
		add(vitalStatusDdc);
		add(subjectStatusDdc);
		add(genderTypeDdc);
		add(dateOfBirthTxtFld);
	}
	
	protected void initialiseSearchForm(){
		subjectUIDTxtFld = new TextField<String>(Constants.SUBJECT_UID);
		firstNameTxtFld = new TextField<String>(Constants.PERSON_FIRST_NAME);
		middleNameTxtFld = new TextField<String>(Constants.PERSON_MIDDLE_NAME);
		lastNameTxtFld = new TextField<String>(Constants.PERSON_LAST_NAME);
		initVitalStatusDdc();
		initSubjectStatusDdc();
		initGenderTypeDdc();
		
		dateOfBirthTxtFld = new DateTextField(Constants.PERSON_DOB,au.org.theark.core.Constants.DD_MM_YYYY);
		ArkDatePicker dobDatePicker = new ArkDatePicker();
		dobDatePicker.bind(dateOfBirthTxtFld);
		dateOfBirthTxtFld.add(dobDatePicker);
	}

	private void initVitalStatusDdc(){
		CompoundPropertyModel<SubjectVO> subjectCpm = cpmModel;
		PropertyModel<LinkSubjectStudy> linkSubjectStudyPm = new PropertyModel<LinkSubjectStudy>(subjectCpm,"subjectStudy");
		PropertyModel<Person> personPm = new PropertyModel<Person>(linkSubjectStudyPm,"person");
		PropertyModel<VitalStatus> vitalStatusPm = new PropertyModel<VitalStatus>(personPm,Constants.VITAL_STATUS);
		Collection<VitalStatus> vitalStatusList = iArkCommonService.getVitalStatus();
		ChoiceRenderer vitalStatusRenderer = new ChoiceRenderer(Constants.NAME, Constants.ID);
		vitalStatusDdc = new DropDownChoice<VitalStatus>(Constants.VITAL_STATUS,vitalStatusPm,(List)vitalStatusList,vitalStatusRenderer);
	}
	
	private void initSubjectStatusDdc(){
		CompoundPropertyModel<SubjectVO> subjectCpm = cpmModel;
		PropertyModel<LinkSubjectStudy> linkSubjectStudyPm = new PropertyModel<LinkSubjectStudy>(subjectCpm,"subjectStudy");
		PropertyModel<SubjectStatus> subjectStatusPm = new PropertyModel<SubjectStatus>(linkSubjectStudyPm,"subjectStatus");
		Collection<SubjectStatus> subjectStatusList = iArkCommonService.getSubjectStatus();
		ChoiceRenderer subjectStatusRenderer = new ChoiceRenderer(Constants.NAME,Constants.SUBJECT_STATUS_ID);
		subjectStatusDdc = new DropDownChoice<SubjectStatus>(Constants.SUBJECT_STATUS,subjectStatusPm,(List)subjectStatusList,subjectStatusRenderer);
	}
	
	private void initGenderTypeDdc(){
		CompoundPropertyModel<SubjectVO> subjectCpm = cpmModel;
		PropertyModel<LinkSubjectStudy> linkSubjectStudyPm = new PropertyModel<LinkSubjectStudy>(subjectCpm,"subjectStudy");
		PropertyModel<Person> personPm = new PropertyModel<Person>(linkSubjectStudyPm,Constants.PERSON);
		PropertyModel<GenderType> genderTypePm = new PropertyModel<GenderType>(personPm,Constants.GENDER_TYPE);
		Collection<GenderType> genderTypeList = iArkCommonService.getGenderType(); 
		ChoiceRenderer genderTypeRenderer = new ChoiceRenderer(Constants.NAME,Constants.ID);
		genderTypeDdc = new DropDownChoice<GenderType>(Constants.GENDER_TYPE,genderTypePm, (List)genderTypeList,genderTypeRenderer);
	}
	
	protected void onNew(AjaxRequestTarget target) {
		// ARK-108:: no longer do full reset to VO
		// Set a default Country on new when the Country field is empty
		if (getModelObject().getSubjectStudy().getCountry() == null) {
			final List<Country> countryList = iArkCommonService.getCountries();
			getModelObject().getSubjectStudy().setCountry(countryList.get(0));
		}
		updateDetailFormPrerender(getModelObject().getSubjectStudy());

		preProcessDetailPanel(target);
	}
	
	
	public void updateDetailFormPrerender(LinkSubjectStudy linkSubjectStudy){
		List<CountryState> countryStateList = iArkCommonService.getStates(linkSubjectStudy.getCountry());
		WebMarkupContainer wmcStateSelector = (WebMarkupContainer) detailFormCompContainer.get(Constants.COUNTRY_STATE_SELECTOR_WMC);
		DropDownChoice<CountryState> stateChoice = (DropDownChoice<CountryState> )wmcStateSelector.get(Constants.SUBJECT_STATE);
		TextField<String> otherState = (TextField<String>)wmcStateSelector.get(Constants.SUBJECT_OTHER_STATE);
		
		if(countryStateList != null && countryStateList.size() > 0){
			stateChoice.getChoices().clear();
			stateChoice.setChoices(countryStateList);
			stateChoice.setVisible(true);
			otherState.setVisible(false);
		}else{
			//hide it
			stateChoice.setVisible(false);
			otherState.setVisible(true);
		}
	
	}

	protected void onSearch(AjaxRequestTarget target){
		
		target.addComponent(feedbackPanel);
		Long sessionStudyId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		getModelObject().getSubjectStudy().setStudy(iArkCommonService.getStudy(sessionStudyId));

		Collection<SubjectVO> subjects = iArkCommonService.getSubject(getModelObject());
		
		if(subjects != null && subjects.size() == 0){
			this.info("There are no subjects with the specified criteria.");
			target.addComponent(feedbackPanel);
		}
		getModelObject().setSubjectList(subjects);
		listView.removeAll();
		listContainer.setVisible(true);//Make the WebMarkupContainer that houses the search results visible
		target.addComponent(listContainer);//For ajax this is required so 
		
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractSearchForm#isSecure(java.lang.String)
	 */
	@Override
	protected boolean isSecure(String actionType) {
		// TODO Auto-generated method stub
		return true;
	}
	

}
