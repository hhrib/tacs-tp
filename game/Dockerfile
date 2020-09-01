#Imagen de la cual partimos
FROM openjdk:11

COPY ./target/game-0.0.1-SNAPSHOT.jar app.jar
 
#Puertos
EXPOSE 8080
 
#ENTRYPOINT una vez que se levanta el container
ENTRYPOINT ["java","-jar","/app.jar"]
