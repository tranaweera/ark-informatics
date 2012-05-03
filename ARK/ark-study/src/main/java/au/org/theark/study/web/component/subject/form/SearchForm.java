/*******************************************************************************
 * Copyright (c) 2011  University of Western Australia. All rights reserved.
 * 
 * This file is part of The Ark.
 * 
 * The Ark is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * 
 * The Ark is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package au.org.theark.study.web.component.subject.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.study.entity.GenderType;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.SubjectStatus;
import au.org.theark.core.model.study.entity.VitalStatus;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.SubjectVO;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.core.web.form.AbstractSearchForm;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;

/**
 * @author nivedann
 * 
 */
public class SearchForm extends AbstractSearchForm<SubjectVO> {

	/**
	 * 
	 */
	private static final long						serialVersionUID	= 1L;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService						iArkCommonService;

	@SpringBean(name = au.org.theark.core.Constants.STUDY_SERVICE)
	private IStudyService							iStudyService;

	private TextField<String>						subjectUIDTxtFld;
	private TextField<String>						firstNameTxtFld;
	private TextField<String>						middleNameTxtFld;
	private TextField<String>						lastNameTxtFld;
	private DropDownChoice<VitalStatus>			vitalStatusDdc;
	private DropDownChoice<GenderType>			genderTypeDdc;
	private DropDownChoice<SubjectStatus>		subjectStatusDdc;
	private DateTextField							dateOfBirthTxtFld;

	// TODO get explanation never accessed, yet we can set it - maybe wicket can access?
	private PageableListView<SubjectVO>			listView;
	private CompoundPropertyModel<SubjectVO>	cpmModel;

	/**
	 * @param id
	 * @param cpmModel
	 */
	public SearchForm(String id, CompoundPropertyModel<SubjectVO> cpmModel, PageableListView<SubjectVO> listView, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVO) {
		// super(id, cpmModel);
		super(id, cpmModel, feedBackPanel, arkCrudContainerVO);

		this.cpmModel = cpmModel;
		this.listView = listView;
		initialiseSearchForm();
		addSearchComponentsToForm();
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		disableSearchForm(sessionStudyId, "There is no study in context. Please select a study");
	}

	protected void addSearchComponentsToForm() {
		add(subjectUIDTxtFld);
		add(firstNameTxtFld);
		add(middleNameTxtFld);
		add(lastNameTxtFld);
		add(vitalStatusDdc);
		add(subjectStatusDdc);
		add(genderTypeDdc);
		add(dateOfBirthTxtFld);
	}

	protected void initialiseSearchForm() {
		subjectUIDTxtFld = new TextField<String>(Constants.SUBJECT_UID);
		firstNameTxtFld = new TextField<String>(Constants.PERSON_FIRST_NAME);
		middleNameTxtFld = new TextField<String>(Constants.PERSON_MIDDLE_NAME);
		lastNameTxtFld = new TextField<String>(Constants.PERSON_LAST_NAME);
		initVitalStatusDdc();
		initSubjectStatusDdc();
		initGenderTypeDdc();

		dateOfBirthTxtFld = new DateTextField(Constants.PERSON_DOB, au.org.theark.core.Constants.DD_MM_YYYY);
		ArkDatePicker dobDatePicker = new ArkDatePicker();
		dobDatePicker.bind(dateOfBirthTxtFld);
		dateOfBirthTxtFld.add(dobDatePicker);
	}

	private void initVitalStatusDdc() {
		CompoundPropertyModel<SubjectVO> subjectCpm = cpmModel;
		PropertyModel<LinkSubjectStudy> linkSubjectStudyPm = new PropertyModel<LinkSubjectStudy>(subjectCpm, "linkSubjectStudy");
		PropertyModel<Person> personPm = new PropertyModel<Person>(linkSubjectStudyPm, "person");
		PropertyModel<VitalStatus> vitalStatusPm = new PropertyModel<VitalStatus>(personPm, Constants.VITAL_STATUS);
		Collection<VitalStatus> vitalStatusList = iArkCommonService.getVitalStatus();
		ChoiceRenderer vitalStatusRenderer = new ChoiceRenderer(Constants.NAME, Constants.ID);
		vitalStatusDdc = new DropDownChoice<VitalStatus>(Constants.VITAL_STATUS, vitalStatusPm, (List) vitalStatusList, vitalStatusRenderer);
	}

