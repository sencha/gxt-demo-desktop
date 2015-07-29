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