package com.sencha.gxt.desktop.client.theme.base.startmenu;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.theme.blue.client.menu.BlueMenuItemAppearance;

public class StartToolMenuItemAppearance extends BlueMenuItemAppearance {

  public interface StartToolMenuItemResources extends BlueMenuItemResources, ClientBundle {
    
    @Override
    @Source({"com/sencha/gxt/theme/base/client/menu/Item.gss",
        "com/sencha/gxt/theme/blue/client/menu/BlueItem.gss",
        "com/sencha/gxt/theme/base/client/menu/MenuItem.gss", //
        "com/sencha/gxt/theme/blue/client/menu/BlueMenuItem.gss", //
        "StartItem.gss",
        "StartToolMenuItem.gss"})
    StartToolMenuItemStyle style();

    ImageResource itemOver();

  }

  public interface StartToolMenuItemStyle extends BlueMenuItemStyle {
    
  }

  public StartToolMenuItemAppearance() {
    this(GWT.<StartToolMenuItemResources> create(StartToolMenuItemResources.class),
        GWT.<MenuItemTemplate> create(MenuItemTemplate.class));
  }

  public StartToolMenuItemAppearance(StartToolMenuItemResources resources, MenuItemTemplate template) {
    super(resources, template);
  }

}
