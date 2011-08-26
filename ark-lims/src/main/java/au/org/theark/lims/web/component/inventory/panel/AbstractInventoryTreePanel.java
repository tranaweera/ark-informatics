package au.org.theark.lims.web.component.inventory.panel;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.tree.AbstractTree;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.model.lims.entity.InvSite;
import au.org.theark.core.model.lims.entity.InvTreeNode;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.security.ArkPermissionHelper;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.component.button.ArkBusyAjaxButton;
import au.org.theark.lims.model.InventoryModel;
import au.org.theark.lims.model.TreeNodeModel;
import au.org.theark.lims.service.IInventoryService;
import au.org.theark.lims.web.Constants;

public abstract class AbstractInventoryTreePanel extends Panel {
	/**
	 * 
	 */
	private static final long			serialVersionUID	= -2299283711321904639L;
	private static final Logger		log					= LoggerFactory.getLogger(AbstractInventoryTreePanel.class);

	@SpringBean(name = Constants.LIMS_INVENTORY_SERVICE)
	private IInventoryService			iInventoryService;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>	iArkCommonService;

	protected Study							study					= null;

	private List<Study>					studyList			= new ArrayList<Study>(0);
	private List<InvSite>				invSites				= new ArrayList<InvSite>(0);

	protected ArkBusyAjaxButton		addSite;
	protected ArkBusyAjaxButton		addTank;
	protected ArkBusyAjaxButton		addTray;
	protected ArkBusyAjaxButton		addBox;

	public AbstractInventoryTreePanel(String id) {
		super(id);
		setOutputMarkupPlaceholderTag(true);
		initialiseButtons();
	}

	/**
	 * Returns the tree on this pages. This is used to collapse, expand the tree and to switch the rootless mode.
	 * 
	 * @return Tree instance on this page
	 */
	protected abstract AbstractTree getTree();

	/**
	 * Creates the model that feeds the tree.
	 * 
	 * @return New instance of tree model.
	 */
	protected TreeModel createTreeModel() {
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);

		if (sessionStudyId != null && sessionStudyId > 0) {
			study = iArkCommonService.getStudy(sessionStudyId);
		}

		studyList.add(study);

		InvSite invSite = new InvSite();

		try {
			invSites = iInventoryService.searchInvSite(invSite);
		}
		catch (ArkSystemException e) {
			log.error(e.getMessage());
		}
		invSite.setStudy(study);
		return convertToTreeModel(studyList.get(0));
	}

	private TreeModel convertToTreeModel(Study study) {
		TreeModel model = null;
		if (study != null) {
			// Default root node was Study
			DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(new TreeNodeModel(study.getName()));
			add(rootNode, invSites);
			model = new DefaultTreeModel(rootNode);
		}
		return model;
	}

	@SuppressWarnings("unchecked")
	private void add(DefaultMutableTreeNode parentNode, List<? extends InvTreeNode> childNodes) {
		for (InvTreeNode node : childNodes) {
			DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(new InventoryModel(node, node.getNodeType()));
			parentNode.add(childNode);

			// If no children, don't bother
			if (node.getChildren() != null && !node.getChildren().isEmpty()) {
				add(childNode, node.getChildren());
			}
		}
	}

	private void initialiseButtons() {
		addSite = new ArkBusyAjaxButton("addSite", new StringResourceModel("addSiteKey", this, null)) {

			/**
			 * 
			 */
			private static final long	serialVersionUID	= -5810881256056986237L;

			@Override
			public boolean isVisible() {
				return ArkPermissionHelper.isActionPermitted(Constants.SAVE);
			}

			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onAddSiteSubmit(target);
			}

		};

		addTank = new ArkBusyAjaxButton("addTank", new StringResourceModel("addTankKey", this, null)) {

			/**
			 * 
			 */
			private static final long	serialVersionUID	= -7724228745851741839L;

			@Override
			public boolean isVisible() {
				return ArkPermissionHelper.isActionPermitted(Constants.SAVE);
			}

			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onAddTankSubmit(target);
			}

		};

		addTray = new ArkBusyAjaxButton("addTray", new StringResourceModel("addTrayKey", this, null)) {

			/**
			 * 
			 */
			private static final long	serialVersionUID	= 4263304574542528954L;

			@Override
			public boolean isVisible() {
				return ArkPermissionHelper.isActionPermitted(Constants.SAVE);
			}

			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onAddTraySubmit(target);
			}

		};

		addBox = new ArkBusyAjaxButton("addBox", new StringResourceModel("addBoxKey", this, null)) {

			/**
			 * 
			 */
			private static final long	serialVersionUID	= -5402311580929456745L;

			@Override
			public boolean isVisible() {
				return ArkPermissionHelper.isActionPermitted(Constants.SAVE);
			}

			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onAddBoxSubmit(target);
			}

		};
	}

	/**
	 * Method implemented by sub-classes to perform acton on button press
	 * 
	 * @param target
	 */
	public abstract void onAddSiteSubmit(AjaxRequestTarget target);

	/**
	 * Method implemented by sub-classes to perform acton on button press
	 * 
	 * @param target
	 */
	public abstract void onAddTankSubmit(AjaxRequestTarget target);

	/**
	 * Method implemented by sub-classes to perform acton on button press
	 * 
	 * @param target
	 */
	public abstract void onAddTraySubmit(AjaxRequestTarget target);

	/**
	 * Method implemented by sub-classes to perform acton on button press
	 * 
	 * @param target
	 */
	public abstract void onAddBoxSubmit(AjaxRequestTarget target);
}
