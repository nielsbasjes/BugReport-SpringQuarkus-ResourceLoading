Resourceloading problem from a SUBDIRECTORY
===
Using the Spring org.springframework.core.io.support.PathMatchingResourcePatternResolver is was found that
if a resource is inside a subdirectory loading fails in Quarkus.

This ONLY happens if the resource loading library is separate from the Quarkus project.

If you use the pom.xml__ file to bind the modules it will suddenly all work fine.

So this is essentially three SEPARATE parts:
- resourcelib
  The smallest variant of the problem I could recreate.
- java-test
  Really only the unit test from resourcelib to verify it works in a normal Java project.
- quarkus-test
  The base project as created with https://quarkus.io/guides/getting-started#bootstrapping-the-project where the resourcelib was added and used to provide the content.

So to build this:
- ( cd resourcelib/  ; mvn clean install )
- ( cd java-test/  ; mvn clean install )
- ( cd quarkus-test/ ; mvn clean package )

The build-and-test.sh script does exactly this.

Note that the java-test will find the resourcefiles

    URL [jar:file:/home/nbasjes/.m2/repository/org/acme/resourcelib/1.0-SNAPSHOT/resourcelib-1.0-SNAPSHOT.jar!/Content/world.txt]
    URL [jar:file:/home/nbasjes/.m2/repository/org/acme/resourcelib/1.0-SNAPSHOT/resourcelib-1.0-SNAPSHOT.jar!/Content/hello.txt]

yet the quarkas test will find

    URL [jar:file:/home/nbasjes/.m2/repository/org/acme/resourcelib/1.0-SNAPSHOT/resourcelib-1.0-SNAPSHOT.jar!/world.txt]
    URL [jar:file:/home/nbasjes/.m2/repository/org/acme/resourcelib/1.0-SNAPSHOT/resourcelib-1.0-SNAPSHOT.jar!/hello.txt]

Because the resources are really located in a subdirectory they will not be found.

So this fails with

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

