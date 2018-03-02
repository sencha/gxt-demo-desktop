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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.event.StoreAddEvent;
import com.sencha.gxt.data.shared.event.StoreAddEvent.StoreAddHandler;
import com.sencha.gxt.data.shared.event.StoreRemoveEvent;
import com.sencha.gxt.data.shared.event.StoreRemoveEvent.StoreRemoveHandler;
import com.sencha.gxt.desktopapp.client.spreadsheet.Evaluator.Location;
import com.sencha.gxt.desktopapp.client.spreadsheet.Evaluator.Operation;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.RowNumberer;
import com.sencha.gxt.widget.core.client.selection.CellSelection;

/**
 * Implementation Notes:
 * <ul>
 * <li>References to <code>rowIndex</code> and <code>columnIndex</code> are
 * 0-based.</li>
 * <li>References to <code>cell</code> columns include the RowNumberer column
 * and are 1-based.</li>
 * <li>The <code>ColumnProvider</code> is 1-based.</li>
 * <li>Row names are 1-based.</li>
 * </ul>
 */
public class Worksheet implements TableValueProvider {

  private static final int DEFAULT_ROW_COUNT = 2;
  private static final int DEFAULT_COLUMN_COUNT = 2;

  private TableFactory tableFactory;
  private ListStore<Row> listStore;
  private ColumnProvider columnProvider;
  private ModelKeyProvider<Row> modelKeyProvider;
  private int nextRowId;
  protected boolean isSuppressAddEvent;

  public Worksheet(ColumnProvider columnProvider) {
    this.columnProvider = columnProvider;
  }

