/*
    GNU LESSER GENERAL PUBLIC LICENSE
    Copyright (C) 2006 The Lobo Project. Copyright (C) 2014 Lobo Evolution

    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

    Contact info: lobochief@users.sourceforge.net; ivan.difrancesco@yahoo.it
*/
package org.loboevolution.html.dom.domimpl;

import javax.swing.JButton;

import org.loboevolution.common.Nodes;
import org.loboevolution.html.FormInput;
import org.loboevolution.html.control.ButtonControl;
import org.loboevolution.html.dom.HTMLButtonElement;
import org.loboevolution.html.dom.HTMLFormElement;
import org.loboevolution.html.renderer.HtmlController;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class HTMLButtonElementImpl extends HTMLAbstractUIElement implements HTMLButtonElement {

	public HTMLButtonElementImpl(String name) {
		super(name);
	}

	@Override
	public String getAccessKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getDisabled() {
		final String disabled = getAttribute("disabled");
		return disabled == null ? false : true;
	}

	@Override
	public HTMLFormElement getForm() {
		Node parent = this.getParentNode();
		while ((parent != null) && !(parent instanceof HTMLFormElement)) {
			parent = parent.getParentNode();
		}
		return (HTMLFormElement) parent;
	}

	@Override
	public String getName() {
		return getAttribute("name");
	}

	@Override
	public int getTabIndex() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType() {
	final String val = getAttribute("type");
	return val == null ? "submit" : val;
	}

	@Override
	public String getValue() {
		return getAttribute("value");
	}

	@Override
	public void setAccessKey(String accessKey) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDisabled(boolean disabled) {
		setAttribute("disabled", String.valueOf(disabled));
		
	}

	@Override
	public void setName(String name) {
		setAttribute("name", name);
		
	}

	@Override
	public void setTabIndex(int tabIndex) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(String value) {
		setAttribute("value", value);
		
	}

	public void draw(ButtonControl buttonControl) {
		final JButton button = new JButton();
		button.setContentAreaFilled(false);
		button.setText(getText());
		button.setEnabled(!getDisabled());
		button.addActionListener(event -> HtmlController.getInstance().onMouseClick(this, null, 0, 0));
		buttonControl.add(button);
	}

	public void submit() {
		FormInput[] formInputs;
		final String name = getName();
		if (name == null) {
			formInputs = null;
		} else {
			formInputs = new FormInput[] { new FormInput(name, getValue()) };
		}

		final HTMLFormElementImpl form = (HTMLFormElementImpl) getForm();
		if (form != null) {
			form.submit(formInputs);
		}
	}

	public void reset() {
		final HTMLFormElementImpl form = (HTMLFormElementImpl) getForm();
		if (form != null) {
			form.reset();
		}
	}
	
	public void resetInput() {
		final HTMLFormElementImpl form = (HTMLFormElementImpl) getForm();
		if (form != null && form.hasChildNodes()) {
			NodeList childNodes = form.getChildNodes();
			for (Node node : Nodes.iterable(childNodes)) {
				if (node instanceof HTMLInputElementImpl) {
					final HTMLInputElementImpl hie = (HTMLInputElementImpl) node;
					hie.resetInput();
				}
			}
		}
	}

	private String getText() {
		String text = "";
		if (hasChildNodes()) {
			NodeList children = getChildNodes();
			for (Node child : Nodes.iterable(children)) {
				if (child.getNodeType() == Node.TEXT_NODE) {
					String nodeValue = child.getNodeValue();
					String childText = "";
					nodeValue = nodeValue.replace('\n', ' ');
					nodeValue = nodeValue.replace('\r', ' ');
					nodeValue = nodeValue.replace('\t', ' ');
					childText = nodeValue;
					text += childText + " ";
				}
			}
		}

		if (text.length() > 0) {
			return text.substring(0, text.length() - 1);
		} else {
			return text;
		}
	}
}