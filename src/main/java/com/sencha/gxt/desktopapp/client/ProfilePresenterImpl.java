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

/**
 * Presents the profile update view to the user. The profile presenter is
 * responsible for retrieving the current profile settings, pushing them to the
 * view, receiving updated profile settings from the view and telling the view
 * what to do as a result.
 * <p/>
 * This particular implementation of the profile presenter uses the services of
 * the desktop application presenter, which knows the details of users and file
 * systems, to perform the actual login.
 * 
 * @see ProfilePresenter
 */
public class ProfilePresenterImpl implements ProfilePresenter {

  private DesktopAppPresenter desktopAppPresenter;
  private ProfileViewImpl profileView;

  /**
   * Creates a profile presenter that uses the services of the specified desktop
   * application presenter to perform the profile update.
   * 
   * @param desktopAppPresenter the desktop application presenter
   */
  ProfilePresenterImpl(DesktopAppPresenter desktopAppPresenter) {
    this.desktopAppPresenter = desktopAppPresenter;
  }

  @Override
  public ProfileModel getProfile() {
    return desktopAppPresenter.getProfile();
  }

  @Override
  public void go(HasWidgets hasWidgets) {
    getProfileView().show();
    getProfileView().getProfileDriver().edit(getProfile());
  }

  @Override
  public void onCancel() {
    getDesktopAppPresenter().onUpdateProfile(null);
    getProfileView().hide();
  }

  @Override
  public void onOkay() {
    ProfileModel profileModel = getProfileView().getProfileDriver().flush();
    if (getProfileView().getProfileDriver().hasErrors()) {
      getProfileView().onValidationError();
    } else {
      getDesktopAppPresenter().onUpdateProfile(profileModel);
      getProfileView().hide();
    }
  }

  private DesktopAppPresenter getDesktopAppPresenter() {
    return desktopAppPresenter;
  }

  private ProfileView getProfileView() {
    if (profileView == null) {
      profileView = new ProfileViewImpl(this);
    }
    return profileView;
  }

}