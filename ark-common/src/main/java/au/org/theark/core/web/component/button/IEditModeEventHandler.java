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
package au.org.theark.core.web.component.button;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;

/**
 * IEditModeEventHandler is an interface for use with the EditModeButtonsPanel
 * 
 * @author elam
 */
public interface IEditModeEventHandler {

	void onEditSave(AjaxRequestTarget target, Form<?> form);		//Save button's onSubmit
	void onEditCancel(AjaxRequestTarget target, Form<?> form);	//Cancel button's onSubmit
	void onEditDelete(AjaxRequestTarget target, Form<?> form);	//Delete button's onSubmit
	
	void onEditSaveError(AjaxRequestTarget target, Form<?> form);		//Save button's onError
	void onEditCancelError(AjaxRequestTarget target, Form<?> form);	//Cancel button's onError
	void onEditDeleteError(AjaxRequestTarget target, Form<?> form);	//Delete button's onError

}
