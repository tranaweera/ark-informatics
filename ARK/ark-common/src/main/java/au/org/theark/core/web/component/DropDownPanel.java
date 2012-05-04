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
package au.org.theark.core.web.component;

import java.util.List;

import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.Panel;

import au.org.theark.core.model.study.entity.ConsentAnswer;

/**
 * @author nivedann
 * @param <T>
 * 
 */
public class DropDownPanel extends Panel {


	private static final long serialVersionUID = 1L;
	private DropDownChoice<ConsentAnswer>	consentAnswerChoice;

	/**
	 * @param id
	 */
	public DropDownPanel(String id, String selectControlId, List<ConsentAnswer> choicesList) {

		super(id);

		ChoiceRenderer<ConsentAnswer> choiceRenderer = new ChoiceRenderer<ConsentAnswer>("name", "id");
		consentAnswerChoice = new DropDownChoice<ConsentAnswer>(selectControlId, (List) choicesList, choiceRenderer);
		add(consentAnswerChoice);
	}

}
