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
package au.org.theark.lims.web.component.global.biospecimen;

import java.util.List;

import javax.swing.tree.DefaultTreeModel;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.web.component.global.biospecimen.form.ContainerForm;

/**
 * 
 * @author cellis
 *
 */
public class BiospecimenContainerPanel extends Panel {

	private static final long						serialVersionUID	= -1L;
	private static final Logger					log					= LoggerFactory.getLogger(BiospecimenContainerPanel.class);
	
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>						iArkCommonService;
	
	protected LimsVO									limsVO				= new LimsVO();
	protected CompoundPropertyModel<LimsVO>	cpModel;

	protected ArkCrudContainerVO					arkCrudContainerVO;

	protected FeedbackPanel							feedbackPanel;
	protected WebMarkupContainer					arkContextMarkup;
	protected ContainerForm							containerForm;
	protected Panel									biospecimenListPanel;
	private WebMarkupContainer		studyNameMarkup;
	private WebMarkupContainer		studyLogoMarkup;

	public BiospecimenContainerPanel(String id, WebMarkupContainer arkContextMarkup, 	WebMarkupContainer studyNameMarkup, 	WebMarkupContainer	studyLogoMarkup, DefaultTreeModel treeModel) {
		super(id);
		this.arkContextMarkup = arkContextMarkup;
		this.studyNameMarkup = studyNameMarkup;
		this.studyLogoMarkup = studyLogoMarkup;
		
		limsVO.setTreeModel(treeModel);
		
		// Get session data (used for subject search)
//		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
//		Study study = iArkCommonService.getStudy(sessionStudyId);
//		limsVO.setStudy(study);
		
		cpModel = new CompoundPropertyModel<LimsVO>(limsVO);
		
		Subject currentUser = SecurityUtils.getSubject();
		ArkUser arkUser;
		try {
			arkUser = iArkCommonService.getArkUser(currentUser.getPrincipal().toString());
			ArkUserVO arkUserVo = new ArkUserVO();
			arkUserVo.setArkUserEntity(arkUser);
			List<Study> studies = iArkCommonService.getStudyListForUser(arkUserVo);
			cpModel.getObject().setStudyList(studies);
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
		}
		
		arkCrudContainerVO = new ArkCrudContainerVO();
		
		initialisePanel();
	}

	public void initialisePanel() {
		containerForm = new ContainerForm("containerForm", cpModel);
				
		// Needed to handle for modalWindow
		containerForm.setMultiPart(true);
		
		containerForm.add(initialiseFeedBackPanel());
		containerForm.add(initialiseSearchPanel());
		containerForm.add(initialiseSearchResultPanel());
		this.add(containerForm);
	}
	
	protected WebMarkupContainer initialiseFeedBackPanel() {
		/* Feedback Panel */
		feedbackPanel = new FeedbackPanel("feedbackMessage");
		feedbackPanel.setOutputMarkupId(true);
		return feedbackPanel;
	}
	
	private WebMarkupContainer initialiseSearchPanel() {
		WebMarkupContainer searchPanelContainer = arkCrudContainerVO.getSearchPanelContainer();
		SearchPanel searchComponentPanel = new SearchPanel("searchPanel", feedbackPanel, containerForm, arkCrudContainerVO);
		searchComponentPanel.initialisePanel();
		searchPanelContainer.add(searchComponentPanel);
		return searchPanelContainer;
	}
	
	private WebMarkupContainer initialiseSearchResultPanel() {
		WebMarkupContainer resultListContainer = arkCrudContainerVO.getSearchResultPanelContainer();
		resultListContainer.setOutputMarkupPlaceholderTag(true);
		
		BiospecimenListPanel biospecimenListPanel = new BiospecimenListPanel("biospecimenListPanel", feedbackPanel, cpModel, arkContextMarkup, studyNameMarkup, studyLogoMarkup);
		this.biospecimenListPanel = biospecimenListPanel;
		resultListContainer.add(biospecimenListPanel);
		return resultListContainer;
	}

	/**
	 * @return the log
	 */
	public static Logger getLog() {
		return log;
	}
}