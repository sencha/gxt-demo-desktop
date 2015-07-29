package com.sencha.gxt.desktopapp.client.spreadsheet;

import java.util.List;

public interface Row {

  List<String> getColumns();

  int getId();

  void setColumns(List<String> columns);

  void setId(int id);
}
