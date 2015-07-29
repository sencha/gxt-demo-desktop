package com.sencha.gxt.desktopapp.client.spreadsheet;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.desktopapp.client.spreadsheet.images.Images;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;
import com.sencha.gxt.widget.core.client.form.SpinnerField;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.toolbar.LabelToolItem;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

public class SpreadsheetToolBar implements IsWidget {

  private SpreadsheetPresenter spreadsheetPresenter;
  private ToolBar toolBar;
  private TextButton openChartTextButton;
  private SpinnerField<Integer> columnCountSpinnerField;
  private SpinnerField<Integer> rowCountSpinnerField;
  private LabelToolItem cellNameLabel;
  private TextField cellValueTextField;
  private SelectHandler openChartSelectHandler;

  public SpreadsheetToolBar(SpreadsheetPresenter spreadsheetPresenter) {
    this.spreadsheetPresenter = spreadsheetPresenter;
  }

  @Override
  public Widget asWidget() {
    return getToolBar();
  }

  public void setDetails(String cellName, String cellValue) {
    getCellNameLabel().setLabel(getCellLabel(cellName));
    getCellValueTextField().setValue(cellValue);
  }

  public void setDimensions(int rowCount, int columnCount) {
    getRowCountSpinnerField().setValue(rowCount);
    getColumnCountSpinnerField().setValue(columnCount);
  }

  private SpinnerField<Integer> createSpinner(final ValueChangeHandler<Integer> valueChangeHandler) {
    final SpinnerField<Integer> spinnerField = new SpinnerField<Integer>(
        new NumberPropertyEditor.IntegerPropertyEditor());
    spinnerField.addSelectionHandler(new SelectionHandler<Integer>() {
      @Override
      public void onSelection(SelectionEvent<Integer> event) {
        ValueChangeEvent.fire(spinnerField, event.getSelectedItem());
      }
    });
    spinnerField.addValueChangeHandler(valueChangeHandler);
    spinnerField.setMaxValue(100);
    spinnerField.setMinValue(1);
    spinnerField.setAllowNegative(false);
    spinnerField.setAllowBlank(false);
    spinnerField.setAllowDecimals(false);
    spinnerField.setWidth(50);
    return spinnerField;
  }

  private String getCellLabel(String cellName) {
    return cellName == null ? "Cell: " : (cellName + ": ");
  }

  private LabelToolItem getCellNameLabel() {
    if (cellNameLabel == null) {
      cellNameLabel = new LabelToolItem(getCellLabel(null));
    }
    return cellNameLabel;
  }

  private BoxLayoutData getCellValueLayoutData() {
    BoxLayoutData cellLayoutData = new BoxLayoutData();
    cellLayoutData.setFlex(1);
    return cellLayoutData;
  }

  private TextField getCellValueTextField() {
    if (cellValueTextField == null) {
      cellValueTextField = new TextField();
      cellValueTextField.setToolTip(SpreadsheetViewImpl.CELL_TOOL_TIP);
      cellValueTextField.addKeyDownHandler(new KeyDownHandler() {
        @Override
        public void onKeyDown(KeyDownEvent event) {
          if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
            Scheduler.get().scheduleDeferred(new ScheduledCommand() {
              @Override
              public void execute() {
                getPresenter().onDetailValueChange(cellValueTextField.getValue());
              }
            });
          }
        }
      });
    }
    return cellValueTextField;
  }

  private SpinnerField<Integer> getColumnCountSpinnerField() {
    if (columnCountSpinnerField == null) {
      columnCountSpinnerField = createSpinner(new ValueChangeHandler<Integer>() {
        @Override
        public void onValueChange(ValueChangeEvent<Integer> event) {
          if (columnCountSpinnerField.validate()) {
            getPresenter().onColumnCountChange(event.getValue());
          }
        }
      });
    }
    return columnCountSpinnerField;
  }

  private SelectHandler getOpenChartSelectHandler() {
    if (openChartSelectHandler == null) {
      openChartSelectHandler = new SelectHandler() {
        @Override
        public void onSelect(SelectEvent event) {
          if (rowCountSpinnerField.validate() && columnCountSpinnerField.validate()) {
            getPresenter().onOpenChart();
          }
        }
      };
    }
    return openChartSelectHandler;
  }

  private TextButton getOpenChartTextButton() {
    if (openChartTextButton == null) {
      openChartTextButton = new TextButton();
      openChartTextButton.setToolTip("Open Chart");
      openChartTextButton.setIcon(Images.getImageResources().chart_bar());
      openChartTextButton.addSelectHandler(getOpenChartSelectHandler());
    }
    return openChartTextButton;
  }

  private SpreadsheetPresenter getPresenter() {
    return spreadsheetPresenter;
  }

  private SpinnerField<Integer> getRowCountSpinnerField() {
    if (rowCountSpinnerField == null) {
      rowCountSpinnerField = createSpinner(new ValueChangeHandler<Integer>() {
        @Override
        public void onValueChange(ValueChangeEvent<Integer> event) {
          if (rowCountSpinnerField.validate()) {
            getPresenter().onRowCountChange(event.getValue());
          }
        }
      });
    }
    return rowCountSpinnerField;
  }

  private Widget getToolBar() {
    if (toolBar == null) {
      toolBar = new ToolBar();
      toolBar.add(getOpenChartTextButton());
      toolBar.add(new LabelToolItem("Rows: "));
      toolBar.add(getRowCountSpinnerField());
      toolBar.add(new LabelToolItem("Cols: "));
      toolBar.add(getColumnCountSpinnerField());
      toolBar.add(getCellNameLabel());
      toolBar.add(getCellValueTextField(), getCellValueLayoutData());
    }
    return toolBar;
  }

}
