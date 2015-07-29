package com.sencha.gxt.desktop.client.theme.base.taskbutton;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.sencha.gxt.desktop.client.widget.TaskButtonCell.TaskButtonCellAppearance;
import com.sencha.gxt.theme.base.client.button.ButtonCellDefaultAppearance;
import com.sencha.gxt.theme.base.client.frame.TableFrame;
import com.sencha.gxt.theme.base.client.frame.TableFrame.TableFrameResources;

public class TaskButtonCellDefaultAppearance<C> extends ButtonCellDefaultAppearance<C> implements
    TaskButtonCellAppearance<C> {

  public interface TaskButtonCellResources extends ButtonCellResources, ClientBundle {

    @Source({"com/sencha/gxt/theme/base/client/button/ButtonCell.gss", "TaskButtonCell.gss"})
    @Override
    TaskButtonCellStyle style();
  }

  public interface TaskButtonCellStyle extends ButtonCellStyle {
  }

  public TaskButtonCellDefaultAppearance() {
    super(GWT.<ButtonCellResources> create(TaskButtonCellResources.class),
        GWT.<ButtonCellTemplates> create(ButtonCellTemplates.class), new TableFrame(
            GWT.<TableFrameResources> create(TaskButtonTableFrameResources.class)));
  }

}
