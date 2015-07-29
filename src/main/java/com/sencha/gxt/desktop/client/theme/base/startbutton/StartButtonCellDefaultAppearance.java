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
