# Trabajo Practico - Tecnologias Avanzadas en la Construccion de Software
Segundo Cuatrimestre 2020 - UTN FRBA - El Mejor Grupo :)

# Como levantar la aplicacion:


# Docker para levantar local:
Una vez dentro del directorio principal, ejecutar docker-compose up

# Docker si se quiere levantar dentro de la máquina en AWS:
1. Conectarse a la Máquina de aws con el certificado .pem. En una consola con ssh:<br />
   $ ssh -i {claves}.pem ubuntu@ec2-3-134-110-81.us-east-2.compute.amazonaws.com
2. Ir al directorio donde está clonado el repo: /home/ubuntu/UTN/tacs-tp/
3. Ejecutar docker-compose up -d
4. Acceder por un navegador a https://tacswololo.tk
5. Para revisar logs de los contenedores. Dentro de la máquina EC2 de AWS:<br />
   * Back: sudo docker logs tacs-tp_back_1
   * Front: sudo docker logs tacs-tp_front_1

# Swagger UI - Ver especificación de la API Rest:
* Se puede verificar ingresando por navegador al endpoint /swagger-ui.html <br />
  - local -> http://localhost:8080/swagger-ui.html <br />
  - cloud -> https://app.tacswololo.tk/swagger-ui.html
  
# Postman
* Se puede usar la coleccion de request para probar en Postman, primero se debe hacer un request de access token a auth0, luego se pueden hacer todos los request que quiera

-----
# Decisiones de Diseño:
1. Spring-Boot
2. Angular
3. Maven
4. Diagrama de Clases (Relación Partida-Provincia-Municipio): https://drive.google.com/file/d/1OYIo54e2kQI0-9aefVhQ_q7i0KizMqq9/view?usp=sharing
5. Base de datos mongodb: las deciciones de diseño que nos llevaron a utilizar mongodb son las siguientes:
    * Poco conocimiento del dominio de la aplicación al momemnto de comenzar el desarrollo: al comenzar a desarrollar decidimos centrarnos en poder lograr un MVP poniendo más foco en la lógica del negocio en lugar de cómo persistir esa lógica, la utilización de mongodb nos permitió lograr esto debido a que la estructura de datos no es estática.
    * Escalbilidad: mongodb nos permite escalar de formas que las bases tradicionales SQL no permiten hacerlo, esto nos da cómo ventaja que si nuestra aplicación comienza a crecer no tener un cuello de botella como puede llegar a sucede con una base de datos SQL.
    * Cloud: Un factor que si bien no fue determinante a la hora de la elección pero si es algo a tener en cuenta es la posibilidad que nos brinda mongoaatlas de poder tener la base de datos en la nube de manera sencilla y lo principal con un espacio gratuito de 500 MB, para lo cual en una primera instancia de un proyecto nos brinda la posibilidad de no preocuparnos por este aspecto.
    
-----
# Suposiciones:
1. En el transcurso de la partida no puedan cambiar los datos obtenidos de las APIs con respecto a la altura de las municipalidades o el nombre de las provincias, por ejemplo.
2. No hay equipos, cada jugador se enfrenta a los demas individualmente.
3. Si un jugador abandona la partida, esta sigue en curso con el resto y los municipios que tenia se redistribuyen entre los demas jugadores.
4. Solo el municipio destino se bloquea al mover gauchos.
5. No se puede cambiar la configuracion de la partida una vez creada.
6. Si un jugador abandona ANTES de que empieze la partida, esta se considera cancelada, de lo contrario, la partida continua con los jugadores restantes.

-----
# Cómo jugar?:
1. Crear una partida: Menú -> New Match
2. Una vez creada la partida: Menú -> Join Game
3. Durante su turno, puede Atacar/Mover tropas/Cambiar estado municipio.
4. Cuando lo decida, finalice su turno: Menú -> End Turn

-----
# CI/CD:
Imagenes creadas con actions de github y publicas en :
* Back: https://hub.docker.com/repository/docker/hhrib/tacs_server
* Front: https://hub.docker.com/repository/docker/hhrib/tacs_client
