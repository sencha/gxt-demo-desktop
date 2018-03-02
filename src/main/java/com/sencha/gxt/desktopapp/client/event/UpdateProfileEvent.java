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