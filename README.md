DataMover
=========

DataMover is a simple framework for business data migration between applications

### Repositories

#### Release version

Or maven central: http://repo1.maven.org/maven2/

__Maven__

```xml
<dependency>
    <groupId>com.arekusu</groupId>
    <artifactId>datamover-core</artifactId>
    <version>0.1.0</version>
</dependency>

```

__Gradle__

```groovy
compile "com.arekusu:datamover-core:0.1.0"
```

#### Snapshot version

__Maven__
```xml
<repositories>
    <repository>
      <id>sonatype-snapshots</id>
      <name>Sonatype</name>
      <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.arekusu</groupId>
    <artifactId>datamover-core</artifactId>
    <version>0.1.0-SNAPSHOT</version>
</dependency>
```

__Gradle__

```groovy
compile "com.arekusu:datamover-core:0.1.0-SNAPSHOT"
```


Basic Process Overview
------

![Process Overview](/docs/images/Process_overview.png "Process Overview")


##### At the core of DataMover lies a simple process (Similar to ETL)

1. Read the model. Model is where defined all the rules that drive following steps of this process.
2. Read entities from some source.
3. Apply some transformations to the entities.
4. Write resulting entities to some destination

DataMover provides several implementations of these interfaces and allow to plug-in your custom implementations.

Model
-----
Model contains the definition of particular business entity so that other classes would be able to work with your data in the form of unified entities.

![Model Overview](/docs/images/Model_overview.png "Model Overview")



Entity
-----
Entity is the central concept of the framework.

![Entity Overview](/docs/images/Entity_overview.png "Entity Overview")

It contains all the information of your business entities in unified way.
The rules necessary to transform business representation to unified entity are defined in the model classes.
Type fields (EntityType, FieldType) are populated with corresponding classes of model when Entity is being created by EntityReader.

The other convenient way to approach Entity is in form of tree:

![Entity Tree](/docs/images/Entity_tree.png "Entity Tree")


Entity Reader implementations:
------
##### 1. XMLFileEntityReader
##### 2. DBEntityReader

Entity Writer implementations:
------
##### 1. XMLFileEntityWriter
##### 2. DBEntityWriter

Model Reader implementations:
------
##### 1. XMLFileModelReader
