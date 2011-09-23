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
package au.org.theark.lims.web.component.subjectlims.subject;

import java.text.SimpleDateFormat;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.security.ArkLdapRealm;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.util.ContextHelper;
import au.org.theark.core.web.component.ArkBusyAjaxLink;
import au.org.theark.core.web.component.ArkDataProvider2;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.web.Constants;
import au.org.theark.lims.web.component.subjectlims.lims.LimsContainerPanel;
import au.org.theark.lims.web.component.subjectlims.subject.form.ContainerForm;
import au.org.theark.lims.web.component.subjectlims.subject.form.DetailForm;

/**
 * @author nivedann
 * 
 */
@SuppressWarnings({ "unchecked", "serial" })
public class SearchResultListPanel extends Panel {

	/**
	 * 
	 */
	private static final long		serialVersionUID	= -8517602411833622907L;
	private static final Logger	log					= LoggerFactory.getLogger(SearchResultListPanel.class);
	private WebMarkupContainer		detailPanelContainer;
	private WebMarkupContainer		detailPanelFormContainer;
	private WebMarkupContainer		searchPanelContainer;
	private WebMarkupContainer		searchResultContainer;
	private WebMarkupContainer		viewButtonContainer;
	private WebMarkupContainer		editButtonContainer;
	private WebMarkupContainer		arkContextMarkup;
	private ContainerForm			subjectContainerForm;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService		iArkCommonService;
	
	@SpringBean(name = "arkLdapRealm")
	private ArkLdapRealm				realm;

	public SearchResultListPanel(String id, WebMarkupContainer detailPanelContainer, WebMarkupContainer detailPanelFormContainer, WebMarkupContainer searchPanelContainer,
			WebMarkupContainer searchResultContainer, WebMarkupContainer viewButtonContainer, WebMarkupContainer editButtonContainer, WebMarkupContainer arkContextMarkup, ContainerForm containerForm) {

		super(id);

		this.detailPanelContainer = detailPanelContainer;
		this.searchPanelContainer = searchPanelContainer;
		this.searchResultContainer = searchResultContainer;
		this.viewButtonContainer = viewButtonContainer;
		this.editButtonContainer = editButtonContainer;
		this.detailPanelFormContainer = detailPanelFormContainer;
		this.arkContextMarkup = arkContextMarkup;
		this.subjectContainerForm = containerForm;
	}

