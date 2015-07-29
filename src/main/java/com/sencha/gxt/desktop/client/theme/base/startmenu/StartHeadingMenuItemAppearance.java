package com.sencha.gxt.desktop.client.theme.base.startmenu;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.theme.blue.client.menu.BlueMenuItemAppearance;

public class StartHeadingMenuItemAppearance extends BlueMenuItemAppearance {

  public interface StartHeadingMenuItemResources extends BlueMenuItemResources, ClientBundle {

    @Override
    @Source({"com/sencha/gxt/theme/base/client/menu/Item.gss",
        "com/sencha/gxt/theme/blue/client/menu/BlueItem.gss",
        "com/sencha/gxt/theme/base/client/menu/MenuItem.gss", //
        "com/sencha/gxt/theme/blue/client/menu/BlueMenuItem.gss", //
        "StartItem.gss",
        "StartHeadingMenuItem.gss"})
    StartHeadingMenuItemStyle style();

    ImageResource itemOver();

  }

  public interface StartHeadingMenuItemStyle extends BlueMenuItemStyle {
  }

  public StartHeadingMenuItemAppearance() {
    this(GWT.<StartHeadingMenuItemResources> create(StartHeadingMenuItemResources.class),
        GWT.<MenuItemTemplate> create(MenuItemTemplate.class));
  }

  public StartHeadingMenuItemAppearance(StartHeadingMenuItemResources resources, MenuItemTemplate template) {
    super(resources, template);
  }

}
