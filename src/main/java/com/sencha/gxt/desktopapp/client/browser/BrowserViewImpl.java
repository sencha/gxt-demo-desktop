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
