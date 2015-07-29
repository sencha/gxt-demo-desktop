package com.sencha.gxt.desktopapp.client.spreadsheet;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.sencha.gxt.desktopapp.client.spreadsheet.images.Images;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import com.sencha.gxt.widget.core.client.menu.SeparatorMenuItem;

public class SpreadsheetMenu {
  private SpreadsheetPresenter spreadsheetPresenter;
  private Menu menu;
  private MenuItem insertAboveMenuItem;
  private MenuItem insertBelowMenuItem;
  private MenuItem insertLeftMenuItem;
  private MenuItem insertRightMenuItem;
  private MenuItem deleteColumnMenuItem;
  private MenuItem deleteRowMenuItem;

  public SpreadsheetMenu(SpreadsheetPresenter spreadsheetPresenter) {
    this.spreadsheetPresenter = spreadsheetPresenter;
  }

  public Menu getMenu() {
    if (menu == null) {
      menu = new Menu();
      menu.add(getInsertAboveMenuItem());
      menu.add(getInsertBelowMenuItem());
      menu.add(getInsertLeftMenuItem());
      menu.add(getInsertRightMenuItem());
      menu.add(new SeparatorMenuItem());
      menu.add(getDeleteColumnMenuItem());
      menu.add(getDeleteRowMenuItem());
    }
    return menu;
  }

  private MenuItem getDeleteColumnMenuItem() {
    if (deleteColumnMenuItem == null) {
      deleteColumnMenuItem = new MenuItem("Delete Column");
      deleteColumnMenuItem.setIcon(Images.getImageResources().table_column_delete());
      deleteColumnMenuItem.addSelectionHandler(new SelectionHandler<Item>() {
        @Override
        public void onSelection(SelectionEvent<Item> event) {
          getSpreadsheetPresenter().onDeleteColumn();
        }
      });
    }
    return deleteColumnMenuItem;
  }

  private MenuItem getDeleteRowMenuItem() {
    if (deleteRowMenuItem == null) {
      deleteRowMenuItem = new MenuItem("Delete Row");
      deleteRowMenuItem.setIcon(Images.getImageResources().table_row_delete());
      deleteRowMenuItem.addSelectionHandler(new SelectionHandler<Item>() {
        @Override
        public void onSelection(SelectionEvent<Item> event) {
          getSpreadsheetPresenter().onDeleteRow();
        }
      });
    }
    return deleteRowMenuItem;
  }

  private MenuItem getInsertAboveMenuItem() {
    if (insertAboveMenuItem == null) {
      insertAboveMenuItem = new MenuItem("Insert Above");
      insertAboveMenuItem.setIcon(Images.getImageResources().arrow_up());
      insertAboveMenuItem.addSelectionHandler(new SelectionHandler<Item>() {
        @Override
        public void onSelection(SelectionEvent<Item> event) {
          getSpreadsheetPresenter().onInsertAbove();
        }
      });
    }
    return insertAboveMenuItem;
  }

  private MenuItem getInsertBelowMenuItem() {
    if (insertBelowMenuItem == null) {
      insertBelowMenuItem = new MenuItem("Insert Below");
      insertBelowMenuItem.setIcon(Images.getImageResources().arrow_down());
      insertBelowMenuItem.addSelectionHandler(new SelectionHandler<Item>() {
        @Override
        public void onSelection(SelectionEvent<Item> event) {
          getSpreadsheetPresenter().onInsertBelow();
        }
      });
    }
    return insertBelowMenuItem;
  }

  private MenuItem getInsertLeftMenuItem() {
    if (insertLeftMenuItem == null) {
      insertLeftMenuItem = new MenuItem("Insert Left");
      insertLeftMenuItem.setIcon(Images.getImageResources().arrow_left());
      insertLeftMenuItem.addSelectionHandler(new SelectionHandler<Item>() {
        @Override
        public void onSelection(SelectionEvent<Item> event) {
          getSpreadsheetPresenter().onInsertLeft();
        }
      });
    }
    return insertLeftMenuItem;
  }

  private MenuItem getInsertRightMenuItem() {
    if (insertRightMenuItem == null) {
      insertRightMenuItem = new MenuItem("Insert Right");
      insertRightMenuItem.setIcon(Images.getImageResources().arrow_right());
      insertRightMenuItem.addSelectionHandler(new SelectionHandler<Item>() {
        @Override
        public void onSelection(SelectionEvent<Item> event) {
          getSpreadsheetPresenter().onInsertRight();
        }
      });
    }
    return insertRightMenuItem;
  }

  private SpreadsheetPresenter getSpreadsheetPresenter() {
    return spreadsheetPresenter;
  }
}
