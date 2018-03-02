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
package com.sencha.gxt.desktopapp.client.wordprocessor;

import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.desktopapp.client.filemanager.images.Images;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.event.ShowEvent;
import com.sencha.gxt.widget.core.client.event.ShowEvent.ShowHandler;
import com.sencha.gxt.widget.core.client.form.HtmlEditor;

public class WordProcessorViewImpl implements WordProcessorView, HideHandler, ShowHandler {

  private WordProcessorPresenter wordProcessorPresenter;

  private Window window;
  private HtmlEditor htmlEditor;

  public WordProcessorViewImpl(WordProcessorPresenter wordProcessorPresenter) {
    this.wordProcessorPresenter = wordProcessorPresenter;
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
    return getHtmlEditor().getValue();
  }

  @Override
  public void onHide(HideEvent event) {
    getWordProcessorPresenter().unbind();
  }

  @Override
  public void onShow(ShowEvent event) {
    getWordProcessorPresenter().bind();
    getHtmlEditor().focus();
  }

  @Override
  public void setTitle(String title) {
    getWindow().setHeading(title);
  }

  @Override
  public void setValue(String value) {
    getHtmlEditor().setValue(value);
  }

  private HtmlEditor getHtmlEditor() {
    if (htmlEditor == null) {
      htmlEditor = new HtmlEditor() {
        {
          textArea.addKeyDownHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
              getWordProcessorPresenter().onContentUpdate();
            }
          });
        }
      };
    }
    return htmlEditor;
  }

  private Window getWindow() {
    if (window == null) {
      window = new Window() {
        {
          removeFromParentOnHide = false;
        }
      };
      window.getHeader().setIcon(Images.getImageResources().page_white());
      window.setMinimizable(true);
      window.setMaximizable(true);
      window.setPixelSize(500, 400);
      window.setOnEsc(false);
      window.addHideHandler(this);
      window.addShowHandler(this);
      window.setWidget(getHtmlEditor());
    }
    return window;
  }

  private WordProcessorPresenter getWordProcessorPresenter() {
    return wordProcessorPresenter;
  }

}
