FROM maven:3.6.3-openjdk-15 as build

WORKDIR /app
COPY pom.xml /app

VOLUME maven-repo:/root/.m2

RUN mvn dependency:resolve

COPY . /app

RUN mvn package -Dmaven.test.skip=true

FROM tomcat:latest as runner

WORKDIR .

COPY --from=build /app/target/raise.war /usr/local/tomcat/webapps/
COPY wait-for-it.sh .

RUN mv /usr/local/tomcat/webapps/raise.war /usr/local/tomcat/webapps/ROOT.war

