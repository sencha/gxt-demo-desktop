/**
 * Sencha GXT 1.0.0-SNAPSHOT - Sencha for GWT
 * Copyright (c) 2006-2018, Sencha Inc.
 *
 * licensing@sencha.com
 * http://www.sencha.com/products/gxt/license/
 *
 * ================================================================================
 * Commercial License
 * ================================================================================
 * This version of Sencha GXT is licensed commercially and is the appropriate
 * option for the vast majority of use cases.
 *
 * Please see the Sencha GXT Licensing page at:
 * http://www.sencha.com/products/gxt/license/
 *
 * For clarification or additional options, please contact:
 * licensing@sencha.com
 * ================================================================================
 *
 *
 *
 *
 *
 *
 *
 *
 * ================================================================================
 * Disclaimer
 * ================================================================================
 * THIS SOFTWARE IS DISTRIBUTED "AS-IS" WITHOUT ANY WARRANTIES, CONDITIONS AND
 * REPRESENTATIONS WHETHER EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE
 * IMPLIED WARRANTIES AND CONDITIONS OF MERCHANTABILITY, MERCHANTABLE QUALITY,
 * FITNESS FOR A PARTICULAR PURPOSE, DURABILITY, NON-INFRINGEMENT, PERFORMANCE AND
 * THOSE ARISING BY STATUTE OR FROM CUSTOM OR USAGE OF TRADE OR COURSE OF DEALING.
 * ================================================================================
 */
package com.sencha.gxt.desktopapp.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.sencha.gxt.desktop.client.layout.DesktopLayoutType;
import com.sencha.gxt.desktop.client.widget.ShortcutCell.ShortcutCellAppearance;
import com.sencha.gxt.desktopapp.client.event.LoginEvent;
import com.sencha.gxt.desktopapp.client.event.LoginEvent.LoginHandler;
import com.sencha.gxt.desktopapp.client.event.LogoutEvent;
import com.sencha.gxt.desktopapp.client.event.LogoutEvent.LogoutHandler;
import com.sencha.gxt.desktopapp.client.service.LoginServiceProvider;
import com.sencha.gxt.desktopapp.client.service.ProfileServiceProvider;
import com.sencha.gxt.desktopapp.client.utility.Prompt;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.form.HtmlEditor;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.treegrid.TreeGrid;

