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
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.resources.CommonStyles;
import com.sencha.gxt.core.client.resources.StyleInjectorHelper;
import com.sencha.gxt.desktop.client.widget.StartMenu;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;

public class StartMenuDefaultAppearance implements StartMenu.Appearance {

  public interface StartMenuResources extends ClientBundle {

    @Source({"StartMenu.gss"})
    StartMenuStyle style();

  }

  public interface StartMenuStyle extends CssResource {

    String body();

    String heading();

    String mainMenu();

    String startMenu();

    String toolMenu();
  }

  public interface StartMenuTemplate extends XTemplates {

    @XTemplate(source = "StartMenu.html")
    SafeHtml template(StartMenuStyle style, String ignoreClass);

  }

  @SuppressWarnings("unused")
  private StartMenuResources resources;
  private StartMenuStyle style;
  private StartMenuTemplate template;

  public StartMenuDefaultAppearance() {
    this(GWT.<StartMenuResources> create(StartMenuResources.class),
        GWT.<StartMenuTemplate> create(StartMenuTemplate.class));
  }

  public StartMenuDefaultAppearance(StartMenuResources resources, StartMenuTemplate template) {
    this.resources = resources;
    this.style = resources.style();
    this.template = template;

    StyleInjectorHelper.ensureInjected(this.style, true);
  }

  public void render(SafeHtmlBuilder result) {
    result.append(template.template((StartMenuStyle) style, CommonStyles.get().ignore()));
  }

  @Override
  public void setHeading(XElement parent, MenuItem heading) {
    XElement element = parent.selectNode("." + style.heading());
    element.removeChildren();
    element.appendChild(heading.getElement());
  }

  @Override
  public void setMainMenu(XElement parent, Menu mainMenu) {
    XElement element = parent.selectNode("." + style.mainMenu());
    element.removeChildren();
    element.appendChild(mainMenu.getElement());
  }

  @Override
  public void setToolMenu(XElement parent, Menu toolMenu) {
    XElement element = parent.selectNode("." + style.toolMenu());
    element.removeChildren();
    element.appendChild(toolMenu.getElement());
  }

}
