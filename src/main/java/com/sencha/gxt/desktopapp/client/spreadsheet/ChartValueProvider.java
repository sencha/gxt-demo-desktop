package com.sencha.gxt.desktopapp.client.spreadsheet;

import com.sencha.gxt.core.client.ValueProvider;

public class ChartValueProvider implements ValueProvider<Row, Number> {
  private int columnIndex;
  private Worksheet worksheet;

  public ChartValueProvider(Worksheet worksheet, int columnIndex) {
    this.worksheet = worksheet;
    this.columnIndex = columnIndex;
  }

  @Override
  public String getPath() {
    return worksheet.getValue(0, columnIndex);
  }

  @Override
  public Number getValue(Row row) {
    Number number;
    String value = row.getColumns().get(columnIndex);
    if (value.startsWith(Evaluator.EXPRESSION_MARKER)) {
      int rowIndex = worksheet.getListStore().indexOf(row);
      number = worksheet.evaluate(value, rowIndex, columnIndex);
    } else {
      try {
        number = Double.parseDouble(value);
      } catch (RuntimeException e) {
        number = 0;
      }
    }
    return number;
  }

  @Override
  public void setValue(Row row, Number value) {
    throw new UnsupportedOperationException();
  }
}