/**
 * A sample application that provides a basic desktop capability in a browser window. This includes a start menu,
 * shortcuts and a task bar with buttons for each active application, as well as support for multiple users, each with
 * their own profile settings. Desktop applications include:
 * <dl>
 * <dt>File Manager</dt>
 * <dd>Uses a {@link TreeGrid} to provides access to a hierarchical, browser-based file system, which is persistent on
 * browsers that support HTML5 Local Storage. Users may create folders, documents, browser shortcuts, and other files,
 * rename files and drag and drop to move files in the file system. As file operations occur, open applications respond
 * appropriately (e.g. to indicate renamed or moved files).</dd>
 * <dt>Word Processor</dt>
 * <dd>Uses a {@link HtmlEditor} to provides a simple rich editing capability with documents that can be stored locally
 * in the browser based file system. Automatically propagates saved changes to other Word Processor windows, closes if
 * the document is deleted in File Manager, as well as handling documents that are renamed or moved using File
 * Manager.</dd>
 * <dt>Browser</dt>
 * <dd>Uses a {@link Frame} to provide a browser-within-a-browser that displays a browser web page within the desktop.
 * Although this may sound like a gimmick, it's amazing how useful it can be to display multiple web pages
 * simultaneously in conjunction with the {@link DesktopLayoutType#TILE} feature.</dd>
 * <dt>Spreadsheet</dt>
 * <dd>Uses a {@link Grid} to provide a simple spreadsheet capability, including sorting, column and row move, insert
 * and delete as well as formulas and charts.</dd>
 * <dt>Program</dt>
 * <dd>Uses <code>JSNI</code> to provide a simple JavaScript snippet interpreter, including use of references to the
 * page's window object and call-backs to a Java method.</dd>
 * </dl>
 * In addition to highlighting the capability of the GXT components and providing useful, reusable function, the desktop
 * application illustrates a number of best practices for creating complex, interactive real-world applications.
 * <p/>
 * <ul>
 * <li>Use of an enhanced Model View Presenter (MVP) pattern to improve separation of concerns and simplify
 * testing.</li>
 * <ul>
 * <li>Model - the structured data used by the application</li>
 * <li>View - the components that display information and receive user input. The View receives data and commands from
 * the Presenter via the View interface and sends Events to the Presenter using the Presenter interface. The actual
 * widgets the View uses to display the data are hidden from the Presenter. To paraphrase Einstein, "The View is as
 * simple as possible, but no simpler."</li>
 * <li>Presenter - the components that prepare Models for display and act on events from the user. The Presenter sends
 * commands and data to the View via the View interface. The Presenter receives events from the View via the Presenter
 * interface. The Presenter is generally responsible for creating the View and connecting it to the application's user
 * interface.</li>
 * </ul>
 * <li>Use of a Bus to decouple providers and consumers</li>
 * <ul>
 * <li>Event Bus - broadcast application level events from one producer to zero or more consumers</li>
 * <li>Service Bus - request a service (with optional response) from one and only one provider</li>
 * </ul>
 * <li>Use of encapsulated field access in declaring type to support lazy instantiation and initialization as well as
 * implicit order of execution</li>
 * </ul>
 * <p/>
 * In addition, this application illustrates the use of a number of GWT and Sencha technologies, including:
 * <ul>
 * <li>GWT Editor / Driver Framework - {@link LoginView}</li>
 * <li>GWT AutoBeans - {@link ProfileModel} and {@link ProfileFactory}</li>
 * <li>GWT / Sencha Appearances - {@link ShortcutCellAppearance}</li>
 * </ul>
 * <p/>
 * If the browser does not support HTML5 Local Storage, or it is not configured for use with the application, this
 * application will prompt the user whether to degrade gracefully to temporary (in-memory) storage that will be freed
 * when the browser terminates or the the browser window is refreshed.
 * <p/>
 * To request that the desktop application prompt the user to clear all local storage for all users of this application,
 * add <code>#clear</code> to the application URL.
 * <p/>
 * This sample application is under continuing development. The API is subject to change without notice.
 */
public class DesktopApp implements EntryPoint {

  private DesktopBus desktopBus;
  private DesktopAppPresenter desktopAppPresenter;
  private LoginHandler loginHandler;
  private LoginServiceProvider loginServiceProvider;
  private ProfileServiceProvider profileServiceProvider;
  private LogoutHandler logoutHandler;
  private LoginPresenter loginPresenter;
  private ProfilePresenter profilePresenter;

  public void loadModule(HasWidgets hasWidgets) {
    setBackground(hasWidgets);
    initializeDesktopBus(hasWidgets);
    if (!getDesktopAppPresenter().isLocalStorageSupported()) {
      loadApplicationAfterAlertingUser(hasWidgets);
    } else {
      loadApplication(hasWidgets);
    }
  }

  @Override
  public void onModuleLoad() {
    setUncaughtExceptionHandler();
    loadModule(RootPanel.get());
  }

  private void checkForLogin(HasWidgets hasWidgets) {
    if (getDesktopAppPresenter().isLoggedIn()) {
      displayView(hasWidgets);
    } else {
      getDesktopBus().invokeLoginService();
    }
  }

  private void displayView(HasWidgets hasWidgets) {
    getDesktopAppPresenter().go(hasWidgets);
  }

  private DesktopAppPresenter getDesktopAppPresenter() {
    if (desktopAppPresenter == null) {
      desktopAppPresenter = new DesktopAppPresenterImpl(getDesktopBus());
    }
    return desktopAppPresenter;
  }

  private DesktopBus getDesktopBus() {
    if (desktopBus == null) {
      desktopBus = new DesktopBus();
    }
    return desktopBus;
  }

