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
package com.sencha.gxt.desktop.client.widget;import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;import com.sencha.gxt.data.shared.LabelProvider;import com.sencha.gxt.data.shared.ListStore;import com.sencha.gxt.data.shared.ModelKeyProvider;import com.sencha.gxt.widget.core.client.form.ComboBox;/** * Provides general purpose combo box support for Java enumerated types. The * {@link Enum#toString()} method is invoked to retrieve the text for each * choice. To support translatable text, override the {@code toString} method * for the enumerated type and/or choices. *  * @param <T> */public class EnumComboBox<T extends Enum<T>> extends ComboBox<T> {  /**   * Constructs an enumerated combo box with the specified values. To include   * all of the values for an enum, use:   * <p/>   * <code>   * EnumComboBox myEnumComboBox{@code <MyEnum>} = new EnumComboBox{@code <MyEnum>}(MyEnum.values());   * </code>   *    * @param values   */  public EnumComboBox(T... values) {    super(new ListStore<T>(new ModelKeyProvider<T>() {      @Override      public String getKey(T item) {        return item.name();      }    }), new LabelProvider<T>() {      @Override      public String getLabel(T item) {        return item.toString();      }    });    for (T value : values) {      getStore().add(value);    }    if (values.length > 0) {      select(0);    }    setEditable(false);    setForceSelection(true);    setTriggerAction(TriggerAction.ALL);  }}