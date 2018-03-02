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
