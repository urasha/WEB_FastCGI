FROM openjdk:22

RUN mkdir -p /opt/user/server

COPY lib/fastcgi-lib.jar /opt/user/server/lib/fastcgi-lib.jar
COPY src/ /opt/user/server/src

WORKDIR /opt/user/server/

RUN mkdir -p out
RUN javac -d out -cp lib/fastcgi-lib.jar src/*.java

RUN echo "Main-Class: Main" > MANIFEST.mf
RUN echo "Class-Path: lib/fastcgi-lib.jar" >> MANIFEST.mf

RUN jar -cvfm server.jar MANIFEST.mf -C out . -C lib/ fastcgi-lib.jar

EXPOSE 1489

CMD ["java", "-DFCGI_PORT=1489", "-jar", "server.jar"]