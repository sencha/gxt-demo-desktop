package com.sencha.gxt.desktopapp.client.spreadsheet;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.util.Util;
import com.sencha.gxt.desktop.client.widget.AutoVerticalLayoutContainer;
import com.sencha.gxt.desktopapp.client.filemanager.images.Images;
import com.sencha.gxt.dnd.core.client.DND.Feedback;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.GridDragSource;
import com.sencha.gxt.dnd.core.client.GridDropTarget;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.ColumnMoveEvent;
import com.sencha.gxt.widget.core.client.event.ColumnMoveEvent.ColumnMoveHandler;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.event.ShowEvent;
import com.sencha.gxt.widget.core.client.event.ShowEvent.ShowHandler;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.RowNumberer;
import com.sencha.gxt.widget.core.client.grid.editing.ClicksToEdit;
import com.sencha.gxt.widget.core.client.grid.editing.GridInlineEditing;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.selection.CellSelection;
import com.sencha.gxt.widget.core.client.selection.CellSelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.CellSelectionChangedEvent.CellSelectionChangedHandler;

public class SpreadsheetViewImpl implements SpreadsheetView, HideHandler, ShowHandler, ColumnProvider {

  public static final String CELL_TOOL_TIP = "Enter a value or expression, for example:<br/>=((C2-B2)/B2)*100 or =SUM(B2:B10)<br/>Use $ for same row or column (e.g. B$ or $2).";

  private static final int DEFAULT_COLUMN_WIDTH = 50;

  private SpreadsheetPresenter spreadsheetPresenter;
  private Window window;
  private Grid<Row> grid;
  private Worksheet worksheet;
  private ColumnModel<Row> columnModel;
  private List<ColumnConfig<Row, ?>> columnConfigs;
  private RowNumberer<Row> rowNumberer;
  private GridInlineEditing<Row> gridInlineEditing;
  private VerticalLayoutContainer verticalLayoutContainer;
  private SpreadsheetToolBar spreadsheetToolBar;
  private Cell<String> displayCell;
  private CellSelectionModel<Row> cellSelectionModel;
  private GridDragSource<Row> gridDragSource;
  private GridDropTarget<Row> gridDropTarget;
  private Menu spreadsheetMenu;
  private NumberFormat defaultNumberFormat;

  public SpreadsheetViewImpl(SpreadsheetPresenter spreadsheetPresenter) {
    this.spreadsheetPresenter = spreadsheetPresenter;
  }

  @Override
  public Widget asWidget() {
    return getWindow();
  }

  @Override
  public void close() {
    getWindow().hide();
  }

  @Override
  public ColumnConfig<Row, Object> getColumn(int cellIndex) {
    return getColumnModel().getColumn(cellIndex);
  }

  @Override
  public int getSelectedColumn() {
    int selectedColumn = -1;
    CellSelection<Row> cell = getCellSelectionModel().getSelectCell();
    if (cell != null) {
      selectedColumn = cell.getCell() - 1;
    }
    return selectedColumn;
  }

  @Override
  public int getSelectedRow() {
    int selectedRow = -1;
    CellSelection<Row> cell = getCellSelectionModel().getSelectCell();
    if (cell != null) {
      selectedRow = cell.getRow();
    }
    return selectedRow;
  }

  @Override
  public String getValue() {
    return getWorksheet().getValue();
  }

  @Override
  public void onHide(HideEvent event) {
    getSpreadsheetPresenter().unbind();
  }

  @Override
  public void onShow(ShowEvent event) {
    getSpreadsheetPresenter().bind();
    getGrid().focus();
  }

  @Override
  public void refresh() {
    getGrid().getView().refresh(false);
  }

  @Override
  public void setColumnHeader(int columnIndex, SafeHtml header) {
    getColumnModel().setColumnHeader(columnIndex, header);
  }

  @Override
  public void setTitle(String title) {
    getWindow().setHeadingText(title);
  }

  @Override
  public void setValue(String value) {
    // Value is provided by setValue(Worksheet worksheet)
  }

  @Override
  public void setValue(Worksheet worksheet) {
    this.worksheet = worksheet;
    updateGrid();
  }

  @Override
  public void updateDetails(String cellName, String cellValue) {
    getSpreadsheetToolBar().setDetails(cellName, cellValue);
  }

  @Override
  public void updateGrid() {
    if (getVerticalLayoutContainer().getWidgetCount() == 1) {
      getVerticalLayoutContainer().add(getGrid(), new VerticalLayoutData(1, 1));
    } else {
      columnModel = null;
      columnConfigs = null;
      getGridInlineEditing().clearEditors();
      getGrid().reconfigure(getWorksheet().getListStore(), getColumnModel());
    }
    adjustColumnWidths();
    getSpreadsheetToolBar().setDimensions(getWorksheet().getRowCount(), getWorksheet().getColumnCount());
  }

