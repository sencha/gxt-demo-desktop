package com.sencha.gxt.desktopapp.client.spreadsheet;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.event.StoreAddEvent.StoreAddHandler;
import com.sencha.gxt.data.shared.event.StoreClearEvent.StoreClearHandler;
import com.sencha.gxt.data.shared.event.StoreDataChangeEvent.StoreDataChangeHandler;
import com.sencha.gxt.data.shared.event.StoreFilterEvent.StoreFilterHandler;
import com.sencha.gxt.data.shared.event.StoreHandlers;
import com.sencha.gxt.data.shared.event.StoreRecordChangeEvent.StoreRecordChangeHandler;
import com.sencha.gxt.data.shared.event.StoreRemoveEvent.StoreRemoveHandler;
import com.sencha.gxt.data.shared.event.StoreSortEvent.StoreSortHandler;
import com.sencha.gxt.data.shared.event.StoreUpdateEvent.StoreUpdateHandler;

public class SpreadsheetChartListStoreAdapter extends ListStore<Row> {
  private ListStore<Row> listStore;

  public void add(int index, Row item) {
    listStore.add(index, item);
  }

  public int hashCode() {
    return listStore.hashCode();
  }

  public void add(Row item) {
    listStore.add(item);
  }

  public boolean addAll(Collection<? extends Row> items) {
    return listStore.addAll(items);
  }

  public boolean addAll(int index, Collection<? extends Row> items) {
    return listStore.addAll(index, items);
  }

  public boolean equals(Object obj) {
    return listStore.equals(obj);
  }

  public void applySort(boolean suppressEvent) {
    listStore.applySort(suppressEvent);
  }

  public void clear() {
    listStore.clear();
  }

  public Row findModelWithKey(String key) {
    return listStore.findModelWithKey(key);
  }

  public Row get(int index) {
    return listStore.get(index+1);
  }

  public List<Row> getAll() {
    return listStore.getAll().subList(1, listStore.size());
  }

  public int indexOf(Row item) {
    return listStore.indexOf(item)-1;
  }

  public Row remove(int index) {
    return listStore.remove(index+1);
  }

  public boolean remove(Row model) {
    return listStore.remove(model);
  }

  public void replaceAll(List<? extends Row> newItems) {
    listStore.replaceAll(newItems);
  }

  public int size() {
    return listStore.size()-1;
  }

  public List<Row> subList(int start, int end) {
    return listStore.subList(start+1, end);
  }

  public void update(Row item) {
    listStore.update(item);
  }

  public String toString() {
    return listStore.toString();
  }

  public void addFilter(com.sencha.gxt.data.shared.Store.StoreFilter<Row> filter) {
    listStore.addFilter(filter);
  }

  public void addSortInfo(int index, com.sencha.gxt.data.shared.Store.StoreSortInfo<Row> info) {
    listStore.addSortInfo(index, info);
  }

  public void addSortInfo(com.sencha.gxt.data.shared.Store.StoreSortInfo<Row> info) {
    listStore.addSortInfo(info);
  }

  public HandlerRegistration addStoreAddHandler(StoreAddHandler<Row> handler) {
    return listStore.addStoreAddHandler(handler);
  }

  public HandlerRegistration addStoreClearHandler(StoreClearHandler<Row> handler) {
    return listStore.addStoreClearHandler(handler);
  }

  public HandlerRegistration addStoreDataChangeHandler(StoreDataChangeHandler<Row> handler) {
    return listStore.addStoreDataChangeHandler(handler);
  }

  public HandlerRegistration addStoreFilterHandler(StoreFilterHandler<Row> handler) {
    return listStore.addStoreFilterHandler(handler);
  }

  public HandlerRegistration addStoreHandlers(StoreHandlers<Row> handlers) {
    return listStore.addStoreHandlers(handlers);
  }

  public HandlerRegistration addStoreRecordChangeHandler(StoreRecordChangeHandler<Row> handler) {
    return listStore.addStoreRecordChangeHandler(handler);
  }

  public HandlerRegistration addStoreRemoveHandler(StoreRemoveHandler<Row> handler) {
    return listStore.addStoreRemoveHandler(handler);
  }

  public HandlerRegistration addStoreSortHandler(StoreSortHandler<Row> handler) {
    return listStore.addStoreSortHandler(handler);
  }

  public HandlerRegistration addStoreUpdateHandler(StoreUpdateHandler<Row> handler) {
    return listStore.addStoreUpdateHandler(handler);
  }

  public void clearSortInfo() {
    listStore.clearSortInfo();
  }

  public void commitChanges() {
    listStore.commitChanges();
  }

  public Row findModel(Row model) {
    return listStore.findModel(model);
  }

  public void fireEvent(GwtEvent<?> event) {
    listStore.fireEvent(event);
  }

  public LinkedHashSet<com.sencha.gxt.data.shared.Store.StoreFilter<Row>> getFilters() {
    return listStore.getFilters();
  }

  public ModelKeyProvider<? super Row> getKeyProvider() {
    return listStore.getKeyProvider();
  }

  public Collection<Record> getModifiedRecords() {
    return listStore.getModifiedRecords();
  }

  @SuppressWarnings("rawtypes")
  public com.sencha.gxt.data.shared.Store.Record getRecord(Row data) {
    return listStore.getRecord(data);
  }

  public List<com.sencha.gxt.data.shared.Store.StoreSortInfo<Row>> getSortInfo() {
    return listStore.getSortInfo();
  }

  public boolean hasMatchingKey(Row model1, Row model2) {
    return listStore.hasMatchingKey(model1, model2);
  }

  public boolean hasRecord(Row data) {
    return listStore.hasRecord(data);
  }

  public boolean isAutoCommit() {
    return listStore.isAutoCommit();
  }

  public boolean isEnableFilters() {
    return listStore.isEnableFilters();
  }

  public boolean isFiltered() {
    return listStore.isFiltered();
  }

  public void rejectChanges() {
    listStore.rejectChanges();
  }

  public void removeFilter(com.sencha.gxt.data.shared.Store.StoreFilter<Row> filter) {
    listStore.removeFilter(filter);
  }

  public void removeFilters() {
    listStore.removeFilters();
  }

  public void setAutoCommit(boolean isAutoCommit) {
    listStore.setAutoCommit(isAutoCommit);
  }

  public void setEnableFilters(boolean enableFilters) {
    listStore.setEnableFilters(enableFilters);
  }

  public SpreadsheetChartListStoreAdapter(ListStore<Row> listStore) {
    super(listStore.getKeyProvider());
    this.listStore = listStore;
  }
}
