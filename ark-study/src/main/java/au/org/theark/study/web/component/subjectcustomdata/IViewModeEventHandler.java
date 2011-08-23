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
package au.org.theark.study.web.component.subjectcustomdata;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;

/**
 * IViewModeEventHandler is an interface for use with the ViewModeButtonsPanel
 * 
 * @author elam
 */
public interface IViewModeEventHandler {

	void onViewEdit(AjaxRequestTarget target, Form<?> form);		//Edit button's onSubmit
	void onViewCancel(AjaxRequestTarget target, Form<?> form);	//Cancel button's onSubmit
	
	void onViewEditError(AjaxRequestTarget target, Form<?> form);		//Edit button's onError
	void onViewCancelError(AjaxRequestTarget target, Form<?> form);	//Cancel button's onError

}
