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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.cell.client.DateCell;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.IconProvider;
import com.sencha.gxt.desktopapp.client.filemanager.images.Images;
import com.sencha.gxt.desktopapp.client.persistence.FileModel;
import com.sencha.gxt.desktopapp.client.persistence.FileModel.FileType;
import com.sencha.gxt.desktopapp.client.persistence.FileModelProperties;
import com.sencha.gxt.desktopapp.client.persistence.FileSystem;
import com.sencha.gxt.dnd.core.client.DND.Feedback;
import com.sencha.gxt.dnd.core.client.TreeGridDragSource;
import com.sencha.gxt.dnd.core.client.TreeGridDropTarget;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.CancelEditEvent;
import com.sencha.gxt.widget.core.client.event.CancelEditEvent.CancelEditHandler;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent.CompleteEditHandler;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.event.RowDoubleClickEvent;
import com.sencha.gxt.widget.core.client.event.RowDoubleClickEvent.RowDoubleClickHandler;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid.GridCell;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;

public class FileManagerViewImpl implements FileManagerView, HideHandler {

  private static final String TITLE = "File Manager";

  private FileSystem fileSystem;
  private FileManagerPresenter fileManagerPresenter;

  private Window window;
  private VerticalLayoutContainer verticalLayoutContainer;
  private FileManagerTreeGrid treeGrid;
  private ColumnConfig<FileModel, FileType> typeConfig;
  private ColumnConfig<FileModel, String> nameConfig;
  private ColumnConfig<FileModel, Date> dateConfig;
  private ColumnConfig<FileModel, Long> sizeConfig;
  private ColumnModel<FileModel> columnModel;
  private FileManagerIconProvider fileManagerIconProvider;
  private FileManagerSizeCell fileManagerSizeCell;
  private FileManagerGridInlineEditing gridEditing;
  private FileManagerToolBar fileManagerToolBar;
  private FileManagerMenu fileManagerMenu;
  private SelectionChangedHandler<FileModel> selectionChangedHandler;
  private TreeGridDragSource<FileModel> treeGridDragSource;
  private TreeGridDropTarget<FileModel> treeGridDropTarget;
  private RowDoubleClickHandler rowDoubleClickHandler;
  private CompleteEditHandler<FileModel> completeEditHandler;
  private CancelEditHandler<FileModel> cancelEditHandler;
  private FileModel editFileModel;

  public FileManagerViewImpl(FileManagerPresenter fileManagerPresenter, FileSystem fileSystem) {
    this.fileManagerPresenter = fileManagerPresenter;
    this.fileSystem = fileSystem;
  }

  @Override
  public Widget asWidget() {
    return getWindow();
  }

  @Override
  public void collapse() {
    FileModel fileModel = getTreeGrid().getSelectionModel().getSelectedItem();
    if (fileModel == null) {
      getTreeGrid().collapseAll();
    } else {
      getTreeGrid().setExpanded(fileModel, false, true);
    }
  }

  @Override
  public void editName(FileModel fileModel) {
    editSaveFileModel(fileModel);
    Element row = getTreeGrid().getView().getRow(fileModel);
    int rowIndex = getTreeGrid().getView().findRowIndex(row);
    getGridEditing().startEditing(new GridCell(rowIndex, 0));
  }

  @Override
  public void expand() {
    FileModel fileModel = getTreeGrid().getSelectionModel().getSelectedItem();
    if (fileModel == null) {
      getTreeGrid().expandAll();
    } else {
      getTreeGrid().setExpanded(fileModel, true, true);
    }
  }

  @Override
  public FileModel getSelectedItem() {
    return getTreeGrid().getSelectionModel().getSelectedItem();
  }

  @Override
  public List<FileModel> getSelectedItems() {
    return getTreeGrid().getSelectionModel().getSelectedItems();
  }

  @Override
  public void onHide(HideEvent event) {
    getTreeGrid().unbind();
  }

  @Override
  public void selectFileModel(FileModel fileModel) {
    getTreeGrid().setExpanded(fileModel, true);
    getTreeGrid().getSelectionModel().select(fileModel, false);
  }

  FileSystem getFileSystem() {
    return fileSystem;
  }

  /**
   * Works around a minor issue with TreeGrid and program initiated GridEditing in which the selection is lost if the
   * user clicks in the active EditField.
   */
  private void editRestoreFileModel() {
    if (editFileModel != null) {
      getTreeGrid().getSelectionModel().select(editFileModel, false);
    }
  }

