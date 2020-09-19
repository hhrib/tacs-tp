# Trabajo Practico - Tecnologias Avanzadas en la Construccion de Software
Segundo Cuatrimestre 2020 - UTN FRBA - El Mejor Grupo :)

# Como levantar la aplicacion:


# Docker:

1. Colocarse a la altura del directorio src
2. mvn clean package-> Genera .jar en directorio /target
3. docker build -t nombreContainer . -> Buildea contenedor
4. docker run -p 8080:8080 nombreContainer -> Ejecuta el contenedor
5. Desde otra terminal, entrar al directorio front-game y ejecutar -> ng serve --open

# Swagger UI - Ver especificación de la API Rest:
* Se puede verificar ingresando por navegador al endpoint /swagger-ui.html <br />
  Por ejemplo, si se levantó local -> http://localhost:8080/swagger-ui.html

-----
# Decisiones de Diseño:
1. Spring-Boot
2. Angular
3. Maven
4. DER: https://drive.google.com/file/d/1NlseerFxcISJEXySQQ5WB8tqhcmMn-Rl/view?usp=sharing
5. Diagrama de Clases (Relación Partida-Provincia-Municipio): https://drive.google.com/file/d/1OYIo54e2kQI0-9aefVhQ_q7i0KizMqq9/view?usp=sharing

-----
# Suposiciones:
1. En el transcurso de la partida no puedan cambiar los datos obtenidos de las APIs con respecto a la altura de las municipalidades o el nombre de las provincias, por ejemplo.
2. No hay equipos, cada jugador se enfrenta a los demas individualmente.
3. Si un jugador abandona la partida, esta sigue en curso con el resto y los municipios que tenia se redistribuyen entre los demas jugadores.
4. La configuracion de una partida no se puede modificar una vez que esta este en juego.