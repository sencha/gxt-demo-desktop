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

import com.google.gwt.user.client.ui.HasWidgets;
import com.sencha.gxt.desktopapp.client.DesktopAppPresenter.LoginStatus;

/**
 * Presents the login view to the user. The login presenter is responsible for
 * receiving login parameters from the view, taking action on them and telling
 * the view what to do as a result.
 * <p/>
 * This particular implementation of the login presenter uses the services of
 * the desktop application presenter, which knows the details of users and file
 * systems, to perform the actual login.
 * 
 * @see LoginPresenter
 */
public class LoginPresenterImpl implements LoginPresenter {

  private DesktopAppPresenter desktopAppPresenter;
  private LoginView loginView;
  private LoginModel loginModel;

  /**
   * Creates a login presenter that uses the services of the specified desktop
   * application presenter to perform the login.
   * 
   * @param desktopAppPresenter the desktop application presenter
   */
  LoginPresenterImpl(DesktopAppPresenter desktopAppPresenter) {
    this.desktopAppPresenter = desktopAppPresenter;
  }

  @Override
  public void go(HasWidgets hasWidgets) {
    getLoginView().show();
    getLoginView().getLoginModelDriver().edit(getLoginModel());
  }

  @Override
  public void onLogin() {
    getLoginView().getLoginModelDriver().flush();
    if (getLoginView().getLoginModelDriver().hasErrors()) {
      getLoginView().onValidationError();
    } else {
      LoginStatus loginStatus = getDesktopAppPresenter().onLogin(getLoginModel());
      switch (loginStatus) {
        case DUPLICATE_USER_NAME:
          getLoginView().onDuplicateUserName();
          break;
        case INVALID_NAME_OR_PASSWORD:
          getLoginView().onInvalidUserNameOrPassword();
          break;
        case SUCCESS:
          getLoginView().hide();
          break;
      }
    }
  }

  private DesktopAppPresenter getDesktopAppPresenter() {
    return desktopAppPresenter;
  }

  private LoginModel getLoginModel() {
    if (loginModel == null) {
      loginModel = new LoginModel();
    }
    return loginModel;
  }

  private LoginView getLoginView() {
    if (loginView == null) {
      loginView = new LoginViewImpl(this);
    }
    return loginView;
  }

}