  private void editSaveFileModel(FileModel editFileModel) {
    this.editFileModel = editFileModel;
  }

  private CancelEditHandler<FileModel> getCancelEditHandler() {
    if (cancelEditHandler == null) {
      cancelEditHandler = new CancelEditHandler<FileModel>() {
        @Override
        public void onCancelEdit(CancelEditEvent<FileModel> event) {
          /*
           * Works around a minor issue with GridInlineEditing in which any update operation that does not change the
           * value is reported as a cancel.
           */
          if (gridEditing.isEnter()) {
            completeEditing();
          } else {
            getFileManagerPresenter().onEditFileNameComplete(false);
          }
        }
      };
    }
    return cancelEditHandler;
  }

  private ColumnModel<FileModel> getColumnModel() {
    if (columnModel == null) {
      List<ColumnConfig<FileModel, ?>> columnConfigs = new ArrayList<ColumnConfig<FileModel, ?>>();
      columnConfigs.add(getNameConfig());
      columnConfigs.add(getDateConfig());
      columnConfigs.add(getTypeConfig());
      columnConfigs.add(getSizeConfig());
      columnModel = new ColumnModel<FileModel>(columnConfigs);
    }
    return columnModel;
  }

  private CompleteEditHandler<FileModel> getCompleteEditHandler() {
    if (completeEditHandler == null) {
      completeEditHandler = new CompleteEditHandler<FileModel>() {
        @Override
        public void onCompleteEdit(CompleteEditEvent<FileModel> event) {
          completeEditing();
        }
      };
    }
    return completeEditHandler;
  }

  private ColumnConfig<FileModel, Date> getDateConfig() {
    if (dateConfig == null) {
      dateConfig = new ColumnConfig<FileModel, Date>(getFileModelProperties().lastModified(), 100, "Date");
      dateConfig.setCell(new DateCell(DateTimeFormat.getFormat(PredefinedFormat.DATE_TIME_MEDIUM)));
    }
    return dateConfig;
  }

  private IconProvider<FileModel> getFileManagerIconProvider() {
    if (fileManagerIconProvider == null) {
      fileManagerIconProvider = new FileManagerIconProvider();
    }
    return fileManagerIconProvider;
  }

  private FileManagerMenu getFileManagerMenu() {
    if (fileManagerMenu == null) {
      fileManagerMenu = new FileManagerMenu(getFileManagerPresenter());
    }
    return fileManagerMenu;
  }

  private FileManagerPresenter getFileManagerPresenter() {
    return fileManagerPresenter;
  }

  private FileManagerSizeCell getFileManagerSizeCell() {
    if (fileManagerSizeCell == null) {
      fileManagerSizeCell = new FileManagerSizeCell(getFileSystem());
    }
    return fileManagerSizeCell;
  }

  private FileManagerToolBar getFileManagerToolBar() {
    if (fileManagerToolBar == null) {
      fileManagerToolBar = new FileManagerToolBar(getFileManagerPresenter());
      fileManagerToolBar.setButtonEnabledState();
    }
    return fileManagerToolBar;
  }

  private FileModelProperties getFileModelProperties() {
    return getFileSystem().getFileModelProperties();
  }

  private FileManagerGridInlineEditing getGridEditing() {
    if (gridEditing == null) {
      gridEditing = new FileManagerGridInlineEditing(getTreeGrid());
      gridEditing.setClicksToEdit(null);
      gridEditing.addEditor(getNameConfig(), gridEditing.getTextField());
      gridEditing.addCompleteEditHandler(getCompleteEditHandler());
      gridEditing.addCancelEditHandler(getCancelEditHandler());
    }
    return gridEditing;
  }

  private ColumnConfig<FileModel, String> getNameConfig() {
    if (nameConfig == null) {
      nameConfig = new ColumnConfig<FileModel, String>(getFileModelProperties().name(), 200, "Name");
    }
    return nameConfig;
  }

  private RowDoubleClickHandler getRowDoubleClickHandler() {
    if (rowDoubleClickHandler == null) {
      rowDoubleClickHandler = new RowDoubleClickHandler() {
        @Override
        public void onRowDoubleClick(RowDoubleClickEvent event) {
          getFileManagerPresenter().onOpen();
        }
      };
    }
    return rowDoubleClickHandler;
  }

