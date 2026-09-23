> **Summary:** A work unit example

## What's this? ##

A work unit example to use within your Interlok configurations.
It contains a service-list that log the message, add two metadata and does an XML transform, plus an example connection XML, nothing complicated.

The message expected for the transform is like:

```xml
<xml world="world">
  <payload>Hello </payload>
</xml>

```

## Building me ##

Use gradle to run `gradle clean build` to build the work unit jar file.
A work unit requires to have an xml file (work-unit.xml by default) and a **META-INF/adaptris-version** file that contains at least: `component.type=work-unit` and `artifactId=my-work-unit` where my-work-unit is the id of the work unit. You can package additional XML files, such as connection definitions, alongside it.

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
