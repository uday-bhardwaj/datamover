DataMover
=========

DataMover is a simple framework for business data migration between applications

Basic Process Overview
------

![Process Overview](/docs/images/Process_overview.png "Process Overview")


##### At the core of DataMover lies a simple process (Similar to ETL)

1. Read the model. Model is where defined all the rules that drive following steps of this process.
2. Read entities from some source.
3. Apply some transformations to the entities.
4. Write resulting entities to some destination

DataMover provides several implementations of these interfaces and allow to plug-in your custom implementations.

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
