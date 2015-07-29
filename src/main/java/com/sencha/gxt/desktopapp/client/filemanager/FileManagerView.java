package com.sencha.gxt.desktopapp.client.filemanager;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.sencha.gxt.desktopapp.client.persistence.FileModel;

public interface FileManagerView extends IsWidget {

  public void collapse();

  public void editName(FileModel childFileModel);

  public void expand();

  public FileModel getSelectedItem();

  public List<FileModel> getSelectedItems();

  public void selectFileModel(FileModel fileModel);

}