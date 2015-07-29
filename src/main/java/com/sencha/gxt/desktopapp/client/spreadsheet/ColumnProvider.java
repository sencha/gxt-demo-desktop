package com.sencha.gxt.desktopapp.client.spreadsheet;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;

public interface ColumnProvider {

  ColumnConfig<Row, Object> getColumn(int cellIndex);

  void setColumnHeader(int columnIndex, SafeHtml fromString);

}
