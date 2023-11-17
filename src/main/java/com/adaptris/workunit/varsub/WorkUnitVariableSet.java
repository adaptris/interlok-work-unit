package com.adaptris.workunit.varsub;

import java.io.InputStream;
import java.util.Properties;

import javax.validation.constraints.NotBlank;

import org.apache.commons.io.FilenameUtils;

import com.adaptris.annotation.AdapterComponent;
import com.adaptris.annotation.ComponentProfile;
import com.adaptris.annotation.InputFieldHint;
import com.adaptris.core.CoreException;
import com.adaptris.core.util.ExceptionHelper;
import com.adaptris.workunit.util.WorkUnitDetector;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@XStreamAlias("work-unit-variable-set")
@AdapterComponent
@ComponentProfile(summary = "The variable set name defined in the work unit.", tag = "work-unit")
public class WorkUnitVariableSet implements VariableSet {

  private static final String PROPERTIES_EXTENSION = ".properties";

  /**
   * The variable set name defined in the work unit. Basically the variables.properties file name without '.properties'. It can be any
   * properties file within the work unit jar.
   */
  @NotBlank
  @InputFieldHint(style = "com.adaptris.workunit.util.WorkUnitDetailsUtils#listVarProperties(workUnitName)")
  @Getter
  @Setter
  @NonNull
  private String name;

  @Override
  public Properties variables(String workUnitName) throws CoreException {
    try {
      InputStream inputStream = WorkUnitDetector.findWorkUnitFile(workUnitName, propertiesFileName()).openStream();
      Properties properties = new Properties();
      properties.load(inputStream);
      return properties;
    } catch (Exception expts) {
      throw ExceptionHelper.wrapCoreException(expts);
    }
  }

  private String propertiesFileName() {
    return FilenameUtils.normalize(getName()) + PROPERTIES_EXTENSION;
  }

}
