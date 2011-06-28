package au.org.theark.lims.web.component.subject.bioCollection;

import org.apache.wicket.markup.html.form.Form;

import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.AbstractDetailModalWindow;
import au.org.theark.lims.model.vo.LimsVO;

public class DetailModalWindow extends AbstractDetailModalWindow
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 4739679880093551297L;
	private DetailPanel detailPanel;

	public DetailModalWindow(String id, ArkCrudContainerVO arkCrudContainerVo)
	{
		super(id, "Collection Detail");
		detailPanel = new DetailPanel("content", this);
		initialiseContentPanel(detailPanel);
	}
	
	public DetailModalWindow(String id, ArkCrudContainerVO arkCrudContainerVo, Form<LimsVO> containerForm)
	{
		super(id, "Collection Detail");
		detailPanel = new DetailPanel("content", this, containerForm);
		initialiseContentPanel(detailPanel);
	}

	/**
	 * @return the detailPanel
	 */
	public DetailPanel getDetailPanel()
	{
		return detailPanel;
	}

	/**
	 * @param detailPanel the detailPanel to set
	 */
	public void setDetailPanel(DetailPanel detailPanel)
	{
		this.detailPanel = detailPanel;
	}
}