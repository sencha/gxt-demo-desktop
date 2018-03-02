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

import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.event.shared.SimpleEventBus;
import com.sencha.gxt.desktopapp.client.event.AddFileModelEvent;
import com.sencha.gxt.desktopapp.client.event.AddFileModelEvent.AddFileModelHandler;
import com.sencha.gxt.desktopapp.client.event.LoginEvent;
import com.sencha.gxt.desktopapp.client.event.LoginEvent.LoginHandler;
import com.sencha.gxt.desktopapp.client.event.LogoutEvent;
import com.sencha.gxt.desktopapp.client.event.LogoutEvent.LogoutHandler;
import com.sencha.gxt.desktopapp.client.event.OpenFileModelEvent;
import com.sencha.gxt.desktopapp.client.event.OpenFileModelEvent.OpenFileModelHandler;
import com.sencha.gxt.desktopapp.client.event.RemoveFileModelEvent;
import com.sencha.gxt.desktopapp.client.event.RemoveFileModelEvent.RemoveFileModelHandler;
import com.sencha.gxt.desktopapp.client.event.SelectFileModelEvent;
import com.sencha.gxt.desktopapp.client.event.SelectFileModelEvent.SelectFileModelHandler;
import com.sencha.gxt.desktopapp.client.event.UpdateFileModelEvent;
import com.sencha.gxt.desktopapp.client.event.UpdateFileModelEvent.UpdateFileModelHandler;
import com.sencha.gxt.desktopapp.client.event.UpdateProfileEvent;
import com.sencha.gxt.desktopapp.client.event.UpdateProfileEvent.UpdateProfileHandler;
import com.sencha.gxt.desktopapp.client.service.LoginServiceProvider;
import com.sencha.gxt.desktopapp.client.service.LoginServiceRequest;
import com.sencha.gxt.desktopapp.client.service.ProfileServiceProvider;
import com.sencha.gxt.desktopapp.client.service.ProfileServiceRequest;
import com.sencha.gxt.desktopapp.client.servicebus.ServiceBus;

/**
 * Provides a mechanism for decoupling producers and consumers. Handles both
 * events and services.
 * <p/>
 * An event is distinguished from a service in that it typically has zero or
 * more consumer that may be invoked in any order and have no official means of
 * returning information to the invoking entity. Event handlers are added to the
 * event bus using {@code add} methods. Events are broadcast using {@code fire}
 * methods.
 * <p/>
 * A service is distinguished from an event in that it has one and only one
 * consumer that may optionally return information to the invoking entity.
 * Service providers are added to the service bus using {@code register}
 * methods. Services are requested using {@code invoke} methods. An
 * {@link IllegalStateException} is thrown if an attempt is made to add a
 * specified service more than once, or if a service is invoked and no
 * corresponding service provider has been registered.
 * <p/>
 * To make it clear which events and services the application supports, this
 * class explicitly advertises them using strongly typed methods instead of the
 * generic {@link EventBus#fireEvent} or {@link ServiceBus#invoke}.
 */
public class DesktopBus {

  private SimpleEventBus eventBus = new SimpleEventBus();
  private ServiceBus serviceBus = new ServiceBus();

  /**
   * Adds a handler that is invoked when a new file is created.
   * 
   * @param handler the add file model handler
   * @return a registration that can be used to remove the handler
   */
  public HandlerRegistration addAddFileModelHandler(AddFileModelHandler handler) {
    return eventBus.addHandler(AddFileModelEvent.TYPE, handler);
  }

  /**
   * Adds a handler that is invoked after a new user successfully logs in.
   * 
   * @param handler the login handler
   * @return a registration that can be used to remove the handler
   */
  public HandlerRegistration addLoginHandler(LoginHandler handler) {
    return eventBus.addHandler(LoginEvent.TYPE, handler);
  }

  /**
   * Adds a handler that is invoked after a user logs out.
   * 
   * @param handler the logout handler
   * @return a registration that can be used to remove the handler
   */
  public HandlerRegistration addLogoutHandler(LogoutHandler handler) {
    return eventBus.addHandler(LogoutEvent.TYPE, handler);
  }

  /**
   * Adds a handler that is invoked when a request is made to launch the
   * application associated with a file.
   * 
   * @param handler the open file handler
   * @return a registration that can be used to remove the handler
   */
  public HandlerRegistration addOpenFileModelHandler(OpenFileModelHandler handler) {
    return eventBus.addHandler(OpenFileModelEvent.TYPE, handler);
  }

