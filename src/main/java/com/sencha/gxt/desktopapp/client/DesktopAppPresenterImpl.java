package com.sencha.gxt.desktopapp.client;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.sencha.gxt.core.shared.FastMap;
import com.sencha.gxt.data.shared.event.StoreAddEvent;
import com.sencha.gxt.data.shared.event.StoreAddEvent.StoreAddHandler;
import com.sencha.gxt.data.shared.event.StoreRemoveEvent;
import com.sencha.gxt.data.shared.event.StoreRemoveEvent.StoreRemoveHandler;
import com.sencha.gxt.data.shared.event.StoreUpdateEvent;
import com.sencha.gxt.data.shared.event.StoreUpdateEvent.StoreUpdateHandler;
import com.sencha.gxt.data.shared.event.TreeStoreRemoveEvent;
import com.sencha.gxt.desktop.client.widget.Desktop;
import com.sencha.gxt.desktopapp.client.browser.BrowserPresenterImpl;
import com.sencha.gxt.desktopapp.client.event.AddFileModelEvent;
import com.sencha.gxt.desktopapp.client.event.LoginEvent;
import com.sencha.gxt.desktopapp.client.event.LogoutEvent;
import com.sencha.gxt.desktopapp.client.event.OpenFileModelEvent;
import com.sencha.gxt.desktopapp.client.event.OpenFileModelEvent.OpenFileModelHandler;
import com.sencha.gxt.desktopapp.client.event.RemoveFileModelEvent;
import com.sencha.gxt.desktopapp.client.event.UpdateFileModelEvent;
import com.sencha.gxt.desktopapp.client.event.UpdateProfileEvent;
import com.sencha.gxt.desktopapp.client.filemanager.FileManagerPresenterImpl;
import com.sencha.gxt.desktopapp.client.interpreter.InterpreterPresenterImpl;
import com.sencha.gxt.desktopapp.client.persistence.BackingStore;
import com.sencha.gxt.desktopapp.client.persistence.FileModel;
import com.sencha.gxt.desktopapp.client.persistence.FileSystem;
import com.sencha.gxt.desktopapp.client.persistence.MemoryBackingStore;
import com.sencha.gxt.desktopapp.client.persistence.StorageBackingStore;
import com.sencha.gxt.desktopapp.client.persistence.StorageProvider;
import com.sencha.gxt.desktopapp.client.spreadsheet.SpreadsheetPresenterImpl;
import com.sencha.gxt.desktopapp.client.wordprocessor.WordProcessorPresenterImpl;
import com.sencha.gxt.widget.core.client.info.Info;

/**
 * Presents the desktop application to the user.
 * 
 * @see DesktopAppPresenter
 */
public class DesktopAppPresenterImpl implements DesktopAppPresenter {

  private static final String STORAGE_KEY_PREFIX = "com.sencha.gxt.desktop.client.desktopapp";
  private static final String STORAGE_KEY_PREFIX_SYSTEM = STORAGE_KEY_PREFIX + ".system";
  private static final String STORAGE_KEY_PREFIX_USER = STORAGE_KEY_PREFIX + ".user";

  private static final String CURRENT_USER_KEY = "currentUser";
  private static final String PROFILE_KEY = "profile";

  private DesktopBus desktopBus;

  private FileSystem fileSystem;
  private StorageProvider storageProvider;
  private ProfileFactory profileFactory;
  private Boolean isLocalStorageSupported;
  private FastMap<BackingStore> backingStoreCache = new FastMap<BackingStore>();
  private OpenFileModelHandler openFileModelHandler;
  private StoreAddHandler<FileModel> storeAddHandler;
  private StoreUpdateHandler<FileModel> storeUpdateHandler;
  private StoreRemoveHandler<FileModel> storeRemoveHandler;
  private DesktopAppView desktopAppView;
  private String currentUser;

  /**
   * Creates a desktop application presenter that communicates with the rest of
   * the application using the specified desktop bus.
   * 
   * @param desktopBus the application bus
   */
  public DesktopAppPresenterImpl(DesktopBus desktopBus) {
    this.desktopBus = desktopBus;
    desktopBus.addOpenFileModelHandler(getOpenFileModelHandler());
  }

  @Override
  public void clearLocalStorage() {
    getStorageProvider().clearAll();
  }

  @Override
  public String getCurrentUser() {
    if (currentUser == null) {
      currentUser = getSystemBackingStore().getItem(CURRENT_USER_KEY);
    }
    return currentUser;
  }

  @Override
  public FileSystem getFileSystem() {
    if (fileSystem == null) {
      fileSystem = new FileSystem(getUserBackingStore(currentUser));
      fileSystem.getTreeStore().addStoreAddHandler(getStoreAddHandler());
      fileSystem.getTreeStore().addStoreUpdateHandler(getStoreUpdateHandler());
      fileSystem.getTreeStore().addStoreRemoveHandler(getStoreRemoveHandler());
    }
    return fileSystem;
  }