	private void initSubjectStatusDdc() {
		CompoundPropertyModel<SubjectVO> subjectCpm = cpmModel;
		PropertyModel<LinkSubjectStudy> linkSubjectStudyPm = new PropertyModel<LinkSubjectStudy>(subjectCpm, "linkSubjectStudy");
		PropertyModel<SubjectStatus> subjectStatusPm = new PropertyModel<SubjectStatus>(linkSubjectStudyPm, "subjectStatus");
		List<SubjectStatus> subjectStatusList = iArkCommonService.getSubjectStatus();
		ChoiceRenderer subjectStatusRenderer = new ChoiceRenderer(Constants.NAME, Constants.SUBJECT_STATUS_ID);
		subjectStatusDdc = new DropDownChoice<SubjectStatus>(Constants.SUBJECT_STATUS, subjectStatusPm, subjectStatusList, subjectStatusRenderer);
	}

	private void initGenderTypeDdc() {
		CompoundPropertyModel<SubjectVO> subjectCpm = cpmModel;
		PropertyModel<LinkSubjectStudy> linkSubjectStudyPm = new PropertyModel<LinkSubjectStudy>(subjectCpm, "linkSubjectStudy");
		PropertyModel<Person> personPm = new PropertyModel<Person>(linkSubjectStudyPm, Constants.PERSON);
		PropertyModel<GenderType> genderTypePm = new PropertyModel<GenderType>(personPm, Constants.GENDER_TYPE);
		Collection<GenderType> genderTypeList = iArkCommonService.getGenderTypes();
		ChoiceRenderer genderTypeRenderer = new ChoiceRenderer(Constants.NAME, Constants.ID);
		genderTypeDdc = new DropDownChoice<GenderType>(Constants.GENDER_TYPE, genderTypePm, (List) genderTypeList, genderTypeRenderer);
	}

	@SuppressWarnings("unchecked")
	protected void onNew(AjaxRequestTarget target) {
		// Disable SubjectUID if auto-generation set
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study studyInContext = iArkCommonService.getStudy(sessionStudyId);
		if ((studyInContext != null) && (studyInContext.getAutoGenerateSubjectUid())) {
			TextField<String> subjectUIDTxtFld = (TextField<String>) arkCrudContainerVO.getDetailPanelFormContainer().get(Constants.SUBJECT_UID);
			getModelObject().getLinkSubjectStudy().setSubjectUID(Constants.SUBJECT_AUTO_GENERATED);
			subjectUIDTxtFld.setEnabled(false);
			target.add(subjectUIDTxtFld);
		}
		else {
			TextField<String> subjectUIDTxtFld = (TextField<String>) arkCrudContainerVO.getDetailPanelFormContainer().get(Constants.SUBJECT_UID);
			subjectUIDTxtFld.setEnabled(true);
			target.add(subjectUIDTxtFld);
		}

		// Available child studies
		List<Study> availableChildStudies = new ArrayList<Study>(0);
		if (studyInContext.getParentStudy() != null) {
			availableChildStudies = iStudyService.getChildStudyListOfParent(studyInContext);
		}
		getModelObject().setAvailableChildStudies(availableChildStudies);

		preProcessDetailPanel(target);
	}

	protected void onSearch(AjaxRequestTarget target) {
		target.add(feedbackPanel);
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		getModelObject().getLinkSubjectStudy().setStudy(iArkCommonService.getStudy(sessionStudyId));

		long count = iArkCommonService.getStudySubjectCount(cpmModel.getObject());
		if (count == 0L) {
			this.info("There are no subjects with the specified criteria.");
			target.add(feedbackPanel);
		}
		arkCrudContainerVO.getSearchResultPanelContainer().setVisible(true);
		target.add(arkCrudContainerVO.getSearchResultPanelContainer());// For ajax this is required so
	}

	@Override
	protected void onBeforeRender() {
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(sessionStudyId);
		boolean parentStudy = (study.getParentStudy() == null || (study.getParentStudy() == study));
		newButton.setEnabled(parentStudy);
		super.onBeforeRender();
	}
}
