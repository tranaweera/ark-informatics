<<<<<<< .mine
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
package au.org.theark.registry.web.component.invoice;

import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.model.geno.entity.Pipeline;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.registry.web.component.invoice.form.ContainerForm;
import au.org.theark.registry.web.component.invoice.form.SearchForm;

/**
 * @author cellis
 * 
 */
@SuppressWarnings("serial")
public class SearchPanel extends Panel {

	private ArkCrudContainerVO					arkCrudContainerVO;
	private FeedbackPanel						feedBackPanel;
	private PageableListView<Pipeline>	listView;
	private ContainerForm						containerForm;

	/* Constructor */
	public SearchPanel(String id, FeedbackPanel feedBackPanel, PageableListView<Pipeline> listView, ContainerForm containerForm, ArkCrudContainerVO arkCrudContainerVO) {
		super(id);
		this.listView = listView;
		this.feedBackPanel = feedBackPanel;
		this.containerForm = containerForm;
		this.arkCrudContainerVO = arkCrudContainerVO;
	}

	public void initialisePanel() {
		SearchForm searchForm = new SearchForm(au.org.theark.core.Constants.SEARCH_FORM, (CompoundPropertyModel<Pipeline>) containerForm.getModel(), listView, feedBackPanel, arkCrudContainerVO);
		add(searchForm);
	}
}
=======
package au.org.theark.registry.web.component.invoice;

import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.model.geno.entity.Pipeline;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.registry.web.component.invoice.form.SearchForm;

/**
 * @author nivedann
 *
 */
public class SearchPanel extends Panel{


	private static final long serialVersionUID = 1L;
	private FeedbackPanel	feedbackPanel;
	private ArkCrudContainerVO	arkCrudContainerVO;
	private CompoundPropertyModel<Pipeline> cpModel;
	
	/**
	 * Constructor
	 * @param id
	 */
	public SearchPanel(String id, CompoundPropertyModel<Pipeline> cpModel,ArkCrudContainerVO arkCrudContainerVO, FeedbackPanel feedBackPanel) {
		super(id);
		this.feedbackPanel = feedBackPanel;
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.cpModel = cpModel;
	}
	
	/**
	 * Instantiate the SearchForm and link it with the Panel
	 */
	public void initialisePanel() {
		
		SearchForm searchForm = new SearchForm(au.org.theark.core.Constants.SEARCH_FORM,cpModel,feedbackPanel,arkCrudContainerVO);//,feedbackPanel,arkCrudContainerVO);
		add(searchForm);
		
	}

}
>>>>>>> .r8461
