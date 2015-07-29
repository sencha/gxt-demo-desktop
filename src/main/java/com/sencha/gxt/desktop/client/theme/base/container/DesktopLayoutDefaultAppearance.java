package com.sencha.gxt.desktop.client.theme.base.container;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.resources.StyleInjectorHelper;
import com.sencha.gxt.desktop.client.widget.DesktopLayoutContainer.DesktopLayoutContainerAppearance;

public class DesktopLayoutDefaultAppearance implements DesktopLayoutContainerAppearance {

  public interface DesktopLayoutBaseResources extends ClientBundle {

    @ImageOptions(repeatStyle = RepeatStyle.None)
    ImageResource desktop();

    @Source("DesktopLayout.gss")
    DesktopLayoutStyle style();

  }
  
  public interface DesktopLayoutStyle extends CssResource {

    String container();

    String inner();

  }

  public interface DesktopLayoutTemplate extends XTemplates {

    @XTemplate("<div class=\"{style.container}\"><div class=\"{style.inner}\"></div></div>")
    SafeHtml template(DesktopLayoutStyle style);

  }

  private final DesktopLayoutBaseResources resources;
  private final DesktopLayoutTemplate template;

  public DesktopLayoutDefaultAppearance() {
    this(GWT.<DesktopLayoutBaseResources> create(DesktopLayoutBaseResources.class),
        GWT.<DesktopLayoutTemplate> create(DesktopLayoutTemplate.class));
  }

  public DesktopLayoutDefaultAppearance(DesktopLayoutBaseResources resources, DesktopLayoutTemplate template) {
    this.resources = resources;
    this.template = template;

    StyleInjectorHelper.ensureInjected(resources.style(), true);
  }

  @Override
  public XElement getShortcutArea(XElement parent) {
    return parent.getFirstChildElement().cast();
  }

  @Override
  public void render(SafeHtmlBuilder sb) {
    sb.append(template.template(resources.style()));
  }

}