  public void changeColumnCount(int newColumnCount) {
    List<Row> rows = getListStore().getAll();
    int rowCount = rows.size();
    for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
      List<String> columns = rows.get(rowIndex).getColumns();
      if (newColumnCount < columns.size()) {
        rows.get(rowIndex).setColumns(new ArrayList<String>(columns.subList(0, newColumnCount)));
      } else {
        createColumns(columns, newColumnCount);
      }
    }
  }

  public void changeRowCount(int newRowCount) {
    if (newRowCount < getRowCount()) {
      List<Row> rows = getListStore().getAll();
      getListStore().replaceAll(new ArrayList<Row>(rows.subList(0, newRowCount)));
    } else {
      createRows(newRowCount, getColumnCount());
    }
  }

  public void deleteColumn(int deleteColumnIndex) {
    List<Row> rows = getListStore().getAll();
    int rowCount = rows.size();
    for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
      List<String> oldColumns = rows.get(rowIndex).getColumns();
      ArrayList<String> newColumns = new ArrayList<String>();
      int columnIndex = 0;
      for (String value : oldColumns) {
        if (columnIndex++ != deleteColumnIndex) {
          newColumns.add(value);
        }
      }
      rows.get(rowIndex).setColumns(newColumns);
    }
    adjustCellReferences(Operation.DELETE, Location.COLUMN, deleteColumnIndex);
  }

  public void deleteRow(int deleteRowIndex) {
    getListStore().remove(deleteRowIndex);
  }

  public double evaluate(String expression) {
    Evaluator evaluator = new Evaluator(expression, this);
    return evaluateWithCatch(evaluator);
  }

  public double evaluate(String expression, int rowIndex, int columnIndex) {
    Evaluator evaluator = new Evaluator(expression, this);
    evaluator.setRowIndex(rowIndex);
    evaluator.setColumnIndex(columnIndex);
    return evaluateWithCatch(evaluator);
  }

  public double evaluateCell(String expression, int rowIndex, int cell) {
    return evaluate(expression, rowIndex, cell - 1);
  }

  public int getCellCount() {
    return getColumnCount() + 1;
  }

  public int getColumnCount() {
    return getRowCount() == 0 ? 0 : getListStore().get(0).getColumns().size();
  }

  public ListStore<Row> getListStore() {
    if (listStore == null) {
      listStore = new ListStore<Row>(getModelKeyProvider());
      listStore.setAutoCommit(true);
      listStore.addStoreAddHandler(new StoreAddHandler<Row>() {
        @Override
        public void onAdd(StoreAddEvent<Row> event) {
          if (!isSuppressAddEvent) {
            int index = event.getIndex();
            adjustCellReferences(Operation.INSERT, Location.ROW, index);
            adjustCellReferences(Operation.RESTORE, Location.ROW, index, listStore.get(index));
          }
        }
      });
      listStore.addStoreRemoveHandler(new StoreRemoveHandler<Row>() {
        @Override
        public void onRemove(StoreRemoveEvent<Row> event) {
          adjustCellReferences(Operation.SAVE, Location.ROW, event.getIndex(), event.getItem());
          adjustCellReferences(Operation.DELETE, Location.ROW, event.getIndex(), event.getItem());
          adjustCellReferences(Operation.DELETE, Location.ROW, event.getIndex());
        }
      });
    }
    return listStore;
  }

  public String getName(CellSelection<Row> cellSelection) {
    String name = null;
    ColumnConfig<Row, Object> columnConfig = getColumnProvider().getColumn(cellSelection.getCell());
    if (!(columnConfig instanceof RowNumberer)) {
      name = columnConfig.getHeader().asString() + (cellSelection.getRow() + 1);
    }
    return name;
  }

  public int getRowCount() {
    return getListStore().size();
  }

  public String getValue() {
    nextRowId = 0;
    List<Row> rows = new LinkedList<Row>();

    int rowCount = getRowCount();
    int columnCount = getColumnCount();

    for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
      List<String> columns = new LinkedList<String>();
      for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
        columns.add(getValue(rowIndex, columnIndex));
      }
      Row row = getTableFactory().row().as();
      row.setId(nextRowId++);
      row.setColumns(columns);
      rows.add(row);
    }

    Table table = getTableFactory().table().as();
    table.setNextRowId(nextRowId);
    table.setRows(rows);

    AutoBean<Table> tableAutoBean = AutoBeanUtils.getAutoBean(table);
    String value = AutoBeanCodex.encode(tableAutoBean).getPayload();
    return value;
  }

  public String getValue(CellSelection<Row> cellSelection) {
    return getValue(cellSelection.getRow(), cellSelection.getCell() - 1);
  }

  public String getValue(int rowIndex, int columnIndex) {
    Row row = getListStore().get(rowIndex);
    ColumnConfig<Row, Object> columnConfig = getColumnProvider().getColumn(columnIndex + 1);
    Object value = columnConfig.getValueProvider().getValue(row);
    return value == null ? "" : value.toString();
  }

  public void insertColumn(int insertColumnIndex) {
    int rowCount = getRowCount();
    int columnCount = getColumnCount() + 1;
    List<Row> rows = getListStore().getAll();
    for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
      List<String> oldColumns = rows.get(rowIndex).getColumns();
      ArrayList<String> newColumns = new ArrayList<String>();
      Iterator<String> iterator = oldColumns.iterator();
      for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
        if (columnIndex == insertColumnIndex) {
          newColumns.add("");
        } else {
          newColumns.add(iterator.next());
        }
      }
      rows.get(rowIndex).setColumns(newColumns);
    }
    adjustCellReferences(Operation.INSERT, Location.COLUMN, insertColumnIndex);
  }

  public void insertRow(int insertRowIndex) {
    Row newRow = getTableFactory().row().as();
    ArrayList<String> columns = new ArrayList<String>();
    createColumns(columns, getColumnCount());
    newRow.setColumns(columns);
    newRow.setId(getNextRowId());
    getListStore().add(insertRowIndex, newRow);
  }

  public void renameColumns(int newCellIndex) {
    int newColumnIndex = newCellIndex - 1;
    String oldColumnName = getColumnProvider().getColumn(newCellIndex).getHeader().asString();
    int oldColumnIndex = SpreadsheetUtilities.getColumnIndex(oldColumnName);
    int rowCount = getRowCount();
    for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
      Row row = getListStore().get(rowIndex);
      adjustCellReferences(Operation.SAVE, Location.COLUMN, oldColumnIndex, row);
    }
    adjustCellReferences(Operation.DELETE, Location.COLUMN, oldColumnIndex);
    adjustCellReferences(Operation.INSERT, Location.COLUMN, newColumnIndex);
    for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
      Row row = getListStore().get(rowIndex);
      adjustCellReferences(Operation.RESTORE, Location.COLUMN, newColumnIndex, row);
    }
    int columnCount = getColumnCount();
    for (int columnIndex = Math.min(oldColumnIndex, newColumnIndex); columnIndex < columnCount; columnIndex++) {
      String columnName = SpreadsheetUtilities.getColumnName(columnIndex);
      getColumnProvider().setColumnHeader(columnIndex + 1, SafeHtmlUtils.fromString(columnName));
    }
  }

  public void setValue(String value) {
    try {
      isSuppressAddEvent = true;
      listStore = null;
      if (value == null || value.isEmpty()) {
        nextRowId = 0;
        createRows(DEFAULT_ROW_COUNT, DEFAULT_COLUMN_COUNT);
      } else {
        Table table = AutoBeanCodex.decode(getTableFactory(), Table.class, value).as();
        nextRowId = table.getNextRowId();
        List<Row> rows = table.getRows();
        getListStore().addAll(rows);
      }
    } finally {
      isSuppressAddEvent = false;
    }
  }

  public void setValue(String value, CellSelection<Row> cellSelection) {
    Row row = getListStore().get(cellSelection.getRow());
    ColumnConfig<Row, Object> columnConfig = getColumnProvider().getColumn(cellSelection.getCell());
    columnConfig.getValueProvider().setValue(row, value);
  }

  protected void adjustCellReferences(Operation operation, Location location, int index, Row row) {
    int columnCount = getColumnCount();
    ListIterator<String> iterator = row.getColumns().listIterator();
    for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
      String value = iterator.next();
      if (value != null && value.startsWith(Evaluator.EXPRESSION_MARKER)) {
        Evaluator evaluator = new Evaluator(value, this);
        value = evaluator.adjustCellReferences(operation, location, value, index);
        iterator.set(value);
      }
    }
  }

  protected double evaluateWithCatch(Evaluator evaluator) {
    double value;
    try {
      value = evaluator.evaluateExpression();
    } catch (RuntimeException e) {
      e.printStackTrace();
      new AlertMessageBox("Expression", e.toString()).show();
      value = Double.NaN;
    }
    return value;
  }

  private void adjustCellReferences(Operation operation, Location location, int index) {
    int rowCount = getRowCount();
    for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
      Row row = getListStore().get(rowIndex);
      adjustCellReferences(operation, location, index, row);
    }
  }

  private void createColumns(List<String> columns, int newColumnCount) {
    while (columns.size() < newColumnCount) {
      columns.add("");
    }
  }

  private void createRows(int newRowCount, int newColumnCount) {
    while (getRowCount() < newRowCount) {
      ArrayList<String> columns = new ArrayList<String>();
      createColumns(columns, newColumnCount);
      Row row = getTableFactory().row().as();
      row.setColumns(columns);
      row.setId(getNextRowId());
      getListStore().add(row);
    }
  }

  private ColumnProvider getColumnProvider() {
    return columnProvider;
  }

  private ModelKeyProvider<Row> getModelKeyProvider() {
    if (modelKeyProvider == null) {
      modelKeyProvider = new ModelKeyProvider<Row>() {
        @Override
        public String getKey(Row row) {
          return Integer.toString(row.getId());
        }
      };
    }
    return modelKeyProvider;
  }

  private int getNextRowId() {
    return nextRowId++;
  }

  private TableFactory getTableFactory() {
    if (tableFactory == null) {
      tableFactory = GWT.create(TableFactory.class);
    }
    return tableFactory;
  }

}
