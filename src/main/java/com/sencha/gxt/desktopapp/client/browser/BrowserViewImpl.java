package com.sencha.gxt.desktopapp.client.browser;

import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.desktopapp.client.filemanager.images.Images;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.event.ShowEvent;
import com.sencha.gxt.widget.core.client.event.ShowEvent.ShowHandler;

public class BrowserViewImpl implements BrowserView, HideHandler, ShowHandler {

  private BrowserPresenter browserPresenter;
  private Window window;
  private Frame frame;

  public BrowserViewImpl(BrowserPresenter browserPresenter) {
    this.browserPresenter = browserPresenter;
  }

  @Override
  public Widget asWidget() {
    return getWindow();
  }

  @Override
  public void close() {
    getWindow().hide();
  }

  @Override
  public String getValue() {
    return getFrame().getUrl();
  }

  @Override
  public void onHide(HideEvent event) {
    getBrowserPresenter().unbind();
  }

  @Override
  public void onShow(ShowEvent event) {
    getBrowserPresenter().bind();
  }

  @Override
  public void setTitle(String title) {
    getWindow().setHeading(title);
  }

  @Override
  public void setValue(String url) {
    getFrame().setUrl(url);
  }

  private BrowserPresenter getBrowserPresenter() {
    return browserPresenter;
  }

  private Frame getFrame() {
    if (frame == null) {
      frame = new Frame();
    }
    return frame;
  }

  private Window getWindow() {
    if (window == null) {
      window = new Window();
      window.getHeader().setIcon(Images.getImageResources().page_white());
      window.setMinimizable(true);
      window.setMaximizable(true);
      window.setOnEsc(false);
      window.setPixelSize(500, 400);
      window.addHideHandler(this);
      window.addShowHandler(this);
      window.setWidget(getFrame());
    }
    return window;
  }

}
