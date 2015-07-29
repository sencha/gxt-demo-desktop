package com.sencha.gxt.desktopapp.client.filemanager;

import com.sencha.gxt.desktopapp.client.Presenter;
import com.sencha.gxt.desktopapp.client.persistence.FileModel;
import com.sencha.gxt.desktopapp.client.persistence.FileModel.FileType;

public interface FileManagerPresenter extends Presenter {

  boolean isEnableCreate();

  boolean isEnableDelete();

  boolean isEnableEditName();

  boolean isEnableOpen();

  void onCollapse();

  void onCreate(FileType fileType);

  void onDelete();

  void onEditFileNameComplete(boolean isSaved);

  void onEditName();

  void onExpand();

  void onOpen();

  void onSelect(FileModel fileModel);

}