	public DataView<LinkSubjectStudy> buildDataView(ArkDataProvider2<LimsVO, LinkSubjectStudy> subjectProvider) {

		DataView<LinkSubjectStudy> linkSubjectStudyDataView = new DataView<LinkSubjectStudy>("subjectList", subjectProvider) {

			@Override
			protected void populateItem(final Item<LinkSubjectStudy> item) {
				LinkSubjectStudy subject = item.getModelObject();
				item.add(buildLink(item.getModelObject()));
				
				Label studyLbl = new Label("studyLbl", subject.getStudy().getName());
				item.add(studyLbl);

				StringBuffer sb = new StringBuffer();
				String firstName = subject.getPerson().getFirstName();
				String midName = subject.getPerson().getMiddleName();
				String lastName = subject.getPerson().getLastName();

				if (firstName != null) {
					sb.append(subject.getPerson().getFirstName());
					sb.append(" ");
				}
				if (midName != null) {
					sb.append(subject.getPerson().getMiddleName());
					sb.append(" ");
				}
				if (lastName != null) {
					sb.append(subject.getPerson().getLastName());
				}

				item.add(new Label(Constants.SUBJECT_FULL_NAME, sb.toString()));

				item.add(new Label("person.genderType.name", subject.getPerson().getGenderType().getName()));

				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
				String dateOfBirth = "";
				if (subject != null && subject.getPerson() != null && subject.getPerson().getDateOfBirth() != null) {
					dateOfBirth = simpleDateFormat.format(subject.getPerson().getDateOfBirth());
					item.add(new Label("person.dateOfBirth", dateOfBirth));
				}
				else {
					item.add(new Label("person.dateOfBirth", ""));
				}

				item.add(new Label("person.vitalStatus.statusName", subject.getPerson().getVitalStatus().getName()));

				if(subject.getConsentStatus() != null) {
					item.add(new Label("consentStatus.name", subject.getConsentStatus().getName()));
				}
				else {
					item.add(new Label("consentStatus.name", ""));
				}

				item.add(new AttributeModifier(Constants.CLASS, true, new AbstractReadOnlyModel() {
					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? Constants.EVEN : Constants.ODD;
					}
				}));
			}
		};
		return linkSubjectStudyDataView;
	}

	/**
	 * Builds the link for selection of subject (gets from database to ensure persistence)s
	 * @param subject
	 * @return
	 */
	private AjaxLink buildLink(final LinkSubjectStudy subject) {
		ArkBusyAjaxLink link = new ArkBusyAjaxLink("subjectUID") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				LinkSubjectStudy subjectFromBackend = new LinkSubjectStudy();
				try {
					
					subjectFromBackend = iArkCommonService.getSubjectByUID(subject.getSubjectUID());
				}
				catch (EntityNotFoundException e) {
					log.error(e.getMessage());
				}

				// Set SubjectUID into context
				SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.SUBJECTUID, subjectFromBackend.getSubjectUID());
				SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.STUDY, subjectFromBackend.getStudy());
				SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID, subjectFromBackend.getStudy().getId());
				
				SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID, subject.getPerson().getId());
				SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.PERSON_TYPE, au.org.theark.core.Constants.PERSON_CONTEXT_TYPE_SUBJECT);
				
				// Force clearing of Cache to re-load roles for the user for the study
				realm.clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());

				LimsVO limsVo = new LimsVO();
				limsVo.setLinkSubjectStudy(subjectFromBackend);
				limsVo.setStudy(subjectFromBackend.getStudy());
				subjectContainerForm.setModelObject(limsVo);
				
				// Set context items
				ContextHelper contextHelper = new ContextHelper();
				contextHelper.setStudyContextLabel(target, subjectFromBackend.getStudy().getName(), arkContextMarkup);
				contextHelper.setSubjectContextLabel(target, subjectFromBackend.getSubjectUID(), arkContextMarkup);

				detailPanelContainer.setVisible(true);
				viewButtonContainer.setVisible(true);
				viewButtonContainer.setEnabled(true);
				detailPanelFormContainer.setEnabled(false);
				searchResultContainer.setVisible(false);
				searchPanelContainer.setVisible(false);
				editButtonContainer.setVisible(false);

				// Always disable subjectUID
				DetailPanel details = (DetailPanel) detailPanelContainer.get("detailsPanel");
				DetailForm detailsForm = (DetailForm) details.get("detailsForm");
				detailsForm.getSubjectUIDTxtFld().setEnabled(false);

				target.add(searchResultContainer);
				target.add(detailPanelContainer);
				target.add(detailPanelFormContainer);
				target.add(searchPanelContainer);
				target.add(viewButtonContainer);
				target.add(editButtonContainer);

				// Refresh the contextUpdateTarget (add)
				if (subjectContainerForm.getContextUpdateLimsWMC() != null) {
					Panel limsContainerPanel = new LimsContainerPanel("limsContainerPanel", arkContextMarkup);
					subjectContainerForm.getContextUpdateLimsWMC().setVisible(true);
					subjectContainerForm.getContextUpdateLimsWMC().addOrReplace(limsContainerPanel);
					target.add(subjectContainerForm.getContextUpdateLimsWMC());
				}
			}
		};
		Label nameLinkLabel = new Label(Constants.SUBJECT_KEY_LBL, subject.getSubjectUID());
		link.add(nameLinkLabel);
		return link;
	}
}