  @Override
  public void updateSelectedCells(String value) {
    CellSelection<Row> cellSelection = getCellSelectionModel().getSelectCell();
    if (cellSelection != null) {
      getWorksheet().setValue(value, cellSelection);
      getGrid().getView().refresh(false);
    }
  }

  private void adjustColumnWidths() {
    int requiredWidth = 0;
    boolean isRefreshRequired = false;
    int cellCount = getWorksheet().getCellCount();
    for (int cellIndex = 0; cellIndex < cellCount; cellIndex++) {
      ColumnConfig<Row, Object> columnConfig = getColumnModel().getColumn(cellIndex);
      if (!columnConfig.isHidden()) {
        int columnWidth = columnConfig.getWidth();
        if (cellIndex > 0 && columnWidth < DEFAULT_COLUMN_WIDTH) {
          columnWidth = DEFAULT_COLUMN_WIDTH;
          columnConfig.setWidth(columnWidth);
          isRefreshRequired = true;
        }
        requiredWidth += columnWidth;
      }
    }
    int offsetWidth = getGrid().getOffsetWidth();
    boolean isAutoFillRequired = requiredWidth < offsetWidth;
    if (isAutoFillRequired != getGrid().getView().isAutoFill()) {
      isRefreshRequired = true;
      getGrid().getView().setAutoFill(isAutoFillRequired);
    }
    if (isRefreshRequired) {
      getGrid().getView().refresh(true);
    }
  }

  private TextField createCellEditorTextField() {
    TextField cellEditorTextField = new TextField();
    cellEditorTextField.setSelectOnFocus(true);
    cellEditorTextField.setToolTip(CELL_TOOL_TIP);
    cellEditorTextField.addValueChangeHandler(new ValueChangeHandler<String>() {
      @Override
      public void onValueChange(ValueChangeEvent<String> event) {
        CellSelection<Row> cell = getCellSelectionModel().getSelectCell();
        getSpreadsheetPresenter().onCellValueChange(cell.getRow(), cell.getCell() - 1, event.getValue());
      }
    });
    return cellEditorTextField;
  }

  private void displaySelectedCell(List<CellSelection<Row>> cellSelections) {
    String name = null;
    String value = null;
    if (cellSelections != null) {
      if (cellSelections.size() > 0) {
        CellSelection<Row> cellSelection = cellSelections.get(0);
        if (cellSelection != null) {
          name = getWorksheet().getName(cellSelection);
          if (name != null) {
            value = getWorksheet().getValue(cellSelection);
          }
        }
      }
    }
    getSpreadsheetToolBar().setDetails(name, value);
  }

  private CellSelectionModel<Row> getCellSelectionModel() {
    if (cellSelectionModel == null) {
      cellSelectionModel = new CellSelectionModel<Row>();
      cellSelectionModel.setSelectionMode(SelectionMode.MULTI);
      cellSelectionModel.addCellSelectionChangedHandler(new CellSelectionChangedHandler<Row>() {
        @Override
        public void onCellSelectionChanged(CellSelectionChangedEvent<Row> event) {
          List<CellSelection<Row>> cellSelections = event.getSelection();
          displaySelectedCell(cellSelections);
        }
      });
    }
    return cellSelectionModel;
  }

  private List<ColumnConfig<Row, ?>> getColumnConfigs() {
    if (columnConfigs == null) {
      columnConfigs = new LinkedList<ColumnConfig<Row, ?>>();
      columnConfigs.add(getRowNumberer());
      int columnCount = getWorksheet().getColumnCount();
      for (int i = 0; i < columnCount; i++) {
        ColumnConfig<Row, String> columnConfig = new ColumnConfig<Row, String>(new RowValueProvider(i));
        columnConfig.setHeader(SpreadsheetUtilities.getColumnName(i));
        columnConfig.setWidth(DEFAULT_COLUMN_WIDTH);
        columnConfig.setCell(getDisplayCell());
        getGridInlineEditing().addEditor(columnConfig, createCellEditorTextField());
        columnConfigs.add(columnConfig);
      }
    }
    return columnConfigs;
  }

  private ColumnModel<Row> getColumnModel() {
    if (columnModel == null) {
      columnModel = new ColumnModel<Row>(getColumnConfigs());
      columnModel.addColumnMoveHandler(new ColumnMoveHandler() {
        @Override
        public void onColumnMove(ColumnMoveEvent event) {
          getWorksheet().renameColumns(event.getIndex());
          getSpreadsheetPresenter().onColumnMove();
        }
      });
    }
    return columnModel;
  }

