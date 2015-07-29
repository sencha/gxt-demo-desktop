package com.sencha.gxt.desktopapp.client.utility;

public abstract class Prompt {

  public static Prompt get() {
    return PromptImpl.INSTANCE;
  }

  public abstract void alert(String title, String text);

  public abstract void alert(String title, String text, Runnable runnable);

  public abstract void confirm(String title, String text, Runnable yesRunnable);

  public abstract void confirm(String title, String text, Runnable yesRunnable, Runnable noRunnable);
}