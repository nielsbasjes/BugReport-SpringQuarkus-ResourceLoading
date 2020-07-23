Resourceloading problem from a SUBDIRECTORY
===
Using the Spring org.springframework.core.io.support.PathMatchingResourcePatternResolver it was found that
if a resource is inside a subdirectory loading fails in Quarkus.

This was reported here: https://github.com/nielsbasjes/yauaa/issues/216

This ONLY happens if the resources are in an external dependency of the Quarkus project and they are in a subdirectory.

If you use the pom.xml__ file to bind the modules it will suddenly all works fine.

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

Note that the java-test will find the resource files in the subdirectory /Content

    URL [jar:file:/home/nbasjes/.m2/repository/org/acme/resourcelib/1.0-SNAPSHOT/resourcelib-1.0-SNAPSHOT.jar!/Content/world.txt]
    URL [jar:file:/home/nbasjes/.m2/repository/org/acme/resourcelib/1.0-SNAPSHOT/resourcelib-1.0-SNAPSHOT.jar!/Content/hello.txt]

yet the quarkas test will find them in the root folder (where they are NOT located!)

    URL [jar:file:/home/nbasjes/.m2/repository/org/acme/resourcelib/1.0-SNAPSHOT/resourcelib-1.0-SNAPSHOT.jar!/world.txt]
    URL [jar:file:/home/nbasjes/.m2/repository/org/acme/resourcelib/1.0-SNAPSHOT/resourcelib-1.0-SNAPSHOT.jar!/hello.txt]

Because the resources are located in a subdirectory they will not be found.

So this fails with

    [executor-thread-1] INFO nl.basjes.content.ContentProvider - Loading resource pattern: classpath*:Content/**/*.txt
    [executor-thread-1] INFO nl.basjes.content.ContentProvider - Found 2 entr(y/ies)
    [executor-thread-1] INFO nl.basjes.content.ContentProvider - -  URL [jar:file:/home/nbasjes/.m2/repository/org/acme/resourcelib/1.0-SNAPSHOT/resourcelib-1.0-SNAPSHOT.jar!/world.txt]
    [executor-thread-1] INFO nl.basjes.content.ContentProvider - -  URL [jar:file:/home/nbasjes/.m2/repository/org/acme/resourcelib/1.0-SNAPSHOT/resourcelib-1.0-SNAPSHOT.jar!/hello.txt]
    [executor-thread-1] INFO nl.basjes.content.ContentProvider - Trying to load them
    [executor-thread-1] INFO nl.basjes.content.ContentProvider - Loading entry: URL [jar:file:/home/nbasjes/.m2/repository/org/acme/resourcelib/1.0-SNAPSHOT/resourcelib-1.0-SNAPSHOT.jar!/world.txt]
    java.io.FileNotFoundException: JAR entry world.txt not found in /home/nbasjes/.m2/repository/org/acme/resourcelib/1.0-SNAPSHOT/resourcelib-1.0-SNAPSHOT.jar
        at sun.net.www.protocol.jar.JarURLConnection.connect(JarURLConnection.java:144)
        at sun.net.www.protocol.jar.JarURLConnection.getInputStream(JarURLConnection.java:152)
        at org.springframework.core.io.UrlResource.getInputStream(UrlResource.java:173)
        at nl.basjes.content.ContentProvider.getContent(ContentProvider.java:44)
