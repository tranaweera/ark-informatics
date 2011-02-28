/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.study.web.component.subject.form;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.DateValidator;
import org.apache.wicket.validation.validator.StringValidator;
import org.odlabs.wiquery.ui.datepicker.DatePicker;

import au.org.theark.core.model.study.entity.Country;
import au.org.theark.core.model.study.entity.CountryState;
import au.org.theark.core.model.study.entity.GenderType;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.MaritalStatus;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.SubjectStatus;
import au.org.theark.core.model.study.entity.TitleType;
import au.org.theark.core.model.study.entity.VitalStatus;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.util.ContextHelper;
import au.org.theark.core.vo.SubjectVO;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;

/**
 * @author nivedann
 *
 */
public class DetailsForm extends AbstractDetailForm<SubjectVO>{

	@SpringBean( name = Constants.STUDY_SERVICE)
	private IStudyService studyService;
	
	@SpringBean( name =  au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;

	private WebMarkupContainer arkContextMarkupContainer;

	private TextField<String> firstNameTxtFld;
	private TextField<String> middleNameTxtFld;
	private TextField<String> lastNameTxtFld;
	private TextField<String> preferredNameTxtFld;
	private TextField<String> subjectUIDTxtFld;
	private DatePicker<Date> dateOfBirth;
	
	private TextField<String> amdrifIdTxtFld;
	private DatePicker<Date> studyApproachDate;
	private TextField<Long> yearOfFirstMamogramTxtFld;
	private TextField<String> yearOfRecentMamogramTxtFld;
	private TextField<String> totalNumberOfMamogramsTxtFld;
	
	//Address Stuff comes here 
	private TextField<String> streetAddressTxtFld;
	private TextField<String> cityTxtFld;
	private TextField<String> postCodeTxtFld;
	private DropDownChoice<Country> countryChoice;
	private DropDownChoice<CountryState> stateChoice;
	private WebMarkupContainer countryStateSelector;
	
	//Reference Data 
	private DropDownChoice<TitleType> titleTypeDdc;
	private DropDownChoice<VitalStatus> vitalStatusDdc;
	private DropDownChoice<GenderType> genderTypeDdc;
	private DropDownChoice<SubjectStatus> subjectStatusDdc;
	private DropDownChoice<MaritalStatus> maritalStatusDdc;
	
	private Study study;
	
	
	public DetailsForm(	String id,
						FeedbackPanel feedBackPanel,
						WebMarkupContainer resultListContainer,
						WebMarkupContainer detailPanelContainer,
						WebMarkupContainer detailPanelFormContainer,
						WebMarkupContainer searchPanelContainer,
						WebMarkupContainer viewButtonContainer,
						WebMarkupContainer editButtonContainer,
						WebMarkupContainer arkContextContainer,
						ContainerForm containerForm				) {
		
	
			super(id,feedBackPanel,resultListContainer,detailPanelContainer,detailPanelFormContainer,searchPanelContainer,viewButtonContainer,editButtonContainer,containerForm);
			this.arkContextMarkupContainer = arkContextContainer;
	}

		
	 public void initialiseDetailForm(){

		firstNameTxtFld = new TextField<String>(Constants.PERSON_FIRST_NAME);
		middleNameTxtFld = new TextField<String>(Constants.PERSON_MIDDLE_NAME);
		lastNameTxtFld = new TextField<String>(Constants.PERSON_LAST_NAME);
		preferredNameTxtFld = new TextField<String>(Constants.PERSON_PREFERRED_NAME);
		subjectUIDTxtFld = new TextField<String>("subjectStudy.subjectUID"); //Constants.SUBJECT_UID);
		
		dateOfBirth = new DatePicker<Date>(Constants.PERSON_DOB);
		dateOfBirth.setChangeMonth(true);
		dateOfBirth.setChangeYear(true);
		
		//Initialise Drop Down Choices 
		//Title We can also have the reference data populated on Application start and refer to a static list instead of hitting the database
		Collection<TitleType> titleTypeList = iArkCommonService.getTitleType();
		ChoiceRenderer<TitleType> defaultChoiceRenderer = new ChoiceRenderer<TitleType>(Constants.NAME,Constants.ID);
		titleTypeDdc = new DropDownChoice<TitleType>(Constants.PERSON_TYTPE_TYPE,(List)titleTypeList,defaultChoiceRenderer);
		
		PropertyModel<LinkSubjectStudy> linkSubjectStudyPm = new PropertyModel<LinkSubjectStudy>(containerForm.getModelObject(),"subjectStudy");
		PropertyModel<Person> personPm = new PropertyModel<Person>(linkSubjectStudyPm,"person");

		
		PropertyModel<VitalStatus> vitalStatusPm = new PropertyModel<VitalStatus>(personPm,Constants.VITAL_STATUS);
		Collection<VitalStatus> vitalStatusList = iArkCommonService.getVitalStatus();
		ChoiceRenderer<VitalStatus> vitalStatusRenderer = new ChoiceRenderer<VitalStatus>(Constants.NAME, Constants.ID);
		vitalStatusDdc = new DropDownChoice<VitalStatus>(Constants.PERSON_VITAL_STATUS,vitalStatusPm,(List)vitalStatusList,vitalStatusRenderer);
		
		PropertyModel<GenderType> genderTypePm = new PropertyModel<GenderType>(personPm,Constants.GENDER_TYPE);
		Collection<GenderType> genderTypeList = iArkCommonService.getGenderType(); 
		ChoiceRenderer<GenderType> genderTypeRenderer = new ChoiceRenderer<GenderType>(Constants.NAME,Constants.ID);
		genderTypeDdc = new DropDownChoice<GenderType>(Constants.PERSON_GENDER_TYPE,genderTypePm,(List)genderTypeList,genderTypeRenderer);
		
		PropertyModel<SubjectStatus> subjectStatusPm = new PropertyModel<SubjectStatus>(linkSubjectStudyPm,"subjectStatus");
		Collection<SubjectStatus> subjectStatusList = iArkCommonService.getSubjectStatus();
		ChoiceRenderer<SubjectStatus> subjectStatusRenderer = new ChoiceRenderer<SubjectStatus>(Constants.NAME,Constants.SUBJECT_STATUS_ID);
		subjectStatusDdc = new DropDownChoice<SubjectStatus>(Constants.SUBJECT_STATUS,subjectStatusPm,(List)subjectStatusList,subjectStatusRenderer);
		
		Collection<MaritalStatus> maritalStatusList = iArkCommonService.getMaritalStatus(); 
		PropertyModel<MaritalStatus> msPM = new  PropertyModel<MaritalStatus>(personPm,"maritalStatus");
		ChoiceRenderer<MaritalStatus> maritalStatusRender = new ChoiceRenderer<MaritalStatus>(Constants.NAME,Constants.ID);
		maritalStatusDdc = new DropDownChoice<MaritalStatus>(Constants.PERSON_MARITAL_STATUS,msPM,(List) maritalStatusList, maritalStatusRender);
		
		initCustomFields();
		
		attachValidators();
		addDetailFormComponents();
	}
	 
	private void initCustomFields(){
		amdrifIdTxtFld = new TextField<String>("subjectStudy.amdrifId");
		
		studyApproachDate = new DatePicker<Date>("subjectStudy.studyApproachDate");
		studyApproachDate.setChangeMonth(true);
		studyApproachDate.setChangeYear(true);
		
		yearOfFirstMamogramTxtFld =  new TextField<Long>("subjectStudy.yearOfFirstMamogram",Long.class);
		yearOfRecentMamogramTxtFld =  new TextField<String>("subjectStudy.yearOfRecentMamogram");
		totalNumberOfMamogramsTxtFld = new TextField<String>("subjectStudy.totalNumberOfMamograms");
		
		streetAddressTxtFld = new TextField<String>("subjectStudy.siteAddress");
		cityTxtFld = new TextField<String>("subjectStudy.city");
		postCodeTxtFld = new TextField<String>("subjectStudy.postCode");
		
		initialiaseCountryDropDown();
		initialiseCountrySelector();
		
	}
	
	private void initialiseCountrySelector(){
		
		countryStateSelector = new WebMarkupContainer("countryStateSelector");
		countryStateSelector.setOutputMarkupPlaceholderTag(true);
		//Get the value selected in Country
		Country selectedCountry  = countryChoice.getModelObject();
		
		//If there is no country selected, back should default to current country and pull the states
		List<CountryState> countryStateList  = iArkCommonService.getStates(selectedCountry);
		ChoiceRenderer<CountryState> defaultStateChoiceRenderer = new ChoiceRenderer<CountryState>("state", Constants.ID);
		stateChoice = new DropDownChoice<CountryState>("subjectStudy.state",countryStateList,defaultStateChoiceRenderer);
		//Add the Country State Dropdown into the WebMarkupContainer - countrySelector
		countryStateSelector.add(stateChoice);
	}
	
	private void initialiaseCountryDropDown(){
		
		final List<Country> countryList = iArkCommonService.getCountries();
		ChoiceRenderer<Country> defaultChoiceRenderer = new ChoiceRenderer<Country>(Constants.NAME, Constants.ID);
		class TestVO{
			public Country selected= countryList.get(0);
		}
		countryChoice = new DropDownChoice<Country>("subjectStudy.country", new PropertyModel(new TestVO(),"selected"),countryList, defaultChoiceRenderer);
		//Attach a behavior, so when it changes it does something
		countryChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				updateCountryStateChoices(countryChoice.getModelObject());
				target.addComponent(countryStateSelector);
			}
		});
	}
	
	/**
	 * A method that will refresh the choices in the State drop down choice based on
	 * what was selected in the Country Dropdown. It uses the country as the argument and invokes
	 * the back-end to fetch relative states.
	 */
	private void updateCountryStateChoices(Country country){
		
		List<CountryState> countryStateList = iArkCommonService.getStates(country);
		stateChoice.getChoices().clear();
		stateChoice.setChoices(countryStateList);
	}
	
	public void addDetailFormComponents(){

		detailPanelFormContainer.add(subjectUIDTxtFld);
		detailPanelFormContainer.add(titleTypeDdc);
		detailPanelFormContainer.add(firstNameTxtFld);
		detailPanelFormContainer.add(middleNameTxtFld);
		detailPanelFormContainer.add(lastNameTxtFld);
		detailPanelFormContainer.add(preferredNameTxtFld);
		detailPanelFormContainer.add(dateOfBirth);
		detailPanelFormContainer.add(vitalStatusDdc);
		detailPanelFormContainer.add(genderTypeDdc);
		detailPanelFormContainer.add(subjectStatusDdc);
		detailPanelFormContainer.add(maritalStatusDdc);
		
		//Add the supposed-to-be custom controls into the form container.
		detailPanelFormContainer.add(amdrifIdTxtFld);
		detailPanelFormContainer.add(studyApproachDate);
		detailPanelFormContainer.add(yearOfFirstMamogramTxtFld);
		detailPanelFormContainer.add(yearOfRecentMamogramTxtFld);
		detailPanelFormContainer.add(totalNumberOfMamogramsTxtFld);
		detailPanelFormContainer.add(streetAddressTxtFld);
		detailPanelFormContainer.add(cityTxtFld);
		detailPanelFormContainer.add(postCodeTxtFld);
		detailPanelFormContainer.add(countryChoice);
		detailPanelFormContainer.add(countryStateSelector);//This contains the drop-downn for State
		
		
	}


	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractDetailForm#processErrors(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void processErrors(AjaxRequestTarget target) {
		target.addComponent(feedBackPanel);
	}
	
	protected  void onCancel(AjaxRequestTarget target){
		SubjectVO subjectVO = new SubjectVO();
		containerForm.setModelObject(subjectVO);
		onCancelPostProcess(target);
		
	}
	

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractDetailForm#attachValidators()
	 */
	@Override
	protected void attachValidators() {
		firstNameTxtFld.setRequired(true);
		dateOfBirth.setRequired(true);
		dateOfBirth.add(DateValidator.maximum(new Date())).setLabel(new StringResourceModel("error.dateofbirth.max.range", this, null));
		
		vitalStatusDdc.setRequired(true);
		genderTypeDdc.setRequired(true);
		subjectUIDTxtFld.setRequired(true);
		subjectUIDTxtFld.add(StringValidator.lengthBetween(1, 8));
	
		
		titleTypeDdc.setRequired(true);
		
		
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onDeleteConfirmed(org.apache.wicket.ajax.AjaxRequestTarget, java.lang.String, org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow)
	 */
	@Override
	protected void onDeleteConfirmed(AjaxRequestTarget target,	String selection, ModalWindow selectModalWindow) {
		
		selectModalWindow.close(target);
		containerForm.setModelObject(new SubjectVO());
		onCancel(target);
		
	}
	
	private boolean validateCustomFields(Long fieldToValidate,String message, AjaxRequestTarget target){
		boolean validFlag=true;
		Calendar calendar = Calendar.getInstance();
		int calYear = calendar.get(Calendar.YEAR);
		System.out.println("\n ---- Calendar Year " + calYear);
		if(fieldToValidate > calYear){
			this.error(message);
			processErrors(target);
			validFlag=false;
		}
		
		return validFlag;
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onSave(org.apache.wicket.markup.html.form.Form, org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onSave(Form<SubjectVO> containerForm,	AjaxRequestTarget target) {
		boolean firstMammogramFlag =false;
		boolean recentMamogramFlag = false;
		target.addComponent(detailPanelContainer);
		
		Long studyId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		if(studyId == null){
			//No study in context
			this.error("There is no study in Context. Please select a study to manage a subject.");
			processErrors(target);
		}
		else{
			study = iArkCommonService.getStudy(studyId);
			boolean validFlag=false;
			Calendar calendar = Calendar.getInstance();
			int calYear = calendar.get(Calendar.YEAR);
			System.out.println("\n ---- Calendar Year " + calYear);
			
			
			 firstMammogramFlag = validateCustomFields(containerForm.getModelObject().getSubjectStudy().getYearOfFirstMamogram(),
								"Year of Fist Mammogram cannot be in the future.",
								target);
			
			 recentMamogramFlag =validateCustomFields(containerForm.getModelObject().getSubjectStudy().getYearOfRecentMamogram(),
					"Year of recent Mammogram cannot be in the future.",
					target);
			
			
			if(firstMammogramFlag  && recentMamogramFlag){
				if(containerForm.getModelObject().getSubjectStudy().getPerson().getId() == null || 
						containerForm.getModelObject().getSubjectStudy().getPerson().getId() == 0){
			
					containerForm.getModelObject().getSubjectStudy().setStudy(study);
					studyService.createSubject(containerForm.getModelObject());
					this.info("Subject has been saved successfully and linked to the study in context " + study.getName());
					processErrors(target);
		
				}else{
		
					studyService.updateSubject(containerForm.getModelObject());
					this.info("Subject has been updated successfully and linked to the study in context " + study.getName());
					processErrors(target);
		
				}
			}
			
			ContextHelper contextHelper = new ContextHelper();
			contextHelper.resetContextLabel(target, arkContextMarkupContainer);
			contextHelper.setStudyContextLabel(target, study.getName(), arkContextMarkupContainer);
			contextHelper.setSubjectContextLabel(target,containerForm.getModelObject().getSubjectUID(), arkContextMarkupContainer);
			
			SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID, containerForm.getModelObject().getSubjectStudy().getPerson().getId());
			//We specify the type of person here as Subject
			SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.PERSON_TYPE, au.org.theark.core.Constants.PERSON_CONTEXT_TYPE_SUBJECT);
			detailPanelContainer.setVisible(true);
		}
		//Only if the validations were good process onSavePostProcess otherwise stay on the same page as we are using custom validations
		if(firstMammogramFlag && recentMamogramFlag){
			onSavePostProcess(target);	
		}
		
		
	}

}
