package com.sencha.gxt.desktopapp.client;

import com.google.gwt.user.client.ui.IsWidget;

public interface FileBasedMiniAppView extends IsWidget {

  void close();

  String getValue();

  void setTitle(String newTitle);

  void setValue(String value);

}
