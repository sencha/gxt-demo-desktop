package com.sencha.gxt.desktopapp.client.spreadsheet;

import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.chart.client.chart.Chart;
import com.sencha.gxt.chart.client.chart.Chart.Position;
import com.sencha.gxt.chart.client.chart.Legend;
import com.sencha.gxt.chart.client.chart.axis.CategoryAxis;
import com.sencha.gxt.chart.client.chart.axis.NumericAxis;
import com.sencha.gxt.chart.client.chart.series.BarSeries;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.desktopapp.client.spreadsheet.images.Images;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.event.ShowEvent;
import com.sencha.gxt.widget.core.client.event.ShowEvent.ShowHandler;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

public class SpreadsheetChartViewImpl implements SpreadsheetChartView, HideHandler, ShowHandler {

  private static final RGB[] colors = new RGB[] {
      //
      new RGB(0xf0, 0x80, 0x80), //
      new RGB(0x80, 0xf0, 0x80), //
      new RGB(0x80, 0x80, 0xf0), //
      new RGB(0xf0, 0xf0, 0x80), //
      new RGB(0x80, 0xf0, 0xf0), //
      new RGB(0xf0, 0x80, 0xf0), //
      //
      new RGB(0xf0, 0xc0, 0xc0), //
      new RGB(0xc0, 0xf0, 0xc0), //
      new RGB(0xc0, 0xc0, 0xf0), //
      new RGB(0xf0, 0xf0, 0xc0), //
      new RGB(0xc0, 0xf0, 0xf0), //
      new RGB(0xf0, 0xc0, 0xf0), //
  };

  @SuppressWarnings("unused")
  private SpreadsheetChartPresenter spreadsheetChartPresenter;

  private Worksheet worksheet;
  private Window window;
  private VerticalLayoutContainer verticalLayoutContainer;
  private ToolBar toolBar;
  private TextButton chartStyleBarTextButton;
  private TextButton chartStyleLineTextButton;
  private TextButton chartStylePieTextButton;
  private ListStore<Row> listStore;
  private Chart<Row> chart;
  private NumericAxis<Row> axis;
  private CategoryAxis<Row, String> categoryAxis;
  private BarSeries<Row> barSeries;
  private Legend<Row> legend;
  private SelectHandler defaultSelectHandler;

  public SpreadsheetChartViewImpl(SpreadsheetChartPresenter spreadsheetChartPresenter) {
    this.spreadsheetChartPresenter = spreadsheetChartPresenter;
  }

  @Override
  public Widget asWidget() {
    return getWindow();
  }

  @Override
  public void configure(Worksheet newWorksheet) {

    if (getWindow().isVisible()) {

      worksheet = newWorksheet;
      listStore = null;

      if (axis != null) {
        getChart().removeAxis(axis);
        axis = null;
      }

      if (categoryAxis != null) {
        getChart().removeAxis(categoryAxis);
        categoryAxis = null;
      }

      if (barSeries != null) {
        getChart().removeSeries(barSeries);
        barSeries = null;
      }

      if (legend != null) {
        getChart().removeLegend();
        legend = null;
      }

      getChart().bindStore(getListStore());
      getChart().addAxis(getAxis());
      getChart().addAxis(getCategoryAxis());
      getChart().addSeries(getBarSeries());
      getChart().setLegend(getLegend());

      getChart().redrawChart();

    }

  }

  public Chart<Row> getChart() {
    if (chart == null) {
      chart = new Chart<Row>();
    }
    return chart;
  }

  @Override
  public void onHide(HideEvent event) {
    getChart().bindStore(null);
  }

  @Override
  public void onShow(ShowEvent event) {
  }

  @Override
  public void setTitle(String newTitle) {
    getWindow().setHeadingText(newTitle);
  }

  private NumericAxis<Row> getAxis() {
    if (axis == null) {
      axis = new NumericAxis<Row>();
      axis.setPosition(Position.LEFT);
      // 1 is to skip labels in column 0
      for (int columnIndex = 1; columnIndex < getWorksheet().getColumnCount(); columnIndex++) {
        ChartValueProvider f = new ChartValueProvider(getWorksheet(), columnIndex);
        axis.addField(f);
      }
      axis.setDisplayGrid(true);
    }
    return axis;
  }

