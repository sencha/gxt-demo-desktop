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
package com.sencha.gxt.desktopapp.client.filemanager;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.desktopapp.client.filemanager.images.Images;
import com.sencha.gxt.desktopapp.client.persistence.FileModel.FileType;
import com.sencha.gxt.widget.core.client.event.BeforeShowEvent;
import com.sencha.gxt.widget.core.client.event.BeforeShowEvent.BeforeShowHandler;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import com.sencha.gxt.widget.core.client.menu.SeparatorMenuItem;

public class FileManagerMenu implements IsWidget {

  private static final String FILE_TYPE = "fileType";

  private FileManagerPresenter fileManagerPresenter;

  private Menu fileMenu;
  private MenuItem createFolderMenuItem;
  private MenuItem createDocumentMenuItem;
  private MenuItem createSpreadsheetMenuItem;
  private MenuItem createProgramMenuItem;
  private MenuItem createBookmarkMenuItem;
  private MenuItem collapseMenuItem;
  private MenuItem expandMenuItem;
  private MenuItem editNameMenuItem;
  private MenuItem openMenuItem;
  private MenuItem deleteMenuItem;
  private BeforeShowHandler beforeShowHandler;
  private SelectionHandler<MenuItem> createSelectionHandler;
  private SelectionHandler<MenuItem> expandSelectionHandler;
  private SelectionHandler<MenuItem> collapseSelectionHandler;
  private SelectionHandler<MenuItem> editNameSelectionHandler;
  private SelectionHandler<MenuItem> openSelectionHandler;
  private SelectionHandler<MenuItem> deleteSelectionHandler;

  public FileManagerMenu(FileManagerPresenter fileManagerPresenter) {
    this.fileManagerPresenter = fileManagerPresenter;
  }

  public Widget asWidget() {
    getFileMenu();
    return fileMenu;
  }

  public Menu getFileMenu() {
    if (fileMenu == null) {
      fileMenu = new Menu();
      fileMenu.add(getCreateFolderMenuItem());
      fileMenu.add(new SeparatorMenuItem());
      fileMenu.add(getCreateDocumentMenuItem());
      fileMenu.add(getCreateSpreadsheetMenuItem());
      fileMenu.add(getCreateProgramMenuItem());
      fileMenu.add(getCreateBookmarkMenuItem());
      fileMenu.add(new SeparatorMenuItem());
      fileMenu.add(getExpandMenuItem());
      fileMenu.add(getCollapseMenuItem());
      fileMenu.add(new SeparatorMenuItem());
      fileMenu.add(getEditNameMenuItem());
      fileMenu.add(new SeparatorMenuItem());
      fileMenu.add(getOpenMenuItem());
      fileMenu.add(new SeparatorMenuItem());
      fileMenu.add(getDeleteMenuItem());
      fileMenu.addBeforeShowHandler(getBeforeShowHandler());
    }
    return fileMenu;
  }

  protected FileManagerPresenter getPresenter() {
    return fileManagerPresenter;
  }

  private BeforeShowHandler getBeforeShowHandler() {
    if (beforeShowHandler == null) {
      beforeShowHandler = new BeforeShowHandler() {
        @Override
        public void onBeforeShow(BeforeShowEvent event) {
          boolean isEnableCreate = getPresenter().isEnableCreate();
          boolean isEnableOpen = getPresenter().isEnableOpen();
          boolean isEnableDelete = getPresenter().isEnableDelete();
          boolean isEnableEditName = getPresenter().isEnableEditName();
          getCreateFolderMenuItem().setEnabled(isEnableCreate);
          getCreateDocumentMenuItem().setEnabled(isEnableCreate);
          getCreateSpreadsheetMenuItem().setEnabled(isEnableCreate);
          getCreateProgramMenuItem().setEnabled(isEnableCreate);
          getCreateBookmarkMenuItem().setEnabled(isEnableCreate);
          getExpandMenuItem().setEnabled(isEnableCreate);
          getCollapseMenuItem().setEnabled(isEnableCreate);
          getEditNameMenuItem().setEnabled(isEnableEditName);
          getOpenMenuItem().setEnabled(isEnableOpen);
          getDeleteMenuItem().setEnabled(isEnableDelete);
        }
      };
    }
    return beforeShowHandler;
  }

  private MenuItem getCollapseMenuItem() {
    if (collapseMenuItem == null) {
      collapseMenuItem = new MenuItem("Collapse", getCollapseSelectionHandler());
      collapseMenuItem.setIcon(Images.getImageResources().arrow_in());
    }
    return collapseMenuItem;
  }

  private SelectionHandler<MenuItem> getCollapseSelectionHandler() {
    if (collapseSelectionHandler == null) {
      collapseSelectionHandler = new SelectionHandler<MenuItem>() {
        @Override
        public void onSelection(SelectionEvent<MenuItem> event) {
          getPresenter().onCollapse();
        }
      };
    }
    return collapseSelectionHandler;
  }

