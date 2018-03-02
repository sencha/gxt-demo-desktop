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
package com.sencha.gxt.desktopapp.client.persistence;

import java.util.Date;

import com.sencha.gxt.desktopapp.client.utility.Utility;

public interface FileModel {
  public enum FileType {
    BOOKMARK, DOCUMENT, FOLDER, PROGRAM, SPREADSHEET;

    @Override
    public String toString() {
      return Utility.capitalize(super.toString());
    }
  }

  public String getContent();

  public FileType getFileType();

  public String getId();

  public Date getLastModified();

  public String getName();

  public Long getSize();

  public void setContent(String content);

  public void setFileType(FileType fileType);

  public void setId(String id);

  public void setLastModified(Date lastModified);

  public void setName(String name);

  public void setSize(Long size);
}