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


Entity
-----
Entity is the central concept of the framework.

![Entity Overview](/docs/images/Entity_overview.png "Entity Overview")

It contains all the information of your business entities in unified way.

![Entity Tree](/docs/images/Entity_tree.png "Entity Tree")


Model
-----
Model is the second core concept

![Model Overview](/docs/images/Model_overview.png "Model Overview")

Model contains the definition of particular business entity so that other classes would be able to work with your data in the form of unified entities.


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
