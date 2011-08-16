/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.study.web.component.subjectcustomdata;

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
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.security.ArkLdapRealm;
import au.org.theark.core.security.PermissionConstants;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;

/**
 * @author elam
 * 
 */
public class SubjectCustomDataContainerPanel extends Panel {

	@SpringBean(name = "arkLdapRealm")
	private ArkLdapRealm						realm;
	
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	protected IArkCommonService					iArkCommonService;

	@SpringBean(name = Constants.STUDY_SERVICE)
	private IStudyService							studyService;
	
	protected CompoundPropertyModel<SubjectCustomDataVO> cpModel;

	protected FeedbackPanel feedbackPanel;
	protected WebMarkupContainer customDataEditorWMC;

	/**
	 * @param id
	 */
	public SubjectCustomDataContainerPanel(String id) {
		super(id);

		Subject currentUser = SecurityUtils.getSubject();
		realm.clearCachedAuthorizationInfo(currentUser.getPrincipals());
		cpModel = new CompoundPropertyModel<SubjectCustomDataVO>(new SubjectCustomDataVO());		
	}
	
	public SubjectCustomDataContainerPanel initialisePanel() {
		add(initialiseFeedbackPanel());
		add(initialiseCustomDataEditorWMC());
		return this;
	}

	protected WebMarkupContainer initialiseCustomDataEditorWMC() {
		customDataEditorWMC = new WebMarkupContainer("customDataEditorWMC");
		Panel dataEditorPanel;
		boolean contextLoaded = prerenderContextCheck();
		if (contextLoaded && isActionPermitted()) {
			dataEditorPanel = new SubjectCustomDataEditorPanel("customDataEditorPanel", cpModel, feedbackPanel).initialisePanel();;
		}
		else if (!contextLoaded) {
			dataEditorPanel = new EmptyPanel("customDataEditorPanel");
			this.error("A study and subject in context are required to proceed.");
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
		String sessionSubjectUID = (String) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.SUBJECTUID);
		Long sessionArkModuleId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.ARK_MODULE_KEY);

		boolean contextLoaded = false;
		if ((sessionStudyId != null) && (sessionSubjectUID != null)) {
			LinkSubjectStudy linkSubjectStudy = null;
			ArkModule arkModule = null;
			Study study = null;
			try {
				study = iArkCommonService.getStudy(sessionStudyId);
				cpModel.getObject().getLinkSubjectStudy().setStudy(study);
				linkSubjectStudy = iArkCommonService.getSubjectByUID(sessionSubjectUID);
				cpModel.getObject().setLinkSubjectStudy(linkSubjectStudy);
				arkModule = iArkCommonService.getArkModuleById(sessionArkModuleId);
				cpModel.getObject().setArkModule(arkModule);
				if (study != null && linkSubjectStudy != null && arkModule != null) {
					contextLoaded = true;
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
