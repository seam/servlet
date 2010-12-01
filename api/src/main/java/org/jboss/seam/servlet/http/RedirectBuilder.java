package org.jboss.seam.servlet.http;

import javax.servlet.http.Cookie;

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
