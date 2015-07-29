package com.sencha.gxt.desktopapp.client.spreadsheet;

import com.google.gwt.user.client.ui.HasWidgets;
import com.sencha.gxt.desktopapp.client.persistence.FileModel;

public interface SpreadsheetChartPresenter {

  void configure(FileModel fileModel, Worksheet worksheet);

  void go(HasWidgets hasWidgets);

  void updateTitle();

}
