set PATH=C:\Program Files\Java\jdk-11.0.12\bin;%PATH%

REM set MVN_REPOSITORY=C:\ext\mvn-repository
set MVN_REPOSITORY=C:\Users\d2fde\.m2\repository
REM set MVN_REPOSITORY=C:\Users\formation\.m2\repository

set MY_H2_DB_URL=jdbc:h2:~/realmdb

set H2_VERSION=2.1.214
set H2_CLASSPATH=%MVN_REPOSITORY%\com\h2database\h2\%H2_VERSION%\h2-%H2_VERSION%.jar
