package com.sencha.gxt.desktopapp.client.images;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface DesktopImages extends ClientBundle {
  public DesktopImages INSTANCE = GWT.create(DesktopImages.class);

  ImageResource application_cascade();

  ImageResource application_tile_horizontal();

  ImageResource door_in();

  ImageResource door_out();

  ImageResource folder();

  ImageResource folder_shortcut();

  ImageResource grid();

  ImageResource table();

  ImageResource user();

  ImageResource user_edit();
}
