/**
 * Sencha GXT 1.0.0-SNAPSHOT - Sencha for GWT
 * Copyright (c) 2006-2018, Sencha Inc.
 *
 * licensing@sencha.com
 * http://www.sencha.com/products/gxt/license/
 *
 * ================================================================================
 * Commercial License
 * ================================================================================
 * This version of Sencha GXT is licensed commercially and is the appropriate
 * option for the vast majority of use cases.
 *
 * Please see the Sencha GXT Licensing page at:
 * http://www.sencha.com/products/gxt/license/
 *
 * For clarification or additional options, please contact:
 * licensing@sencha.com
 * ================================================================================
 *
 *
 *
 *
 *
 *
 *
 *
 * ================================================================================
 * Disclaimer
 * ================================================================================
 * THIS SOFTWARE IS DISTRIBUTED "AS-IS" WITHOUT ANY WARRANTIES, CONDITIONS AND
 * REPRESENTATIONS WHETHER EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE
 * IMPLIED WARRANTIES AND CONDITIONS OF MERCHANTABILITY, MERCHANTABLE QUALITY,
 * FITNESS FOR A PARTICULAR PURPOSE, DURABILITY, NON-INFRINGEMENT, PERFORMANCE AND
 * THOSE ARISING BY STATUTE OR FROM CUSTOM OR USAGE OF TRADE OR COURSE OF DEALING.
 * ================================================================================
 */
package com.sencha.gxt.desktop.client.widget;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.Style.Anchor;
import com.sencha.gxt.core.client.Style.AnchorAlignment;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

/**
 * Provides a "start button" for use with the desktop. A start button is a text
 * button with a unique cell appearance. It has a start menu associated with it.
 * Clicking on the start button displays the start menu.
 */
public class StartButton extends TextButton {

  private StartMenu startMenu;
  private SelectHandler selectHandler;

  /**
   * Creates a start button using the start button cell appearance.
   */
  public StartButton() {
    super(new StartButtonCell());
    addSelectHandler(getSelectHandler());
  }

  /**
   * Returns the start menu associated with the start button.
   * 
   * @return the start menu
   */
  public StartMenu getStartMenu() {
    if (startMenu == null) {
      startMenu = new StartMenu();
    }
    return startMenu;
  }

  private SelectHandler getSelectHandler() {
    if (selectHandler == null) {
      selectHandler = new SelectHandler() {
        @Override
        public void onSelect(SelectEvent event) {
          onStartButton();
        }
      };
    }
    return selectHandler;
  }

  private void onStartButton() {
    Widget taskBar = getContainingTaskBar();
    Element alignmentElement = taskBar == null ? getElement() : taskBar.getElement();
    getStartMenu().show(alignmentElement, new AnchorAlignment(Anchor.BOTTOM_LEFT, Anchor.TOP_LEFT, true), 0, 0);
  }

  private Widget getContainingTaskBar() {
    Widget parent;
    while ((parent = getParent()) != null) {
      if (parent instanceof TaskBar) {
        return parent;
      }
    }
    return null;
  }
}
