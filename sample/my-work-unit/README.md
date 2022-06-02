> **Summary:** A work unit example

## What's this? ##

A work unit example ot use within your Interlok configurations.
It contains a service-list. That is to say you may have a complex and/or often used set of services that you find yourself copying into multiple installations of Interlok and you now want to make that list of services more modular.

The work unit allows you to black box that process making it easier for the next person to re-use your process (set of services).

## Building me ##

You can use gradle to un `gradle clean assemble` to build the work unit jar file.
A work unit requires to have an xml file (work-unit.xml by default) and a **META-INF/adaptris-version** file that contains at least: `component.type=work-unit`.

### Using me ###

In the main Interlok configuration add:

```xml
<work-unit-service>
  <unique-id>my-work-unit-service</unique-id>
  <!-- Jar name without .jar -->
  <work-unit-name>my-work-unit</work-unit-name>
  <!-- Xml name without .xml. Optional, default to work-unit -->
  <xml-config-name>work-unit</xml-config-name>
</work-unit-service>
```
