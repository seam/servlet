package org.jboss.seam.servlet.http;

import javax.servlet.http.Cookie;

/**
 * A builder, similar in style to the ResponseBuilder from JAX-RS, that
 * simplifies the task of sending a redirect URL to the client.
 *
 * <strong>This is a proposed API and is subject to change</strong>
 *
 * @author <a href="http://community.jboss.org/people/dan.j.allen">Dan Allen</a>
 */
public interface RedirectBuilder
{
   void send();

   RedirectBuilder redirect();

   RedirectBuilder redirect(String path);

   RedirectBuilder temporaryRedirect(String path);

   RedirectBuilder seeOther(String path);

   RedirectBuilder cookie(Cookie... cookies);

   RedirectBuilder param(String name, Object... values);
   
   RedirectBuilder param(boolean replace, String name, Object... values);

   RedirectBuilder header(String name, Object... values);
   
   RedirectBuilder header(boolean replace, String name, Object... values);
   
   RedirectBuilder fragment(String fragment);

   RedirectBuilder mirror();
   
   RedirectBuilder encodeSessionId();
}
