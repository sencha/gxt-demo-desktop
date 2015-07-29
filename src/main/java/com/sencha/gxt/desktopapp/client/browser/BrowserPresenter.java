package com.sencha.gxt.desktopapp.client.browser;

import com.sencha.gxt.desktopapp.client.Presenter;

public interface BrowserPresenter extends Presenter {

  void bind();

  void onClose();

  void onSave();

  void unbind();

}