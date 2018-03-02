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

/**
 * A simple plain old Java object (POJO) for collecting login parameters.
 */
public class LoginModel {

  private String userName;
  private String password;
  private boolean isNewUser;

  /**
   * Returns the password.
   * 
   * @return the password
   */
  public String getPassword() {
    return password;
  }

  /**
   * Returns the user name.
   * 
   * @return user name
   */
  public String getUserName() {
    return userName;
  }

  /**
   * Returns true to create a new user.
   * 
   * @return true to create a new user
   */
  public boolean isNewUser() {
    return isNewUser;
  }

  /**
   * sets the new user flag.
   * 
   * @param isNewUser true to create a new user
   */
  public void setNewUser(boolean isNewUser) {
    this.isNewUser = isNewUser;
  }

  /**
   * Sets the password.
   * 
   * @param password the password
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Sets the user name.
   * 
   * @param userName the user name
   */
  public void setUserName(String userName) {
    this.userName = userName;
  }

}