  /**
   * Adds a handler that is invoked after a file is removed.
   * 
   * @param handler the file remove handler
   * @return a registration that can be used to remove the handler
   */
  public HandlerRegistration addRemoveFileModelHandler(RemoveFileModelHandler handler) {
    return eventBus.addHandler(RemoveFileModelEvent.TYPE, handler);
  }

  /**
   * Adds a handler that is invoked when a user selects a file in the file
   * manager without opening it.
   * 
   * @param handler the file select handler
   * @return a registration that can be used to remove the handler
   */
  public HandlerRegistration addSelectFileModelHandler(SelectFileModelHandler handler) {
    return eventBus.addHandler(SelectFileModelEvent.TYPE, handler);
  }

  /**
   * Adds a handler that is invoked when a file (or it's meta-information) is
   * updated.
   * 
   * @param handler the file update handler
   * @return a registration that can be used to remove the handler
   */
  public HandlerRegistration addUpdateFileModelHandler(UpdateFileModelHandler handler) {
    return eventBus.addHandler(UpdateFileModelEvent.TYPE, handler);
  }

  /**
   * Adds a handler that is invoked when the user has updated their profile, or
   * cancelled the profile update operation.
   * 
   * @param handler the profile update handler
   * @return a registration that can be used to remove the handler
   */
  public HandlerRegistration addUpdateProfileHandler(UpdateProfileHandler handler) {
    return eventBus.addHandler(UpdateProfileEvent.TYPE, handler);
  }

  /**
   * Fires an event indicating a new file has been created.
   * 
   * @param event the file add event
   */
  public void fireAddFileModelEvent(AddFileModelEvent event) {
    eventBus.fireEvent(event);
  }

  /**
   * Fires an event indicating a user has logged in.
   * 
   * @param event the login event
   */
  public void fireLoginEvent(LoginEvent event) {
    eventBus.fireEvent(event);
  }

  /**
   * Fires an event indicating a user has logged out.
   * 
   * @param event the logout event
   */
  public void fireLogoutEvent(LogoutEvent event) {
    eventBus.fireEvent(event);
  }

  /**
   * Fires an event indicating a user wishes to launch the application
   * associated with a file.
   * 
   * @param event the file open event
   */
  public void fireOpenFileModelEvent(OpenFileModelEvent event) {
    eventBus.fireEvent(event);
  }

  /**
   * Fires an event indicating a file has been removed.
   * 
   * @param event the file remove event
   */
  public void fireRemoveFileModelEvent(RemoveFileModelEvent event) {
    eventBus.fireEvent(event);
  }

  /**
   * Fires an event indicating the user has selected a file without opening it.
   * 
   * @param event the file select event
   */
  public void fireSelectFileModelEvent(SelectFileModelEvent event) {
    eventBus.fireEvent(event);
  }

  /**
   * Fires an event indicating a file (or its meta-information) has been
   * updated.
   * 
   * @param event the file update event
   */
  public void fireUpdateFileModelEvent(UpdateFileModelEvent event) {
    eventBus.fireEvent(event);
  }

  /**
   * Fires an event indicating the user's profile has been updated, or the
   * update operation has been cancelled.
   * 
   * @param event the profile update event
   */
  public void fireUpdateProfileEvent(UpdateProfileEvent event) {
    eventBus.fireEvent(event);
  }

  /**
   * Invokes a service capable of giving the user an opportunity to log in.
   */
  public void invokeLoginService() {
    serviceBus.invoke(new LoginServiceRequest());
  }

  /**
   * Invokes a service capable of giving the user an opportunity to update
   * profile settings.
   */
  public void invokeProfileService() {
    serviceBus.invoke(new ProfileServiceRequest());
  }

  /**
   * Registers a service provider capable of giving the user an opportunity to
   * log in. A login service provider generally collects login parameters and
   * invokes {@link DesktopAppPresenter#onLogin(LoginModel)}.
   * 
   * @param provider the login service provider
   */
  public void registerLoginService(LoginServiceProvider provider) {
    serviceBus.registerServiceProvider(LoginServiceRequest.class, provider);
  }

  /**
   * Registers a service provider capable of giving the user an opportunity to
   * update profile settings. A profile service provider generally displays
   * current profile settings, allows the user to update them and invokes
   * {@link DesktopAppPresenter#onUpdateProfile(ProfileModel)}.
   * 
   * @param provider the profile service provider
   */
  public void registerProfileService(ProfileServiceProvider provider) {
    serviceBus.registerServiceProvider(ProfileServiceRequest.class, provider);
  }
}
