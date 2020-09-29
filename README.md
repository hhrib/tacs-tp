# Trabajo Practico - Tecnologias Avanzadas en la Construccion de Software
Segundo Cuatrimestre 2020 - UTN FRBA - El Mejor Grupo :)

# Como levantar la aplicacion:


# Docker:

Para build:
1. Ejecutar docker-compose -f docker-compose-build.yml start
2. Entrar en el browser a localhost:4200
Para ejecuacion
1. Ejecutar docker-compose up
2. Entrar en el browser a localhost:4200

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
4. Solo el municipio destino se bloquea al mover gauchos.
5. No se puede cambiar la configuracion de la partida una vez creada.

-----
# CI/CD:
Imagenes creadas con actions de github y publicas en :
* Back: https://hub.docker.com/repository/docker/hhrib/tacs_server
* Front: https://hub.docker.com/repository/docker/hhrib/tacs_client
