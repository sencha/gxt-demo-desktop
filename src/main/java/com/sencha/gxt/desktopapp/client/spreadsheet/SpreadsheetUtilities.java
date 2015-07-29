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
