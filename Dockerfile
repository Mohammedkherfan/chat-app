FROM openjdk:17
RUN useradd -s /bin/bash chat-app
USER chat-app
WORKDIR /opt
COPY api/target/api-1.0-SNAPSHOT.jar chat-api-service-1.0.0.jar
CMD ["java","-XX:CICompilerCount=2","-Dfile.encoding=UTF-8","-Xmx300m","-Xss512K","-Dspring.profiles.active=server","-jar","chat-api-1.0.0.jar"]