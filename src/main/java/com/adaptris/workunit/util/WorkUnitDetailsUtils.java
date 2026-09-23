package com.adaptris.workunit.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

public class WorkUnitDetailsUtils {

  // The variable prefix and suffix are not configurable here
  private static final String VARIABLE_REGEX = Pattern.quote("${") + "(\\S*)" + Pattern.quote("}");
  private static final String PROPERTIES = ".properties";
  private static final String VARIABLE_PROPERTIES_REGEX = "variables(-.*)?\\" + PROPERTIES;

  private WorkUnitDetailsUtils() {
  }

  /**
   * List all variable names across every XML config file in the work unit jar.
   *
   * @param workUnitName
   * @return array of all variable names found in every XML config file in the work unit
   * @throws IOException
   * @throws URISyntaxException
   */
  public static String[] listVars(String workUnitName) throws IOException, URISyntaxException {
    URL workUnitUrl = WorkUnitUrlUtils.toFileURL(WorkUnitDetector.findWorkUnitAdaptrisVersionUrl(workUnitName));
    try (JarFile jarFile = new JarFile(new File(workUnitUrl.toURI()))) {
      Set<String> variables = jarFile.stream().filter(WorkUnitDetailsUtils::isXmlConfig).flatMap(e -> findVariables(jarFile, e).stream())
          .collect(Collectors.toSet());
      return variables.toArray(new String[] {});
    }
  }

  /**
   * List all variable names in the named work unit xml config file.
   *
   * @param workUnitName
   * @param xmlConfigName
   * @return array of all variable names in the named work unit XML file
   * @throws IOException
   */
  public static String[] listVars(String workUnitName, String xmlConfigName) throws IOException {
    String xml = fileContent(workUnitName, xmlConfigName);

    Set<String> variables = findVariables(xml);

    return variables.toArray(new String[] {});
  }

  /**
   * List all variable properties file names (potential variable properties) in a work unit
   *
   * @param workUnitName
   * @return array of all variable properties file names in a work unit
   * @throws IOException
   * @throws URISyntaxException
   */
  public static String[] listVarProperties(String workUnitName) throws IOException, URISyntaxException {
    URL workUnitUrl = WorkUnitUrlUtils.toFileURL(WorkUnitDetector.findWorkUnitAdaptrisVersionUrl(workUnitName));
    try (JarFile jarFile = new JarFile(new File(workUnitUrl.toURI()))) {
      Set<String> variableProperties = jarFile.stream().filter(WorkUnitDetailsUtils::isVariableProperties).map(WorkUnitDetailsUtils::stripProperties)
          .collect(Collectors.toSet());

      return variableProperties.toArray(new String[] {});
    }
  }

  /**
   * Return the content of README.md or readme.md as the first item in the String array We're doing this way so that can be used in the UI
   * via the current <i>InputFieldHint(style = "ClassName#methodName")</i> system
   *
   * @param workUnitName
   * @return Readme markdown content
   * @throws IOException
   */
  public static String[] readme(String workUnitName) throws IOException {
    String xml = null;
    try {
      xml = fileContent(workUnitName, "README.md");
    } catch (FileNotFoundException fnfe) {
      xml = fileContent(workUnitName, "readme.md");
    }

    return new String[] { xml };
  }

  private static String stripProperties(JarEntry jarEntity) {
    return StringUtils.removeEnd(jarEntity.getName(), ".properties");
  }

  private static boolean isVariableProperties(JarEntry jarEntity) {
    return jarEntity.getName().matches(VARIABLE_PROPERTIES_REGEX);
  }

  private static Set<String> findVariables(String xml) {
    Set<String> variables = new HashSet<>();
    Pattern p = Pattern.compile(VARIABLE_REGEX);
    Matcher m = p.matcher(xml);
    while (m.find()) {
      variables.add(m.group(1));
    }
    return variables;
  }

  private static Set<String> findVariables(JarFile jarFile, JarEntry jarEntry) {
    try (InputStream inputStream = jarFile.getInputStream(jarEntry)) {
      return findVariables(IOUtils.toString(inputStream, StandardCharsets.UTF_8));
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }

  private static boolean isXmlConfig(JarEntry jarEntry) {
    return !jarEntry.isDirectory() && jarEntry.getName().endsWith(".xml");
  }

  private static String fileContent(String workUnitName, String xmlConfigName) throws IOException {
    URL url = WorkUnitDetector.findWorkUnitFile(workUnitName, xmlConfigName);
    return IOUtils.toString(url.openStream(), StandardCharsets.UTF_8);
  }

}
