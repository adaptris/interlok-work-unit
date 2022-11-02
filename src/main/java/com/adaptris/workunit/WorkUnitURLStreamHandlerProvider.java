package com.adaptris.workunit;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.net.spi.URLStreamHandlerProvider;

import org.apache.commons.lang3.StringUtils;

import com.adaptris.workunit.util.WorkUnitDetector;

public class WorkUnitURLStreamHandlerProvider extends URLStreamHandlerProvider {

  public static final String WORK_UNIT_PROTOCOL = "workunit";
  public static final String SEPARATOR = "!/";

  @Override
  public URLStreamHandler createURLStreamHandler(String protocol) {
    if (WORK_UNIT_PROTOCOL.equals(protocol)) {
      return new URLStreamHandler() {
        @Override
        protected URLConnection openConnection(URL url) throws IOException {
          return convertUrl(url).openConnection();
        }
      };
    }
    return null;
  }

  protected URL convertUrl(URL url) throws IOException {
    String[] split = url.getPath().split(SEPARATOR);

    final String jarName = split.length > 1 ? split[0] : null;
    String path = url.getPath().replaceFirst(StringUtils.trimToEmpty(jarName) + SEPARATOR, "");

    return WorkUnitDetector.listUrls(path).stream().filter(u -> WorkUnitDetector.isCorrectJar(u, jarName)).findFirst().get();
  }

  public static URL url(String workUnitName, String path) throws MalformedURLException {
    return new URL(WORK_UNIT_PROTOCOL + ":" + workUnitName + SEPARATOR + path);
  }

}
