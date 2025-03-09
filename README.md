# Finder
##Registro de usuarios:
Permite crear un nuevo usuario proporcionando nombre, correo electrónico, contraseña y foto de perfil (convertida a base64 desde un input file).

##Inicio de sesión:
Verificación de credenciales y establecimiento de sesión para acceder a los perfiles.

##Visualización de perfiles:
Una vez autenticado, el usuario puede ver perfiles de otros usuarios a los que aún no ha evaluado (excluyendo a los que ya le han dado dislike a él previamente o los que él ya haya evaluado).

##Evaluación (Like/Dislike):
Permite dar like o dislike a los perfiles y, en caso de match (cuando ambos se dan like), se notifica al usuario. Los usuarios se cargan uno a uno de la base de datos para evitar sobrecargar la aplicación

##Listado de matches:
Se muestra un historial de todos los matches que ha generado el usuario al pulsar el botón "Matches". Al hacer click en alguno se abre la conversación correspondiente.

##Sistema de mensajería:
Los usuarios que hayan hecho match pueden intercambiar mensajes en un chat (aunque hay que recargar las páginas para verlo). Al chat se accede desde el listado de matches, clicando sobre el usuario con el que se quiere hablar.

##Actualización de foto de perfil:
El usuario autenticado puede cambiar su foto de perfil, y los cambios se guardan en la base de datos.
