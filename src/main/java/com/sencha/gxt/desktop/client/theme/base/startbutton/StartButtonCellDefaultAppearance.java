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
package com.sencha.gxt.desktop.client.theme.base.startbutton;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.cell.core.client.ButtonCell;
import com.sencha.gxt.desktop.client.widget.StartButtonCell.StartButtonCellAppearance;
import com.sencha.gxt.theme.base.client.button.ButtonCellDefaultAppearance;
import com.sencha.gxt.theme.base.client.frame.TableFrame;
import com.sencha.gxt.theme.base.client.frame.TableFrame.TableFrameResources;

public class StartButtonCellDefaultAppearance<C> extends ButtonCellDefaultAppearance<C> implements
    StartButtonCellAppearance<C> {

  public interface StartButtonCellResources extends ButtonCellResources, ClientBundle {

    ImageResource startButtonIcon();

    @Source({"com/sencha/gxt/theme/base/client/button/ButtonCell.gss", "StartButtonCell.gss"})
    @Override
    StartButtonCellStyle style();
  }

  public interface StartButtonCellStyle extends ButtonCellStyle {
  }

  public StartButtonCellDefaultAppearance() {
    super(GWT.<ButtonCellResources> create(StartButtonCellResources.class),
        GWT.<ButtonCellTemplates> create(ButtonCellTemplates.class), new TableFrame(
            GWT.<TableFrameResources> create(StartButtonTableFrameResources.class)));
  }

  @Override
  public void render(final ButtonCell<C> cell, Context context, C value, SafeHtmlBuilder sb) {
    cell.setIcon(((StartButtonCellResources) resources).startButtonIcon());
    super.render(cell, context, value, sb);
  }

}
