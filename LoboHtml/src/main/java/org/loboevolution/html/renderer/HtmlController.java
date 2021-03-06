package org.loboevolution.html.renderer;

import java.awt.Cursor;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.loboevolution.html.dom.domimpl.HTMLAbstractUIElement;
import org.loboevolution.html.dom.domimpl.HTMLButtonElementImpl;
import org.loboevolution.html.dom.domimpl.HTMLInputElementImpl;
import org.loboevolution.html.dom.domimpl.HTMLLinkElementImpl;
import org.loboevolution.html.dom.domimpl.HTMLSelectElementImpl;
import org.loboevolution.html.dom.domimpl.ModelNode;
import org.loboevolution.html.dom.domimpl.NodeImpl;
import org.loboevolution.html.js.Executor;
import org.loboevolution.html.js.events.EventImpl;
import org.loboevolution.html.js.events.MouseEventImpl;
import org.loboevolution.html.renderstate.RenderState;
import org.loboevolution.http.HtmlRendererContext;
import org.mozilla.javascript.Function;

public class HtmlController {
	private static final HtmlController instance = new HtmlController();
	private static final Logger logger = Logger.getLogger(HtmlController.class.getName());

	public static HtmlController getInstance() {
		return instance;
	}

	public boolean onChange(ModelNode node) {
		if (node instanceof HTMLSelectElementImpl) {
			final HTMLSelectElementImpl uiElement = (HTMLSelectElementImpl) node;
			final Function f = uiElement.getOnchange();
			if (f != null) {
				final EventImpl evt = new EventImpl();
				evt.initEvent("change", false, false);
				if (!Executor.executeFunction(uiElement, f, evt, new Object[0])) {
					return false;
				}
			}
		}
		return false;
	}

	public boolean onContextMenu(ModelNode node, MouseEvent event, int x, int y) {
		if (logger.isLoggable(Level.INFO)) {
			logger.info("onContextMenu(): node=" + node + ",class=" + node.getClass().getName());
		}
		if (node instanceof HTMLAbstractUIElement) {
			final HTMLAbstractUIElement uiElement = (HTMLAbstractUIElement) node;
			final Function f = uiElement.getOncontextmenu();
			if (f != null) {
				final MouseEventImpl evt = new MouseEventImpl();
				evt.initMouseEvent("contextmenu", false, false, null, 0, 0, 0, x, y, true, true, true, true, (short) 0, null);
				evt.setIe(event);
				if (!Executor.executeFunction(uiElement, f, evt, new Object[0])) {
					return false;
				}
			}
			final HtmlRendererContext rcontext = uiElement.getHtmlRendererContext();
			if (rcontext != null) {
				if (!rcontext.onContextMenu(uiElement, event)) {
					return false;
				}
			}
		}
		final ModelNode parent = node.getParentModelNode();
		if (parent == null) {
			return true;
		}
		return onContextMenu(parent, event, 0, 0);
	}

	/**
	 * @return True to propagate further, false if consumed.
	 */
	public boolean onDoubleClick(ModelNode node, MouseEvent event, int x, int y) {
		if (logger.isLoggable(Level.INFO)) {
			logger.info("onDoubleClick(): node=" + node + ",class=" + node.getClass().getName());
		}
		if (node instanceof HTMLAbstractUIElement) {
			final HTMLAbstractUIElement uiElement = (HTMLAbstractUIElement) node;
			final Function f = uiElement.getOndblclick();
			if (f != null) {
				final MouseEventImpl evt = new MouseEventImpl();
				evt.initMouseEvent("dblclick", false, false, null, 0, 0, 0, x, y, true, true, true, true, (short) 0, null);
				evt.setIe(event);
				if (!Executor.executeFunction(uiElement, f, evt, new Object[0])) {
					return false;
				}
			}
			final HtmlRendererContext rcontext = uiElement.getHtmlRendererContext();
			if (rcontext != null) {
				if (!rcontext.onDoubleClick(uiElement, event)) {
					return false;
				}
			}
		}
		final ModelNode parent = node.getParentModelNode();
		if (parent == null) {
			return true;
		}
		return onDoubleClick(parent, event, 0, 0);
	}

	/**
	 * @return True to propagate further and false if the event was consumed.
	 */
	public boolean onEnterPressed(ModelNode node) {
		final HTMLInputElementImpl hie = (HTMLInputElementImpl) node;
		hie.submitForm(null);
		return false;
	}

