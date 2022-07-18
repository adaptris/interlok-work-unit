package com.adaptris.workunit.varsub;

import java.io.InputStream;
import java.util.Properties;

import javax.validation.constraints.NotBlank;

import com.adaptris.core.CoreException;
import com.adaptris.core.util.ExceptionHelper;
import com.adaptris.workunit.WorkUnitURLStreamHandlerProvider;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

public class WorkUnitVariableSet implements VariableSet {

  private static final String PROPERTIES_EXTENSION = ".properties";

  /**
   * The variable set name defined in the work unit. Basically the variables.properties file name without '.properties'.
   */
  @NotBlank
  @Getter
  @Setter
  @NonNull
  private String name;

  @Override
  public Properties variables(String workUnitName) throws CoreException {
    try {
      InputStream inputStream = WorkUnitURLStreamHandlerProvider.url(workUnitName, getName() + PROPERTIES_EXTENSION).openStream();
      Properties properties = new Properties();
      properties.load(inputStream);
      return properties;
    } catch (Exception expts) {
      throw ExceptionHelper.wrapCoreException(expts);
    }
  }

}
