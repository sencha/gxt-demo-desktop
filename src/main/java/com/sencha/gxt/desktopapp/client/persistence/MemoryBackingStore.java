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

import com.sencha.gxt.core.shared.FastMap;

public class MemoryBackingStore implements BackingStore {

  private String storageKeyPrefix;
  private FastMap<String> map = new FastMap<String>();

  public MemoryBackingStore(String storageKeyPrefix) {
    this.storageKeyPrefix = storageKeyPrefix;
  }

  @Override
  public void clear() {
    map.clear();
  }

  @Override
  public String getItem(String key) {
    return map.get(getStorageKey(key));
  }

  @Override
  public void removeItem(String key) {
    map.remove(getStorageKey(key));
  }

  @Override
  public void setItem(String key, String data) {
    map.put(getStorageKey(key), data);
  }

  private String getStorageKey(String key) {
    return storageKeyPrefix + "." + key;
  }

}
