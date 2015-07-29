package com.sencha.gxt.desktopapp.client.persistence;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;
import com.google.web.bindery.autobean.shared.AutoBeanFactory.Category;

@Category(AutoBeanToString.class)
public interface FileModelFactory extends AutoBeanFactory {

  AutoBean<FileModel> fileModel();

}