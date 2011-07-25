package au.org.theark.lims.web.component.subjectlims.lims.biocollection;

import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.web.component.subjectlims.lims.biocollection.form.BioCollectionModalDetailForm;

public class BioCollectionModalDetailPanel extends Panel {
	/**
	 * 
	 */
	private static final long						serialVersionUID	= -8745753185256494362L;

	private FeedbackPanel							detailFeedbackPanel;
	private ModalWindow								modalWindow;
	private BioCollectionModalDetailForm			detailForm;
	private ArkCrudContainerVO						arkCrudContainerVo;

	protected CompoundPropertyModel<LimsVO>	cpModel;

	public BioCollectionModalDetailPanel(String id, ModalWindow modalWindow, CompoundPropertyModel<LimsVO> cpModel) {
		super(id);
		this.detailFeedbackPanel = initialiseFeedBackPanel();
		this.setModalWindow(modalWindow);
		this.arkCrudContainerVo = new ArkCrudContainerVO();
		this.cpModel = cpModel;
		initialisePanel();
	}

	protected FeedbackPanel initialiseFeedBackPanel() {
		/* Feedback Panel */
		detailFeedbackPanel = new FeedbackPanel("detailFeedback");
		detailFeedbackPanel.setOutputMarkupId(true);
		return detailFeedbackPanel;
	}

	public void initialisePanel() {
		detailForm = new BioCollectionModalDetailForm("detailForm", detailFeedbackPanel, arkCrudContainerVo, modalWindow, cpModel);
		detailForm.initialiseDetailForm();
		add(detailFeedbackPanel);
		add(detailForm);
	}

	/**
	 * @param modalWindow
	 *           the modalWindow to set
	 */
	public void setModalWindow(ModalWindow modalWindow) {
		this.modalWindow = modalWindow;
	}

	/**
	 * @return the modalWindow
	 */
	public ModalWindow getModalWindow() {
		return modalWindow;
	}

}