  @Override
  public ProfileModel getProfile() {
    ProfileModel profileModel = getProfile(currentUser);
    return profileModel;
  }

  @Override
  public void go(HasWidgets hasWidgets) {
    hasWidgets.add(getDesktopAppView().asWidget());
    getDesktopAppView().setDesktopLayoutType(getProfile().getDesktopLayoutType());
    getDesktopAppView().setScaleOnResize(getProfile().isScaleOnResize());
  }

  @Override
  public boolean isLocalStorageSupported() {
    if (!isLocalStorageSupportChecked()) {
      isLocalStorageSupported = Storage.isLocalStorageSupported();
    }
    return isLocalStorageSupported;
  }

  @Override
  public boolean isLoggedIn() {
    return getCurrentUser() != null;
  }

  @Override
  public DesktopAppPresenter.LoginStatus onLogin(LoginModel loginModel) {
    String userName = loginModel.getUserName();
    String password = loginModel.getPassword();
    boolean isNewUser = loginModel.isNewUser();
    if (isExistingUser(userName)) {
      if (isNewUser) {
        return DesktopAppPresenter.LoginStatus.DUPLICATE_USER_NAME;
      } else if (!validCredentials(userName, password)) {
        return DesktopAppPresenter.LoginStatus.INVALID_NAME_OR_PASSWORD;
      }
    } else {
      if (isNewUser) {
        createNewUser(userName);
      } else {
        return DesktopAppPresenter.LoginStatus.INVALID_NAME_OR_PASSWORD;
      }
    }
    setCurrentUser(userName);
    getDesktopBus().fireLoginEvent(new LoginEvent(userName, isNewUser));
    return DesktopAppPresenter.LoginStatus.SUCCESS;
  }

  @Override
  public void onLogout() {
    currentUser = null;
    getSystemBackingStore().removeItem(CURRENT_USER_KEY);
    getDesktopBus().fireLogoutEvent(new LogoutEvent());
  }

  @Override
  public void onOpenFileManager() {
    new FileManagerPresenterImpl(getFileSystem(), getDesktopBus()).go(getDesktopAppView());
  }

  @Override
  public void onOpenProfile() {
    getDesktopBus().invokeProfileService();
  }

  @Override
  public DesktopAppPresenter.ProfileStatus onUpdateProfile(ProfileModel profileModel) {
    boolean isUpdate = profileModel != null;
    if (isUpdate) {
      setProfile(profileModel);
      getDesktopAppView().setDesktopLayoutType(profileModel.getDesktopLayoutType());
      getDesktopAppView().setScaleOnResize(profileModel.isScaleOnResize());
    }
    getDesktopBus().fireUpdateProfileEvent(new UpdateProfileEvent(profileModel, isUpdate));
    return DesktopAppPresenter.ProfileStatus.SUCCESS;
  }

  protected boolean validCredentials(String userName, String password) {
    return true;
  }

  private BackingStore createBackingStore(String storageKeyPrefix) {
    BackingStore backingStore;
    if (!isLocalStorageSupportChecked()) {
      // Prevent unintentional use of MemoryBackingStore
      throw new IllegalStateException("Must invoke isLocalStorageSupported before invoking getBackingStore");
    }
    if (!isLocalStorageSupported()) {
      backingStore = new MemoryBackingStore(storageKeyPrefix);
    } else {
      backingStore = new StorageBackingStore(getStorageProvider().getStorage(), storageKeyPrefix);
    }
    return backingStore;
  }

  private void createNewUser(String userName) {
    if (isExistingUser(userName)) {
      throw new IllegalStateException("user " + userName + " already exists");
    }
    AutoBean<ProfileModel> profileAutoBean = getProfileAutoBeanFactory().profileModel();
    ProfileModel profileModel = profileAutoBean.as();
    initializeProfile(profileModel);
    setProfile(profileModel, userName);
  }

  private BackingStore getBackingStore(String storeName) {
    BackingStore backingStore = backingStoreCache.get(storeName);
    if (backingStore == null) {
      backingStore = createBackingStore(storeName);
      backingStoreCache.put(storeName, backingStore);
    }
    return backingStore;
  }

  private DesktopAppView getDesktopAppView() {
    if (desktopAppView == null) {
      desktopAppView = new DesktopAppViewImpl(this);
    }
    return desktopAppView;
  }

  private DesktopBus getDesktopBus() {
    return desktopBus;
  }

  private OpenFileModelHandler getOpenFileModelHandler() {
    if (openFileModelHandler == null) {
      openFileModelHandler = new OpenFileModelHandler() {
        @Override
        public void onOpenFileModel(OpenFileModelEvent openFileModelEvent) {
          openFileModel(openFileModelEvent);
        }
      };
    }
    return openFileModelHandler;
  }

