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
package au.org.theark.study.web.component.subjectcustomdata;

import java.util.Iterator;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.SubjectCustomFieldData;
import au.org.theark.core.security.ArkPermissionHelper;
import au.org.theark.core.web.component.ArkDataProvider2;
import au.org.theark.core.web.component.customfield.dataentry.CustomDataEditorDataView;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.subjectcustomdata.form.CustomDataEditorForm;

/**
 * @author elam
 * 
 */
@SuppressWarnings({ "unchecked", "serial" })
public class SubjectCustomDataEditorPanel extends Panel {

	/**
	 * 
	 */
	private static final long		serialVersionUID	= -1L;
	private static final Logger	log					= LoggerFactory.getLogger(SubjectCustomDataEditorPanel.class);

	private CompoundPropertyModel<SubjectCustomDataVO>			cpModel;

	@SpringBean(name = Constants.STUDY_SERVICE)
	private IStudyService							studyService;

	protected FeedbackPanel				feedbackPanel;
	protected CustomDataEditorForm	customDataEditorForm;
	protected ArkDataProvider2<SubjectCustomDataVO, SubjectCustomFieldData> scdDataProvider;
	protected DataView<SubjectCustomFieldData> dataView;

	public SubjectCustomDataEditorPanel(String id, CompoundPropertyModel<SubjectCustomDataVO> cpModel, FeedbackPanel feedBackPanel) {
		super(id);

		this.cpModel = cpModel;
		this.feedbackPanel = feedBackPanel;
	}
	
	public SubjectCustomDataEditorPanel initialisePanel() {
		
		initialiseDataView();
		customDataEditorForm = new CustomDataEditorForm("customDataEditorForm", cpModel, feedbackPanel).initialiseForm();
		AjaxPagingNavigator pageNavigator = new AjaxPagingNavigator("navigator", dataView) {
			@Override
			protected void onAjaxEvent(AjaxRequestTarget target) {
				target.addComponent(customDataEditorForm.getDataViewWMC());
				target.addComponent(this);
			}
		};
		pageNavigator.setOutputMarkupId(true);
		customDataEditorForm.getDataViewWMC().add(dataView);
		this.add(customDataEditorForm);
		this.add(pageNavigator);
		
		return this;
	}
	
	private void initialiseDataView() {
		// TODO fix for READ permission check
		if (ArkPermissionHelper.isActionPermitted(au.org.theark.core.Constants.SEARCH)) {
			// Data provider to get pageable results from backend
			scdDataProvider = new ArkDataProvider2<SubjectCustomDataVO, SubjectCustomFieldData>() {
				
				public int size() {
					LinkSubjectStudy lss = criteriaModel.getObject().getLinkSubjectStudy();
					ArkFunction arkFunction = criteriaModel.getObject().getArkFunction();
	
					return studyService.getSubjectCustomFieldDataCount(lss, arkFunction);
				}
	
				public Iterator<SubjectCustomFieldData> iterator(int first, int count) {
					LinkSubjectStudy lss = criteriaModel.getObject().getLinkSubjectStudy();
					ArkFunction arkFunction = criteriaModel.getObject().getArkFunction();
	
					List<SubjectCustomFieldData> subjectCustomDataList = studyService.getSubjectCustomFieldDataList(lss, arkFunction, first, count);
					cpModel.getObject().setSubjectCustomFieldDataList(subjectCustomDataList);
					return cpModel.getObject().getSubjectCustomFieldDataList().iterator();
				}
			};
			// Set the criteria for the data provider
			scdDataProvider.setCriteriaModel(cpModel);
		}
		else {
			// Since module is not accessible, create a dummy dataProvider that returns nothing
			scdDataProvider = new ArkDataProvider2<SubjectCustomDataVO, SubjectCustomFieldData>() {
				
				public Iterator<? extends SubjectCustomFieldData> iterator(int first, int count) {
					return null;
				}

				public int size() {
					return 0;
				}
			};
		}
		
		dataView = this.buildDataView(scdDataProvider);
		dataView.setItemsPerPage(au.org.theark.core.Constants.ROWS_PER_PAGE);
	}
	
	public DataView<SubjectCustomFieldData> buildDataView(ArkDataProvider2<SubjectCustomDataVO, SubjectCustomFieldData> scdDataProvider2) {

		DataView<SubjectCustomFieldData> studyCompDataView = new CustomDataEditorDataView<SubjectCustomFieldData>("subjectCustomDataList", scdDataProvider2) {

			@Override
			protected void populateItem(final Item<SubjectCustomFieldData> item) {
				SubjectCustomFieldData subjectCustomData = item.getModelObject();
				// Ensure we tie Subject in context to the item if that link isn't there already
				if (subjectCustomData.getLinkSubjectStudy() == null) {
					subjectCustomData.setLinkSubjectStudy(cpModel.getObject().getLinkSubjectStudy());
				}
				super.populateItem(item);
			}

			@Override
			protected Form<?> getCustomDataEditorForm() {
				return customDataEditorForm;
			}

			@Override
			protected Logger getLog() {
				return log;
			}
		};
		return studyCompDataView;
	}
}
