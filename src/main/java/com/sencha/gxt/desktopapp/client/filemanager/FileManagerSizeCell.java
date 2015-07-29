package com.sencha.gxt.desktopapp.client.filemanager;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.desktopapp.client.persistence.FileModel;
import com.sencha.gxt.desktopapp.client.persistence.FileSystem;

public class FileManagerSizeCell extends AbstractCell<Long> {
  private FileSystem fileSystem;

  public FileManagerSizeCell(FileSystem fileSystem) {
    this.fileSystem = fileSystem;
  }

  @Override
  public void render(Context context, Long size, SafeHtmlBuilder sb) {
    if (size == null) {
      size = Long.valueOf(0);
    }
    sb.append(size);
    String key = (String) context.getKey();
    FileModel fileModel = fileSystem.getTreeStore().findModelWithKey(key);
    switch (fileModel.getFileType()) {
      case FOLDER:
        if (size == 1) {
          sb.appendEscaped(" File");
        } else {
          sb.appendEscaped(" Files");
        }
        break;
      default:
        if (size == 1) {
          sb.appendEscaped(" Byte");
        } else {
          sb.appendEscaped(" Bytes");
        }
        break;
    }
  }
}