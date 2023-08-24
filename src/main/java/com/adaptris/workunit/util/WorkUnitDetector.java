package com.adaptris.workunit.util;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Stream;

import org.apache.commons.io.FilenameUtils;

import com.adaptris.core.util.PropertyHelper;

public class WorkUnitDetector {

  private static final String ADAPTRIS_VERSION = "META-INF/adaptris-version";

  private WorkUnitDetector() {
  }

  public static Collection<URL> listUrls(String resourceName) throws IOException {
    return Collections.list(WorkUnitDetector.class.getClassLoader().getResources(resourceName));
  }

  private static Stream<URL> listWorkUnitsUrl() throws IOException {
    // Maybe we could cache that
    return listUrls(ADAPTRIS_VERSION).stream().filter(url -> isWorkUnit(url));
  }

  /**
   * List all the work unit names in the classpath (jars with a META-INF/adaptris-version file containing component.type=work-unit)
   *
   * @return list of work unit names
   * @throws IOException
   */
  public static String[] list() throws IOException {
    return listWorkUnitsUrl().map(url -> WorkUnitUrlUtils.jarName(url.toString())).toArray(String[]::new);
  }

  /**
   * Return the jar URL of the work unit META-INF/adaptris-version file for the given jarName
   *
   * @param jarName
   * @return jar META-INF/adaptris-version URL
   * @throws IOException
   */
  public static URL findWorkUnitAdaptrisVersionUrl(String jarName) throws IOException {
    return listWorkUnitsUrl().filter(u -> WorkUnitUrlUtils.isCorrectJar(u, jarName)).findFirst().get();
  }

  /**
   * Return the jar URL of the work unit fileName file for the given jarName
   *
   * @param jarName
   * @param fileName
   * @return jar fileName URL
   * @throws IOException
   */
  public static URL findWorkUnitFile(String jarName, String fileName) throws IOException {
    String normalizeFileName = FilenameUtils.normalize(fileName);
    Objects.requireNonNull(normalizeFileName, fileName + " cannot be null and must be a normalised path, e.g. not starting with ..");
    return new URL(WorkUnitDetector.findWorkUnitAdaptrisVersionUrl(jarName).toString().replace(ADAPTRIS_VERSION, normalizeFileName));
  }

  /**
   * Check if the jar of the given URL is a work unit. Meaning it has a property component.type=work-unit
   *
   * @param url
   * @return true if this is a work unit
   */
  private static boolean isWorkUnit(URL url) {
    Properties properties = PropertyHelper.loadQuietly(url);
    return properties.getOrDefault("component.type", "").equals("work-unit");
  }

}
