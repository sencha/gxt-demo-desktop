package com.sencha.gxt.desktopapp.client.spreadsheet;

import com.sencha.gxt.desktopapp.client.FileBasedMiniAppView;

public interface SpreadsheetView extends FileBasedMiniAppView {

  int getSelectedColumn();

  int getSelectedRow();

  void refresh();

  void setValue(Worksheet worksheet);

  void updateDetails(String cellName, String cellValue);

  void updateGrid();

  void updateSelectedCells(String value);

}