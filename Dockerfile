FROM openjdk:8-jdk-alpine
RUN apk add --update \
    curl \
    && rm -rf /var/cache/apk/*
RUN apk add openssl
RUN mkdir -p src/main/resources
RUN cd src/main/resources && \
curl https://certs.secureserver.net/repository/sf-class2-root.crt -O && \
openssl x509 -outform der -in sf-class2-root.crt -out temp_file.der && \
keytool -import -alias cassandra -keystore cassandra_truststore.jks -file temp_file.der -storepass p4ssw0rd -noprompt

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 8080