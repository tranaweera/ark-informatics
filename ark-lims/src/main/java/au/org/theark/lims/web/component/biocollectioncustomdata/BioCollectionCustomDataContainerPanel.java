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
package au.org.theark.lims.web.component.biocollectioncustomdata;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.lims.entity.BioCollection;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.security.ArkLdapRealm;
import au.org.theark.core.security.ArkPermissionHelper;
import au.org.theark.core.security.PermissionConstants;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.lims.service.ILimsService;

/**
 * @author elam
 * 
 */
public class BioCollectionCustomDataContainerPanel extends Panel {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	@SpringBean(name = "arkLdapRealm")
	private ArkLdapRealm						realm;
	
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	protected IArkCommonService			iArkCommonService;

	@SpringBean(name = au.org.theark.lims.web.Constants.LIMS_SERVICE)
	protected ILimsService					iLimsService;
	
	protected CompoundPropertyModel<BioCollectionCustomDataVO> cpModel;

	protected FeedbackPanel feedbackPanel;
	protected WebMarkupContainer customDataEditorWMC;

	/**
	 * @param id
	 */
	public BioCollectionCustomDataContainerPanel(String id) {
		super(id);
	/* This doesn't need to be done here assuming that it is already done via SubjectSubMenuTab's processAuthorizationCache(..) */
//		Subject currentUser = SecurityUtils.getSubject();
//		realm.clearCachedAuthorizationInfo(currentUser.getPrincipals());
		
		cpModel = new CompoundPropertyModel<BioCollectionCustomDataVO>(new BioCollectionCustomDataVO());		
	}
	
	public BioCollectionCustomDataContainerPanel initialisePanel() {
		add(initialiseFeedbackPanel());
		add(initialiseCustomDataEditorWMC());
		if (!ArkPermissionHelper.isModuleFunctionAccessPermitted()) {
			this.error(au.org.theark.core.Constants.MODULE_NOT_ACCESSIBLE_MESSAGE);
			customDataEditorWMC.setVisible(false);
		}
		return this;
	}

	protected WebMarkupContainer initialiseCustomDataEditorWMC() {
		customDataEditorWMC = new WebMarkupContainer("customDataEditorWMC");
		Panel dataEditorPanel;
		boolean contextLoaded = prerenderContextCheck();
		if (contextLoaded && isActionPermitted()) {
			dataEditorPanel = new BioCollectionCustomDataEditorPanel("customDataEditorPanel", cpModel, feedbackPanel).initialisePanel();;
		}
		else if (!contextLoaded) {
			dataEditorPanel = new EmptyPanel("customDataEditorPanel");
			this.error("A study and LIMS collection in context are required to proceed.");
		}
		else {
			dataEditorPanel = new EmptyPanel("customDataEditorPanel");
			this.error("You do not have sufficient permissions to access this function");
		}
		customDataEditorWMC.add(dataEditorPanel);
		return customDataEditorWMC;
	}

	protected WebMarkupContainer initialiseFeedbackPanel() {
		/* Feedback Panel doesn't have to sit within a form */
		feedbackPanel = new FeedbackPanel("feedbackMessage");
		feedbackPanel.setOutputMarkupId(true);
		return feedbackPanel;
	}

	protected boolean isActionPermitted() {
		boolean flag = false;
		SecurityManager securityManager = ThreadContext.getSecurityManager();
		Subject currentUser = SecurityUtils.getSubject();
		if (securityManager.isPermitted(currentUser.getPrincipals(), PermissionConstants.READ)) {
			flag = true;
		}
		else {
			flag = false;
		}
		return flag;
	}

	protected boolean prerenderContextCheck() {
		// Get the Study, SubjectUID and ArkModule from Context
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Long sessionBioCollectionId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.lims.web.Constants.BIO_COLLECTION);
		Long sessionArkModuleId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.ARK_MODULE_KEY);

		boolean contextLoaded = false;
		if ((sessionStudyId != null) && (sessionBioCollectionId != null)) {
			BioCollection bioCollection = null;
			ArkModule arkModule = null;
			Study study = null;
			try {
				study = iArkCommonService.getStudy(sessionStudyId);
				cpModel.getObject().getBioCollection().setStudy(study);
				bioCollection = iLimsService.getBioCollection(sessionBioCollectionId);
				cpModel.getObject().setBioCollection(bioCollection);
				arkModule = iArkCommonService.getArkModuleById(sessionArkModuleId);
//				cpModel.getObject().setArkModule(arkModule);
				if (study != null && bioCollection != null && arkModule != null) {
					contextLoaded = true;
					cpModel.getObject().setArkFunction(iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_LIMS_COLLECTION));
				}
			}
			catch (EntityNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return contextLoaded;
	}

}
