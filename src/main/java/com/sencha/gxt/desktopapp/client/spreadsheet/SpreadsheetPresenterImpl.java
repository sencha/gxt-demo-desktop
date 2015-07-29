package com.sencha.gxt.desktopapp.client.spreadsheet;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.sencha.gxt.desktopapp.client.DesktopBus;
import com.sencha.gxt.desktopapp.client.FileBasedMiniAppPresenterImpl;
import com.sencha.gxt.desktopapp.client.persistence.FileModel;
import com.sencha.gxt.desktopapp.client.persistence.FileSystem;

public class SpreadsheetPresenterImpl extends FileBasedMiniAppPresenterImpl implements SpreadsheetPresenter {

  private SpreadsheetChartPresenter spreadsheetChartPresenter;
  private Worksheet worksheet;

  public SpreadsheetPresenterImpl(DesktopBus desktopBus, FileSystem fileSystem, FileModel fileModel) {
    super(desktopBus, fileSystem, fileModel);
  }

  @Override
  public void onCellValueChange(int rowIndex, int columnIndex, String cellValue) {
    String cellName = SpreadsheetUtilities.getCellName(rowIndex, columnIndex);
    getSpreadsheetView().updateDetails(cellName, cellValue);

    // Give the change a chance to propagate to model and store
    Scheduler.get().scheduleFixedDelay(new RepeatingCommand() {
      @Override
      public boolean execute() {
        onSave();
        getSpreadsheetView().refresh();
        return false;
      }
    }, 250);

  }

  @Override
  public void onColumnCountChange(int newColumnCount) {
    getWorksheet().changeColumnCount(newColumnCount);
    getSpreadsheetView().updateGrid();
    onSave();
    updateChart();
  }

  @Override
  public void onColumnMove() {
    onSave();
  }

  @Override
  public void onDeleteColumn() {
    int deleteColumnIndex = getSpreadsheetView().getSelectedColumn();
    if (deleteColumnIndex != -1) {
      getWorksheet().deleteColumn(deleteColumnIndex);
      getSpreadsheetView().updateGrid();
      onSave();
      updateChart();
    }
  }

  @Override
  public void onDeleteRow() {
    int deleteRowIndex = getSpreadsheetView().getSelectedRow();
    if (deleteRowIndex != -1) {
      getWorksheet().deleteRow(deleteRowIndex);
      getSpreadsheetView().updateGrid();
      onSave();
    }
  }

  @Override
  public void onDetailValueChange(String value) {
    getSpreadsheetView().updateSelectedCells(value);
    onSave();
  }

  @Override
  public void onDragDrop() {
    onSave();
  }

  @Override
  public void onInsertAbove() {
    insertRow(0);
  }

  @Override
  public void onInsertBelow() {
    insertRow(1);
  }

  @Override
  public void onInsertLeft() {
    insertColumn(0);
  }

  @Override
  public void onInsertRight() {
    insertColumn(1);
  }

  @Override
  public void onOpenChart() {
    getSpreadsheetChartPresenter().go(null); // before configure
    getSpreadsheetChartPresenter().configure(getFileModel(), getWorksheet());
    getSpreadsheetChartPresenter().updateTitle();
  }

  @Override
  public void onRowCountChange(int newRowCount) {
    getWorksheet().changeRowCount(newRowCount);
    onSave();
  }

  @Override
  protected SpreadsheetView createFileBasedMiniAppView() {
    return new SpreadsheetViewImpl(this);
  }

  @Override
  protected String getTitle() {
    return "Spreadsheet - " + super.getTitle();
  }

  @Override
  protected void setDisplayedContent(String value) {
    super.setDisplayedContent(value);
    getWorksheet().setValue(value);
    getSpreadsheetView().setValue(getWorksheet());
    updateChart();
  }

  protected void updateChart() {
    if (isSpreadsheetChartPresenter()) {
      getSpreadsheetChartPresenter().configure(getFileModel(), getWorksheet());
    }
  }

  @Override
  protected void updateTitle() {
    super.updateTitle();
    if (isSpreadsheetChartPresenter()) {
      getSpreadsheetChartPresenter().updateTitle();
    }
  }

  private SpreadsheetChartPresenter getSpreadsheetChartPresenter() {
    if (spreadsheetChartPresenter == null) {
      spreadsheetChartPresenter = new SpreadsheetChartPresenterImpl(getFileSystem());
    }
    return spreadsheetChartPresenter;
  }

  private SpreadsheetView getSpreadsheetView() {
    return (SpreadsheetView) super.getFileBasedMiniAppView();
  }

  private Worksheet getWorksheet() {
    if (worksheet == null) {
      worksheet = new Worksheet((ColumnProvider) getSpreadsheetView());
    }
    return worksheet;
  }

  private void insertColumn(int delta) {
    int insertColumnIndex = getSpreadsheetView().getSelectedColumn();
    if (insertColumnIndex != -1) {
      getWorksheet().insertColumn(insertColumnIndex + delta);
      getSpreadsheetView().updateGrid();
      onSave();
      updateChart();
    }
  }

  private void insertRow(int delta) {
    int insertRowIndex = getSpreadsheetView().getSelectedRow();
    if (insertRowIndex != -1) {
      getWorksheet().insertRow(insertRowIndex + delta);
      getSpreadsheetView().updateGrid();
      onSave();
    }
  }

  private boolean isSpreadsheetChartPresenter() {
    return spreadsheetChartPresenter != null;
  }

}
