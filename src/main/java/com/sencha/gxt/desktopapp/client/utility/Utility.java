package com.sencha.gxt.desktopapp.client.utility;

import java.util.List;

import com.google.gwt.user.client.ui.HasWidgets;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FormPanel.LabelAlign;
import com.sencha.gxt.widget.core.client.form.FormPanelHelper;

public class Utility {

  public static void alignLabels(LabelAlign labelAlign, HasWidgets panel) {
    List<FieldLabel> labels = FormPanelHelper.getFieldLabels(panel);
    for (FieldLabel label : labels) {
      label.setLabelAlign(labelAlign);
    }
  }

  public static String capitalize(String value) {
    int length = value.length();
    StringBuilder s = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      char c = value.charAt(i);
      if (i == 0) {
        c = Character.toUpperCase(c);
      } else {
        c = Character.toLowerCase(c);
      }
      s.append(c);
    }
    return s.toString();
  }

}
