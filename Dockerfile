FROM tomcat:8.5.93-jdk8

COPY tomcat/app.war /usr/local/tomcat/webapps/ROOT.war
COPY lib/ojdbc8.jar /usr/local/tomcat/lib/