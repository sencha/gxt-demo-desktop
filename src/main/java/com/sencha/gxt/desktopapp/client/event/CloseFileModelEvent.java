package com.sencha.gxt.desktopapp.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.sencha.gxt.desktopapp.client.event.CloseFileModelEvent.CloseFileModelHandler;
import com.sencha.gxt.desktopapp.client.persistence.FileModel;

public class CloseFileModelEvent extends GwtEvent<CloseFileModelHandler> {

  public interface CloseFileModelHandler extends EventHandler {
    void onCloseFileModel(CloseFileModelEvent event);
  }

  public static Type<CloseFileModelHandler> TYPE = new Type<CloseFileModelHandler>();
  private FileModel fileModel;
  private String content;

  public CloseFileModelEvent(FileModel fileModel, String content) {
    this.fileModel = fileModel;
    this.content = content;
  }

  @Override
  public Type<CloseFileModelHandler> getAssociatedType() {
    return TYPE;
  }

  public String getContent() {
    return content;
  }

  public FileModel getFileModel() {
    return fileModel;
  }

  @Override
  protected void dispatch(CloseFileModelHandler handler) {
    handler.onCloseFileModel(this);
  }

}