package com.sencha.gxt.desktopapp.client.wordprocessor;

import com.sencha.gxt.desktopapp.client.DesktopBus;
import com.sencha.gxt.desktopapp.client.FileBasedMiniAppPresenterImpl;
import com.sencha.gxt.desktopapp.client.persistence.FileModel;
import com.sencha.gxt.desktopapp.client.persistence.FileSystem;

public class WordProcessorPresenterImpl extends FileBasedMiniAppPresenterImpl implements WordProcessorPresenter {

  public WordProcessorPresenterImpl(DesktopBus desktopBus, FileSystem fileSystem, FileModel fileModel) {
    super(desktopBus, fileSystem, fileModel);
  }

  @Override
  public void bind() {
    super.bind();
    startAutoSaveTimer();
  }

  @Override
  public void unbind() {
    stopAutoSaveTimer();
    super.unbind();
  }

  @Override
  protected WordProcessorViewImpl createFileBasedMiniAppView() {
    return new WordProcessorViewImpl(this);
  }

  @Override
  protected String getTitle() {
    return "Word Processor - " + super.getTitle();
  }

}
