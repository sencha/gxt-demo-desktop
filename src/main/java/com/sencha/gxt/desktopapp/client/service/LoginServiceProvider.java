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
package com.sencha.gxt.desktopapp.client.service;import com.google.gwt.user.client.ui.HasWidgets;import com.sencha.gxt.desktopapp.client.LoginPresenter;import com.sencha.gxt.desktopapp.client.servicebus.ServiceProvider;public class LoginServiceProvider implements ServiceProvider<LoginServiceRequest> {  private LoginPresenter loginPresenter;  private HasWidgets hasWidgets;  public LoginServiceProvider(LoginPresenter loginPresenter, HasWidgets hasWidgets) {    this.loginPresenter = loginPresenter;    this.hasWidgets = hasWidgets;  }  @Override  public void onServiceRequest(LoginServiceRequest loginServiceRequest) {    loginPresenter.go(hasWidgets);  }}