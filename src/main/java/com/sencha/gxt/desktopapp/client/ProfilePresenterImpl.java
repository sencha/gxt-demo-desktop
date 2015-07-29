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