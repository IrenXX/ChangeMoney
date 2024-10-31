# Docker file for Ubuntu with OpenJDK 17 and Tomcat 9.
FROM tomcat:9.0.83-jdk17-temurin

LABEL maintainer="Irina Kemova <irinareg98@gmail.com>"

# Enviroment variables
ENV CATALINA_HOME /usr/local/tomcat
ENV TOMCAT_WEBAPPS $CATALINA_HOME/webapps

# Set up current directory
WORKDIR /currencyexchange

# Copy project into Ubuntu
COPY . .

# Update OS
RUN apt -y update && apt-get -y upgrade
# Install Maven.
RUN apt -y install maven
# Create .WAR file
RUN mvn package
# Copy .WAR file into /usr/local/tomcat
RUN cp ./target/currencyexchange.war $TOMCAT_WEBAPPS
# Copy database into /usr/local/tomcat/webapps
RUN cp ./src/main/resources/exchange $CATALINA_HOME

# Set up port
EXPOSE 8080
