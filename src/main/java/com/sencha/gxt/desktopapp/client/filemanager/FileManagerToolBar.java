package com.sencha.gxt.desktopapp.client.filemanager;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.desktopapp.client.filemanager.images.Images;
import com.sencha.gxt.desktopapp.client.persistence.FileModel.FileType;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.toolbar.SeparatorToolItem;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

public class FileManagerToolBar implements IsWidget {

  private static final String FILE_TYPE = "fileType";

  private FileManagerPresenter fileManagerPresenter;

  private ToolBar toolBar;
  private TextButton createFolderTextButton;
  private TextButton createDocumentTextButton;
  private TextButton createSpreadsheetTextButton;
  private TextButton createProgramTextButton;
  private TextButton createBookmarkTextButton;
  private TextButton collapseTextButton;
  private TextButton expandTextButton;
  private TextButton editNameTextButton;
  private TextButton openTextButton;
  private TextButton deleteTextButton;
  private SelectHandler createSelectHandler;
  private SelectHandler expandSelectHandler;
  private SelectHandler collapseSelectHandler;
  private SelectHandler editNameSelectHandler;
  private SelectHandler openSelectHandler;
  private SelectHandler deleteSelectHandler;

  public FileManagerToolBar(FileManagerPresenter fileManagerPresenter) {
    this.fileManagerPresenter = fileManagerPresenter;
  }

  public Widget asWidget() {
    return getToolBar();
  }

  public void setButtonEnabledState() {
    boolean isEnableCreate = getPresenter().isEnableCreate();
    boolean isEnableOpen = getPresenter().isEnableOpen();
    boolean isEnableDelete = getPresenter().isEnableDelete();
    boolean isEnableEditName = getPresenter().isEnableEditName();
    getCreateFolderTextButton().setEnabled(isEnableCreate);
    getCreateDocumentTextButton().setEnabled(isEnableCreate);
    getCreateSpreadsheetTextButton().setEnabled(isEnableCreate);
    getCreateProgramTextButton().setEnabled(isEnableCreate);
    getCreateBookmarkTextButton().setEnabled(isEnableCreate);
    getExpandTextButton().setEnabled(isEnableCreate);
    getCollapseTextButton().setEnabled(isEnableCreate);
    getEditNameTextButton().setEnabled(isEnableEditName);
    getOpenTextButton().setEnabled(isEnableOpen);
    getDeleteTextButton().setEnabled(isEnableDelete);
  }

  protected FileManagerPresenter getPresenter() {
    return fileManagerPresenter;
  }

  private SelectHandler getCollapseSelectHandler() {
    if (collapseSelectHandler == null) {
      collapseSelectHandler = new SelectHandler() {
        @Override
        public void onSelect(SelectEvent event) {
          getPresenter().onCollapse();
        }
      };
    }
    return collapseSelectHandler;
  }

  private TextButton getCollapseTextButton() {
    if (collapseTextButton == null) {
      collapseTextButton = new TextButton();
      collapseTextButton.setToolTip("Collapse");
      collapseTextButton.setIcon(Images.getImageResources().arrow_in());
      collapseTextButton.addSelectHandler(getCollapseSelectHandler());
    }
    return collapseTextButton;
  }

  private TextButton getCreateBookmarkTextButton() {
    if (createBookmarkTextButton == null) {
      createBookmarkTextButton = new TextButton();
      createBookmarkTextButton.setToolTip("New Bookmark");
      createBookmarkTextButton.setIcon(Images.getImageResources().world_add());
      createBookmarkTextButton.setData(FILE_TYPE, FileType.BOOKMARK);
      createBookmarkTextButton.addSelectHandler(getCreateSelectHandler());
    }
    return createBookmarkTextButton;
  }

  private TextButton getCreateDocumentTextButton() {
    if (createDocumentTextButton == null) {
      createDocumentTextButton = new TextButton();
      createDocumentTextButton.setToolTip("New Document");
      createDocumentTextButton.setIcon(Images.getImageResources().page_white_add());
      createDocumentTextButton.setData(FILE_TYPE, FileType.DOCUMENT);
      createDocumentTextButton.addSelectHandler(getCreateSelectHandler());
    }
    return createDocumentTextButton;
  }

  private TextButton getCreateFolderTextButton() {
    if (createFolderTextButton == null) {
      createFolderTextButton = new TextButton();
      createFolderTextButton.setToolTip("New Folder");
      createFolderTextButton.setIcon(Images.getImageResources().folder_add());
      createFolderTextButton.setData(FILE_TYPE, FileType.FOLDER);
      createFolderTextButton.addSelectHandler(getCreateSelectHandler());
    }
    return createFolderTextButton;
  }

