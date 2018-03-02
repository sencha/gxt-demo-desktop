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
import com.sencha.gxt.desktop.client.theme.base.startmenu.StartHeadingMenuItemAppearance;
import com.sencha.gxt.widget.core.client.menu.MenuItem;

/**
 * Provides a heading to use with a start menu. This item is typically displayed
 * at the top of a start menu to provide additional information (e.g. the
 * currently logged-in user). A start heading menu item is menu item with a
 * unique appearance that is tailored for use with a start menu.
 */
public class StartHeadingMenuItem extends MenuItem {

  /**
   * Creates a heading menu item with a default unique appearance tailored for
   * use with a start menu.
   */
  public StartHeadingMenuItem() {
    this(new StartHeadingMenuItemAppearance());
  }

  /**
   * Creates a heading menu item with the specified appearance.
   * 
   * @param startHeadingMenuItemAppearance the appearance
   */
  public StartHeadingMenuItem(StartHeadingMenuItemAppearance startHeadingMenuItemAppearance) {
    super(startHeadingMenuItemAppearance);
  }

  /**
   * Creates a heading menu item with a default start menu item appearance and
   * the specified text.
   * 
   * @param text the heading text
   */
  public StartHeadingMenuItem(String text) {
    this();
    setText(text);
  }

  /**
   * Creates a heading menu item with a default start menu item appearance and
   * the specified text and icon
   * 
   * @param text the heading text
   * @param icon the heading icon
   */
  public StartHeadingMenuItem(String text, ImageResource icon) {
    this(text);
    setIcon(icon);
  }

}