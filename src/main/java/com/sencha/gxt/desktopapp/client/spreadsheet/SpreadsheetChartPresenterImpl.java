package com.sencha.gxt.desktopapp.client.spreadsheet;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.desktopapp.client.persistence.FileModel;
import com.sencha.gxt.desktopapp.client.persistence.FileSystem;
import com.sencha.gxt.widget.core.client.Window;

/**
 * Implementation Notes:
 * <p/>
 * <ul>
 * <li>Changes in row count are handled via Store events.</li>
 * <li>Changes in column count require a call to updateChart.</li>
 * </ul>
 */
public class SpreadsheetChartPresenterImpl implements SpreadsheetChartPresenter {

  private SpreadsheetChartView spreadsheetChartView;
  private FileModel fileModel;
  private FileSystem fileSystem;

  public SpreadsheetChartPresenterImpl(FileSystem fileSystem) {
    this.fileSystem = fileSystem;
  }

  @Override
  public void configure(FileModel fileModel, Worksheet worksheet) {
    this.fileModel = fileModel;
    getSpreadsheetChartView().configure(worksheet);
  }

  @Override
  public void go(HasWidgets hasWidgets) {
    Widget widget = getSpreadsheetChartView().asWidget();
    if (widget instanceof Window) {
      Window window = (Window) widget;
      window.show();
    }
  }

  @Override
  public void updateTitle() {
    String newTitle = getTitle();
    getSpreadsheetChartView().setTitle(newTitle);
  }

  private FileModel getFileModel() {
    return fileModel;
  }

  private FileSystem getFileSystem() {
    return fileSystem;
  }

  private SpreadsheetChartView getSpreadsheetChartView() {
    if (spreadsheetChartView == null) {
      spreadsheetChartView = new SpreadsheetChartViewImpl(this);
    }
    return spreadsheetChartView;
  }

  private String getTitle() {
    return "Chart - " + getFileSystem().getPath(getFileModel());
  }

}
