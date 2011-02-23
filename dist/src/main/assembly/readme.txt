
 Seam Servlet Module
 ${project.version}

 What is it?
 ===========

 Seam Servlet provide portable enhancements to the Servlet API. Features
 include producers for implicit Servlet objects and request state, briding
 Servlet events to the CDI event bus and making the BeanManager available
 through the Servlet context.
 
 Contents of distribution
 ========================

 artifacts/
 
    Provided libraries

 lib/

    Dependencies

 doc/

    API Docs and reference guide.
  
 source/

    Module source code
  
 Licensing
 =========

 This distribution, as a whole, is licensed under the terms of the Apache
 Software License, Version 2.0 (ASL).

 Seam Servlet URLs
 =================

 Seam 3 Servlet Module: http://sfwk.org/Seam3/ServletModule
 Seam 3 project:        http://sfwk.org/Seam3
 Downloads:             http://sfwk.org/Seam3/DistributionDownloads
 Forums:                http://sfwk.org/Community/Seam3Users
 Source Code:           http://github.com/seam/servlet
 Issue Tracking:        http://issues.jboss.org/browse/SEAMSERVLET

 Release Notes
 =============

 * Migration to Seam Solder
 * Declarative exception handler facility
 * Additional startup event (@Started WebApplication)
 * Support for Enum, Date and Calendar in HTTP data injections
 * Combined API + implement JAR (seam-servlet)
 * @HttpMethod lifecycle event selector
 * Documentation edits
 * Assign name to web-fragment.xml
