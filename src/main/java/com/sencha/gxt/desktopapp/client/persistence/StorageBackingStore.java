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
