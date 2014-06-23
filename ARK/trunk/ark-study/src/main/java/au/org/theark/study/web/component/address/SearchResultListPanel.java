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
package au.org.theark.study.web.component.address;

import java.text.SimpleDateFormat;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.study.entity.Address;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.ArkCRUDHelper;
import au.org.theark.core.web.component.link.ArkBusyAjaxLink;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.address.form.ContainerForm;
import au.org.theark.study.web.component.address.form.SearchForm;

/**
 * @author nivedann
 * 
 */
public class SearchResultListPanel extends Panel {


	private static final long	serialVersionUID	= 1L;
	private ContainerForm		containerForm;
	private ArkCrudContainerVO	arkCrudContainerVO;
	
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService		iArkCommonService;

	public SearchResultListPanel(String id, ArkCrudContainerVO arkCrudContainerVO, ContainerForm containerForm) {
		super(id);
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.containerForm = containerForm;
	}

	@SuppressWarnings("unchecked")
	public PageableListView<Address> buildPageableListView(IModel iModel) {

		PageableListView<Address> pageableListView = new PageableListView<Address>(Constants.ADDRESS_LIST, iModel, iArkCommonService.getRowsPerPage()) {


			private static final long	serialVersionUID	= 1L;

			@Override
			protected void populateItem(final ListItem<Address> item) {

				Address address = item.getModelObject();
				item.add(buildLink(address));

				if (address.getCity() != null) {
					item.add(new Label("city", address.getCity()));
				}
				else {
					item.add(new Label("city", ""));
				}
				
				if (address.getState() != null && address.getState().getName() != null) {
					item.add(new Label("state.name", address.getState().getName()));//TODO things like this might almost need to be constants
				}
				else {
					item.add(new Label("state.name", (address.getOtherState()!=null && !address.getOtherState().isEmpty())?
							("other: "+address.getOtherState())
							:"not defined"));
				}

				if (address.getPostCode() != null) {
					item.add(new Label("postCode", address.getPostCode()));
				}
				else {
					item.add(new Label("postCode", ""));
				}

				if (address.getCountry() != null && address.getCountry().getName() != null) {
					item.add(new Label("country.name", address.getCountry().getName()));
				}
				else {
					item.add(new Label("country.name", ""));
				}

				if (address.getAddressType() != null && address.getAddressType().getName() != null) {
					item.add(new Label("addressType.name", address.getAddressType().getName()));
				}
				else {
					item.add(new Label("addressType.name", ""));
				}

				if (address.getDateReceived() != null) {
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
					String dateReceived = "";
					dateReceived = simpleDateFormat.format(address.getDateReceived());
					item.add(new Label("address.dateReceived", dateReceived));
				}
				else {
					item.add(new Label("address.dateReceived", ""));
				}

				if (address.getPreferredMailingAddress() != null && address.getPreferredMailingAddress() == true) {
					item.add(new ContextImage("address.preferredMailingAddress", new Model<String>("images/icons/tick.png")));
				}
				else {
					item.add(new Label("address.preferredMailingAddress", ""));
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

	private AjaxLink<String> buildLink(final Address address) {

		ArkBusyAjaxLink<String> link = new ArkBusyAjaxLink<String>("address") {


			private static final long	serialVersionUID	= 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {

				containerForm.getModelObject().setAddress(address);

				// Update the state choices based on selected address pre-render...
				SearchForm searchForm = (SearchForm) ((SearchPanel) arkCrudContainerVO.getSearchPanelContainer().get("searchComponentPanel")).get("searchForm");
				searchForm.updateDetailFormPrerender(address);

				ArkCRUDHelper.preProcessDetailPanelOnSearchResults(target, arkCrudContainerVO);

			}

		};
		String add1 = address.getAddressLineOne()!=null?address.getAddressLineOne():"";
		String streetAdd = address.getStreetAddress()==null?"":address.getStreetAddress();

		String concat = null;
		if(add1.isEmpty() && streetAdd.isEmpty()){
			concat = "no street address specified";
		}
		else{
			concat = (add1.isEmpty()?streetAdd:(add1 + " " + streetAdd) );
		}
		//could be neatened up a little more...but that will do for now.  presentation later
		Label nameLinkLabel = new Label(Constants.ADDRESS_LABEL, concat);
		link.add(nameLinkLabel);
		return link;
	}

}
