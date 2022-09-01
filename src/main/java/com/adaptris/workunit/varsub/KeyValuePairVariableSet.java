package com.adaptris.workunit.varsub;

import java.util.Properties;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.adaptris.util.KeyValuePairSet;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

public class KeyValuePairVariableSet implements VariableSet {

  /**
   * Set of key / value variables to use in the work unit variables substitution. The 'key' should match the ${key} to replace in the work
   * unit XML with the 'value'.
   */
  @Valid
  @NotNull
  @Getter
  @Setter
  @NonNull
  private KeyValuePairSet variables = new KeyValuePairSet();

  @Override
  public Properties variables(String workUnitName) {
    return KeyValuePairSet.asProperties(variables);
  }

}
