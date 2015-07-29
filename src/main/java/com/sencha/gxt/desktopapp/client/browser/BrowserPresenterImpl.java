package com.sencha.gxt.desktopapp.client.browser;

import com.sencha.gxt.desktopapp.client.DesktopBus;
import com.sencha.gxt.desktopapp.client.FileBasedMiniAppPresenterImpl;
import com.sencha.gxt.desktopapp.client.FileBasedMiniAppView;
import com.sencha.gxt.desktopapp.client.persistence.FileModel;
import com.sencha.gxt.desktopapp.client.persistence.FileSystem;

public class BrowserPresenterImpl extends FileBasedMiniAppPresenterImpl implements BrowserPresenter {

  public BrowserPresenterImpl(DesktopBus desktopBus, FileSystem fileSystem, FileModel fileModel) {
    super(desktopBus, fileSystem, fileModel);
  }

  @Override
  protected FileBasedMiniAppView createFileBasedMiniAppView() {
    return new BrowserViewImpl(this);
  }

  @Override
  protected String getDisplayedContent(FileModel fileModel) {
    return fileModel.getName();
  }

  @Override
  protected String getTitle() {
    return "Browser - " + super.getTitle();
  }

  protected boolean isModified() {
    return false;
  }

}
