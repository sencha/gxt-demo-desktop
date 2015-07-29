package com.sencha.gxt.desktopapp.client.persistence;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

public class AutoBeanToString {
  public static String toString(AutoBean<?> instance) {
    return AutoBeanCodex.encode(instance).getPayload();
  }
}