# interlok-work-unit

[![GitHub tag](https://img.shields.io/github/tag/adaptris/interlok-work-unit.svg)](https://github.com/adaptris/interlok-work-unit/tags)
[![license](https://img.shields.io/github/license/adaptris/interlok-work-unit.svg)](https://github.com/adaptris/interlok-work-unit/blob/develop/LICENSE)
[![Actions Status](https://github.com/adaptris/interlok-work-unit/actions/workflows/gradle-publish.yml/badge.svg)](https://github.com/adaptris/interlok-work-unit/actions)
[![codecov](https://codecov.io/gh/adaptris/interlok-work-unit/branch/develop/graph/badge.svg)](https://codecov.io/gh/adaptris/interlok-work-unit)
[![CodeQL](https://github.com/adaptris/interlok-work-unit/workflows/CodeQL/badge.svg)](https://github.com/adaptris/interlok-work-unit/security/code-scanning)
[![Known Vulnerabilities](https://snyk.io/test/github/adaptris/interlok-work-unit/badge.svg?targetFile=build.gradle)](https://snyk.io/test/github/adaptris/interlok-work-unit?targetFile=build.gradle)
[![Closed PRs](https://img.shields.io/github/issues-pr-closed/adaptris/interlok-work-unit)](https://github.com/adaptris/interlok-work-unit/pulls?q=is%3Apr+is%3Aclosed)

## What's this? ##

A work unit is a Jar file to add to Interlok and that contains a service-list. That is to say you may have a complex and/or often used set of services that you find yourself copying into multiple installations of Interlok and you now want to make that list of services more modular.

The work unit allows you to black box that process making it easier for the next person to re-use your process (set of services).

## Testing me ##

The first thing is to build your work-unit.

Work unit's are simply a service-list configuration and any supporting files your services require bundled into a jar file. 

### Configuring your service-list ###

That's it, it's just a service list.  An example;

```xml
<service-collection class="service-list">
  <services>
    <service class="log-message-service">
      <unique-id>log-message-service</unique-id>
      <log-level>DEBUG</log-level>
    </service>
  </services>
</service-collection>
```

Sometimes your service-list will require additional files; for example if you're configuring a transform then you'll need the XSL file. Sometimes the additional resource required will be remote, perhaps you're serving a transform over HTTP. But other times you want the resource locally packaged with your configuration. In this case your work unit jar file will contain your service-list configuration and another supporting file like an XSL transform named "my-transform.xsl". In these cases it's important that in your work unit's service list you refer to the packaged resource file like this using the __workunit:__ URL handler; The URL is in the form of:

`protocol:work-unit-name!/path/to/file: workunit:my-work-unit!/my-transform.xsl`. **work-unit-name** is the artifactId of your work unit defined in the work unit **adaptris-version** file.

```xml
<service-collection class="service-list">
  <services>
    <xml-transform-service>
      <unique-id>transform-service</unique-id>
      <url>workunit:my-work-unit!/my-transform.xsl</url>
    </xml-transform-service>
  </services>
</service-collection>
```

### Work Unit Service ###

Once you have your work unit Jar file and it's dropped into your Interlok lib directory (make sure your re-start Interlok) you can then edit your main configuration adding a new service.
If you have a service-list file packed in that my-work-unit.jar jar file named  __work-unit.xml__ , then inside your main Interlok configuration you can refer to your work unit's service list simply like this;

```xml
<work-unit-service>
  <unique-id>work-unit-service</unique-id>
  <!-- Jar name without .jar -->
  <work-unit-name>my-work-unit</work-unit-name>
  <!-- Xml name without .xml. Optional, default to work-unit -->
  <xml-config-name>work-unit</xml-config-name>
</work-unit-service>
```

### Variables Substitutions ###

A work-unit jar file can have variables.properties along with the work-unit.xml files that can be used to replace tokens in the config.
To use these variable properties, you will have to use `work-unit-variable-set`. Let say you want to use the variables in the variables.properties file within your work unit.

```xml
<work-unit-service>
...
  <work-unit-variable-set>
    <name>variables</name>
  </work-unit-variable-set>
...
</work-unit-service>
```

You can add multiple variable set with the latest one taking precedences. Let say you also have a variables-test.properties file.

```xml
<work-unit-service>
...
  <work-unit-variable-set>
    <name>variables</name>
  </work-unit-variable-set>
  <work-unit-variable-set>
    <name>variables-test</name>
  </work-unit-variable-set>
...
</work-unit-service>
```

The other way to do variables substitutions is to provide key value pairs in the work unit service like:

```xml
<work-unit-service>
...
  <key-value-pair-variable-set>
    <variables>
      <key-value-pair>
        <key>key1</key>
        <value>Value 1</value>
      </key-value-pair>
      <key-value-pair>
        <key>key2</key>
        <value>Value 2</value>
      </key-value-pair>
    </variables>
  </key-value-pair-variable-set>
...
</work-unit-service>
```
Where *key1* and *key2* is a variable replacement key in the work-unit.xml file.

### Building a Work Unit ###

The work unit is a jar file with a work-unit.xml and other configuration files in it.
The work unit jar also need a file called **adaptris-version** in a **META-INF** directory.
The adaptris-version content should be like:

```
component.name=My Work Unit
component.description=My work unit that does something
component.type=work-unit
groupId=com.adaptris
artifactId=my-work-unit
```
component.name, component.type, groupId and artifactId are mandatory.

**The best way to build your work unit jar is to use gradle and have the work-unit.xml and the variables.properties in an Interlok type project.**

You can follow the sample project [my-work-unit](sample/my-work-unit)

With a command line prompt run: `gradle clean build`

The **build.gradle** file will package the project in the right way and will create and fill the **adaptris-version** file accordingly.
The **gradle.properties** file is where you can specify the *componentName*, *componentDesc*, *organizationName* and *organizationUrl*.

The artifactId is the project directory name but can also be customised in the **settings.gradle** file: `rootProject.name = "my-work-unit"`

### Documenting a Work Unit ###

If you use the example build.gradle file from [my-work-unit](sample/my-work-unit) to build your work unit, it will package the README.md file at the root of the work unit project. The content of the README.md file will be available in the Interlok UI when adding a WorkUnitService to explain what the work unit is doing.

