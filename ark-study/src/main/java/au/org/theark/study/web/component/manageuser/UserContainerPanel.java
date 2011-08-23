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
package au.org.theark.study.web.component.manageuser;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.core.web.component.AbstractContainerPanel;
import au.org.theark.study.service.IUserService;
import au.org.theark.study.web.component.manageuser.form.ContainerForm;

@SuppressWarnings("serial")
public class UserContainerPanel extends AbstractContainerPanel<ArkUserVO> {

	private ContainerForm					containerForm;
	private DetailPanel						detailsPanel;
	private SearchPanel						searchPanel;
	private SearchResultListPanel			searchResultListPanel;
	private PageableListView<ArkUserVO>	pageableListView;

	/* Spring Beans to Access Service Layer */

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService				iArkCommonService;

	@SpringBean(name = "userService")
	private IUserService						userService;

	public UserContainerPanel(String id) {

		super(id, true);

		cpModel = new CompoundPropertyModel<ArkUserVO>(new ArkUserVO());
		containerForm = new ContainerForm("containerForm", cpModel);
		initCrudContainerVO();// THe CRUD Container VO that will have all the WebMarkupContainers.This will be passed around to various panels
		containerForm.add(initialiseFeedBackPanel());
		containerForm.add(initialiseDetailPanel());
		containerForm.add(initialiseSearchResults());
		containerForm.add(initialiseSearchPanel());

		add(containerForm);
	}

	@Override
	protected WebMarkupContainer initialiseSearchResults() {

		SearchResultListPanel searchResultListPanel = new SearchResultListPanel("searchResults", arkCrudContainerVO, containerForm, feedBackPanel);
		iModel = new LoadableDetachableModel<Object>() {
			private static final long	serialVersionUID	= 1L;

			@Override
			protected Object load() {
				ArkUserVO arkUserVO = new ArkUserVO();
				List<ArkUserVO> userResultList = new ArrayList<ArkUserVO>();
				try {
					Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
					if (isActionPermitted()) {
						if (sessionStudyId != null && sessionStudyId > 0) {
							// Search Users must list all the users from ArkUser Group and will include all users across studies.
							userResultList = userService.searchUser(arkUserVO);
							containerForm.getModelObject().setUserList(userResultList);
						}
						pageableListView.removeAll();
					}
				}
				catch (ArkSystemException e) {
					feedBackPanel.error("A System Error has occured. Please contact support.");
				}
				return userResultList;
			}
		};

		pageableListView = searchResultListPanel.buildPageableListView(iModel, arkCrudContainerVO.getSearchResultPanelContainer());
		pageableListView.setReuseItems(true);
		PagingNavigator pageNavigator = new PagingNavigator("navigator", pageableListView);
		searchResultListPanel.add(pageNavigator);
		searchResultListPanel.add(pageableListView);
		arkCrudContainerVO.getSearchResultPanelContainer().add(searchResultListPanel);
		return arkCrudContainerVO.getSearchResultPanelContainer();
	}

	@Override
	protected WebMarkupContainer initialiseDetailPanel() {
		detailsPanel = new DetailPanel("detailsPanel", feedBackPanel, arkCrudContainerVO, containerForm);
		detailsPanel.initialisePanel();
		arkCrudContainerVO.getDetailPanelContainer().add(detailsPanel);
		return arkCrudContainerVO.getDetailPanelContainer();
	}

	@Override
	protected WebMarkupContainer initialiseSearchPanel() {

		ArkUserVO arkUserVO = new ArkUserVO();
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		List<ArkUserVO> userResultList = new ArrayList<ArkUserVO>();

		try {
			if (sessionStudyId != null && sessionStudyId > 0) {
				userResultList = userService.searchUser(arkUserVO);
			}
		}
		catch (ArkSystemException arkException) {

		}

		containerForm.getModelObject().setUserList(userResultList);
		searchPanel = new SearchPanel("searchPanel", arkCrudContainerVO, feedBackPanel, containerForm, pageableListView);
		searchPanel.initialisePanel(cpModel);
		arkCrudContainerVO.getSearchPanelContainer().add(searchPanel);
		return arkCrudContainerVO.getSearchPanelContainer();
	}

}
