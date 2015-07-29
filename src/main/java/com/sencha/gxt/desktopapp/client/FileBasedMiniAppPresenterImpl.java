package com.sencha.gxt.desktopapp.client;

import java.util.Date;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.sencha.gxt.core.client.util.DelayedTask;
import com.sencha.gxt.core.client.util.Util;
import com.sencha.gxt.desktopapp.client.event.AddFileModelEvent;
import com.sencha.gxt.desktopapp.client.event.AddFileModelEvent.AddFileModelHandler;
import com.sencha.gxt.desktopapp.client.event.RemoveFileModelEvent;
import com.sencha.gxt.desktopapp.client.event.RemoveFileModelEvent.RemoveFileModelHandler;
import com.sencha.gxt.desktopapp.client.event.UpdateFileModelEvent;
import com.sencha.gxt.desktopapp.client.event.UpdateFileModelEvent.UpdateFileModelHandler;
import com.sencha.gxt.desktopapp.client.persistence.FileModel;
import com.sencha.gxt.desktopapp.client.persistence.FileSystem;

public abstract class FileBasedMiniAppPresenterImpl implements FileBasedMiniAppPresenter {

  protected static final int AUTO_SAVE_DELAY_MS = 750;
  protected static final int AUTO_SAVE_QUEUE_MAX = 10;

  private DesktopBus desktopBus;
  private FileSystem fileSystem;
  private FileModel fileModel;

  private FileBasedMiniAppView fileBasedMiniAppView;
  private HandlerRegistration addFileModelRegistration;
  private AddFileModelHandler addFileModelHandler;
  private HandlerRegistration updateFileModelRegistration;
  private UpdateFileModelHandler updateFileModelHandler;
  private HandlerRegistration removeFileModelRegistration;
  private RemoveFileModelHandler removeFileModelHandler;
  private String currentValue;
  private int autoSaveCount;
  private DelayedTask autoSaveDelayedTask;

  public FileBasedMiniAppPresenterImpl(DesktopBus desktopBus, FileSystem fileSystem, FileModel fileModel) {
    this.desktopBus = desktopBus;
    this.fileSystem = fileSystem;
    this.fileModel = fileModel;
  }

  @Override
  public void bind() {
    getAddFileModelRegistration();
    getUpdateFileModelRegistration();
    getRemoveFileModelRegistration();
    updateCurrentValue(getDisplayedContent(getFileModel()));
    updateTitle();
  }

  @Override
  public void go(HasWidgets hasWidgets) {
    hasWidgets.add(getFileBasedMiniAppView().asWidget());
  }

  @Override
  public void onClose() {
  }

  @Override
  public void onContentUpdate() {
    if (++autoSaveCount > AUTO_SAVE_QUEUE_MAX) {
      getAutoSaveDelayedTask().delay(0);
    } else {
      startAutoSaveTimer();
    }
  }

  @Override
  public void onSave() {
    String currentValue = getFileBasedMiniAppView().getValue();
    // if current value has changed, update store, which publishes to other
    // presenters
    if (!Util.equalWithNull(currentValue, this.currentValue)) {
      this.currentValue = currentValue;
      getFileModel().setContent(currentValue);
      getFileModel().setLastModified(new Date()); // TODO: move to FileSystem
      getFileSystem().getTreeStore().update(getFileModel());
    }
  }

  @Override
  public void unbind() {
    getAddFileModelRegistration().removeHandler();
    getUpdateFileModelRegistration().removeHandler();
    getRemoveFileModelRegistration().removeHandler();
  }

  protected abstract FileBasedMiniAppView createFileBasedMiniAppView();

  protected DelayedTask getAutoSaveDelayedTask() {
    if (autoSaveDelayedTask == null) {
      autoSaveDelayedTask = new DelayedTask() {
        @Override
        public void onExecute() {
          onSave();
          autoSaveDelayedTask.delay(AUTO_SAVE_DELAY_MS);
          autoSaveCount = 0;
        }
      };
    }
    return autoSaveDelayedTask;
  }

  protected DesktopBus getDesktopBus() {
    return desktopBus;
  }

  protected String getDisplayedContent(FileModel fileModel) {
    return fileModel.getContent();
  }

