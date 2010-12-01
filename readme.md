#Seam Servlet

Provides portable and convenient enhancements to the Servlet API. Features include:

+ producers for implicit Servlet objects and HTTP request state
+ propogation of Servlet events to the CDI event bus (Servlet event bridge)
+ forwarding uncaught exceptions to the Seam exception handling infrastructure (Seam Catch integration)
+ binding the BeanManager to a Servlet context attribute
+ and more...

Seam Servlet is independent of CDI implementation and fully portable between
Java EE 6 and Servlet environments enhanced with CDI. The use of Servlet 3.0 is
recommended, but not required.

For more information, see the [Seam Servlet project page](http://seamframework.org/Seam3/ServletModule).

##Building

   mvn clean install
