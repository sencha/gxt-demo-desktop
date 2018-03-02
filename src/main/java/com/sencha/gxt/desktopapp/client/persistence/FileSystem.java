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
package com.sencha.gxt.desktopapp.client.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;
import com.google.web.bindery.autobean.shared.AutoBeanFactory.Category;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.data.shared.event.StoreAddEvent;
import com.sencha.gxt.data.shared.event.StoreClearEvent;
import com.sencha.gxt.data.shared.event.StoreDataChangeEvent;
import com.sencha.gxt.data.shared.event.StoreFilterEvent;
import com.sencha.gxt.data.shared.event.StoreHandlers;
import com.sencha.gxt.data.shared.event.StoreRecordChangeEvent;
import com.sencha.gxt.data.shared.event.StoreRemoveEvent;
import com.sencha.gxt.data.shared.event.StoreSortEvent;
import com.sencha.gxt.data.shared.event.StoreUpdateEvent;
import com.sencha.gxt.data.shared.event.TreeStoreRemoveEvent;
import com.sencha.gxt.desktopapp.client.persistence.FileModel.FileType;
import com.sencha.gxt.desktopapp.client.utility.Utility;

public class FileSystem {

  public interface StorageItem {

    List<String> getChildIds();

    Splittable getData();

    String getId();

    void setChildIds(List<String> children);

    void setData(Splittable data);

    void setId(String id);
  }

  @Category(AutoBeanToString.class)
  public interface StorageItemFactory extends AutoBeanFactory {
    AutoBean<StorageItem> storageItem();
  }

  public class TreeStoreHandlers implements StoreHandlers<FileModel> {

    @Override
    public void onAdd(StoreAddEvent<FileModel> event) {
      List<FileModel> files = event.getItems();
      FileModel parent = treeStore.getParent(files.get(0));
      if (parent == null) {
        persistRoot();
      } else {
        parent.setLastModified(new Date());
        update(parent);
      }
      persist(files);
    }

    @Override
    public void onClear(StoreClearEvent<FileModel> event) {
      FileSystem.this.backingStore.clear();
    }

    @Override
    public void onDataChange(StoreDataChangeEvent<FileModel> event) {
      // no-op, we won't be replacing whole sets of items
    }

    @Override
    public void onFilter(StoreFilterEvent<FileModel> event) {
      // no-op, we're going to ignore this
    }

    @Override
    public void onRecordChange(StoreRecordChangeEvent<FileModel> event) {
      // no-op, can't happen since we are autocommit=true
    }

    @Override
    public void onRemove(StoreRemoveEvent<FileModel> event) {
      TreeStoreRemoveEvent<FileModel> treeStoreRemoveEvent = (TreeStoreRemoveEvent<FileModel>) event;
      FileModel parent = treeStoreRemoveEvent.getParent();
      if (parent == null) {
        persistRoot();
      } else {
        parent.setLastModified(new Date());
        update(parent);
      }

      // get the old child and clean it and its children from storage
      remove(getStorageItem(event.getItem().getId()));
    }

    @Override
    public void onSort(StoreSortEvent<FileModel> event) {
      // no-op, we're going to ignore this
    }

    @Override
    public void onUpdate(StoreUpdateEvent<FileModel> event) {
      for (FileModel item : event.getItems()) {
        update(item);
      }
    }
  }

  private static final String ROOT_ID = "0";
  private static final String ID_KEY = "id";
  private static final String PT_PREFIX = "p";

  private BackingStore backingStore;
  private TreeStore<FileModel> treeStore;
  private FileModelProperties fileModelProperties;
  private FileModelFactory dataFactory;
  private StorageItemFactory storageFactory;

  public FileSystem(BackingStore backingStore) {
    this.backingStore = backingStore;
  }

  public FileModel createFileModel(FileModel parentFileModel, String name, FileType fileType) {

    FileModel childFileModel = getDataFactory().fileModel().as();
    childFileModel.setName(name);
    childFileModel.setFileType(fileType);
    childFileModel.setLastModified(new Date());
    childFileModel.setId(allocateId(PT_PREFIX));
    childFileModel.setSize(0l);

    if (parentFileModel == null) {
      getTreeStore().add(childFileModel);
    } else {
      getTreeStore().add(parentFileModel, childFileModel);
    }

    return childFileModel;
  }

  public FileModelProperties getFileModelProperties() {
    if (fileModelProperties == null) {
      fileModelProperties = GWT.create(FileModelProperties.class);
    }
    return fileModelProperties;
  }

  public String getNextUntitledFileName(FileModel parentFileModel, FileType fileType) {
    String nextUntitledFileName;
    List<FileModel> children;
    if (parentFileModel == null) {
      children = getTreeStore().getRootItems();
    } else {
      children = getTreeStore().getChildren(parentFileModel);
    }
    int index = 1;
    if (fileType == FileType.BOOKMARK) {
      nextUntitledFileName = "http://www.sencha.com";
    } else {
      String fileNameTemplate = Utility.capitalize(fileType.toString()) + " ";
      do {
        nextUntitledFileName = fileNameTemplate + index;
        index++;
      } while (containsName(children, nextUntitledFileName));
    }
    return nextUntitledFileName;
  }

