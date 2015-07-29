package com.sencha.gxt.desktopapp.client.persistence;

public interface BackingStore {

  void clear();

  String getItem(String key);

  void removeItem(String key);

  void setItem(String key, String data);

}