  private BarSeries<Row> getBarSeries() {
    if (barSeries == null) {
      barSeries = new BarSeries<Row>();
      barSeries.setYAxisPosition(Position.LEFT);
      // 1 is to skip labels in column 0
      for (int columnIndex = 1; columnIndex < getWorksheet().getColumnCount(); columnIndex++) {
        ChartValueProvider f = new ChartValueProvider(getWorksheet(), columnIndex);
        barSeries.addYField(f);
        barSeries.addColor(colors[(columnIndex - 1) % colors.length]);
        barSeries.setColumn(true);
      }
    }
    return barSeries;
  }

  private CategoryAxis<Row, String> getCategoryAxis() {
    if (categoryAxis == null) {
      categoryAxis = new CategoryAxis<Row, String>();
      categoryAxis.setPosition(Position.BOTTOM);
      categoryAxis.setField(new RowValueProvider(0));
      TextSprite title = new TextSprite(getCategoryTitle());
      title.setFontSize(18);
      categoryAxis.setTitleConfig(title);
    }
    return categoryAxis;
  }

  private String getCategoryTitle() {
    return getWorksheet().getListStore().get(0).getColumns().get(0);
  }

  private TextButton getChartStyleBarTextButton() {
    if (chartStyleBarTextButton == null) {
      chartStyleBarTextButton = new TextButton();
      chartStyleBarTextButton.setToolTip("Display as bar chart");
      chartStyleBarTextButton.setIcon(Images.getImageResources().chart_bar());
      chartStyleBarTextButton.addSelectHandler(getDefaultSelectHandler());
    }
    return chartStyleBarTextButton;
  }

  private TextButton getChartStyleLineTextButton() {
    if (chartStyleLineTextButton == null) {
      chartStyleLineTextButton = new TextButton();
      chartStyleLineTextButton.setToolTip("Display as line chart");
      chartStyleLineTextButton.setIcon(Images.getImageResources().chart_line());
      chartStyleLineTextButton.addSelectHandler(getDefaultSelectHandler());
    }
    return chartStyleLineTextButton;
  }

  private TextButton getChartStylePieTextButton() {
    if (chartStylePieTextButton == null) {
      chartStylePieTextButton = new TextButton();
      chartStylePieTextButton.setToolTip("Display as pie chart");
      chartStylePieTextButton.setIcon(Images.getImageResources().chart_pie());
      chartStylePieTextButton.addSelectHandler(getDefaultSelectHandler());
    }
    return chartStylePieTextButton;
  }

  private SelectHandler getDefaultSelectHandler() {
    if (defaultSelectHandler == null) {
      defaultSelectHandler = new SelectHandler() {
        @Override
        public void onSelect(SelectEvent event) {
          Info.display("Spreadsheet Charts", "More spreadsheet chart features are in the works!");
        }
      };
    }
    return defaultSelectHandler;
  }

  private Legend<Row> getLegend() {
    if (legend == null) {
      legend = new Legend<Row>();
      legend.setPosition(Position.RIGHT);
      legend.setItemHighlighting(true);
      legend.setItemHiding(true);
    }
    return legend;
  }

  private ListStore<Row> getListStore() {
    if (listStore == null) {
      listStore = new SpreadsheetChartListStoreAdapter(getWorksheet().getListStore());
    }
    return listStore;
  }

  private ToolBar getToolBar() {
    if (toolBar == null) {
      toolBar = new ToolBar();
      toolBar.add(getChartStyleBarTextButton());
      toolBar.add(getChartStyleLineTextButton());
      toolBar.add(getChartStylePieTextButton());
    }
    return toolBar;
  }

  private VerticalLayoutContainer getVerticalLayoutContainer() {
    if (verticalLayoutContainer == null) {
      verticalLayoutContainer = new VerticalLayoutContainer();
      verticalLayoutContainer.setBorders(true);
      verticalLayoutContainer.add(getToolBar(), new VerticalLayoutData(1, -1));
      verticalLayoutContainer.add(getChart(), new VerticalLayoutData(1, 1));
    }
    return verticalLayoutContainer;
  }

  private Window getWindow() {
    if (window == null) {
      window = new Window();
      window.getHeader().setIcon(Images.getImageResources().chart_bar());
      window.setMinimizable(true);
      window.setMaximizable(true);
      window.setPixelSize(400, 400);
      window.setOnEsc(false);
      getWindow().setWidget(getVerticalLayoutContainer());
      window.addHideHandler(this);
      window.addShowHandler(this);
    }
    return window;
  }

  private Worksheet getWorksheet() {
    return worksheet;
  }

}
