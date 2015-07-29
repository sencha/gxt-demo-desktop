package com.sencha.gxt.desktop.client.widget;

import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.desktop.client.theme.base.startmenu.StartMainMenuAppearance;
import com.sencha.gxt.widget.core.client.menu.Menu;

/**
 * Provides the "main menu" component of the start menu. The main menu is
 * typically displayed on the left side of the start menu and contains items for
 * desktop applications.
 * 
 * @see StartMainMenuItem
 */
public class StartMainMenu extends Menu {

  private StartMenu startMenu;

  /**
   * Creates a main menu for the specified start menu.
   * 
   * @param startMenu the start menu that contains this main menu
   */
  public StartMainMenu(StartMenu startMenu) {
    this(startMenu, new StartMainMenuAppearance());
  }

  /**
   * Creates a main menu for the specified start menu, with the specified
   * appearance.
   * 
   * @param startMenu the start menu that contains this main menu
   * @param startMainMenuAppearance the appearance
   */
  public StartMainMenu(StartMenu startMenu, StartMainMenuAppearance startMainMenuAppearance) {
    super(startMainMenuAppearance);
    this.startMenu = startMenu;
  }

  /**
   * {@inheritDoc}
   * 
   * @throws IllegalArgumentException if the specified widget is not a
   *           {@link StartMainMenuItem}
   */
  @Override
  public void add(Widget child) {
    if (!(child instanceof StartMainMenuItem)) {
      throw new IllegalArgumentException("Widget is not a StartMainMenuItem");
    }
    super.add(child);
  }

  @Override
  public void hide(boolean deep) {
    if (activeItem != null) {
      ((StartMainMenuItem) activeItem).hideSubMenu();
      activeItem = null;
    }
    startMenu.hide();
  }
}