	/**
	 * @return True to propagate further and false if the event was consumed.
	 */
	public boolean onMouseClick(ModelNode node, MouseEvent event, int x, int y) {
		if (logger.isLoggable(Level.INFO)) {
			logger.info("onMouseClick(): node=" + node + ",class=" + node.getClass().getName());
		}
		if (node instanceof HTMLAbstractUIElement) {
			final HTMLAbstractUIElement uiElement = (HTMLAbstractUIElement) node;
			final Function f = uiElement.getOnclick();
			final MouseEventImpl evt = new MouseEventImpl();
			evt.initMouseEvent("click", false, false, null, 0, 0, 0, x, y, true, true, true, true, (short) 0, null);
			evt.setIe(event);
			uiElement.dispatchEvent(uiElement, evt);
			if (f != null) {
				if (!Executor.executeFunction(uiElement, f, evt, new Object[0])) {
					return false;
				}
			}
			final HtmlRendererContext rcontext = uiElement.getHtmlRendererContext();
			if (rcontext != null) {
				if (!rcontext.onMouseClick(uiElement, event)) {
					return false;
				}
			}
		}
		if (node instanceof HTMLLinkElementImpl) {
			((HTMLLinkElementImpl) node).navigate();
			return false;
		} else if (node instanceof HTMLButtonElementImpl) {
			final HTMLButtonElementImpl btn = (HTMLButtonElementImpl) node;
			switch (btn.getType()) {
			case "submit":
				btn.submit();
				break;
			case "reset":
				btn.reset();
				break;
			case "button":
			default:
				break;
			}
			return false;
		}
		final ModelNode parent = node.getParentModelNode();
		if (parent == null) {
			return true;
		}
		return onMouseClick(parent, event, 0, 0);
	}

	/**
	 * @return True to propagate further, false if consumed.
	 */
	public boolean onMouseDisarmed(ModelNode node, MouseEvent event) {
		if (node instanceof HTMLLinkElementImpl) {
			((HTMLLinkElementImpl) node).getCurrentStyle().setOverlayColor(null);
			return false;
		}
		final ModelNode parent = node.getParentModelNode();
		if (parent == null) {
			return true;
		}
		return onMouseDisarmed(parent, event);
	}

	/**
	 * @return True to propagate further, false if consumed.
	 */
	public boolean onMouseDown(ModelNode node, MouseEvent event, int x, int y) {
		boolean pass = true;
		if (node instanceof HTMLAbstractUIElement) {
			final HTMLAbstractUIElement uiElement = (HTMLAbstractUIElement) node;
			final Function f = uiElement.getOnmousedown();
			if (f != null) {
				final MouseEventImpl evt = new MouseEventImpl();
				evt.initMouseEvent("mousedown", false, false, null, 0, 0, 0, x, y, true, true, true, true, (short) 0, null);
				evt.setIe(event);
				pass = Executor.executeFunction(uiElement, f, evt, new Object[0]);
			}
		}
		if (node instanceof HTMLLinkElementImpl) {
			((HTMLLinkElementImpl) node).getCurrentStyle().setOverlayColor("#9090FF80");
			return false;
		}
		if (!pass) {
			return false;
		}
		final ModelNode parent = node.getParentModelNode();
		if (parent == null) {
			return true;
		}
		return onMouseDown(parent, event, 0, 0);
	}

	public void onMouseOut(ModelNode node, MouseEvent event, int x, int y, ModelNode limit) {
		while (node != null) {
			if (node == limit) {
				break;
			}
			if (node instanceof HTMLAbstractUIElement) {
				final HTMLAbstractUIElement uiElement = (HTMLAbstractUIElement) node;
				uiElement.setMouseOver(false);
				final Function f = uiElement.getOnmouseout();
				if (f != null) {
					final MouseEventImpl evt = new MouseEventImpl();
					evt.initMouseEvent("mouseout", false, false, null, 0, 0, 0, x, y, true, true, true, true, (short) 0, null);
					evt.setIe(event);
					Executor.executeFunction(uiElement, f, evt, new Object[0]);
				}
				final HtmlRendererContext rcontext = uiElement.getHtmlRendererContext();
				if (rcontext != null) {
					rcontext.onMouseOut(uiElement, event);
				}
			}
			node = node.getParentModelNode();
		}
	}

