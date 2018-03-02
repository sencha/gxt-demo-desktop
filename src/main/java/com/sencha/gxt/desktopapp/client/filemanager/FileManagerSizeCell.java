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