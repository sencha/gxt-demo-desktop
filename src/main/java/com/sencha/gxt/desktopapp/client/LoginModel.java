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