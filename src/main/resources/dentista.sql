--
-- Base de datos: `dentista`
--
-- Traslado del proyecto dwes_jsp_jdbc (club de baloncesto) al proyecto
-- dwes_scriplet_dentista (clínica dental).
--
-- Tablas:
--   paciente   -> datos de los pacientes de la clínica (clave: id_paciente)
--   odontologos-> profesionales de la clínica (clave: id_odontologo)
--   cita       -> citas de los pacientes (clave: id_cita, FK: id_paciente, id_odontologo)
--

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de datos: `dentista`
--
CREATE DATABASE IF NOT EXISTS `dentista` DEFAULT CHARACTER SET utf8 COLLATE utf8_spanish2_ci;
USE `dentista`;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `paciente`
--

CREATE TABLE IF NOT EXISTS `paciente` (
  `id_paciente` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(40) COLLATE utf8_spanish2_ci NOT NULL,
  `apellidos` varchar(80) COLLATE utf8_spanish2_ci NOT NULL,
  `dni` varchar(9) COLLATE utf8_spanish2_ci NOT NULL,
  `fecha_nacimiento` date DEFAULT NULL,
  `telefono` varchar(20) COLLATE utf8_spanish2_ci DEFAULT NULL,
  `email` varchar(80) COLLATE utf8_spanish2_ci DEFAULT NULL,
  `direccion` varchar(120) COLLATE utf8_spanish2_ci DEFAULT NULL,
  `alergias` varchar(255) COLLATE utf8_spanish2_ci DEFAULT NULL,
  PRIMARY KEY (`id_paciente`),
  UNIQUE KEY `uk_paciente_dni` (`dni`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish2_ci;

--
-- Estructura de tabla para la tabla `odontologos`
--

CREATE TABLE IF NOT EXISTS `odontologos` (
  `id_odontologo` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(80) COLLATE utf8_spanish2_ci NOT NULL,
  `especialidad` varchar(80) COLLATE utf8_spanish2_ci DEFAULT NULL,
  PRIMARY KEY (`id_odontologo`),
  UNIQUE KEY `uk_odontologo_nombre` (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish2_ci;

--
-- Estructura de tabla para la tabla `cita`
--

CREATE TABLE IF NOT EXISTS `cita` (
  `id_cita` int NOT NULL AUTO_INCREMENT,
  `fecha` date NOT NULL,
  `hora` time NOT NULL,
  `id_paciente` int NOT NULL,
  `id_odontologo` int NOT NULL,
  `motivo` varchar(200) COLLATE utf8_spanish2_ci DEFAULT NULL,
  PRIMARY KEY (`id_cita`),
  KEY `fk_cita_paciente` (`id_paciente`),
  KEY `fk_cita_odontologo` (`id_odontologo`),
  CONSTRAINT `fk_cita_paciente` FOREIGN KEY (`id_paciente`) REFERENCES `paciente` (`id_paciente`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_cita_odontologo` FOREIGN KEY (`id_odontologo`) REFERENCES `odontologos` (`id_odontologo`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish2_ci;

-- --------------------------------------------------------

--
-- Volcado de datos para la tabla `paciente`
--

INSERT INTO `paciente` (`id_paciente`, `nombre`, `apellidos`, `dni`, `fecha_nacimiento`, `telefono`, `email`, `direccion`, `alergias`) VALUES
(1, 'Ana María', 'Bermúdez Espada', '12345678A', '1980-05-12', '600112233', 'ana.bermudez@example.com', 'Calle Marqués de Larios, 15', 'Penicilina'),
(2, 'Margarita', 'Cano Cuenca', '23456789B', '1976-11-03', '600223344', 'margarita.cano@example.com', 'Avenida de Andalucía, 42', NULL),
(3, 'Adrián Manuel', 'Doña Enríquez', '34567890C', '1992-02-27', '600334455', 'adrian.dona@example.com', 'Calle Álamos, 7', 'Latex'),
(4, 'Esther', 'Fernández Padilla', '45678901D', '1998-08-19', '600445566', 'esther.fernandez@example.com', 'Paseo del Prado, 88', NULL),
(5, 'Ester María', 'Galán Bazán', '56789012E', '1974-01-30', '600556677', 'ester.galan@example.com', 'Calle Hoyo de Esparteros, 3', 'Anestésicos locales'),
(6, 'Eva', 'Martín Jurado', '67890123F', '1982-04-21', '600667788', 'eva.martin@example.com', 'Avenida de Velázquez, 120', NULL),
(7, 'Carlos', 'Moreno Blanco', '78901234G', '2001-09-08', '600778899', 'carlos.moreno@example.com', 'Calle Elena, 24', NULL),
(8, 'Juan Antonio', 'Narváez Gálvez', '89012345H', '1969-06-15', '600889900', 'juan.narvaez@example.com', 'Camino de Churriana, 61', 'Ibuprofeno'),
(9, 'Micaela Yanina', 'Rossi', '90123456J', '1990-12-02', '600990011', 'micaela.rossi@example.com', 'Calle Santa Ana, 9', NULL),
(10, 'Desire', 'Alcoholado Gómez', '01234567K', '1994-03-25', '601001122', 'desire.alcoholado@example.com', 'Avenida de Villazan, 55', 'Metales pesados'),
(11, 'Priscila María', 'Anaya Perea', '11223344L', '1988-07-11', '601112233', 'priscila.anaya@example.com', 'Calle Huéscar, 13', NULL),
(12, 'Diego', 'Domínguez García', '22334455M', '1985-10-05', '601223344', 'diego.dominguez@example.com', 'Plaza de la Marina, 2', NULL),
(13, 'María Esther', 'Fuentes García', '33445566N', '1979-05-23', '601334455', 'maria.fuentes@example.com', 'Calle Eslava, 30', 'Aspirina'),
(14, 'Ana Rocío', 'García Beltrán', '44556677P', '1993-02-14', '601445566', 'ana.garcia@example.com', 'Calle Wilkins, 19', NULL),
(15, 'José Alberto', 'García Pendón', '55667788Q', '1971-09-18', '601556677', 'jose.garcia@example.com', 'Camino de la Concepción, 44', NULL);

--
-- Volcado de datos para la tabla `odontologos`
--

INSERT INTO `odontologos` (`id_odontologo`, `nombre`, `especialidad`) VALUES
(1, 'Dra. Elena García Ruiz', 'Endodoncia'),
(2, 'Dr. Miguel Ruiz Navarro', 'Ortodoncia'),
(3, 'Dra. Lucía Molina Sánchez', 'Prótesis y estética dental'),
(4, 'Dr. Andrés Santos Vega', 'Cirugía maxilofacial');

--
-- Volcado de datos para la tabla `cita`
--
-- Las fechas se calculan respecto al día de importación (CURDATE()) para que
-- siempre haya citas próximas y citas pasadas.
--

INSERT INTO `cita` (`id_cita`, `fecha`, `hora`, `id_paciente`, `id_odontologo`, `motivo`) VALUES
(1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '09:30:00', 1, 1, 'Revisión anual y limpieza'),
(2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '11:00:00', 4, 2, 'Empaste de pieza molar'),
(3, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '10:00:00', 2, 3, 'Tratamiento de sarro'),
(4, DATE_ADD(CURDATE(), INTERVAL 3 DAY), '16:30:00', 6, 4, 'Endoscopia dental'),
(5, DATE_ADD(CURDATE(), INTERVAL 5 DAY), '09:00:00', 3, 2, 'Extracción de wisdom tooth'),
(6, DATE_ADD(CURDATE(), INTERVAL 6 DAY), '12:00:00', 11, 1, 'Ortodoncia - control mensual'),
(7, DATE_ADD(CURDATE(), INTERVAL 8 DAY), '17:00:00', 8, 3, 'Prótesis dental'),
(8, DATE_ADD(CURDATE(), INTERVAL 10 DAY), '10:30:00', 5, 4, 'Revisión de implantes'),
(9, DATE_ADD(CURDATE(), INTERVAL 12 DAY), '09:30:00', 13, 1, 'Blanqueamiento'),
(10, DATE_ADD(CURDATE(), INTERVAL 14 DAY), '15:00:00', 9, 2, 'Sellado de fisuras'),
(11, DATE_ADD(CURDATE(), INTERVAL 15 DAY), '11:30:00', 14, 3, 'Revisión anual y limpieza'),
(12, DATE_ADD(CURDATE(), INTERVAL -2 DAY), '10:00:00', 10, 4, 'Dolor de muela'),
(13, DATE_ADD(CURDATE(), INTERVAL -5 DAY), '09:30:00', 12, 2, 'Curas postoperatorias'),
(14, DATE_ADD(CURDATE(), INTERVAL -9 DAY), '16:00:00', 7, 1, 'Primera visita'),
(15, DATE_ADD(CURDATE(), INTERVAL -20 DAY), '12:30:00', 15, 3, 'Revisión anual');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
