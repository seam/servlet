/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.seam.servlet.test.util;

import org.jboss.seam.servlet.support.ServletMessages;
import org.jboss.shrinkwrap.api.ArchivePath;
import org.jboss.shrinkwrap.api.Filter;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;
import org.jboss.shrinkwrap.resolver.impl.maven.filter.StrictFilter;

/**
 * A utility class to create seed archives for Arquillian tests.
 * 
 * @author <a href="http://community.jboss.org/people/dan.j.allen">Dan Allen</a>
 */
public class Deployments {
    public static JavaArchive createBeanArchive() {
        return ShrinkWrap.create(JavaArchive.class, "test.jar").addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    public static WebArchive createBeanWebArchive() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                // add packages to include generated classes
                .addPackages(false, ServletMessages.class.getPackage())
                .addAsLibraries(
                        DependencyResolvers.use(MavenDependencyResolver.class).loadReposFromPom("pom.xml")
                                .artifact("org.jboss.seam.solder:seam-solder").exclusion("*")
                                .resolveAs(GenericArchive.class))
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    public static WebArchive createMockableBeanWebArchive() {
        return createBeanWebArchive().addAsLibraries(
                DependencyResolvers.use(MavenDependencyResolver.class).loadReposFromPom("pom.xml")
                .artifact("org.mockito:mockito-all").resolveAs(GenericArchive.class, new StrictFilter()));
    }

    public static Filter<ArchivePath> exclude(final Class<?>... classes) {
        return new Filter<ArchivePath>() {
            public boolean include(ArchivePath ap) {
                String path = ap.get().replace('$', '=');
                for (Class<?> c : classes) {
                    if (path.matches("^/" + c.getName().replace('.', '/') + "(=[1-9])?.class$")) {
                        return false;
                    }
                }
                return true;
            }

        };
    }
}
