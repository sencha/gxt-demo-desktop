package com.sencha.gxt.desktopapp.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.sencha.gxt.desktopapp.client.event.RemoveFileModelEvent.RemoveFileModelHandler;
import com.sencha.gxt.desktopapp.client.persistence.FileModel;

public class RemoveFileModelEvent extends GwtEvent<RemoveFileModelHandler> {

  public interface RemoveFileModelHandler extends EventHandler {
    void onRemoveFileModel(RemoveFileModelEvent event);
  }

  public static Type<RemoveFileModelHandler> TYPE = new Type<RemoveFileModelHandler>();
  private FileModel fileModel;

  public RemoveFileModelEvent(FileModel fileModel) {
    this.fileModel = fileModel;
  }

  @Override
  public Type<RemoveFileModelHandler> getAssociatedType() {
    return TYPE;
  }

  public FileModel getFileModel() {
    return fileModel;
  }

  @Override
  protected void dispatch(RemoveFileModelHandler handler) {
    handler.onRemoveFileModel(this);
  }
}