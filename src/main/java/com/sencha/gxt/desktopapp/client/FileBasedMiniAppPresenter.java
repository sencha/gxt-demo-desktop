package com.sencha.gxt.desktopapp.client;

public interface FileBasedMiniAppPresenter extends Presenter {

  void bind();

  void onClose();

  void onSave();

  void onContentUpdate();

  void unbind();
}
