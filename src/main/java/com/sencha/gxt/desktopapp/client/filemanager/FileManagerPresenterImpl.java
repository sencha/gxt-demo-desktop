package com.sencha.gxt.desktopapp.client.filemanager;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.ui.HasWidgets;
import com.sencha.gxt.desktopapp.client.DesktopBus;
import com.sencha.gxt.desktopapp.client.event.OpenFileModelEvent;
import com.sencha.gxt.desktopapp.client.event.SelectFileModelEvent;
import com.sencha.gxt.desktopapp.client.persistence.FileModel;
import com.sencha.gxt.desktopapp.client.persistence.FileModel.FileType;
import com.sencha.gxt.desktopapp.client.persistence.FileSystem;

public class FileManagerPresenterImpl implements FileManagerPresenter {

  private FileSystem fileSystem;
  private DesktopBus desktopBus;

  private FileManagerView fileManagerView;
  private boolean isNewlyCreated;

  public FileManagerPresenterImpl(FileSystem fileSystem, DesktopBus desktopBus) {
    this.fileSystem = fileSystem;
    this.desktopBus = desktopBus;
  }

  public DesktopBus getDesktopBus() {
    return desktopBus;
  }

  @Override
  public void go(HasWidgets hasWidgets) {
    hasWidgets.add(getFileManagerView().asWidget());
  }

  @Override
  public boolean isEnableCreate() {
    FileModel selectedItem = getFileManagerView().getSelectedItem();
    FileType fileType = selectedItem == null ? null : selectedItem.getFileType();
    return fileType == null || fileType == FileType.FOLDER;
  }

  @Override
  public boolean isEnableDelete() {
    FileModel selectedItem = getFileManagerView().getSelectedItem();
    return selectedItem != null;
  }

  @Override
  public boolean isEnableEditName() {
    FileModel selectedItem = getFileManagerView().getSelectedItem();
    return selectedItem != null;
  }

  @Override
  public boolean isEnableOpen() {
    boolean isEnableOpen = false;
    FileModel selectedItem = getFileManagerView().getSelectedItem();
    if (selectedItem != null) {
      FileType fileType = selectedItem.getFileType();
      switch (fileType) {
        case BOOKMARK:
        case DOCUMENT:
        case PROGRAM:
        case SPREADSHEET:
          isEnableOpen = true;
          break;
        case FOLDER:
          // do nothing
          break;
      }
    }
    return isEnableOpen;
  }

  @Override
  public void onCollapse() {
    getFileManagerView().collapse();
  }

  @Override
  public void onCreate(FileType fileType) {
    FileModel parentFileModel = getFileManagerView().getSelectedItem();
    String name = getFileSystem().getNextUntitledFileName(parentFileModel, fileType);
    FileModel childFileModel = getFileSystem().createFileModel(parentFileModel, name, fileType);
    getFileManagerView().selectFileModel(childFileModel);
    isNewlyCreated = true;
    getFileManagerView().editName(childFileModel);
  }

  @Override
  public void onDelete() {
    List<FileModel> fileModels = getFileManagerView().getSelectedItems();
    for (FileModel fileModel : fileModels) {
      getFileSystem().remove(fileModel);
    }
  }

  @Override
  public void onEditFileNameComplete(boolean isSaved) {
    FileModel fileModel = getFileManagerView().getSelectedItem();
    if (fileModel != null) {
      fileModel.setLastModified(new Date());
    }
    if (isNewlyCreated) {
      isNewlyCreated = false;
      if (isSaved) {
        onOpen();
      } else {
        onDelete();
      }
    }
  }

  @Override
  public void onEditName() {
    FileModel selectedItem = getFileManagerView().getSelectedItem();
    if (selectedItem != null) {
      getFileManagerView().editName(selectedItem);
    }
  }

  @Override
  public void onExpand() {
    getFileManagerView().expand();
  }

  @Override
  public void onOpen() {
    List<FileModel> fileModels = getFileManagerView().getSelectedItems();
    for (FileModel fileModel : fileModels) {
      getDesktopBus().fireOpenFileModelEvent(new OpenFileModelEvent(getFileSystem(), fileModel));
    }
  }

  @Override
  public void onSelect(FileModel fileModel) {
    getDesktopBus().fireSelectFileModelEvent(new SelectFileModelEvent(fileModel));
  }

  private FileManagerView getFileManagerView() {
    if (fileManagerView == null) {
      fileManagerView = new FileManagerViewImpl(this, getFileSystem());
    }
    return fileManagerView;
  }

  private FileSystem getFileSystem() {
    return fileSystem;
  }
}
