package com.adaptris.workunit.varsub;

import java.util.Properties;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.adaptris.annotation.AdapterComponent;
import com.adaptris.annotation.ComponentProfile;
import com.adaptris.util.KeyValuePairSet;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@XStreamAlias("key-value-pair-variable-set")
@AdapterComponent
@ComponentProfile(summary = "Set of key / value variables to use in the work unit variables substitution.", tag = "work-unit")
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
