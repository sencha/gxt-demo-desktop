package com.sencha.gxt.desktopapp.client.filemanager.images;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public class Images {

  public interface ImageResources extends ClientBundle {
    ImageResource arrow_in();

    ImageResource arrow_out();

    ImageResource bin_closed();

    ImageResource bullet_go();

    ImageResource cross();

    ImageResource disk();

    ImageResource folder();

    ImageResource folder_add();

    ImageResource page_white();

    ImageResource page_white_add();

    ImageResource script();

    ImageResource script_add();

    ImageResource table();

    ImageResource table_add();

    ImageResource textfield_rename();

    ImageResource world();

    ImageResource world_add();
  }

  private static ImageResources imageResources;

  public static ImageResources getImageResources() {
    if (imageResources == null) {
      imageResources = GWT.create(ImageResources.class);
    }
    return imageResources;
  }
}
