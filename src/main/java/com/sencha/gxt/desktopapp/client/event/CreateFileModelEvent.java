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