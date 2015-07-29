package com.sencha.gxt.desktopapp.client;

import com.google.gwt.user.client.ui.HasWidgets;

/**
 * Presents the profile settings view to the user.
 */
public interface ProfilePresenter {

  /**
   * Gets a copy of the current profile. This copy may be updated with changes
   * to the profile settings.
   * 
   * @return a copy of the current profile
   */
  ProfileModel getProfile();

  /**
   * Creates a profile view and connects it to the user interface.
   * 
   * @param hasWidgets the user interface
   */
  void go(HasWidgets hasWidgets);

  /**
   * Notifies the presenter that the user has cancelled the profile update
   * operation.
   */
  void onCancel();

  /**
   * Notifies the presenter that the user has completed the profile update
   * operation.
   */
  void onOkay();

}
