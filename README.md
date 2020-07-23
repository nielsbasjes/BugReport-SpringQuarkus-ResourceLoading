Resourceloading problem from a SUBDIRECTORY
===
Using the Spring org.springframework.core.io.support.PathMatchingResourcePatternResolver is was found that
if a resource is inside a subdirectory loading fails in Quarkus.

This ONLY happens if the resource loading library is separate from the Quarkus project.

So to build this:
- ( cd resourcelib/  ; mvn clean install )
- ( cd quarkus-test/ ; mvn clean package )

This fails with

    [executor-thread-1] INFO nl.basjes.content.ContentProvider - Loading resource pattern: classpath*:Content/**/*.txt
    [executor-thread-1] INFO nl.basjes.content.ContentProvider - Found 1 entr(y/ies)
    [executor-thread-1] INFO nl.basjes.content.ContentProvider - Loading entry: URL [jar:file:/home/nbasjes/.m2/repository/org/acme/resourcelib/1.0-SNAPSHOT/resourcelib-1.0-SNAPSHOT.jar!/helloworld.txt]
    java.io.FileNotFoundException: JAR entry helloworld.txt not found in /home/nbasjes/.m2/repository/org/acme/resourcelib/1.0-SNAPSHOT/resourcelib-1.0-SNAPSHOT.jar
        at sun.net.www.protocol.jar.JarURLConnection.connect(JarURLConnection.java:144)
        at sun.net.www.protocol.jar.JarURLConnection.getInputStream(JarURLConnection.java:152)
        at nl.basjes.shaded.org.springframework.core.io.UrlResource.getInputStream(UrlResource.java:173)
        at nl.basjes.content.ContentProvider.getContent(ContentProvider.java:40)
        at nl.basjes.content.ContentProvider.getContent(ContentProvider.java:25)
        at org.acme.getting.started.GreetingResource.hello(GreetingResource.java:15)
        at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)

