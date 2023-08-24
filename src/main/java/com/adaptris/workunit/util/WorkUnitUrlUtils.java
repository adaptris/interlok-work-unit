package com.adaptris.workunit.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.adaptris.core.util.Args;

public class WorkUnitUrlUtils {

  public static final String WORK_UNIT_PROTOCOL = "workunit";
  public static final String SEPARATOR = "!/";

  private static final Pattern UNKONWN_ARTIFACT_PATTERN = Pattern.compile("jar:file:.*/(.*)\\.jar!/.*");

  private WorkUnitUrlUtils() {
  }

  /**
   * Convert a work unit url to a jar url. e.g. <i>workunit:my-work-unit!/work-unit.xml ->
   * jar:file:/path/to/my-work-unit.jar!/work-unit.xml</i> using the {@link WorkUnitRegistry} to find the JAR url
   *
   * @param url
   * @return a jar url
   * @throws IOException
   */
  public static URL toJarUrl(URL url) throws IOException {
    String[] split = url.getPath().split(SEPARATOR);

    final String jarName = split.length > 1 ? split[0] : null;
    Args.notBlank(jarName, "work-unit-name in workunit:work-unit-name!/file url");
    final String path = url.getPath().replaceFirst(StringUtils.trimToEmpty(jarName) + SEPARATOR, "");

    return getFromRegistry(jarName, path);
  }

  /**
   * Find a jar url with the work unit name and a file path like <i>jar:file:/path/to/my-work-unit.jar!/work-unit.xml</i>
   *
   * @param workUnitName
   * @param path
   * @return a jar url with the file path
   * @throws IOException
   */
  public static URL toJarUrl(String workUnitName, String path) throws IOException {
    return toJarUrl(workUnitUrl(workUnitName, path));
  }

  private static URL getFromRegistry(String jarName, String path) throws MalformedURLException {
    Optional<URL> jarUrlOptional = WorkUnitRegistry.getInstance().get(jarName);
    if (jarUrlOptional.isPresent()) {
      return new URL(jarUrlOptional.get().toString() + path);
    }
    return null;
  }

  /**
   * Remove file path from jar url. work-unit.xml is removed e.g. <i>jar:file:/path/to/my-work-unit.jar!/work-unit.xml ->
   * jar:file:/path/to/my-work-unit.jar!/</i>
   *
   * @param url
   * @return only jar url
   * @throws MalformedURLException
   */
  public static URL stripJarUrlFilePath(URL url) throws MalformedURLException {
    String[] split = url.toString().split(SEPARATOR);
    return split.length > 0 ? new URL(split[0] + SEPARATOR) : null;
  }

  /**
   * Remove jar: and the file path from jar url. e.g. <i>jar:file:/path/to/my-work-unit.jar!/work-unit.xml ->
   * file:/path/to/my-work-unit.jar</i>
   *
   * @param url
   * @return only jar url
   * @throws MalformedURLException
   */
  public static URL toFileURL(URL url) throws MalformedURLException {
    return new URL(stripJarUrlFilePath(url).getPath().replace(SEPARATOR, ""));
  }

  /**
   * Build a work unit url with the work unit name and a file path. e.g. <i>workunit:my-work-unit!/work-unit.xml</i>
   *
   * @param workUnitName
   * @param path
   * @return a work unit urk
   * @throws MalformedURLException
   */
  public static URL workUnitUrl(String workUnitName, String path) throws MalformedURLException {
    return new URL(WORK_UNIT_PROTOCOL + ":" + workUnitName + SEPARATOR + path);
  }

  public static boolean isCorrectJar(URL url, String jarName) {
    return StringUtils.isBlank(jarName) || jarName(url.toString()).equals(jarName);
  }

  // jar:file:/path/to/my-work-unit.jar!/META-INF/adaptris-version
  // into just my-work-unit
  public static String jarName(String url) {
    Matcher matcher = UNKONWN_ARTIFACT_PATTERN.matcher(url);
    if (matcher.matches()) {
      return matcher.group(1);
    }
    return url;
  }

}