  private TextButton getCreateProgramTextButton() {
    if (createProgramTextButton == null) {
      createProgramTextButton = new TextButton();
      createProgramTextButton.setToolTip("New Program");
      createProgramTextButton.setIcon(Images.getImageResources().script_add());
      createProgramTextButton.setData(FILE_TYPE, FileType.PROGRAM);
      createProgramTextButton.addSelectHandler(getCreateSelectHandler());
    }
    return createProgramTextButton;
  }

  private SelectHandler getCreateSelectHandler() {
    if (createSelectHandler == null) {
      createSelectHandler = new SelectHandler() {
        @Override
        public void onSelect(SelectEvent event) {
          FileType fileType = ((TextButton) event.getSource()).<FileType> getData(FILE_TYPE);
          getPresenter().onCreate(fileType);
        }
      };
    }
    return createSelectHandler;
  }

  private TextButton getCreateSpreadsheetTextButton() {
    if (createSpreadsheetTextButton == null) {
      createSpreadsheetTextButton = new TextButton();
      createSpreadsheetTextButton.setToolTip("New Spreadsheet");
      createSpreadsheetTextButton.setIcon(Images.getImageResources().table_add());
      createSpreadsheetTextButton.setData(FILE_TYPE, FileType.SPREADSHEET);
      createSpreadsheetTextButton.addSelectHandler(getCreateSelectHandler());
    }
    return createSpreadsheetTextButton;
  }

  private SelectHandler getDeleteSelectHandler() {
    if (deleteSelectHandler == null) {
      deleteSelectHandler = new SelectHandler() {
        @Override
        public void onSelect(SelectEvent event) {
          getPresenter().onDelete();
        }
      };
    }
    return deleteSelectHandler;
  }

  private TextButton getDeleteTextButton() {
    if (deleteTextButton == null) {
      deleteTextButton = new TextButton();
      deleteTextButton.setToolTip("Delete");
      deleteTextButton.setIcon(Images.getImageResources().bin_closed());
      deleteTextButton.addSelectHandler(getDeleteSelectHandler());
    }
    return deleteTextButton;
  }

  private SelectHandler getEditNameSelectHandler() {
    if (editNameSelectHandler == null) {
      editNameSelectHandler = new SelectHandler() {
        @Override
        public void onSelect(SelectEvent event) {
          getPresenter().onEditName();
        }
      };
    }
    return editNameSelectHandler;
  }

  private TextButton getEditNameTextButton() {
    if (editNameTextButton == null) {
      editNameTextButton = new TextButton();
      editNameTextButton.setToolTip("Edit Name");
      editNameTextButton.setIcon(Images.getImageResources().textfield_rename());
      editNameTextButton.addSelectHandler(getEditNameSelectHandler());
    }
    return editNameTextButton;
  }

  private SelectHandler getExpandSelectHandler() {
    if (expandSelectHandler == null) {
      expandSelectHandler = new SelectHandler() {
        @Override
        public void onSelect(SelectEvent event) {
          getPresenter().onExpand();
        }
      };
    }
    return expandSelectHandler;
  }

  private TextButton getExpandTextButton() {
    if (expandTextButton == null) {
      expandTextButton = new TextButton();
      expandTextButton.setToolTip("Expand");
      expandTextButton.addSelectHandler(getExpandSelectHandler());
      expandTextButton.setIcon(Images.getImageResources().arrow_out());
    }
    return expandTextButton;
  }

  private SelectHandler getOpenSelectHandler() {
    if (openSelectHandler == null) {
      openSelectHandler = new SelectHandler() {
        @Override
        public void onSelect(SelectEvent event) {
          getPresenter().onOpen();
        }
      };
    }
    return openSelectHandler;
  }

  private TextButton getOpenTextButton() {
    if (openTextButton == null) {
      openTextButton = new TextButton();
      openTextButton.setToolTip("Open");
      openTextButton.setIcon(Images.getImageResources().bullet_go());
      openTextButton.addSelectHandler(getOpenSelectHandler());
    }
    return openTextButton;
  }

  private Widget getToolBar() {
    if (toolBar == null) {
      toolBar = new ToolBar();
      toolBar.add(getCreateFolderTextButton());
      toolBar.add(new SeparatorToolItem());
      toolBar.add(getCreateDocumentTextButton());
      toolBar.add(getCreateSpreadsheetTextButton());
      toolBar.add(getCreateProgramTextButton());
      toolBar.add(getCreateBookmarkTextButton());
      toolBar.add(new SeparatorToolItem());
      toolBar.add(getExpandTextButton());
      toolBar.add(getCollapseTextButton());
      toolBar.add(new SeparatorToolItem());
      toolBar.add(getEditNameTextButton());
      toolBar.add(new SeparatorToolItem());
      toolBar.add(getOpenTextButton());
      toolBar.add(new SeparatorToolItem());
      toolBar.add(getDeleteTextButton());
    }
    return toolBar;
  }

}