  private LoginHandler getLoginHandler(final HasWidgets hasWidgets) {
    if (loginHandler == null) {
      loginHandler = new LoginHandler() {
        @Override
        public void onLogin(LoginEvent loginEvent) {
          displayView(hasWidgets);
          if (loginEvent.isNewUser()) {
            getDesktopBus().invokeProfileService();
          }
        }
      };
    }
    return loginHandler;
  }

  private LoginPresenter getLoginPresenter() {
    if (loginPresenter == null) {
      loginPresenter = new LoginPresenterImpl(getDesktopAppPresenter());
    }
    return loginPresenter;
  }

  private LoginServiceProvider getLoginServiceProvider(HasWidgets rootPanel) {
    if (loginServiceProvider == null) {
      loginServiceProvider = new LoginServiceProvider(getLoginPresenter(), rootPanel);
    }
    return loginServiceProvider;
  }

  private LogoutHandler getLogoutHandler() {
    if (logoutHandler == null) {
      logoutHandler = new LogoutHandler() {
        @Override
        public void onLogout(LogoutEvent logoutEvent) {
          com.google.gwt.user.client.Window.Location.reload();
        }
      };
    }
    return logoutHandler;
  }

  private ProfilePresenter getProfilePresenter() {
    if (profilePresenter == null) {
      profilePresenter = new ProfilePresenterImpl(getDesktopAppPresenter());
    }
    return profilePresenter;
  }

  private ProfileServiceProvider getProfileServiceProvider(HasWidgets hasWidgets) {
    if (profileServiceProvider == null) {
      profileServiceProvider = new ProfileServiceProvider(getProfilePresenter(), hasWidgets);
    }
    return profileServiceProvider;
  }

  private void initializeDesktopBus(HasWidgets hasWidgets) {
    getDesktopBus().registerLoginService(getLoginServiceProvider(hasWidgets));
    getDesktopBus().registerProfileService(getProfileServiceProvider(hasWidgets));
    getDesktopBus().addLoginHandler(getLoginHandler(hasWidgets));
    getDesktopBus().addLogoutHandler(getLogoutHandler());
  }

  private void loadApplication(HasWidgets hasWidgets) {
    if ("clear".equals(History.getToken())) {
      promptToClearStorage(hasWidgets);
    } else {
      checkForLogin(hasWidgets);
    }
  }

  private void loadApplicationAfterAlertingUser(final HasWidgets hasWidgets) {
    Prompt.get().alert("Local Storage is Not Supported",
        "Either your browser does not support HTML5 Local Storage or it is not configured for use by this application.<br/><br/>This application will continue to run, but anything you create will be discarded when the browser terminates or the browser window is refreshed.",
        new Runnable() {
          @Override
          public void run() {
            loadApplication(hasWidgets);
          }
        });
  }

  private void promptToClearStorage(final HasWidgets hasWidgets) {
    Prompt.get().confirm("Desktop", "Would you like to clear this domain's local storage before continuing?",
        new Runnable() {
          @Override
          public void run() {
            getDesktopAppPresenter().clearLocalStorage();
            checkForLogin(hasWidgets);
          }
        }, new Runnable() {
          @Override
          public void run() {
            checkForLogin(hasWidgets);
          }
        });
  }

  private void setBackground(HasWidgets hasWidgets) {
    if (hasWidgets instanceof UIObject) {
      ((UIObject) hasWidgets).addStyleName("x-desktop");
    }
  }

  private void setUncaughtExceptionHandler() {
    GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
      @Override
      public void onUncaughtException(Throwable e) {
        e.printStackTrace();
        Throwable rootCause = getRootCause(e);
        new AlertMessageBox("Exception", rootCause.toString()).show();
      }
    });
  }

  private Throwable getRootCause(Throwable e) {
    Throwable lastCause;
    do {
      lastCause = e;
    } while ((e = e.getCause()) != null);
    return lastCause;
  }

}
