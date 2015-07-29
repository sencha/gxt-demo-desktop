package com.sencha.gxt.desktopapp.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.sencha.gxt.desktopapp.client.event.LoginEvent.LoginHandler;

public class LoginEvent extends GwtEvent<LoginHandler> {

  public interface LoginHandler extends EventHandler {
    void onLogin(LoginEvent event);
  }

  public static Type<LoginHandler> TYPE = new Type<LoginHandler>();
  private String userName;
  private boolean isNewUser;

  public LoginEvent(String userName, boolean isNew) {
    this.userName = userName;
    this.isNewUser = isNew;
  }

  @Override
  public Type<LoginHandler> getAssociatedType() {
    return TYPE;
  }

  public String getUserName() {
    return userName;
  }

  public boolean isNewUser() {
    return isNewUser;
  }

  @Override
  protected void dispatch(LoginHandler handler) {
    handler.onLogin(this);
  }
}