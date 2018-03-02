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
package com.sencha.gxt.desktopapp.client.filemanager;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Event;
import com.sencha.gxt.desktopapp.client.persistence.FileModel;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.Grid.GridCell;
import com.sencha.gxt.widget.core.client.grid.editing.GridInlineEditing;

/**
 * Works around a minor issue with GridInlineEditing in which any update
 * operation that does not change the value is reported as a cancel.
 */
public class FileManagerGridInlineEditing extends GridInlineEditing<FileModel> {

  private boolean isEnter;

  FileManagerGridInlineEditing(Grid<FileModel> editableGrid) {
    super(editableGrid);
  }

  public boolean isEnter() {
    return isEnter;
  }

  @Override
  public void startEditing(GridCell cell) {
    isEnter = false;
    super.startEditing(cell);
  }

  /**
   * The original work around used onEnter to set the flag. However this no
   * longer works because onEnter is invoked after onCancel has been invoked.
   */
  @Override
  protected void onEnter(NativeEvent evt) {
    isEnter = true;
    super.onEnter(evt);
  }

  public TextField getTextField() {
    FileManagerGridInlineEditingTextField textField = new FileManagerGridInlineEditingTextField();
    textField.setSelectOnFocus(true);
    return textField;
  }

  /**
   * The new improved work around uses a special TextField override to set the
   * flag. When the underlying issue is resolved, this TextField can be replaced
   * with a plain old TextField.
   */
  public class FileManagerGridInlineEditingTextField extends TextField {

    @Override
    public void onBrowserEvent(Event event) {
      if ("keydown".equals(event.getType()) && event.getKeyCode() == KeyCodes.KEY_ENTER) {
        isEnter = true;
      }
      super.onBrowserEvent(event);
    }

  }
}