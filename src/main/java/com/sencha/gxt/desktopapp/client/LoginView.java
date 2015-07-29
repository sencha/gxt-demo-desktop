package com.sencha.gxt.desktopapp.client;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;

/**
 * Provides a login view capable of gathering login parameters from a user. The
 * view uses the GWT Editor / Driver framework so that presenter does not need
 * to know too many details of the view's implementation and the view does not
 * need to know too many details about the models structure.
 * 
 * See <a
 * href='http://code.google.com/webtoolkit/doc/latest/DevGuideUiEditors.html'>
 * GWT Editors and Drivers</a> for more information.
 */
public interface LoginView {

  /**
   * Provides access to the GWT Editor / Driver framework so that the presenter
   * can copy data to and from the view without knowing too many details of the
   * view or the view knowing too many details of the model.
   * 
   * @return an editor driver capable of copying model data to / from the view
   */
  SimpleBeanEditorDriver<LoginModel, Editor<? super LoginModel>> getLoginModelDriver();

  /**
   * Hides the view.
   */
  void hide();

  /**
   * Informs the user that the parameters the user entered would result in the
   * creation of a duplicate user name.
   */
  void onDuplicateUserName();

  /**
   * Informs the user that the parameters the user entered are invalid due to a
   * non-existing user name or a non-matching password.
   */
  void onInvalidUserNameOrPassword();

  /**
   * Informs the user that the Editor / Driver framework reported that there are
   * field validation errors.
   */
  void onValidationError();

  /**
   * Shows the view.
   */
  void show();

}
