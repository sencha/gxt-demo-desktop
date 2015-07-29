package com.sencha.gxt.desktop.client.widget;

import com.sencha.gxt.cell.core.client.ButtonCell.IconAlign;
import com.sencha.gxt.widget.core.client.button.TextButton;

/**
 * A desktop shortcut.
 */
public class Shortcut extends TextButton {

  /**
   * Creates a new shortcut.
   */
  public Shortcut() {
    this(null, "");
  }

  /**
   * Creates a new shortcut.
   * 
   * @param id the shortcut id
   * @param text the shortcut text
   */
  public Shortcut(String id, String text) {
    super(new ShortcutCell());
    setIconAlign(IconAlign.TOP);
    setId(id);
    setText(text);
  }

}
