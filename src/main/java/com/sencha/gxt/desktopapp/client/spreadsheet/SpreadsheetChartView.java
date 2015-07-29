package com.sencha.gxt.desktopapp.client.spreadsheet;

import com.google.gwt.user.client.ui.IsWidget;

public interface SpreadsheetChartView extends IsWidget {

  void configure(Worksheet worksheet);

  void setTitle(String newTitle);

}
