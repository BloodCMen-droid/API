# Imagen base con Tomcat 11 y Java 21
FROM tomcat:11-jre21

# Eliminamos aplicaciones default de Tomcat
RUN rm -rf /usr/local/tomcat/webapps/*

# Copiamos tu WAR al contenedor
COPY Api_Producto_p2.war /usr/local/tomcat/webapps/ROOT.war

# Exponemos el puerto 8080
EXPOSE 8080

# Comando para arrancar Tomcat
CMD ["catalina.sh", "run"]
