package com.sencha.gxt.desktopapp.client.spreadsheet.images;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public class Images {

  public interface ImageResources extends ClientBundle {

    ImageResource arrow_down();

    ImageResource arrow_left();

    ImageResource arrow_right();

    ImageResource arrow_up();

    ImageResource chart_bar();

    ImageResource chart_line();

    ImageResource chart_pie();

    ImageResource table_column_delete();

    ImageResource table_row_delete();

  }

  private static ImageResources imageResources;

  public static ImageResources getImageResources() {
    if (imageResources == null) {
      imageResources = GWT.create(ImageResources.class);
    }
    return imageResources;
  }
}
