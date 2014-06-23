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
package au.org.theark.study.web.component.phone;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.study.entity.Phone;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.ArkCRUDHelper;
import au.org.theark.core.web.component.link.ArkBusyAjaxLink;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.phone.form.ContainerForm;

/**
 * @author nivedann
 * 
 */
public class SearchResultListPanel extends Panel {


	private static final long		serialVersionUID	= 1L;
	private ContainerForm			containerForm;
	protected ArkCrudContainerVO	arkCrudContainerVO;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService		iArkCommonService;
	
	/**
	 * 
	 * @param id
	 * @param arkCrudContainerVO
	 * @param containerForm
	 */
	public SearchResultListPanel(String id, ArkCrudContainerVO arkCrudContainerVO, ContainerForm containerForm) {
		super(id);
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.containerForm = containerForm;
	}

	/**
	 * 
	 * @param iModel
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageableListView<Phone> buildPageableListView(IModel iModel) {

		PageableListView<Phone> pageableListView = new PageableListView<Phone>(Constants.PHONE_LIST, iModel, iArkCommonService.getRowsPerPage()) {


			private static final long	serialVersionUID	= 1L;

			@Override
			protected void populateItem(final ListItem<Phone> item) {
				Phone phone = item.getModelObject();
				item.add(buildLink(phone));

				if (phone.getId() != null) {
					item.add(new Label("id", phone.getId().toString()));
				}
				else {
					item.add(new Label("id", ""));
				}

				if (phone.getAreaCode() != null) {
					item.add(new Label("areaCode", phone.getAreaCode()));
				}
				else {
					item.add(new Label("areaCode", ""));
				}
				if (phone.getPhoneType() != null && phone.getPhoneType().getName() != null) {
					item.add(new Label("phoneType.name", phone.getPhoneType().getName()));
				}
				else {
					item.add(new Label("phoneType.name", ""));
				}

				item.add(new AttributeModifier("class", new AbstractReadOnlyModel<String>() {
					
					private static final long	serialVersionUID	= 1L;

					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));
			}
		};
		return pageableListView;

	}

	private AjaxLink<String> buildLink(final Phone phone) {

		ArkBusyAjaxLink<String> link = new ArkBusyAjaxLink<String>("phoneNumberLink") {


			private static final long	serialVersionUID	= 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {

				containerForm.getModelObject().setPhone(phone);

				ArkCRUDHelper.preProcessDetailPanelOnSearchResults(target, arkCrudContainerVO);

			}

		};
		Label nameLinkLabel = new Label(Constants.PHONE_NUMBER_VALUE, phone.getPhoneNumber());
		link.add(nameLinkLabel);
		return link;
	}

}
