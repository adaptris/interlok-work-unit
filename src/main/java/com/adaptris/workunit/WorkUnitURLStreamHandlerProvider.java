package com.adaptris.workunit;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.net.spi.URLStreamHandlerProvider;

import com.adaptris.workunit.util.WorkUnitUrlUtils;

/**
 * Convert at runtime all the URLs with protocol workunit to their real jar URLs using {@link WorkUnitUrlUtils#toJarUrl(URL)}
 */
public class WorkUnitURLStreamHandlerProvider extends URLStreamHandlerProvider {

  public static final String WORK_UNIT_PROTOCOL = "workunit";
  public static final String SEPARATOR = "!/";

  @Override
  public URLStreamHandler createURLStreamHandler(String protocol) {
    if (WORK_UNIT_PROTOCOL.equals(protocol)) {
      return new URLStreamHandler() {
        @Override
        protected URLConnection openConnection(URL url) throws IOException {
          return WorkUnitUrlUtils.toJarUrl(url).openConnection();
        }
      };
    }
    return null;
  }

}
