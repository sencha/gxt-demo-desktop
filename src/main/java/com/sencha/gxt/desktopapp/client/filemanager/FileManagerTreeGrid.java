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