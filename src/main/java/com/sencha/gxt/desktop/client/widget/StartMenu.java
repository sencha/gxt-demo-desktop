package com.sencha.gxt.desktop.client.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.ui.RootPanel;
import com.sencha.gxt.core.client.Style.AnchorAlignment;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.dom.XDOM;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.BaseEventPreview;
import com.sencha.gxt.desktop.client.theme.base.startmenu.StartMenuDefaultAppearance;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.ComponentHelper;
import com.sencha.gxt.widget.core.client.event.BeforeHideEvent;
import com.sencha.gxt.widget.core.client.event.BeforeShowEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.ShowEvent;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;

/**
 * Provides a desktop start menu. A desktop start menu is a component with a
 * heading, a main menu and a tool menu. The heading is typically displayed at
 * the top of the start menu, and the main menu and tool menu are displayed side
 * by side below the heading.
 * <p/>
 * For more information on the use of the appearance pattern, see <a
 * href='http://www.sencha.com/blog/ext-gwt-3-appearance-design'>Sencha GXT 3.0
 * Appearance Design</a>
 * 
 * @see StartMainMenu
 * @see StartToolMenu
 * @see StartHeadingMenuItem
 */
public class StartMenu extends Component {

  /**
   * Defines the appearance interface for a start menu.
   * <p/>
   * The appearance interface defines the interaction between the widget and an
   * appearance instance. The concrete implementation of the appearance
   * interface typically incorporates the external HTML and CSS source using the
   * {@link XTemplates} and {@link CssResource} interfaces.
   */
  public interface Appearance {

    public void setHeading(XElement parent, MenuItem heading);

    public void setMainMenu(XElement parent, Menu mainMenu);

    public void setToolMenu(XElement parent, Menu toolMenu);

    void render(SafeHtmlBuilder builder);

  }

  private StartHeadingMenuItem heading;
  private StartMainMenu mainMenu;
  private StartToolMenu toolMenu;
  private BaseEventPreview eventPreview;
  private boolean showing;

  /**
   * Creates a start menu with the default appearance.
   */
  public StartMenu() {
    this(GWT.<StartMenuDefaultAppearance> create(StartMenuDefaultAppearance.class));
  }

  /**
   * Creates a start menu with the specified appearance.
   * 
   * @param appearance the start menu appearance
   */
  public StartMenu(StartMenuDefaultAppearance appearance) {

    SafeHtmlBuilder builder = new SafeHtmlBuilder();
    appearance.render(builder);

    XElement element = XDOM.create(builder.toSafeHtml());
    setElement((Element)element);

    appearance.setHeading(element, getHeading());
    appearance.setMainMenu(element, getMainMenu());
    appearance.setToolMenu(element, getToolMenu());

    sinkEvents(Event.MOUSEEVENTS | Event.ONCLICK);
  }

  /**
   * Adds an item to the "main menu" area of the start menu. The main menu
   * typically appears on the left side of the start menu and includes items for
   * desktop applications.
   * 
   * @param menuItem the item to add to the main menu area of the start menu
   */
  public void add(StartMainMenuItem menuItem) {
    getMainMenu().add(menuItem);
  }

  /**
   * Adds an item to the "tool menu" area of the start menu. The tool menu
   * typically appears on the right side of the start menu and includes items
   * that manage the state of the desktop.
   * 
   * @param startToolMenuItem the item to add to the tool menu area of the start
   *          menu
   */
  public void addTool(StartToolMenuItem startToolMenuItem) {
    getToolMenu().add(startToolMenuItem);
  }

  /**
   * Adds a separator to the "tool menu" area of the start menu.
   */
  public void addToolSeparator() {
    // TODO: Provide implementation
  }

  /**
   * Hides the start menu.
   */
  public void hide() {
    if (showing) {
      if (fireCancellableEvent(new BeforeHideEvent())) {
        showing = false;
        onHide();
        RootPanel.get().remove(this);
        eventPreview.remove();
        fireEvent(new HideEvent());
      }
    }
  }

  /**
   * Sets the text to be displayed in the start menu heading. The heading
   * typically appears above the main menu and tool menu.
   * 
   * @param text the start menu heading text
   */
  public void setHeading(String text) {
    getHeading().setText(text);
  }

  /**
   * Sets the icon to be displayed in the start menu heading. The heading
   * typically appears above main menu and tool menu.
   * 
   * @param icon the start menu heading icon
   */
  public void setIcon(ImageResource icon) {
    getHeading().setIcon(icon);
  }

  /**
   * Displays this menu relative to another element.
   * 
   * @param elem the element to align to
   * @param alignment the {@link XElement#alignTo} anchor position to use in
   *          aligning to the element (defaults to defaultAlign)
   * @param offsetX X offset
   * @param offsetY Y offset
   */
  public void show(Element elem, AnchorAlignment alignment, int offsetX, int offsetY) {
    if (fireCancellableEvent(new BeforeShowEvent())) {
      showing = true;
      RootPanel.get().add(this);
      getElement().makePositionable(true);
      onShow();
      getElement().updateZIndex(0);
      doAutoSize();
      getElement().alignTo(elem, alignment, offsetX, offsetY);
      // TODO: Add support for scrolling as in Menu.show
      getElement().show();
      getEventPreview().add();
      focus();
      fireEvent(new ShowEvent());
    }
  }

  @Override
  protected void doAttachChildren() {
    super.doAttachChildren();
    ComponentHelper.doAttach(heading);
    ComponentHelper.doAttach(mainMenu);
    ComponentHelper.doAttach(toolMenu);
  }

  protected void doAutoSize() {
    String w = getElement().getStyle().getWidth();
    if (showing && "".equals(w)) {
      // TODO: get main menu container from appearance as in Menu.doAutoSize
      getMainMenu().getElement().setWidth(195, true);
    }
  }

  @Override
  protected void doDetachChildren() {
    super.doDetachChildren();
    ComponentHelper.doDetach(heading);
    ComponentHelper.doDetach(mainMenu);
    ComponentHelper.doDetach(toolMenu);
  }

  private BaseEventPreview getEventPreview() {
    if (eventPreview == null) {
      eventPreview = new BaseEventPreview() {

        protected boolean onAutoHide(NativePreviewEvent pe) {
          // TODO: Add support for sub-menus as in Menu.onAutoHide
          hide();
          return true;
        }

        @Override
        protected void onPreviewKeyPress(NativePreviewEvent pe) {
          if (pe.getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            hide();
          }
        }

      };
      eventPreview.getIgnoreList().add(getMainMenu().getElement());
      eventPreview.getIgnoreList().add(getToolMenu().getElement());
    }
    return eventPreview;
  }

  private StartHeadingMenuItem getHeading() {
    if (heading == null) {
      heading = new StartHeadingMenuItem();
    }
    return heading;
  }

  private StartMainMenu getMainMenu() {
    if (mainMenu == null) {
      mainMenu = new StartMainMenu(this);
    }
    return mainMenu;
  }

  private StartToolMenu getToolMenu() {
    if (toolMenu == null) {
      toolMenu = new StartToolMenu(this);
    }
    return toolMenu;
  }
}
