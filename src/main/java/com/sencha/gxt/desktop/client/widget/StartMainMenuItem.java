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

import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.widget.core.client.menu.MenuItem;

/**
 * Provides a "main menu" item for a start menu. A main menu item is displayed
 * in the main menu of the start menu, which is typically to the left of the
 * tool menu. In it's current implementation, a main menu item has the same
 * appearance as a menu item.
 * 
 * @see StartMainMenu
 */
public class StartMainMenuItem extends MenuItem {

  /**
   * Creates a main menu item for the start menu, with the default main menu
   * item appearance.
   */
  public StartMainMenuItem() {
  }

  /**
   * Creates a main menu item for the start menu with the default appearance and
   * the specified text.
   * 
   * @param text the main menu item text
   */
  public StartMainMenuItem(String text) {
    this();
    setText(text);
  }

  /**
   * Creates a main menu item for the start menu with the default appearance and
   * the specified text and icon.
   * 
   * @param text the item text
   * @param icon the icon
   */
  public StartMainMenuItem(String text, ImageResource icon) {
    this(text);
    setIcon(icon);
  }

  /**
   * Hides a displayed sub-menu, if any.
   */
  void hideSubMenu() {
    super.deactivate();
  }

}