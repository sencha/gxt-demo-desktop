package com.sencha.gxt.desktopapp.client;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;
import com.google.web.bindery.autobean.shared.AutoBeanFactory.Category;
import com.sencha.gxt.desktopapp.client.persistence.AutoBeanToString;

/**
 * Uses the GWT auto bean factory framework to create a factory capable of
 * vending profile model auto beans.
 * 
 * @see AutoBeanFactory
 */
@Category(AutoBeanToString.class)
public interface ProfileFactory extends AutoBeanFactory {

  /**
   * Creates an auto bean that wraps a profile model.
   * 
   * @return an auto bean that wraps a profile model
   */
  AutoBean<ProfileModel> profileModel();

}
