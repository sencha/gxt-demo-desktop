package com.sencha.gxt.desktopapp.client;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;

/**
 * Provides a profile update view capable of displaying current profile settings
 * and receiving updates from a user. The view uses the GWT Editor / Driver
 * framework so that presenter does not need to know too many details of the
 * view's implementation and the view does not need to know too many details
 * about the models structure.
 * 
 * See <a
 * href='http://code.google.com/webtoolkit/doc/latest/DevGuideUiEditors.html'>
 * GWT Editors and Drivers</a> for more information.
 */
public interface ProfileView {

  /**
   * Provides access to the GWT Editor / Driver framework so that the presenter
   * can copy data to and from the view without knowing too many details of the
   * view or the view knowing too many details of the model.
   * 
   * @return an editor driver capable of copying model data to / from the view
   */
  SimpleBeanEditorDriver<ProfileModel, Editor<? super ProfileModel>> getProfileDriver();

  /**
   * Hides the view.
   */
  void hide();

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
