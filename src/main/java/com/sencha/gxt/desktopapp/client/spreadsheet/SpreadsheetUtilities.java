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

public class SpreadsheetUtilities {

  public static String getCellName(int rowIndex, int columnIndex) {
    return getCellName(rowIndex, getColumnName(columnIndex));
  }

  public static String getCellName(int rowIndex, String columnName) {
    return getCellName(Integer.toString(rowIndex + 1), columnName);
  }

  public static String getCellName(String rowName, int columnIndex) {
    return getCellName(rowName, getColumnName(columnIndex));
  }

  public static String getCellName(String rowName, String columnName) {
    return columnName + rowName;
  }

  public static int getColumnIndex(String columnName) {
    int columnIndex = -1;
    int length = columnName.length();
    for (int i = 0; i < length; i++) {
      char c = columnName.charAt(i);
      if ('a' <= c && c <= 'z') {
        c -= 'a' - 'A';
      }
      columnIndex = ((columnIndex + 1) * 26) + c - 'A';
    }
    return columnIndex;
  }

  public static String getColumnName(int columnIndex) {
    boolean moreDigits = true;
    StringBuilder s = new StringBuilder();
    while (moreDigits) {
      int remainder = columnIndex % 26;
      columnIndex /= 26;
      char c = (char) ('A' + remainder);
      s.insert(0, c);
      moreDigits = columnIndex > 0;
      columnIndex--;
    }
    return s.toString();
  }

}
