package com.adaptris.workunit;

import java.net.URL;
import java.net.URLStreamHandler;
import java.net.spi.URLStreamHandlerProvider;
import java.util.ServiceLoader;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChainURLStreamHandlerProvider extends URLStreamHandlerProvider {

  private Iterable<URLStreamHandlerProvider> providers;

  public ChainURLStreamHandlerProvider() {
    providers = ServiceLoader.load(URLStreamHandlerProvider.class);
  }

  @Override
  public URLStreamHandler createURLStreamHandler(String protocol) {
    for (URLStreamHandlerProvider provider : providers) {
      URLStreamHandler createURLStreamHandler = provider.createURLStreamHandler(protocol);
      if (createURLStreamHandler != null) {
        return createURLStreamHandler;
      }
    }
    return null;
  }

  public static void init() {
    try {
      URL.setURLStreamHandlerFactory(new ChainURLStreamHandlerProvider());
    } catch (Error error) {
      log.error("A URLStreamHandlerFactory was already registered and therefore the ChainURLStreamHandlerProvider cannot be registered",
          error);
    }
  }

}
