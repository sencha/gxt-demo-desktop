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
import com.sencha.gxt.desktopapp.client.persistence.FileSystem;

/**
 * Presents the desktop application to the user. Creates a desktop application
 * view that interacts with the user. Provides data for the view and handles
 * events from the view.
 * <p/>
 * There is an implied order of invocation:
 * <p/>
 * <table border="1">
 * <tr>
 * <th>Any State</th>
 * <th>Storage State</th>
 * <th>Login State</th>
 * <th>Profile State</th></th>
 * <tr>
 * <td>clearLocalStorage</td>
 * <td></td>
 * <td></td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>isLocalStorageSupported</td>
 * <td></td>
 * <td></td>
 * <td></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>getCurrentUser</td>
 * <td></td>
 * <td></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>isLoggedIn</td>
 * <td></td>
 * <td></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>onLogin</td>
 * <td></td>
 * <td></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td></td>
 * <td>getFileSystem</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td></td>
 * <td>getProfile</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td></td>
 * <td>go</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td></td>
 * <td>onOpenFileManager</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td></td>
 * <td>onOpenProfile</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td></td>
 * <td></td>
 * <td>onUpdateProfile</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td></td>
 * <td>onLogout</td>
 * <td></td>
 * </tr>
 * </table>
 * <p/>
 * To prevent applications from accidentally using in-memory storage storage on
 * browsers that don't support persistent local storage, callers must invoke
 * {@link #isLocalStorageSupported()} prior to any method that uses local
 * storage. Failure to do so will result in a {@link IllegalStateException}.
 * <p/>
 * Some methods require a currently logged in user. Failure to successfully
 * invoke {@link #onLogin(LoginModel)} prior to invoking these methods will
 * result in a {@link IllegalArgumentException}.
 * <p/>
 * Note: method names beginning with {@code on} are generally invoked by the
 * view in response to a user generated event.
 */
public interface DesktopAppPresenter {

  /**
   * The outcome of a user login attempt.
   */
  public enum LoginStatus {
    SUCCESS, INVALID_NAME_OR_PASSWORD, DUPLICATE_USER_NAME,
  }

  /**
   * The outcome of a profile update operation.
   */
  public enum ProfileStatus {
    SUCCESS, NO_CURRENT_USER
  }

  /**
   * Clears local storage for the application. Generally this is the storage
   * associated with the domain of the URL.
   */
  void clearLocalStorage();

  /**
   * Gets the currently logged-in user or null if no user is logged in.
   * 
   * @return the current user or null if no user is logged in
   */
  String getCurrentUser();

  /**
   * Gets the file system for the current user.
   * 
   * @return the file system for the current user
   */
  FileSystem getFileSystem();

  /**
   * Gets the current values of the profile settings for the current user.
   * 
   * @return the profile for the current user
   */
  ProfileModel getProfile();

  /**
   * Creates a desktop application view and connects it to the user interface.
   * 
   * @param hasWidgets the user interface
   */
  void go(HasWidgets hasWidgets);

  /**
   * Returns true if the browser supports local storage and it is configured for
   * use with this domain. This method must be invoked prior to using HTML5
   * Local Storage to prevent the caller from accidentally using in-memory
   * temporary storage on browsers that don't support persistent local storage.
   * 
   * @return true if persistent local storage is supported, false if subsequent
   *         storage access will use in-memory storage that will be freed if the
   *         browser is refreshed or closed.
   */
  boolean isLocalStorageSupported();

  /**
   * Returns true if a user is currently logged in or is still logged in from a
   * previous session.
   * 
   * @return true if a user is currently logged in
   */
  boolean isLoggedIn();

  /**
   * Attempts to log the user into the application, validating the user name and
   * password, or attempting to create the user if requested.
   * 
   * @param loginModel the data representing the login request
   * @return {@link LoginStatus#SUCCESS} if the user is successfully logged in.
   *         See {@link DesktopAppPresenter.LoginStatus} for other possible
   *         outcomes.
   */
  LoginStatus onLogin(LoginModel loginModel);

  /**
   * Logs the user out of the system and refreshes the current page, returning
   * the page to the initial state, ready for a new login.
   */
  void onLogout();

  /**
   * Opens a file manager.
   */
  void onOpenFileManager();

  /**
   * Opens the current user's profile so the user can view or update the
   * settings.
   */
  void onOpenProfile();

  /**
   * Updates the current user's profile.
   * 
   * @param profileModel the update profile, or null to indicate the user has
   *          cancelled the update operation
   * @return {@link ProfileStatus#SUCCESS} if the profile update was successful.
   */
  DesktopAppPresenter.ProfileStatus onUpdateProfile(ProfileModel profileModel);

}
