package com.sencha.gxt.desktopapp.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.desktopapp.client.images.DesktopImages;
import com.sencha.gxt.desktopapp.client.utility.Utility;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FormPanel.LabelAlign;
import com.sencha.gxt.widget.core.client.form.PasswordField;
import com.sencha.gxt.widget.core.client.form.TextField;

/**
 * Provides a login view capable of gathering login parameters from a user.
 * <p/>
 * Implementation Notes:
 * <p/>
 * TODO: Add translatable Messages for user visible text
 * 
 * @see LoginView
 */
public class LoginViewImpl implements LoginView, Editor<LoginModel> {

  /**
   * Uses the GWT Editor / Driver framework to create a driver capable of
   * transferring data between the model to the view (editor).
   */
  interface LoginModelDriver extends SimpleBeanEditorDriver<LoginModel, LoginViewImpl> {
  }

  private LoginPresenter loginPresenter;

  private Window window;
  private VerticalLayoutContainer verticalLayoutContainer;
  private VerticalLayoutData layoutData;
  private Margins margins;
  private FieldLabel userNameFieldLabel;
  private FieldLabel passwordFieldLabel;
  private TextField userNameTextField;
  private PasswordField passwordField;
  private CheckBox isNewUserCheckBox;
  private TextButton loginButton;
  private SelectHandler loginButtonSelectHandler;
  private SimpleBeanEditorDriver<LoginModel, Editor<? super LoginModel>> loginModelDriver;

  /**
   * Creates a login view that uses the services of the specified login
   * presenter to perform the login.
   * 
   * @param loginPresenter the login presenter
   */
  public LoginViewImpl(LoginPresenter loginPresenter) {
    this.loginPresenter = loginPresenter;
  }

  @Override
  public SimpleBeanEditorDriver<LoginModel, Editor<? super LoginModel>> getLoginModelDriver() {
    if (loginModelDriver == null) {
      loginModelDriver = GWT.create(LoginModelDriver.class);
      loginModelDriver.initialize(this);
    }
    return loginModelDriver;
  }

  @Override
  public void hide() {
    getWindow().hide();
  }

  @Override
  public void onDuplicateUserName() {
    new AlertMessageBox("Login", "The specified user name already exists.").show();
  }

  @Override
  public void onInvalidUserNameOrPassword() {
    new AlertMessageBox("Login", "The specified user name does not exist.").show();
  }

  @Override
  public void onValidationError() {
    new AlertMessageBox("Validation Errors", "Please correct the errors and try again").show();
  }

  @Override
  public void show() {
    getWindow().setFocusWidget(getUserNameTextField());
    getWindow().show();
  }

  /**
   * Provides GWT Editor / Driver framework support for displaying and entering
   * the "new user" boolean.
   * 
   * @return a check box that provides the user interface for the "new user"
   *         boolean
   */
  @Path("newUser")
  CheckBox getIsNewUserCheckBox() {
    if (isNewUserCheckBox == null) {
      isNewUserCheckBox = new CheckBox();
      isNewUserCheckBox.setBoxLabel("Create new user");
    }
    return isNewUserCheckBox;
  }

  /**
   * Provides the GWT Editor / Driver framework support for displaying and
   * entering the password.
   * 
   * @return a field that provides the user interface for the password
   */
  @Path("password")
  PasswordField getPasswordField() {
    if (passwordField == null) {
      passwordField = new PasswordField();
    }
    return passwordField;
  }

  /**
   * Provides the GWT Editor / Driver framework support for displaying and
   * entering the user name.
   * 
   * @return a field that provides the user interface for the user name
   */
  @Path("userName")
  TextField getUserNameTextField() {
    if (userNameTextField == null) {
      userNameTextField = new TextField();
      userNameTextField.setAllowBlank(false);
    }
    return userNameTextField;
  }

  private VerticalLayoutData getLayoutData() {
    if (layoutData == null) {
      layoutData = new VerticalLayoutData(1, -1, getMargins());
    }
    return layoutData;
  }

  private TextButton getLoginButton() {
    if (loginButton == null) {
      loginButton = new TextButton("Login");
      loginButton.addSelectHandler(getLoginButtonSelectHandler());
    }
    return loginButton;
  }

  private SelectHandler getLoginButtonSelectHandler() {
    if (loginButtonSelectHandler == null) {
      loginButtonSelectHandler = new SelectHandler() {
        @Override
        public void onSelect(SelectEvent event) {
          getLoginPresenter().onLogin();
        }
      };
    }
    return loginButtonSelectHandler;
  }

  private LoginPresenter getLoginPresenter() {
    return loginPresenter;
  }

  private Margins getMargins() {
    if (margins == null) {
      margins = new Margins(5);
    }
    return margins;
  }

  private FieldLabel getPasswordFieldLabel() {
    if (passwordFieldLabel == null) {
      passwordFieldLabel = new FieldLabel(getPasswordField(), "Password");
    }
    return passwordFieldLabel;
  }

  private FieldLabel getUserNameFieldLabel() {
    if (userNameFieldLabel == null) {
      userNameFieldLabel = new FieldLabel(getUserNameTextField(), "User Name");
    }
    return userNameFieldLabel;
  }

  private VerticalLayoutContainer getVerticalLayoutContainer() {
    if (verticalLayoutContainer == null) {
      verticalLayoutContainer = new VerticalLayoutContainer();
      verticalLayoutContainer.add(getUserNameFieldLabel(), getLayoutData());
      verticalLayoutContainer.add(getPasswordFieldLabel(), getLayoutData());
      verticalLayoutContainer.add(getIsNewUserCheckBox(), getLayoutData());
      Utility.alignLabels(LabelAlign.TOP, verticalLayoutContainer);
    }
    return verticalLayoutContainer;
  }

  private Window getWindow() {
    if (window == null) {
      window = new Window();
      window.setHeadingText("Desktop Login");
      window.getHeader().setIcon(DesktopImages.INSTANCE.door_in());
      window.setPixelSize(200, 200);
      window.setButtonAlign(BoxLayoutPack.END);
      window.setModal(true);
      window.setBlinkModal(true);
      window.setClosable(false);
      window.setOnEsc(false);
      window.add(getVerticalLayoutContainer());
      window.addButton(getLoginButton());
    }
    return window;
  }
}
