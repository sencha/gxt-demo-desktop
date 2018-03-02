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

import java.util.Map.Entry;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.storage.client.StorageEvent;
import com.google.gwt.storage.client.StorageEvent.Handler;
import com.google.gwt.storage.client.StorageMap;
import com.sencha.gxt.core.client.GXTLogConfiguration;

public class StorageProvider {

  private static Logger logger = Logger.getLogger(StorageProvider.class.getName());
  private Storage storage;

  public StorageProvider() {
    storage = Storage.getLocalStorageIfSupported();
    dumpStorage("initialize");
    addStorageChangeTrace();
  }

  public void clearAll() {
    if (GXTLogConfiguration.loggingIsEnabled()) {
      logger.severe("clearAll: clearing local storage");
    }
    storage.clear();
  }

  public Storage getStorage() {
    return storage;
  }

  private void addStorageChangeTrace() {
    if (GXTLogConfiguration.loggingIsEnabled()) {
      Storage.addStorageEventHandler(new Handler() {
        @Override
        public void onStorageChange(StorageEvent event) {
          String key = event.getKey();
          String oldValue = event.getOldValue();
          String newValue = event.getNewValue();
          logger.finer("onStorageChange: key=" + key + "\noldValue=" + oldValue + "\nnewValue=" + newValue);
          dumpStorage("change");
        }
      });
    }
  }

  private void dumpStorage(String reason) {
    if (GXTLogConfiguration.loggingIsEnabled() && !GWT.isProdMode()) {
      logger.finest("dumpStorage: reason=" + reason);
      if (storage == null) {
        logger.severe("local storage is not available");
      } else {
        StorageMap storageMap = new StorageMap(storage);
        for (Entry<?, ?> entry : storageMap.entrySet()) {
          logger.finest("key=" + entry.getKey() + ", value=" + entry.getValue());
        }
      }
    }
  }

}
