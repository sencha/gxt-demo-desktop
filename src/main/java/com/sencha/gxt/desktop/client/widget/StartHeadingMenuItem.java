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