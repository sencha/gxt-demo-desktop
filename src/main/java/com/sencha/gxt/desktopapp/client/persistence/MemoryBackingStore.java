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
