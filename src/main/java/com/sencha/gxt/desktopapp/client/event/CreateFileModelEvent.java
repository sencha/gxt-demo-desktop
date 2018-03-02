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
package com.sencha.gxt.desktopapp.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.sencha.gxt.desktopapp.client.event.CreateFileModelEvent.CreateFileModelHandler;
import com.sencha.gxt.desktopapp.client.persistence.FileModel;
import com.sencha.gxt.desktopapp.client.persistence.FileModel.FileType;

public class CreateFileModelEvent extends GwtEvent<CreateFileModelHandler> {

  public interface CreateFileModelHandler extends EventHandler {
    void onCreateFileModel(CreateFileModelEvent event);
  }

  public static Type<CreateFileModelHandler> TYPE = new Type<CreateFileModelHandler>();
  private FileModel parentFileModel;
  private FileType fileType;

  public CreateFileModelEvent(FileModel parentFileModel, FileType fileType) {
    this.parentFileModel = parentFileModel;
    this.fileType = fileType;
  }

  @Override
  public Type<CreateFileModelHandler> getAssociatedType() {
    return TYPE;
  }

  public FileType getFileType() {
    return fileType;
  }

  public FileModel getParentFileModel() {
    return parentFileModel;
  }

  @Override
  protected void dispatch(CreateFileModelHandler handler) {
    handler.onCreateFileModel(this);
  }

}