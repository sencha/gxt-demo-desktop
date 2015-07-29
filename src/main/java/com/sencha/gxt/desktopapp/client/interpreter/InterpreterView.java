package com.sencha.gxt.desktopapp.client.interpreter;

import com.sencha.gxt.desktopapp.client.FileBasedMiniAppView;

public interface InterpreterView extends FileBasedMiniAppView {

  void print(String value);

}