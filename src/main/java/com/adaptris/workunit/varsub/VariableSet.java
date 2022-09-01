package com.adaptris.workunit.varsub;

import java.util.Properties;

import com.adaptris.core.CoreException;

public interface VariableSet {

  Properties variables(String workUnitName) throws CoreException;

}
