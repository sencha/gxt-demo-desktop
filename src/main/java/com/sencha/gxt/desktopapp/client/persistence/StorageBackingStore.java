package com.sencha.gxt.desktopapp.client.persistence;

import java.util.Iterator;

import com.google.gwt.storage.client.Storage;
import com.google.gwt.storage.client.StorageMap;

// TODO: Consider decorator pattern for encryption layer support

public class StorageBackingStore implements BackingStore {

  private static final String DELIMITER = ".";

  private Storage storage;
  private String storageKeyPrefix;

  public StorageBackingStore(Storage storage, String storageKeyPrefix) {
    this.storage = storage;
    this.storageKeyPrefix = storageKeyPrefix;
  }

  @Override
  public void clear() {
    String qualifiedStorageKeyPrefix = storageKeyPrefix + DELIMITER;
    StorageMap storageMap = new StorageMap(storage);
    Iterator<String> iterator = storageMap.keySet().iterator();
    while (iterator.hasNext()) {
      String key = iterator.next();
      if (key.startsWith(qualifiedStorageKeyPrefix)) {
        iterator.remove();
      }
    }
  }

  @Override
  public String getItem(String key) {
    return storage.getItem(getStorageKey(key));
  }

  @Override
  public void removeItem(String key) {
    storage.removeItem(getStorageKey(key));
  }

  @Override
  public void setItem(String key, String data) {
    storage.setItem(getStorageKey(key), data);
  }

  private String getStorageKey(String key) {
    return storageKeyPrefix + DELIMITER + key;
  }

}