  protected FileBasedMiniAppView getFileBasedMiniAppView() {
    if (fileBasedMiniAppView == null) {
      fileBasedMiniAppView = createFileBasedMiniAppView();
    }
    return fileBasedMiniAppView;
  }

  protected FileModel getFileModel() {
    return fileModel;
  }

  protected FileSystem getFileSystem() {
    return fileSystem;
  }

  protected String getTitle() {
    return getFileSystem().getPath(fileModel);
  }

  protected void setDisplayedContent(String value) {
    getFileBasedMiniAppView().setValue(value);
  }

  protected void setValue(FileModel fileModel) {
    String currentValue = getDisplayedContent(fileModel);
    // if current value has changed, update view with current value
    if (!Util.equalWithNull(currentValue, this.currentValue)) {
      updateCurrentValue(currentValue);
    }
  }

  protected void startAutoSaveTimer() {
    getAutoSaveDelayedTask().delay(AUTO_SAVE_DELAY_MS);
  }

  protected void stopAutoSaveTimer() {
    getAutoSaveDelayedTask().cancel();
  }

  protected void updateTitle() {
    String newTitle = getTitle();
    getFileBasedMiniAppView().setTitle(newTitle);
  }

  private AddFileModelHandler getAddFileModelHandler() {
    if (addFileModelHandler == null) {
      addFileModelHandler = new AddFileModelHandler() {
        @Override
        public void onAddFileModel(AddFileModelEvent addFileModelEvent) {
          onAddFileModelEvent(addFileModelEvent);
        }
      };
    }
    return addFileModelHandler;
  }

  private HandlerRegistration getAddFileModelRegistration() {
    if (addFileModelRegistration == null) {
      addFileModelRegistration = getDesktopBus().addAddFileModelHandler(getAddFileModelHandler());
    }
    return addFileModelRegistration;
  }

  private RemoveFileModelHandler getRemoveFileModelHandler() {
    if (removeFileModelHandler == null) {
      removeFileModelHandler = new RemoveFileModelHandler() {
        @Override
        public void onRemoveFileModel(RemoveFileModelEvent removeFileModelEvent) {
          onRemoveFileModelEvent(removeFileModelEvent);
        }
      };
    }
    return removeFileModelHandler;
  }

  private HandlerRegistration getRemoveFileModelRegistration() {
    if (removeFileModelRegistration == null) {
      removeFileModelRegistration = getDesktopBus().addRemoveFileModelHandler(getRemoveFileModelHandler());
    }
    return removeFileModelRegistration;
  }

  private UpdateFileModelHandler getUpdateFileModelHandler() {
    if (updateFileModelHandler == null) {
      updateFileModelHandler = new UpdateFileModelHandler() {
        @Override
        public void onUpdateFileModel(UpdateFileModelEvent updateFileModelEvent) {
          onUpdateFileModelEvent(updateFileModelEvent);
        }
      };
    }
    return updateFileModelHandler;
  }

  private HandlerRegistration getUpdateFileModelRegistration() {
    if (updateFileModelRegistration == null) {
      updateFileModelRegistration = getDesktopBus().addUpdateFileModelHandler(getUpdateFileModelHandler());
    }
    return updateFileModelRegistration;
  }

  private void onAddFileModelEvent(AddFileModelEvent addFileModelEvent) {
    // update title on any add to pick up path name change due to move
    updateTitle();
  }

  private void onRemoveFileModelEvent(RemoveFileModelEvent removeFileModelEvent) {
    FileModel fileModel = removeFileModelEvent.getFileModel();
    if (fileModel == getFileModel()) {
      // Distinguish between remove and move (remove + add)
      Scheduler.get().scheduleDeferred(new ScheduledCommand() {
        @Override
        public void execute() {
          if (getFileSystem().getTreeStore().findModel(getFileModel()) == null) {
            getFileBasedMiniAppView().close();
          }
        }
      });
    }
  }

  private void onUpdateFileModelEvent(UpdateFileModelEvent updateFileModelEvent) {
    FileModel fileModel = updateFileModelEvent.getFileModel();
    if (fileModel == getFileModel()) {
      setValue(fileModel);
    }
    updateTitle();
  }

  private void updateCurrentValue(String currentValue) {
    this.currentValue = currentValue;
    setDisplayedContent(currentValue);
  }
}