  private SelectionChangedHandler<FileModel> getSelectionChangedHandler() {
    if (selectionChangedHandler == null) {
      selectionChangedHandler = new SelectionChangedHandler<FileModel>() {
        @Override
        public void onSelectionChanged(SelectionChangedEvent<FileModel> event) {
          FileModel fileModel = treeGrid.getSelectionModel().getSelectedItem();
          if (fileModel != null) {
            getFileManagerPresenter().onSelect(fileModel);
          }
          getFileManagerToolBar().setButtonEnabledState();
          getWindow().setHeading(getTitle(fileModel));
        }
      };
    }
    return selectionChangedHandler;
  }

  private ColumnConfig<FileModel, Long> getSizeConfig() {
    if (sizeConfig == null) {
      sizeConfig = new ColumnConfig<FileModel, Long>(getFileModelProperties().size(), 100, "Size");
      sizeConfig.setCell(getFileManagerSizeCell());
    }
    return sizeConfig;
  }

  private String getTitle(FileModel fileModel) {
    return fileModel == null ? TITLE : TITLE + " - " + getFileSystem().getPath(fileModel);
  }

  private FileManagerTreeGrid getTreeGrid() {
    if (treeGrid == null) {
      treeGrid = new FileManagerTreeGrid(getFileSystem().getTreeStore(), getColumnModel(), getNameConfig());
      treeGrid.getView().setEmptyText("Use tool bar or context menu to create files and folders.");
      treeGrid.setBorders(false);
      treeGrid.getView().setTrackMouseOver(false);
      treeGrid.getView().setForceFit(true);
      treeGrid.getView().setAutoFill(true);
      treeGrid.setIconProvider(getFileManagerIconProvider());
      treeGrid.setContextMenu(getFileManagerMenu().getFileMenu());
      treeGrid.getSelectionModel().addSelectionChangedHandler(getSelectionChangedHandler());
      treeGrid.addRowDoubleClickHandler(getRowDoubleClickHandler());
      getGridEditing();
      getTreeGridDragSource();
      getTreeGridDropTarget();
    }
    return treeGrid;
  }

  private TreeGridDragSource<FileModel> getTreeGridDragSource() {
    if (treeGridDragSource == null) {
      treeGridDragSource = new TreeGridDragSource<FileModel>(getTreeGrid());
    }
    return treeGridDragSource;
  }

  private TreeGridDropTarget<FileModel> getTreeGridDropTarget() {
    if (treeGridDropTarget == null) {
      treeGridDropTarget = new TreeGridDropTarget<FileModel>(getTreeGrid());
      treeGridDropTarget.setAllowSelfAsSource(true);
      treeGridDropTarget.setFeedback(Feedback.BOTH);
    }
    return treeGridDropTarget;
  }

  private ColumnConfig<FileModel, ?> getTypeConfig() {
    if (typeConfig == null) {
      typeConfig = new ColumnConfig<FileModel, FileType>(getFileModelProperties().fileType(), 100, "Type");
    }
    return typeConfig;
  }

  private VerticalLayoutContainer getVerticalLayoutContainer() {
    if (verticalLayoutContainer == null) {
      verticalLayoutContainer = new VerticalLayoutContainer();
      verticalLayoutContainer.setBorders(true);
      verticalLayoutContainer.add(getFileManagerToolBar(), new VerticalLayoutData(1, -1));
      verticalLayoutContainer.add(getTreeGrid(), new VerticalLayoutData(1, 1));
    }
    return verticalLayoutContainer;
  }

  private Window getWindow() {
    if (window == null) {
      window = new Window();
      window.setHeading(getTitle(null));
      window.getHeader().setIcon(Images.getImageResources().folder());
      window.setMinimizable(true);
      window.setMaximizable(true);
      window.setPixelSize(500, 400);
      window.setOnEsc(false);
      window.addHideHandler(this);
      window.setWidget(getVerticalLayoutContainer());
    }
    return window;
  }

  private void completeEditing() {
    editRestoreFileModel();
    getFileManagerPresenter().onEditFileNameComplete(true);
    // Give the change a chance to propagate to model and store
    Scheduler.get().scheduleFixedDelay(new RepeatingCommand() {
      @Override
      public boolean execute() {
        getWindow().setHeading(getTitle(getSelectedItem()));
        return false;
      }
    }, 250);
  }
}
