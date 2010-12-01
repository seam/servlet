
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

 docs/

    API Docs and reference guide.
  
 examples/

    Seam Servlet examples
  
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
 Issue Tracking:        http://jira.jboss.org/jira/browse/SEAMSERVLET

 Release Notes
 =============

 * Initial public release
 * Servlet to CDI event bridge
 * Catch Servlet integration
 * Producers for implicit Servlet objects (including producer for [Http]ServletResponse)
 * Narrowing type producers for HTTP request state (parameters, headers & cookies)
 * Binding BeanManager to a Servlet context attribute
 * Proposed RedirectBuilder API
 * @HttpRequest qualifier

