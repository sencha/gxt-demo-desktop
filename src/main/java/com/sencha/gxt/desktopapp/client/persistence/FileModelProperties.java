package com.sencha.gxt.desktopapp.client.persistence;

import java.util.Date;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.desktopapp.client.persistence.FileModel.FileType;

public interface FileModelProperties extends PropertyAccess<FileModel> {
  
  @Path("id")
  ModelKeyProvider<FileModel> key();

  ValueProvider<FileModel, FileType> fileType();

  ValueProvider<FileModel, Date> lastModified();

  ValueProvider<FileModel, String> name();

  ValueProvider<FileModel, Long> size();
  
}

