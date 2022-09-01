package com.adaptris.core.varsub;

import java.util.Properties;

import com.adaptris.core.CoreException;

/**
 * Extends {@link Processor} but the class is public and can be used by the WorkUnit class
 */
public class WorkUnitVarSubPreProcessor {

  private Properties configuration;

  public WorkUnitVarSubPreProcessor(Properties configuration) {
    this.configuration = configuration;
  }

  public String process(String xml, Properties variables) throws CoreException {
    return new Processor(configuration).process(xml, variables);
  }

}
