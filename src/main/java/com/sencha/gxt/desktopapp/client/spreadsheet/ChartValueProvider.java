/**
 * Sencha GXT 1.0.0-SNAPSHOT - Sencha for GWT
 * Copyright (c) 2006-2018, Sencha Inc.
 *
 * licensing@sencha.com
 * http://www.sencha.com/products/gxt/license/
 *
 * ================================================================================
 * Commercial License
 * ================================================================================
 * This version of Sencha GXT is licensed commercially and is the appropriate
 * option for the vast majority of use cases.
 *
 * Please see the Sencha GXT Licensing page at:
 * http://www.sencha.com/products/gxt/license/
 *
 * For clarification or additional options, please contact:
 * licensing@sencha.com
 * ================================================================================
 *
 *
 *
 *
 *
 *
 *
 *
 * ================================================================================
 * Disclaimer
 * ================================================================================
 * THIS SOFTWARE IS DISTRIBUTED "AS-IS" WITHOUT ANY WARRANTIES, CONDITIONS AND
 * REPRESENTATIONS WHETHER EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE
 * IMPLIED WARRANTIES AND CONDITIONS OF MERCHANTABILITY, MERCHANTABLE QUALITY,
 * FITNESS FOR A PARTICULAR PURPOSE, DURABILITY, NON-INFRINGEMENT, PERFORMANCE AND
 * THOSE ARISING BY STATUTE OR FROM CUSTOM OR USAGE OF TRADE OR COURSE OF DEALING.
 * ================================================================================
 */
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