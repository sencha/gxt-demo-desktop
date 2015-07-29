package com.sencha.gxt.desktopapp.client.spreadsheet;

import com.google.web.bindery.autobean.shared.AutoBean;

public class TableCategory {
  public static int incrementNextRowId(AutoBean<Table> instance) {
    Table table = instance.as();
    int nextRowId = table.getNextRowId();
    table.setNextRowId(nextRowId + 1);
    return nextRowId;
  }
}
