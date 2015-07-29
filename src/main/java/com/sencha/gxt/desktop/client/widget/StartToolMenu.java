package com.sencha.gxt.desktop.client.widget;

import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.desktop.client.theme.base.startmenu.StartToolMenuAppearance;
import com.sencha.gxt.widget.core.client.menu.Menu;

/**
 * Provides the "tool menu" component of the start menu. The tool menu is
 * typically displayed on the right side of the start menu and contains items
 * that modify the state of the desktop.
 * 
 * @see StartToolMenuItem
 */
public class StartToolMenu extends Menu {

  private StartMenu startMenu;

  /**
   * Creates a tool menu for the specified start menu
   * 
   * @param startMenu the start menu that contains this tool menu
   */
  public StartToolMenu(StartMenu startMenu) {
    this(startMenu, new StartToolMenuAppearance());
  }

  /**
   * Creates a tool menu for the specified start menu, with the specified
   * appearance
   * 
   * @param startMenu the start menu that contains this tool menu
   * @param startToolMenuAppearance the appearance
   */
  public StartToolMenu(StartMenu startMenu, StartToolMenuAppearance startToolMenuAppearance) {
    super(startToolMenuAppearance);
    this.startMenu = startMenu;
  }

  /**
   * {@inheritDoc}
   * 
   * @throws IllegalArgumentException if the specified widget is not a
   *           {@link StartToolMenuItem}
   */
  @Override
  public void add(Widget child) {
    if (!(child instanceof StartToolMenuItem)) {
      throw new IllegalArgumentException("Widget is not a StartToolMenuItem");
    }
    super.add(child);
  }

  @Override
  public void hide(boolean deep) {
    if (activeItem != null) {
      ((StartToolMenuItem) activeItem).hideSubMenu();
      activeItem = null;
    }
    startMenu.hide();
  }
}