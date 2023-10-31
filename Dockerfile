FROM openjdk:17

COPY target/cs203g1t3-0.0.1-SNAPSHOT.jar cs203g1t3-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/cs203g1t3-0.0.1-SNAPSHOT.jar"]