	public void onMouseOver(ModelNode node, MouseEvent event, int x, int y, ModelNode limit) {
		while (node != null) {
			if (node == limit) {
				break;
			}
			if (node instanceof HTMLAbstractUIElement) {
				final HTMLAbstractUIElement uiElement = (HTMLAbstractUIElement) node;
				uiElement.setMouseOver(true);
				final Function f = uiElement.getOnmouseover();
				if (f != null) {
					final MouseEventImpl evt = new MouseEventImpl();
					evt.initMouseEvent("mouseover", false, false, null, 0, 0, 0, x, y, true, true, true, true, (short) 0, null);
					evt.setIe(event);
					Executor.executeFunction(uiElement, f, evt, new Object[0]);
				}
				final HtmlRendererContext rcontext = uiElement.getHtmlRendererContext();
				if (rcontext != null) {
					rcontext.onMouseOver(uiElement, event);
				}
			}
			node = node.getParentModelNode();
		}
	}
	
	
	public void onMouseOver(final BaseBoundableRenderable renderable, final ModelNode nodeStart, final MouseEvent event, final int x, final int y, final ModelNode limit) {
		ModelNode node = nodeStart;
		while (node != null) {
			if (node == limit) {
				break;
			}
			if (node instanceof HTMLAbstractUIElement) {
				final HTMLAbstractUIElement uiElement = (HTMLAbstractUIElement) node;
				uiElement.setMouseOver(true);
				final Function f = uiElement.getOnmouseover();
				if (f != null) {
					final MouseEventImpl evt = new MouseEventImpl();
					evt.initMouseEvent("mouseover", false, false, null, 0, 0, 0, x, y, true, true, true, true, (short) 0, null);
					evt.setIe(event);
					Executor.executeFunction(uiElement, f, evt, new Object[0]);
				}
				final HtmlRendererContext rcontext = uiElement.getHtmlRendererContext();
				if (rcontext != null) {
					rcontext.onMouseOver(uiElement, event);
				}
			}
			node = node.getParentModelNode();
		}

		setMouseOnMouseOver(renderable, nodeStart, limit);
	}

	private static void setMouseOnMouseOver(final BaseBoundableRenderable renderable, final ModelNode nodeStart, final ModelNode limit) {
		ModelNode node = nodeStart;
		while (node != null) {
			if (node == limit) {
				break;
			}
			if (node instanceof NodeImpl) {
				final NodeImpl uiElement = (NodeImpl) node;
				final HtmlRendererContext rcontext = uiElement.getHtmlRendererContext();
				final RenderState rs = uiElement.getRenderState();
				final Optional<Cursor> cursorOpt = rs.getCursor();
				if (cursorOpt.isPresent()) {
					rcontext.setCursor(cursorOpt);
					break;
				} else {
					if (node.getParentModelNode() == limit) {
						if (renderable instanceof RWord || renderable instanceof RBlank) {
							rcontext.setCursor(Optional.of(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR)));
						}
					}
				}
			}
			node = node.getParentModelNode();
		}
	}

	/**
	 * @return True to propagate further, false if consumed.
	 */
	public boolean onMouseUp(ModelNode node, MouseEvent event, int x, int y) {
		boolean pass = true;
		if (node instanceof HTMLAbstractUIElement) {
			final HTMLAbstractUIElement uiElement = (HTMLAbstractUIElement) node;
			final Function f = uiElement.getOnmouseup();
			if (f != null) {
				final MouseEventImpl evt = new MouseEventImpl();
				evt.initMouseEvent("mouseup", false, false, null, 0, 0, 0, x, y, true, true, true, true, (short) 0, null);
				evt.setIe(event);
				pass = Executor.executeFunction(uiElement, f, evt, new Object[0]);
			}
		}
		if (node instanceof HTMLLinkElementImpl) {
			((HTMLLinkElementImpl) node).getCurrentStyle().setOverlayColor(null);
			return false;
		}
		if (!pass) {
			return false;
		}
		final ModelNode parent = node.getParentModelNode();
		if (parent == null) {
			return true;
		}
		return onMouseUp(parent, event, 0, 0);
	}

	/**
	 * @param node The node generating the event.
	 * @param x    For images only, x coordinate of mouse click.
	 * @param y    For images only, y coordinate of mouse click.
	 * @return True to propagate further, false if consumed.
	 */
	public boolean onPressed(ModelNode node, InputEvent event, int x, int y) {
		if (node instanceof HTMLAbstractUIElement) {
			final HTMLAbstractUIElement uiElement = (HTMLAbstractUIElement) node;
			final Function f = uiElement.getOnclick();
			if (f != null) {
				final MouseEventImpl evt = new MouseEventImpl();
				evt.initMouseEvent("click", false, false, null, 0, 0, 0, x, y, true, true, true, true, (short) 0, null);
				evt.setIe(event);
				uiElement.dispatchEvent(uiElement, evt);
				if (!Executor.executeFunction(uiElement, f, evt, new Object[0])) {
					return false;
				}
			}
		}
		
		if (node instanceof HTMLInputElementImpl) {
			final HTMLInputElementImpl hie = (HTMLInputElementImpl) node;
			switch (hie.getType()) {
			case "submit":
				hie.submit();
				break;
			case "reset":
				hie.reset();
				break;
			case "image":
				hie.submitImage(x, y);
				break;
			case "button":
			default:
				break;
			}
		}
		return false;
	}
}