  private NumberFormat getDefaultNumberFormat() {
    if (defaultNumberFormat == null) {
      defaultNumberFormat = NumberFormat.getFormat("#.##");
    }
    return defaultNumberFormat;
  }

  private Cell<String> getDisplayCell() {
    if (displayCell == null) {
      displayCell = new AbstractCell<String>() {
        @Override
        public void render(Context context, String value, SafeHtmlBuilder sb) {
          if (Util.isEmptyString(value)) {
            sb.appendHtmlConstant("&nbsp;");
          } else {
            if (value.startsWith(Evaluator.EXPRESSION_MARKER)) {
              double result = getWorksheet().evaluateCell(value, context.getIndex(), context.getColumn());
              String formattedResult = getDefaultNumberFormat().format(result);
              sb.appendHtmlConstant("<b>" + formattedResult + "</b>");
            } else {
              sb.appendEscaped(value);
            }
          }
        }
      };
    }
    return displayCell;
  }

  private Grid<Row> getGrid() {
    if (grid == null) {
      grid = new Grid<Row>(getWorksheet().getListStore(), getColumnModel());
      grid.setSelectionModel(getCellSelectionModel());
      grid.getView().setStripeRows(true);
      grid.getView().setColumnLines(true);
      grid.setColumnReordering(true);
      grid.setContextMenu(getSpreadsheetMenu());
      getRowNumberer().initPlugin(grid);
      getGridInlineEditing().setEditableGrid(grid);
      getGridDragSource();
      getGridDropTarget();
      grid.addResizeHandler(new ResizeHandler() {
        @Override
        public void onResize(ResizeEvent event) {
          adjustColumnWidths();
        }
      });
    }
    return grid;
  }

  private GridDragSource<Row> getGridDragSource() {
    if (gridDragSource == null) {
      gridDragSource = new GridDragSource<Row>(getGrid());
    }
    return gridDragSource;
  }

  private GridDropTarget<Row> getGridDropTarget() {
    if (gridDropTarget == null) {
      gridDropTarget = new GridDropTarget<Row>(getGrid()) {
        @Override
        protected void onDragDrop(DndDropEvent e) {
          getGrid().getStore().clearSortInfo();
          getGrid().getView().getHeader().refresh();
          super.onDragDrop(e);
          getSpreadsheetPresenter().onDragDrop();
        }
      };
      gridDropTarget.setAllowSelfAsSource(true);
      gridDropTarget.setFeedback(Feedback.BOTH);
    }
    return gridDropTarget;
  }

  private GridInlineEditing<Row> getGridInlineEditing() {
    if (gridInlineEditing == null) {
      gridInlineEditing = new GridInlineEditing<Row>(null);
      gridInlineEditing.setClicksToEdit(ClicksToEdit.TWO);
    }
    return gridInlineEditing;
  }

  private RowNumberer<Row> getRowNumberer() {
    if (rowNumberer == null) {
      rowNumberer = new RowNumberer<Row>();
    }
    return rowNumberer;
  }

  private Menu getSpreadsheetMenu() {
    if (spreadsheetMenu == null) {
      spreadsheetMenu = new SpreadsheetMenu(getSpreadsheetPresenter()).getMenu();
    }
    return spreadsheetMenu;
  }

  private SpreadsheetPresenter getSpreadsheetPresenter() {
    return spreadsheetPresenter;
  }

  private SpreadsheetToolBar getSpreadsheetToolBar() {
    if (spreadsheetToolBar == null) {
      spreadsheetToolBar = new SpreadsheetToolBar(getSpreadsheetPresenter());
    }
    return spreadsheetToolBar;
  }

  private VerticalLayoutContainer getVerticalLayoutContainer() {
    if (verticalLayoutContainer == null) {
      verticalLayoutContainer = new AutoVerticalLayoutContainer();
      verticalLayoutContainer.setBorders(true);
      verticalLayoutContainer.add(getSpreadsheetToolBar(), new VerticalLayoutData(1, -1));
    }
    return verticalLayoutContainer;
  }

  private Window getWindow() {
    if (window == null) {
      window = new Window();
      window.getHeader().setIcon(Images.getImageResources().table());
      window.setMinimizable(true);
      window.setMaximizable(true);
      window.setPixelSize(500, 400);
      window.setOnEsc(false);
      window.setWidget(getVerticalLayoutContainer());
      window.addHideHandler(this);
      window.addShowHandler(this);
    }
    return window;
  }

  private Worksheet getWorksheet() {
    return worksheet;
  }
}
