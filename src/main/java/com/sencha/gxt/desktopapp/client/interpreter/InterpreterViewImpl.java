package com.sencha.gxt.desktopapp.client.interpreter;

import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.desktopapp.client.filemanager.images.Images;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.event.ShowEvent;
import com.sencha.gxt.widget.core.client.event.ShowEvent.ShowHandler;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

public class InterpreterViewImpl implements InterpreterView, HideHandler, ShowHandler {

  private InterpreterPresenter interpreterPresenter;
  private Window window;
  private TextArea programTextArea;
  private HTML outputHtml;
  private BorderLayoutContainer borderLayoutContainer;
  private VerticalLayoutContainer centerContainer;
  private BorderLayoutData centerLayoutData;
  private BorderLayoutData southLayoutData;
  private ToolBar programToolBar;
  private TextButton runButton;
  private SelectHandler runSelectHandler;
  private VerticalLayoutContainer southContainer;
  private TextButton clearButton;
  private SelectHandler clearSelectHandler;

  public InterpreterViewImpl(InterpreterPresenter interpreterPresenter) {
    this.interpreterPresenter = interpreterPresenter;
  }

  @Override
  public Widget asWidget() {
    return getWindow();
  }

  public void clear() {
    getOutputHtml().setText("");
  }

  @Override
  public void close() {
    getWindow().hide();
  }

  @Override
  public String getValue() {
    return getProgramTextArea().getCurrentValue();
  }

  @Override
  public void onHide(HideEvent event) {
    getInterpreterPresenter().unbind();
  }

  @Override
  public void onShow(ShowEvent event) {
    getInterpreterPresenter().bind();
    getProgramTextArea().focus();
  }

  @Override
  public void print(String value) {
    String html = outputHtml.getHTML();
    if (html.isEmpty()) {
      html = value;
    } else {
      html = html + "<br/>" + value;
    }
    outputHtml.setHTML(html);
    outputHtml.getElement().setScrollTop(
        outputHtml.getElement().getScrollHeight() - outputHtml.getElement().getClientHeight());
  }

  @Override
  public void setTitle(String title) {
    getWindow().setHeadingText(title);
  }

  @Override
  public void setValue(String value) {
    getProgramTextArea().setValue(value);
  }

  private BorderLayoutContainer getBorderLayoutContainer() {
    if (borderLayoutContainer == null) {
      borderLayoutContainer = new BorderLayoutContainer();
      borderLayoutContainer.setBorders(true);
      borderLayoutContainer.setCenterWidget(getCenterContainer(), getCenterLayoutData());
      borderLayoutContainer.setSouthWidget(getSouthContainer(), getSouthLayoutData());
    }
    return borderLayoutContainer;
  }

  private VerticalLayoutContainer getCenterContainer() {
    if (centerContainer == null) {
      centerContainer = new VerticalLayoutContainer();
      centerContainer.add(getProgramToolBar(), new VerticalLayoutData(1, -1));
      centerContainer.add(getProgramTextArea(), new VerticalLayoutData(1, 1));
    }
    return centerContainer;
  }

  private BorderLayoutData getCenterLayoutData() {
    if (centerLayoutData == null) {
      centerLayoutData = new BorderLayoutData();
    }
    return centerLayoutData;
  }

  private TextButton getClearButton() {
    if (clearButton == null) {
      clearButton = new TextButton();
      clearButton.setToolTip("Clear Output");
      clearButton.setIcon(Images.getImageResources().bin_closed());
      clearButton.addSelectHandler(getClearSelectHandler());
    }
    return clearButton;
  }

  private SelectHandler getClearSelectHandler() {
    if (clearSelectHandler == null) {
      clearSelectHandler = new SelectHandler() {
        @Override
        public void onSelect(SelectEvent event) {
          clear();
        }
      };
    }
    return clearSelectHandler;
  }

  private InterpreterPresenter getInterpreterPresenter() {
    return interpreterPresenter;
  }

  private HTML getOutputHtml() {
    if (outputHtml == null) {
      outputHtml = new HTML();
      outputHtml.getElement().getStyle().setBackgroundColor("white");
      outputHtml.getElement().getStyle().setOverflow(Overflow.AUTO);
    }
    return outputHtml;
  }

  private TextArea getProgramTextArea() {
    if (programTextArea == null) {
      programTextArea = new TextArea();
      programTextArea.addKeyDownHandler(new KeyDownHandler() {
        @Override
        public void onKeyDown(KeyDownEvent event) {
          getInterpreterPresenter().onContentUpdate();
        }
      });
    }
    return programTextArea;
  }

  private ToolBar getProgramToolBar() {
    if (programToolBar == null) {
      programToolBar = new ToolBar();
      programToolBar.add(getRunButton());
      programToolBar.add(getClearButton());
    }
    return programToolBar;
  }

  private TextButton getRunButton() {
    if (runButton == null) {
      runButton = new TextButton();
      runButton.setToolTip("Run Program");
      runButton.setIcon(Images.getImageResources().bullet_go());
      runButton.addSelectHandler(getRunSelectHandler());
    }
    return runButton;
  }

  private SelectHandler getRunSelectHandler() {
    if (runSelectHandler == null) {
      runSelectHandler = new SelectHandler() {
        @Override
        public void onSelect(SelectEvent event) {
          getInterpreterPresenter().run();
        }
      };
    }
    return runSelectHandler;
  }

  private VerticalLayoutContainer getSouthContainer() {
    if (southContainer == null) {
      southContainer = new VerticalLayoutContainer();
      southContainer.setBorders(true);
      southContainer.add(getOutputHtml(), new VerticalLayoutData(1, 1));
    }
    return southContainer;
  }

  private BorderLayoutData getSouthLayoutData() {
    if (southLayoutData == null) {
      southLayoutData = new BorderLayoutData(0.25d);
      southLayoutData.setMargins(new Margins(5, 0, 0, 0));
      southLayoutData.setSplit(true);
    }
    return southLayoutData;
  }

  private Window getWindow() {
    if (window == null) {
      window = new Window();
      window.getHeader().setIcon(Images.getImageResources().script());
      window.setMinimizable(true);
      window.setMaximizable(true);
      window.setPixelSize(500, 400);
      window.setOnEsc(false);
      window.addHideHandler(this);
      window.addShowHandler(this);
      window.setWidget(getBorderLayoutContainer());
    }
    return window;
  }
}
