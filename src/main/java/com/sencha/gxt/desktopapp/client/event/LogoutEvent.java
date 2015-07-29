package com.sencha.gxt.desktopapp.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.sencha.gxt.desktopapp.client.event.LogoutEvent.LogoutHandler;

public class LogoutEvent extends GwtEvent<LogoutHandler> {

  public interface LogoutHandler extends EventHandler {
    void onLogout(LogoutEvent event);
  }

  public static Type<LogoutHandler> TYPE = new Type<LogoutHandler>();

  public LogoutEvent() {
  }

  @Override
  public Type<LogoutHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(LogoutHandler handler) {
    handler.onLogout(this);
  }
}