  private ProfileModel getProfile(String userName) {
    String payload = getUserBackingStore(userName).getItem(PROFILE_KEY);
    ProfileModel profileModel = AutoBeanCodex.decode(getProfileAutoBeanFactory(), ProfileModel.class, payload).as();
    return profileModel;
  }

  private ProfileFactory getProfileAutoBeanFactory() {
    if (profileFactory == null) {
      profileFactory = GWT.create(ProfileFactory.class);
    }
    return profileFactory;
  }

  private StorageProvider getStorageProvider() {
    if (storageProvider == null) {
      storageProvider = new StorageProvider();
    }
    return storageProvider;
  }

  private StoreAddHandler<FileModel> getStoreAddHandler() {
    if (storeAddHandler == null) {
      storeAddHandler = new StoreAddHandler<FileModel>() {
        @Override
        public void onAdd(StoreAddEvent<FileModel> event) {
          for (FileModel fileModel : event.getItems()) {
            getDesktopBus().fireAddFileModelEvent(new AddFileModelEvent(fileModel));
          }
        }
      };
    }
    return storeAddHandler;
  }

  private StoreRemoveHandler<FileModel> getStoreRemoveHandler() {
    if (storeRemoveHandler == null) {
      storeRemoveHandler = new StoreRemoveHandler<FileModel>() {
        @Override
        public void onRemove(StoreRemoveEvent<FileModel> event) {
          getDesktopBus().fireRemoveFileModelEvent(new RemoveFileModelEvent(event.getItem()));
          List<FileModel> children = ((TreeStoreRemoveEvent<FileModel>) event).getChildren();
          for (FileModel child : children) {
            getDesktopBus().fireRemoveFileModelEvent(new RemoveFileModelEvent(child));
          }
        }
      };
    }
    return storeRemoveHandler;
  }

  private StoreUpdateHandler<FileModel> getStoreUpdateHandler() {
    if (storeUpdateHandler == null) {
      storeUpdateHandler = new StoreUpdateHandler<FileModel>() {
        @Override
        public void onUpdate(StoreUpdateEvent<FileModel> event) {
          for (FileModel fileModel : event.getItems()) {
            getDesktopBus().fireUpdateFileModelEvent(new UpdateFileModelEvent(fileModel));
          }
        }
      };
    }
    return storeUpdateHandler;
  }

  private BackingStore getSystemBackingStore() {
    return getBackingStore(STORAGE_KEY_PREFIX_SYSTEM);
  }

  private BackingStore getUserBackingStore(String userName) {
    if (userName == null) {
      throw new IllegalArgumentException("userName cannot be null");
    }
    return getBackingStore(STORAGE_KEY_PREFIX_USER + "." + userName);
  }

  private void initializeProfile(ProfileModel profileModel) {
    profileModel.setDesktopLayoutType(Desktop.DEFAULT_DESKTOP_LAYOUT_TYPE);
  }

  private boolean isExistingUser(String userName) {
    return getUserBackingStore(userName).getItem(PROFILE_KEY) != null;
  }

  private boolean isLocalStorageSupportChecked() {
    return isLocalStorageSupported != null;
  }

  private void openFileModel(OpenFileModelEvent openFileModelEvent) {
    Presenter presenter = null;
    FileSystem fileSystem = openFileModelEvent.getFileSystem();
    FileModel fileModel = openFileModelEvent.getFileModel();
    switch (fileModel.getFileType()) {
      case BOOKMARK:
        presenter = new BrowserPresenterImpl(getDesktopBus(), fileSystem, fileModel);
        break;
      case DOCUMENT:
        presenter = new WordProcessorPresenterImpl(getDesktopBus(), fileSystem, fileModel);
        break;
      case FOLDER:
        // no action currently supported
        break;
      case PROGRAM:
        presenter = new InterpreterPresenterImpl(getDesktopBus(), fileSystem, fileModel);
        break;
      case SPREADSHEET:
        presenter = new SpreadsheetPresenterImpl(getDesktopBus(), fileSystem, fileModel);
        break;
      default:
        Info.display("Desktop", "You opened " + fileModel.getName() + ", which is of type " + fileModel.getFileType()
            + " and currently not supported.");
        break;
    }
    if (presenter != null) {
      presenter.go(getDesktopAppView());
    }
  }

  private void setCurrentUser(String currentUser) {
    this.currentUser = currentUser;
    getSystemBackingStore().setItem(CURRENT_USER_KEY, currentUser);
  }

  private void setProfile(ProfileModel profileModel) {
    setProfile(profileModel, currentUser);
  }

  private void setProfile(ProfileModel profileModel, String userName) {
    String payload = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(profileModel)).getPayload();
    getUserBackingStore(userName).setItem(PROFILE_KEY, payload);
  }

}
