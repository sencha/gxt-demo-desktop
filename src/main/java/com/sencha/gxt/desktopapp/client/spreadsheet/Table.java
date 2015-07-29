package com.sencha.gxt.desktopapp.client.spreadsheet;

import java.util.List;

public interface Table {

  int getNextRowId();

  List<Row> getRows();

  int incrementNextRowId();

  void setNextRowId(int nextRowId);

  void setRows(List<Row> rows);
}
