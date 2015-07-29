package com.sencha.gxt.desktopapp.client.filemanager;

import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.data.shared.IconProvider;
import com.sencha.gxt.desktopapp.client.filemanager.images.Images;
import com.sencha.gxt.desktopapp.client.persistence.FileModel;
import com.sencha.gxt.desktopapp.client.persistence.FileModel.FileType;

public class FileManagerIconProvider implements IconProvider<FileModel> {

  @Override
  public ImageResource getIcon(FileModel fileModel) {
    ImageResource icon = null;
    FileType fileType = fileModel.getFileType();
    switch (fileType) {
      case BOOKMARK:
        icon = Images.getImageResources().world();
        break;
      case DOCUMENT:
        icon = Images.getImageResources().page_white();
        break;
      case FOLDER:
        icon = Images.getImageResources().folder();
        break;
      case PROGRAM:
        icon = Images.getImageResources().script();
        break;
      case SPREADSHEET:
        icon = Images.getImageResources().table();
        break;
    }
    return icon;
  }
}