  private MenuItem getCreateBookmarkMenuItem() {
    if (createBookmarkMenuItem == null) {
      createBookmarkMenuItem = new MenuItem("New Bookmark", getCreateSelectionHandler());
      createBookmarkMenuItem.setIcon(Images.getImageResources().world_add());
      createBookmarkMenuItem.setData(FILE_TYPE, FileType.BOOKMARK);
    }
    return createBookmarkMenuItem;
  }

  private MenuItem getCreateDocumentMenuItem() {
    if (createDocumentMenuItem == null) {
      createDocumentMenuItem = new MenuItem("New Document", getCreateSelectionHandler());
      createDocumentMenuItem.setIcon(Images.getImageResources().page_white_add());
      createDocumentMenuItem.setData(FILE_TYPE, FileType.DOCUMENT);
    }
    return createDocumentMenuItem;
  }

  private MenuItem getCreateFolderMenuItem() {
    if (createFolderMenuItem == null) {
      createFolderMenuItem = new MenuItem("New Folder", getCreateSelectionHandler());
      createFolderMenuItem.setIcon(Images.getImageResources().folder_add());
      createFolderMenuItem.setData(FILE_TYPE, FileType.FOLDER);
    }
    return createFolderMenuItem;
  }

  private MenuItem getCreateProgramMenuItem() {
    if (createProgramMenuItem == null) {
      createProgramMenuItem = new MenuItem("New Program", getCreateSelectionHandler());
      createProgramMenuItem.setIcon(Images.getImageResources().script_add());
      createProgramMenuItem.setData(FILE_TYPE, FileType.PROGRAM);
    }
    return createProgramMenuItem;
  }

  private SelectionHandler<MenuItem> getCreateSelectionHandler() {
    if (createSelectionHandler == null) {
      createSelectionHandler = new SelectionHandler<MenuItem>() {
        @Override
        public void onSelection(SelectionEvent<MenuItem> event) {
          FileType fileType = event.getSelectedItem().<FileType> getData(FILE_TYPE);
          getPresenter().onCreate(fileType);
        }
      };
    }
    return createSelectionHandler;
  }

  private MenuItem getCreateSpreadsheetMenuItem() {
    if (createSpreadsheetMenuItem == null) {
      createSpreadsheetMenuItem = new MenuItem("New Spreadsheet", getCreateSelectionHandler());
      createSpreadsheetMenuItem.setIcon(Images.getImageResources().table_add());
      createSpreadsheetMenuItem.setData(FILE_TYPE, FileType.SPREADSHEET);
    }
    return createSpreadsheetMenuItem;
  }

  private MenuItem getDeleteMenuItem() {
    if (deleteMenuItem == null) {
      deleteMenuItem = new MenuItem("Delete", getDeleteSelectionHandler());
      deleteMenuItem.setIcon(Images.getImageResources().bin_closed());
    }
    return deleteMenuItem;
  }

  private SelectionHandler<MenuItem> getDeleteSelectionHandler() {
    if (deleteSelectionHandler == null) {
      deleteSelectionHandler = new SelectionHandler<MenuItem>() {
        @Override
        public void onSelection(SelectionEvent<MenuItem> event) {
          getPresenter().onDelete();
        }
      };
    }
    return deleteSelectionHandler;
  }

  private MenuItem getEditNameMenuItem() {
    if (editNameMenuItem == null) {
      editNameMenuItem = new MenuItem("Edit Name", getEditNameSelectionHandler());
      editNameMenuItem.setIcon(Images.getImageResources().textfield_rename());
    }
    return editNameMenuItem;
  }

  private SelectionHandler<MenuItem> getEditNameSelectionHandler() {
    if (editNameSelectionHandler == null) {
      editNameSelectionHandler = new SelectionHandler<MenuItem>() {
        @Override
        public void onSelection(SelectionEvent<MenuItem> event) {
          getPresenter().onEditName();
        }
      };
    }
    return editNameSelectionHandler;
  }

  private MenuItem getExpandMenuItem() {
    if (expandMenuItem == null) {
      expandMenuItem = new MenuItem("Expand", getExpandSelectionHandler());
      expandMenuItem.setIcon(Images.getImageResources().arrow_out());
    }
    return expandMenuItem;
  }

  private SelectionHandler<MenuItem> getExpandSelectionHandler() {
    if (expandSelectionHandler == null) {
      expandSelectionHandler = new SelectionHandler<MenuItem>() {
        @Override
        public void onSelection(SelectionEvent<MenuItem> event) {
          getPresenter().onExpand();
        }
      };
    }
    return expandSelectionHandler;
  }

  private MenuItem getOpenMenuItem() {
    if (openMenuItem == null) {
      openMenuItem = new MenuItem("Open", getOpenSelectionHandler());
      openMenuItem.setIcon(Images.getImageResources().bullet_go());
    }
    return openMenuItem;
  }

  private SelectionHandler<MenuItem> getOpenSelectionHandler() {
    if (openSelectionHandler == null) {
      openSelectionHandler = new SelectionHandler<MenuItem>() {
        @Override
        public void onSelection(SelectionEvent<MenuItem> event) {
          getPresenter().onOpen();
        }
      };
    }
    return openSelectionHandler;
  }

}
