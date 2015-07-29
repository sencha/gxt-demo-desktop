package com.sencha.gxt.desktopapp.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.sencha.gxt.desktopapp.client.event.AddFileModelEvent.AddFileModelHandler;
import com.sencha.gxt.desktopapp.client.persistence.FileModel;

public class AddFileModelEvent extends GwtEvent<AddFileModelHandler> {

  public interface AddFileModelHandler extends EventHandler {
    void onAddFileModel(AddFileModelEvent event);
  }

  public static Type<AddFileModelHandler> TYPE = new Type<AddFileModelHandler>();
  private FileModel fileModel;

  public AddFileModelEvent(FileModel fileModel) {
    this.fileModel = fileModel;
  }

  @Override
  public Type<AddFileModelHandler> getAssociatedType() {
    return TYPE;
  }

  public FileModel getFileModel() {
    return fileModel;
  }

  @Override
  protected void dispatch(AddFileModelHandler handler) {
    handler.onAddFileModel(this);
  }
}