  /**
   * Returns the parent of the specified file model or null if the parent is
   * root or the file model does not exist. This method is not necessary if
   * TreeStore.getParent is modified so that it does not assert the fileModel
   * exists.
   * 
   * @param fileModel the file model to return the parent of
   * @return the parent of the file model or null if the parent is root or the
   *         file model does not exist
   */
  public FileModel getParent(FileModel fileModel) {
    return getTreeStore().findModel(fileModel) == null ? null : getTreeStore().getParent(fileModel);
  }

  public String getPath(FileModel fileModel) {
    StringBuilder s = new StringBuilder();
    while (fileModel != null) {
      String name = fileModel.getName();
      s.insert(0, "/" + name);
      fileModel = getParent(fileModel);
    }
    return s.toString();
  }

  public StorageItem getStorageItem(FileModel model) {
    String id = model.getId();
    StorageItem item = createItem(id);
    item.setData(AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(model)));
    for (FileModel child : getTreeStore().getChildren(model)) {
      item.getChildIds().add(child.getId());
    }
    return item;
  }

  public TreeStore<FileModel> getTreeStore() {
    if (treeStore == null) {
      treeStore = new TreeStore<FileModel>(getFileModelProperties().key());
      treeStore.setAutoCommit(true);
      addChildrenToStore(getRootStorageItem(), null, treeStore);
      treeStore.addStoreHandlers(new TreeStoreHandlers());
    }
    return treeStore;
  }

  /**
   * Removes the specified file model if it exists. This method is not necessary
   * if TreeStore.remove is modified so that it does not assert the fileModel
   * exists.
   * 
   * @param fileModel the file model to remove
   * @return true if the file model existed and was removed
   */
  public boolean remove(FileModel fileModel) {
    return getTreeStore().findModel(fileModel) == null ? false : getTreeStore().remove(fileModel);
  }

  protected String allocateId(String prefix) {
    String thisId = backingStore.getItem(ID_KEY);
    if (thisId == null) {
      thisId = ROOT_ID;
    }
    String nextId = Integer.toString(Integer.parseInt(thisId) + 1);
    backingStore.setItem(ID_KEY, nextId);
    return prefix + thisId;
  }

  protected boolean containsName(List<FileModel> children, String name) {
    for (FileModel fileModel : children) {
      if (name.equals(fileModel.getName())) {
        return true;
      }
    }
    return false;
  }

  private void addChildrenToStore(StorageItem item, FileModel parent, TreeStore<FileModel> treeStore) {
    for (String childId : item.getChildIds()) {
      StorageItem child = getStorageItem(childId);
      FileModel model = AutoBeanCodex.decode(getDataFactory(), FileModel.class, child.getData()).as();
      if (parent != null) {
        treeStore.add(parent, model);
      } else {
        treeStore.add(model);
      }
      addChildrenToStore(child, model, treeStore);
    }
  }

  private StorageItem createItem(String id) {
    StorageItem item = getStorageFactory().storageItem().as();
    item.setId(id);
    item.setChildIds(new ArrayList<String>());
    return item;
  }

  private FileModelFactory getDataFactory() {
    if (dataFactory == null) {
      dataFactory = GWT.create(FileModelFactory.class);
    }
    return dataFactory;
  }

  private StorageItem getRootStorageItem() {
    StorageItem root = getStorageItem(PT_PREFIX + ROOT_ID);
    if (root == null) {
      root = createItem(allocateId(PT_PREFIX));
      persist(root);
    }
    return root;
  }

  private StorageItemFactory getStorageFactory() {
    if (storageFactory == null) {
      storageFactory = GWT.create(StorageItemFactory.class);
    }
    return storageFactory;
  }

  private StorageItem getStorageItem(String string) {
    String payload = backingStore.getItem(string);
    if (payload == null) {
      return null;
    }
    return AutoBeanCodex.decode(getStorageFactory(), StorageItem.class, payload).as();
  }

  private void persist(FileModel model) {
    persist(getStorageItem(model));
  }

  private void persist(List<FileModel> files) {
    for (FileModel file : files) {
      persist(file);
      persist(getTreeStore().getChildren(file));
    }
  }

  private void persist(StorageItem item) {
    assert item.getData() != null || item.getId().equals(PT_PREFIX + ROOT_ID) : "No data in non-root item";

    String payload = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(item)).getPayload();
    backingStore.setItem(item.getId(), payload);
  }

  private void persistRoot() {
    StorageItem item = getRootStorageItem();
    item.getChildIds().clear();
    for (FileModel child : getTreeStore().getRootItems()) {
      item.getChildIds().add(child.getId());
    }
    persist(item);
  }

  private void remove(StorageItem item) {
    backingStore.removeItem(item.getId());
    for (String child : item.getChildIds()) {
      remove(getStorageItem(child));
    }
  }

  private void update(final FileModel fileModel) {
    final long length;
    if (fileModel.getFileType() == FileType.FOLDER) {
      length = (long) getTreeStore().getChildCount(fileModel);
    } else {
      String content = fileModel.getContent();
      length = (long) (content == null ? 0 : content.length());
    }
    if (length != fileModel.getSize()) {
      fileModel.setSize(length);
      // prevents recursive update via onUpdate handler
      Scheduler.get().scheduleFinally(new ScheduledCommand() {
        @Override
        public void execute() {
          getTreeStore().update(fileModel);
        }
      });
    }
    persist(fileModel);
  }

}
