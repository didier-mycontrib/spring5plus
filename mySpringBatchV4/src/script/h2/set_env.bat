
REM set MVN_REPOSITORY=C:\ext\mvn-repository
set MVN_REPOSITORY=C:\Users\d2fde\.m2\repository
REM set MVN_REPOSITORY=C:\Users\formation\.m2\repository

set MY_H2_DB_URL_OUTPUT=jdbc:h2:~/outputDbV4
set MY_H2_DB_URL_INPUT=jdbc:h2:~/inputDbV4
set MY_H2_DB_URL_JOBREPOSITORY=jdbc:h2:~/jobRepositoryDbV4

set PATH="C:\Prog\java\eclipse-jee-2023-12\eclipse\plugins\org.eclipse.justj.openjdk.hotspot.jre.full.win32.x86_64_17.0.9.v20231028-0858\jre\bin"

set H2_VERSION=2.1.214
set H2_CLASSPATH=%MVN_REPOSITORY%\com\h2database\h2\%H2_VERSION%\h2-%H2_VERSION%.jar
