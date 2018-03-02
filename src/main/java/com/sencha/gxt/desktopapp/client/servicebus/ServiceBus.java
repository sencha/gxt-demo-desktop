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
package com.sencha.gxt.desktopapp.client.servicebus;import java.util.HashMap;public class ServiceBus {  private HashMap<Object, Object> serviceProviders = new HashMap<Object, Object>();  public ServiceBus() {  }  @SuppressWarnings("unchecked")  public <T extends ServiceRequest> T invoke(T serviceRequest) {    ServiceProvider<T> serviceProvider = (ServiceProvider<T>) serviceProviders.get(serviceRequest.getClass());    if (serviceProvider == null) {      throw new IllegalStateException("No service provider, serviceRequest=" + serviceRequest.getClass().getName());    }    serviceProvider.onServiceRequest(serviceRequest);    return serviceRequest; // for one step access to getters  }  public <T extends ServiceRequest> void registerServiceProvider(Class<T> serviceRequestClass,      ServiceProvider<T> serviceProvider) {    Object previousServiceProvider = serviceProviders.put(serviceRequestClass, serviceProvider);    if (previousServiceProvider != null) {      throw new IllegalStateException("Provider already defined, serviceRequest=" + serviceRequestClass.getName()          + ", serviceProvider=" + serviceProvider.getClass().getName());    }  }  public <T extends ServiceRequest> void removeServiceProvider(Class<T> serviceRequestClass) {    serviceProviders.remove(serviceRequestClass);  }}