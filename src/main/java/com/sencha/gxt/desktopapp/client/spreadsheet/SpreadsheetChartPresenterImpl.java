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
