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

import com.sencha.gxt.desktop.client.layout.DesktopLayoutType;
import com.sencha.gxt.desktopapp.client.persistence.FileSystem;

/**
 * Provides a profile model interface for use with the GWT auto bean factory framework. The profile model auto bean
 * factory is generated automatically by the framework and is capable of creating instances of these profile models.
 * <p/>
 * The profile model represents the user's profile settings. In this implementation of the desktop application, it is
 * managed by the {@link ProfilePresenterImpl} and {@link DesktopAppPresenterImpl} and persisted in the
 * {@link FileSystem}.
 * 
 * @see ProfileFactory
 */
public interface ProfileModel {

  /**
   * Returns the type of desktop layout in the user's profile.
   * 
   * @return the current desktop layout type
   */
  DesktopLayoutType getDesktopLayoutType();

  /**
   * Returns the user's currently defined display name.
   * 
   * @return the current display name
   */
  String getDisplayName();

  /**
   * Returns true if the desktop should scale the position and size of desktop windows when the browser window is
   * resized.
   * 
   * @return true if the desktop should scale desktop windows when the browser window is resized
   */
  boolean isScaleOnResize();

  /**
   * Sets the desktop layout type.
   * 
   * @param desktopLayoutType the desktop layout type
   */
  void setDesktopLayoutType(DesktopLayoutType desktopLayoutType);

  /**
   * Sets the display name.
   * 
   * @param displayName the display name
   */
  void setDisplayName(String displayName);
  
  /**
   * Sets whether the desktop should scale the position and size of desktop windows when the browser window is
   * resized.
   * 
   * @param isScaleOnResize true if the desktop should scale desktop windows when the browser window is resized 
   */
  void setScaleOnResize(boolean isScaleOnResize);

}
