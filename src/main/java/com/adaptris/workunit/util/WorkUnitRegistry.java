package com.adaptris.workunit.util;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class WorkUnitRegistry {

  private final Map<String, URL> registry = new HashMap<>();

  private static WorkUnitRegistry INSTANCE;

  public static WorkUnitRegistry getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new WorkUnitRegistry();
    }
    return INSTANCE;
  }

  public Optional<URL> get(String name) {
    return Optional.ofNullable(registry.get(name));
  }

  public void register(String name, URL url) {
    registry.putIfAbsent(name, url);
  }

  public void unregister(String name) {
    registry.remove(name);
  }

}
