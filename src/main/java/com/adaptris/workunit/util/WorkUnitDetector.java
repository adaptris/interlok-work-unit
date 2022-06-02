package com.adaptris.workunit.util;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.adaptris.core.util.PropertyHelper;

public class WorkUnitDetector {

  private static final String ADAPTRIS_VERSION = "META-INF/adaptris-version";

  private WorkUnitDetector() {
  }

  public static Collection<URL> listUrls(String resourceName) throws IOException {
    return Collections.list(WorkUnitDetector.class.getClassLoader().getResources(resourceName));
  }

  /**
   * List all the work unit names in the classpath (jars with a META-INF/adaptris-version file containing component.type=work-unit)
   *
   * @return list of work unit names
   * @throws IOException
   */
  public static String[] list() throws IOException {
    return listUrls(ADAPTRIS_VERSION).stream().filter(url -> isWorkUnit(url)).map(url -> jarName(url.toString())).toArray(String[]::new);
  }

  private static final Pattern UNKONWN_ARTIFACT_PATTERN = Pattern.compile("jar:file:.*/(.*)\\.jar!/.*");

  // jar:file:/path/to/my-work-unit.jar!/META-INF/adaptris-version
  // into just my-work-unit
  public static String jarName(String url) {
    Matcher matcher = UNKONWN_ARTIFACT_PATTERN.matcher(url);
    if (matcher.matches()) {
      return matcher.group(1);
    }
    return url;
  }

  public static boolean isCorrectJar(URL url, String jarName) {
    return StringUtils.isBlank(jarName) || jarName(url.toString()).equals(jarName);
  }

  public static boolean isWorkUnit(URL url) {
    Properties properties = PropertyHelper.loadQuietly(url);
    return properties.getOrDefault("component.type", "").equals("work-unit");
  }

}
