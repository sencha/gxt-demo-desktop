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
package com.sencha.gxt.desktopapp.client.utility;

import com.google.gwt.core.client.GWT;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent.DialogHideHandler;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;

public class PromptImpl extends Prompt {

  /**
   * Use a GWT.create() here to make it simple to hijack the default implementation.
   */
  public static final Prompt INSTANCE = GWT.create(PromptImpl.class);

  @Override
  public void alert(String title, String text) {
    alert(title, text, null);
  }

  @Override
  public void alert(String title, String text, final Runnable runnable) {
    AlertMessageBox alertMessageBox = new AlertMessageBox(title, text);
    alertMessageBox.addHideHandler(new HideHandler() {
      @Override
      public void onHide(HideEvent event) {
        if (runnable != null) {
          runnable.run();
        }
      }
    });
    alertMessageBox.setWidth(300);
    alertMessageBox.show();
  }

  @Override
  public void confirm(String title, String text, final Runnable yesRunnable) {
    confirm(title, text, yesRunnable, null);
  }

  @Override
  public void confirm(String title, String text, final Runnable yesRunnable, final Runnable noRunnable) {
    ConfirmMessageBox confirmMessageBox = new ConfirmMessageBox(title, text);
    confirmMessageBox.addDialogHideHandler(new DialogHideHandler() {
      @Override
      public void onDialogHide(DialogHideEvent event) {
        if (event.getHideButton() == PredefinedButton.YES) {
          if (yesRunnable != null) {
            yesRunnable.run();
          }
        } else {
          if (noRunnable != null) {
            noRunnable.run();
          }
        }
      }
    });
    confirmMessageBox.setWidth(300);
    confirmMessageBox.show();
  }

}
