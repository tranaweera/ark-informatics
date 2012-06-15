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
package au.org.theark.core.web.component.wizard;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.wizard.Wizard;
import org.apache.wicket.markup.html.WebMarkupContainer;

/**
 * Index page for the wizard example.
 * 
 * @author Eelco Hillenius
 */
public class ArkWizardIndex extends WebMarkupContainer {

	private static final long	serialVersionUID	= -7747910094233928517L;

	/**
	 * Link to the wizard. It's an internal link instead of a bookmarkable page to help us with backbutton surpression. Wizards by default do not
	 * partipcate in versioning, which has the effect that whenever a button is clicked in the wizard, it will never result in a change of the
	 * redirection url. However, though that'll work just fine when you are already in the wizard, there is still the first access to the wizard. But
	 * if you link to the page that renders it using and internal link, you'll circumvent that.
	 * 
	 * @param <T>
	 */
	@SuppressWarnings({ "unused", "serial", "unchecked" })
	private static final class WizardLink<T> extends AjaxLink<T> {

		private final Class<? extends Wizard>	wizardClass;

		/**
		 * Construct.
		 * 
		 * @param <C>
		 * 
		 * @param id
		 *           Component id
		 * @param wizardClass
		 *           Class of the wizard to instantiate
		 */
		public <C extends Wizard> WizardLink(String id, Class<C> wizardClass) {
			super(id);
			this.wizardClass = wizardClass;
		}

		@Override
		public void onClick(AjaxRequestTarget target) {
			// TODO Auto-generated method stub

		}
	}

	/**
	 * Construct.
	 */
	public ArkWizardIndex(String id) {
		super(id);
		// add(new WizardLink("staticWizardLink", StaticWizard.class));
		// add(new WizardLink("staticWizardWithPanelsLink", StaticWizardWithPanels.class));
		// add(new WizardLink("newUserWizardLink", NewUserWizard.class));
	}
}
