package com.sencha.gxt.desktopapp.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.sencha.gxt.desktopapp.client.ProfileModel;
import com.sencha.gxt.desktopapp.client.event.UpdateProfileEvent.UpdateProfileHandler;

public class UpdateProfileEvent extends GwtEvent<UpdateProfileHandler> {

  public interface UpdateProfileHandler extends EventHandler {
    void onUpdateProfile(UpdateProfileEvent event);
  }

  public static Type<UpdateProfileHandler> TYPE = new Type<UpdateProfileHandler>();
  private ProfileModel profileModel;
  private boolean isUpdate;

  public UpdateProfileEvent(ProfileModel profileModel, boolean isUpdate) {
    this.profileModel = profileModel;
    this.isUpdate = isUpdate;
  }

  @Override
  public Type<UpdateProfileHandler> getAssociatedType() {
    return TYPE;
  }

  /**
   * Returns the updated profile, or null if the profile update was cancelled.
   * 
   * @return the updated profile, or null if cancelled
   */
  public ProfileModel getProfile() {
    return profileModel;
  }

  public boolean isUpdate() {
    return isUpdate;
  }

  @Override
  protected void dispatch(UpdateProfileHandler handler) {
    handler.onUpdateProfile(this);
  }

}