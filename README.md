What is all about
=================

How to build modern, stateless and scalable web service? This question is a stumbling-stone in todays life. 
As I used to create high loaded services I created a template that allows to set up a web service fast and easily. Now I'd like to share it with you.

The architecture will be the following: RESTfull Java based backend and BackBone one-page JavaScript-based frontend. In this article we'll focus on the first one - the server-side REST-api.

This will be a plain web service and it will run on build-in Jetty Server. As a database we'll use mongoDb - it is easily clasterized and very fast if the structure is well formed. Also we'll include a Hazelcast into the system. I'll allow us to create locks to emulate transactions if needed. 

As the service will be stateless and the MongoDB is easily clasterized we get a horizontally scalable web service.

Jersey and JAXB we'll help us to provide a RESTfull api. Nginx will proxy requests. 

In this example the whole app is packaged into the debian package. You can use any other packaging method you like.

Beans
=====
First of all we need to create our beans - objects we'll work with - where we'll keep our data and those that we'll return to the client.
All our beans will be stored in a separate module - template-beans. 
The fun is that we don't have to create POJOs and add necessary annotations manually. XJC will do the job. We just need to create xsd description of our beans and bindings.

In package template-beans/src/main/resources you'll find xsd files describing beans we'd like to create. I tried to show all frequently used patterns - inheritance, lists, enumerations.

In bindings.xjb you'll find global binding to simple - this will create @XmlRootElement annotation in beans.
Also thanks to maven-jaxb2-plugin we can add annotations using bindings. You'll find there how we add SpringData (for Mongo) annotations: Id, Document and DBRef using XPATH. Also we define collection names.

Thats it. If we just run [mvn clean process-resoures] - all our beans will be generated in the target/generated-sources/xjc/ directory. Now we can use them in all our modules.


Data Access Layer (DAL)
=======================
The second module will communicate with our database. To work with Mongo we'll use Spring Data and the Repository-style approach.

All configuration happens in resources/domainContext.xml.
Here we create our own property placeholder configurer so we could use both property files - local file for debugging and common files in /etc/.. directory on the server.
<bean id="apiProps" class="com.mycompany.template.utils.TemplatePropertiesPlaceholderConfigurer" >
        <property name="ignoreResourceNotFound" value="true"/>
        <property name="localOverride" value="true"/>
        <property name="locations">
            <list>
                <value>file:///${project.parent.basedir}/template-rest/conf/template-api-local.properties</value>
                <value>file:///etc/template/api/template-api.properties</value>
                <value>file:///etc/template/api/template-api-hazelcast.properties</value>
            </list>
        </property>
    </bean>

Also we define a useful ConfigUtils class that could be used anywhere in the code. ConfigUtils calss was generated previously in the beans module.

<bean class="com.mycompany.template.beans.ConfigUtils">
        <property name="authEnabled" value="${auth.enabled}" />
    </bean>

Next we configure mongo connections, replica sets, db and user names and passwords, define mongo factory and mongoTemplate.
Here we say that our custom repository implementations will be available in package com.mycompany.template.repositories and are marked by postfix as CustomImpl (will explain it a little bit later).

All properties are stored in property files in teplate-rest module (later about that).

Now we'll have to create mongo repositories. 
Firs of all - utils package contains 2 classes. TemplatePropertiesPlaceholderConfigurer as said before - provides properties from several files - searches for the property file from the top of the list and uses only one. ImMemoryMongoFactory will help us later to create tests.

To perform simple actions with collection you can just define an interface extending PagingAndSortingRepository. It is generic so you can define a bean class to work with.
As an example - PropertyRepository in com.mycompany.template.repositories package. By including this repository using @Autowired (will show it later) you'll be able to perform fetching, saving and removing data wothout a single line of the implementing code! Wow!

But sometimes we need to perform more complex actions on data. So we can create a custom implementation of the Repository. See SomeBeanRepository as na exaple. It also extends SomeBeanRepositoryCustom where we define our xtra methods. In the package com.mycompany.template.repositories.custom.impl you'll find a class SomeBeanRepositoryCustomImpl (with CustomImpl postifx - remember?) which implements SomeBeanRepositoryCustom interface. Here you can create different queries, exclude field, sort objects and so on. See the example SomeBeanRepositoryCustomImpl.

Thats it. We've created a DAL repositories we can use in other modules.


Business logic layer. Services.
===============================
I preffer to create separate layers for business logic and web-servicing itself. This allows us to change the business implementation without changing the REST interface contract.
So, here is the "teplate-service" module. It is a simple module. We just define each class as @Service and use @Autowire to be able to use DAL repositories. See the example: com.mycompany.template.SomeBeanService

Rest Layer. How user see the serice.
====================================
Here we create REST API using Jersey. See the example: com.mycompany.template.api.SomeBeanRestService.
We use @Autowire annotations to include business logic beans. By annotating methods with GET, POST, PUT, DELETE, Produces, Consumes and parameters with PathParam and QueryParam annotations we define a REST API. I tried to use all friquently used combinations in the example class. 
All requests and responds will be automatiocall marshalled and unmarshalled. Magic!

In utils package you'll find PGJsonProducer class - just leave it there. It will provide json output using Jackson. Otherwise if you'll return a list with just 1 object it'll be marshalled as an object, but not as an array with 1 object which is a good source of bugs on the cliet side.

Also you'll find a scheduler. It is just a simple idea to implement regulary running tasks. Also in a SchedulerJob you'll find an example how to use Hazelcast locks to provide transactions.


THE MAIN!
=========
Ok, we created objects, DAL, services and REST. How to make it all work? 
In the template-rest module you'll find the Main class. A jetty server configuration happens there. It also starts the app. You can define a port, number of threads, web.xml source there. And yep, don't forget a web.xml. It is placed in resources/META-INF/webapp/WEB-INF. It defines context and servlets handlers. Also you can include a spring sequrity context there. But this is already another story...  


Build and Run
==============
In teplate-rest module there is a debian directory. It allows to build a debian package using debhelper. Just run [dch -i] to update a changelog and [debuild] afterwards.
All services are packed into jar and placed into /usr/share/template/template-api/. To run an application you can use an upstart script also included in the debian directory:
java -cp "/usr/share/template/template-api/lib/*" com.mycompany.template.Main /usr/share/template/template-api/ > /var/log/template/api.log 2>&1


In the source root you can find a file called template-api. It is an nginx configuration file. It is installed to the /etc/nginx/conf.d directory and can be included into the main nginx.conf.


Tests
=====
Service and Rest modules contain tests. They are using inmemory Fongo and they are pretty easy - so there is no need to explain how they work. The only interesting thing is that to test a rest we have to start a jetty-server using maven jetty plugin. But it all is already configured in example pom files. 


ToCome
======
Pager service, online docs, client generation


 
