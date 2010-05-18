package org.jboss.seam.servlet.event;

import org.junit.Test;

//@RunWith(Arquillian.class)
public class ServletEventBridgeTest
{

   @Test
   public void dummy()
   {

   }
   /*
    * @Deployment public static JavaArchive createTestArchive() { JavaArchive a
    * = Archives.create("test.jar",
    * JavaArchive.class).addClasses(ServletEventBridge.class,
    * ServletEventObserver.class).addManifestResource(new ByteArrayAsset(new
    * byte[0]), ArchivePaths.create("beans.xml"));
    * System.out.println(a.toString(Formatters.VERBOSE)); return a; }
    * 
    * @Inject ServletEventBridge listener;
    * 
    * @Inject ServletEventObserver observer;
    * 
    * @Test public void testObserveAnySessionEvent() { observer.reset();
    * HttpSessionEvent e1 = new HttpSessionEvent(null); HttpSessionEvent e2 =
    * new HttpSessionEvent(null); HttpSessionEvent e3 = new
    * HttpSessionEvent(null); HttpSessionEvent e4 = new HttpSessionEvent(null);
    * listener.sessionCreated(e1); listener.sessionDidActivate(e2);
    * listener.sessionWillPassivate(e3); listener.sessionDestroyed(e4);
    * observer.assertObservations("1", e1, e2, e3, e4); }
    * 
    * @Test public void testObserveSessionActivated() { observer.reset();
    * HttpSessionEvent e = new HttpSessionEvent(null);
    * listener.sessionDidActivate(e); observer.assertObservations("2", e); }
    * 
    * @Test public void testObserveSessionPassivated() { observer.reset();
    * HttpSessionEvent e = new HttpSessionEvent(null);
    * listener.sessionWillPassivate(e); observer.assertObservations("3", e); }
    * 
    * @Test public void testObserveSessionCreated() { observer.reset();
    * HttpSessionEvent e = new HttpSessionEvent(null);
    * listener.sessionCreated(e); observer.assertObservations("4", e); }
    * 
    * @Test public void testObserveSessionDestroyed() { observer.reset();
    * HttpSessionEvent e = new HttpSessionEvent(null);
    * listener.sessionDestroyed(e); observer.assertObservations("5", e); }
    * 
    * @Test public void testObserveAnySessionBindingEvent() { observer.reset();
    * HttpSessionBindingEvent e1 = new HttpSessionBindingEvent(null, "");
    * HttpSessionBindingEvent e2 = new HttpSessionBindingEvent(null, "");
    * HttpSessionBindingEvent e3 = new HttpSessionBindingEvent(null, "");
    * HttpSessionBindingEvent e4 = new HttpSessionBindingEvent(null, "");
    * HttpSessionBindingEvent e5 = new HttpSessionBindingEvent(null, "");
    * listener.attributeAdded(e1); listener.attributeRemoved(e2);
    * listener.attributeReplaced(e3); listener.valueBound(e4);
    * listener.valueUnbound(e5); observer.assertObservations("6", e1, e2, e3,
    * e4, e5); }
    * 
    * @Test public void testObserveSessionAttributeAdded() { observer.reset();
    * HttpSessionBindingEvent e = new HttpSessionBindingEvent(null, "");
    * listener.attributeAdded(e); observer.assertObservations("7", e); }
    * 
    * @Test public void testObserveSessionAttributeReplaced() {
    * observer.reset(); HttpSessionBindingEvent e = new
    * HttpSessionBindingEvent(null, ""); listener.attributeReplaced(e);
    * observer.assertObservations("8", e); }
    * 
    * @Test public void testObserveSessionAttributeRemoved() { observer.reset();
    * HttpSessionBindingEvent e = new HttpSessionBindingEvent(null, "");
    * listener.attributeRemoved(e); observer.assertObservations("9", e); }
    * 
    * @Test public void testObserveSessionValueBound() { observer.reset();
    * HttpSessionBindingEvent e = new HttpSessionBindingEvent(null, "");
    * listener.valueBound(e); observer.assertObservations("10", e); }
    * 
    * @Test public void testObserveSessionValueUnbound() { observer.reset();
    * HttpSessionBindingEvent e = new HttpSessionBindingEvent(null, "");
    * listener.valueUnbound(e); observer.assertObservations("11", e); }
    * 
    * @Test public void testObserveSpecificSessionAttributeAdded() {
    * observer.reset(); HttpSessionBindingEvent e = new
    * HttpSessionBindingEvent(null, ""); listener.attributeAdded(e);
    * observer.assertObservations("12", e); }
    * 
    * @Test public void testObserveSpecificSessionAttributeReplaced() {
    * observer.reset(); HttpSessionBindingEvent e = new
    * HttpSessionBindingEvent(null, ""); listener.attributeReplaced(e);
    * observer.assertObservations("13", e); }
    * 
    * @Test public void testObserveSpecificSessionAttributeRemoved() {
    * observer.reset(); HttpSessionBindingEvent e = new
    * HttpSessionBindingEvent(null, ""); listener.attributeRemoved(e);
    * observer.assertObservations("14", e); }
    * 
    * @Test public void testObserveSpecificSessionAttribute() {
    * observer.reset(); HttpSessionBindingEvent e1 = new
    * HttpSessionBindingEvent(null, ""); HttpSessionBindingEvent e2 = new
    * HttpSessionBindingEvent(null, ""); HttpSessionBindingEvent e3 = new
    * HttpSessionBindingEvent(null, ""); listener.attributeAdded(e1);
    * listener.attributeRemoved(e2); listener.attributeReplaced(e3);
    * observer.assertObservations("14a", e1, e2, e3); }
    * 
    * @Test public void testObserveSpecificSessionValueBound() {
    * observer.reset(); HttpSessionBindingEvent e = new
    * HttpSessionBindingEvent(null, ""); listener.valueBound(e);
    * observer.assertObservations("15", e); }
    * 
    * @Test public void testObserveSpecificSessionValueUnbound() {
    * observer.reset(); HttpSessionBindingEvent e = new
    * HttpSessionBindingEvent(null, ""); listener.valueUnbound(e);
    * observer.assertObservations("16", e); }
    * 
    * @Test public void testObserveSpecificSessionValue() { observer.reset();
    * HttpSessionBindingEvent e1 = new HttpSessionBindingEvent(null, "");
    * HttpSessionBindingEvent e2 = new HttpSessionBindingEvent(null, "");
    * listener.valueBound(e1); listener.valueUnbound(e2);
    * observer.assertObservations("16a", e1, e2); }
    * 
    * @Test public void testObserveServletContext() { observer.reset();
    * ServletContextEvent e1 = new ServletContextEvent(null);
    * ServletContextEvent e2 = new ServletContextEvent(null);
    * listener.contextInitialized(e1); listener.contextDestroyed(e2);
    * observer.assertObservations("17", e1, e2); }
    * 
    * @Test public void testObserveServletContextInitialized() {
    * observer.reset(); ServletContextEvent e = new ServletContextEvent(null);
    * listener.contextInitialized(e); observer.assertObservations("18", e); }
    * 
    * @Test public void testObserveServletContextDestroyed() { observer.reset();
    * ServletContextEvent e = new ServletContextEvent(null);
    * listener.contextInitialized(e); observer.assertObservations("19", e); }
    * 
    * @Test public void testObserveServletContextAttribute() { observer.reset();
    * ServletContextAttributeEvent e1 = new ServletContextAttributeEvent(null,
    * "", null); ServletContextAttributeEvent e2 = new
    * ServletContextAttributeEvent(null, "", null); ServletContextAttributeEvent
    * e3 = new ServletContextAttributeEvent(null, "", null);
    * listener.attributeAdded(e1); listener.attributeRemoved(e2);
    * listener.attributeReplaced(e3); observer.assertObservations("20", e1, e2,
    * e3); }
    * 
    * @Test public void testObserveAnyServletContextAttributeAdded() {
    * observer.reset(); ServletContextAttributeEvent e = new
    * ServletContextAttributeEvent(null, "", null); listener.attributeAdded(e);
    * observer.assertObservations("21", e); }
    * 
    * @Test public void testObserveAnyServletContextAttributeReplaced() {
    * observer.reset(); ServletContextAttributeEvent e = new
    * ServletContextAttributeEvent(null, "", null);
    * listener.attributeReplaced(e); observer.assertObservations("22", e); }
    * 
    * @Test public void testObserveAnyServletContextAttributeRemoved() {
    * observer.reset(); ServletContextAttributeEvent e = new
    * ServletContextAttributeEvent(null, "", null);
    * listener.attributeRemoved(e); observer.assertObservations("23", e); }
    * 
    * @Test public void testObserveSpecificServletContextAttributeAdded() {
    * observer.reset(); ServletContextAttributeEvent e = new
    * ServletContextAttributeEvent(null, "", null); listener.attributeAdded(e);
    * observer.assertObservations("24", e); }
    * 
    * @Test public void testObserveSpecificServletContextAttributeReplaced() {
    * observer.reset(); ServletContextAttributeEvent e = new
    * ServletContextAttributeEvent(null, "", null);
    * listener.attributeReplaced(e); observer.assertObservations("25", e); }
    * 
    * @Test public void testObserveSpecificServletContextAttributeRemoved() {
    * observer.reset(); ServletContextAttributeEvent e = new
    * ServletContextAttributeEvent(null, "", null);
    * listener.attributeRemoved(e); observer.assertObservations("26", e); }
    * 
    * @Test public void testObserveServletRequest() { observer.reset();
    * ServletRequestEvent e1 = new ServletRequestEvent(null, null);
    * ServletRequestEvent e2 = new ServletRequestEvent(null, null);
    * listener.requestInitialized(e1); listener.requestDestroyed(e2);
    * observer.assertObservations("27", e1, e2); }
    * 
    * @Test public void testObserveRequestInitialized() { observer.reset();
    * ServletRequestEvent e = new ServletRequestEvent(null, null);
    * listener.requestInitialized(e); observer.assertObservations("28", e); }
    * 
    * @Test public void testObserveRequestDestroyed() { observer.reset();
    * ServletRequestEvent e = new ServletRequestEvent(null, null);
    * listener.requestDestroyed(e); observer.assertObservations("29", e); }
    * 
    * @Test public void testObserveAnyServletRequestAttributeAdded() {
    * observer.reset(); ServletRequestAttributeEvent e = new
    * ServletRequestAttributeEvent(null, null, "", null);
    * listener.attributeAdded(e); observer.assertObservations("30", e); }
    * 
    * @Test public void testObserveAnyServletRequestAttributeReplaced() {
    * observer.reset(); ServletRequestAttributeEvent e = new
    * ServletRequestAttributeEvent(null, null, "", null);
    * listener.attributeReplaced(e); observer.assertObservations("31", e); }
    * 
    * @Test public void testObserveAnyServletRequestAttributeRemoved() {
    * observer.reset(); ServletRequestAttributeEvent e = new
    * ServletRequestAttributeEvent(null, null, "", null);
    * listener.attributeRemoved(e); observer.assertObservations("32", e); }
    * 
    * @Test public void testObserveSpecificServletRequestAttributeAdded() {
    * observer.reset(); ServletRequestAttributeEvent e = new
    * ServletRequestAttributeEvent(null, null, "", null);
    * listener.attributeAdded(e); observer.assertObservations("33", e); }
    * 
    * @Test public void testObserveSpecificServletRequestAttributeReplaced() {
    * observer.reset(); ServletRequestAttributeEvent e = new
    * ServletRequestAttributeEvent(null, null, "", null);
    * listener.attributeReplaced(e); observer.assertObservations("34", e); }
    * 
    * @Test public void testObserveSpecificServletRequestAttributeRemoved() {
    * observer.reset(); ServletRequestAttributeEvent e = new
    * ServletRequestAttributeEvent(null, null, "", null);
    * listener.attributeRemoved(e); observer.assertObservations("35", e); }
    * 
    * @Test public void testObserveAsynchrnousEventCompleted() throws
    * IOException { observer.reset(); AsyncEvent e = new AsyncEvent(null);
    * listener.onComplete(e); observer.assertObservations("36", e); }
    * 
    * @Test public void testObserveAsynchrnousEventError() throws IOException {
    * observer.reset(); AsyncEvent e = new AsyncEvent(null);
    * listener.onError(e); observer.assertObservations("37", e); }
    * 
    * @Test public void testObserveAsynchrnousEventStarted() throws IOException
    * { observer.reset(); AsyncEvent e = new AsyncEvent(null);
    * listener.onStartAsync(e); observer.assertObservations("38", e); }
    * 
    * @Test public void testObserveAsynchrnousEventTimedOut() throws IOException
    * { observer.reset(); AsyncEvent e = new AsyncEvent(null);
    * listener.onTimeout(e); observer.assertObservations("39", e); }
    * 
    * @Test public void testObserveAsynchrnousEventT() throws IOException {
    * observer.reset(); AsyncEvent e1 = new AsyncEvent(null); AsyncEvent e2 =
    * new AsyncEvent(null); AsyncEvent e3 = new AsyncEvent(null); AsyncEvent e4
    * = new AsyncEvent(null); listener.onComplete(e1); listener.onError(e2);
    * listener.onStartAsync(e3); listener.onTimeout(e4);
    * observer.assertObservations("40", e1, e2, e3, e4); }
    */
}