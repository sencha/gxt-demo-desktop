package com.sencha.gxt.desktopapp.client.filemanager;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.user.client.Event;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.desktopapp.client.persistence.FileModel;
import com.sencha.gxt.desktopapp.client.persistence.FileModel.FileType;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.treegrid.TreeGrid;

public class FileManagerTreeGrid extends TreeGrid<FileModel> {

  public FileManagerTreeGrid(TreeStore<FileModel> store, ColumnModel<FileModel> cm,
      ColumnConfig<FileModel, ?> treeColumn) {
    super(store, cm, treeColumn);
  }

  @Override
  public boolean isLeaf(FileModel model) {
    return model.getFileType() != FileType.FOLDER;
  }

  public void unbind() {
    if (storeHandlerRegistration != null) {
      storeHandlerRegistration.removeHandler();
    }
  }

  @Override
  protected void onClick(Event event) {
    super.onClick(event);
    EventTarget eventTarget = event.getEventTarget();
    if (Element.is(eventTarget)) {
      FileModel m = store.get(getView().findRowIndex(Element.as(eventTarget)));
      if (m == null) {
        getSelectionModel().deselectAll();
      }
    }
  }
}