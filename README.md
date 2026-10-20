# Clínica Dental

Aplicación web de gestión de pacientes y citas, implementada con **Servlets, JSP, JDBC y MySQL**. El servlet recibe las peticiones, el DAO concentra las consultas y modificaciones de base de datos, los modelos representan los datos y las JSP se limitan a presentar las vistas.

## Requisitos

- JDK 21.
- Maven 3.8 o superior.
- Apache Tomcat 9.0.x (la aplicación usa `javax.servlet`, no `jakarta.servlet`).
- MySQL Server en `localhost:3306`.

## Base de datos

El script `src/main/resources/dentista.sql` crea la base `dentista`, las tablas `paciente`, `odontologos` y `cita`, además de cargar datos de ejemplo. Importa el script desde la carpeta del proyecto:

```bash
mysql -u user -p < src/main/resources/dentista.sql
```

Introduce la contraseña de MySQL de la cuenta `user` cuando se solicite. La cuenta necesita permiso para crear la base de datos y sus tablas. Si ya cargaste los datos, no vuelvas a ejecutar el script sin vaciar o recrear la base, porque los registros de ejemplo pueden duplicarse.

La conexión JDBC se configura una sola vez en `ClinicaDAO`. Por defecto utiliza:

- URL: `jdbc:mysql://localhost:3306/dentista`
- Usuario: `user`
- Contraseña: `user`

Si tu instalación usa otros valores, configúralos como variables de entorno `DB_URL`, `DB_USER` y `DB_PASSWORD` antes de arrancar Tomcat. Por ejemplo, en Linux/macOS:

```bash
export DB_URL='jdbc:mysql://localhost:3306/dentista?useSSL=false&allowPublicKeyRetrieval=true&characterEncoding=UTF-8'
export DB_USER='tu_usuario'
export DB_PASSWORD='tu_contraseña'
```

La cuenta configurada debe tener permisos sobre la base `dentista`.

## Compilar y ejecutar

Desde la raíz del proyecto, genera el WAR:

```bash
mvn clean package
```

El artefacto generado es `target/scriplet_dentista.war`. Despliégalo en Tomcat 9 (por ejemplo, copiándolo a `TOMCAT_HOME/webapps/`) e inicia Tomcat. Con el puerto por defecto, abre:

```text
http://localhost:8080/scriplet_dentista/
```

La ruta principal del servlet es `/dentista`; `index.jsp` redirige a esa ruta. El listado de citas muestra las próximas por defecto; desde la página se puede consultar el histórico completo.

## Estructura y responsabilidades

```text
src/main/java/es/dwes/dentista/
├── dao/
│   └── ClinicaDAO.java             # Conexión y operaciones JDBC
├── model/
│   ├── Cita.java                   # Datos de una cita
│   ├── Odontologo.java             # Datos de un odontólogo
│   └── Paciente.java               # Datos de un paciente
└── servlet/
    └── DentistaServlet.java        # Controlador único de peticiones

src/main/webapp/
├── WEB-INF/vistas/                 # JSP no accesibles directamente
├── index.jsp                       # Redirección al controlador
└── estilos.css
```

- `DentistaServlet` centraliza navegación, lectura de parámetros, validación y respuesta a las peticiones GET/POST.
- `ClinicaDAO` es el único lugar con conexión y SQL. Usa `PreparedStatement` para inserciones y borrados, y cierra conexiones y consultas con *try-with-resources*.
- Los modelos `Paciente`, `Cita` y `Odontologo` transportan los datos entre el DAO, el servlet y las vistas.
- Las JSP bajo `WEB-INF/vistas` renderizan los datos con JSTL y no ejecutan consultas SQL. No se necesita `web.xml`: el servlet se registra con `@WebServlet`.
- Las altas y bajas se envían por POST y, al completarse, redirigen al listado correspondiente.

## Funcionalidades

- Consultar pacientes y el número de citas asociadas.
- Consultar próximas citas o el histórico completo.
- Registrar pacientes y citas.
- Anular citas y borrar pacientes (el borrado en cascada elimina también las citas de ese paciente).
- Elegir pacientes y odontólogos existentes al registrar una cita.

## Entrega

Para generar el ZIP de entrega, incluye el código fuente y los archivos del proyecto, pero excluye `target/` y cualquier carpeta `build/`. El WAR compilado no es necesario si la entrega requiere el código fuente.
