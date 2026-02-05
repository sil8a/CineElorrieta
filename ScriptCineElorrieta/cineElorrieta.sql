create database cine_Elorrieta
collate utf8mb4_spanish_ci;
use cine_Elorrieta;
-- 1. Tabla SALA
CREATE TABLE sala (
    id_sala INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50),
    numero_sillas INT NOT NULL -- También llamado 'capacidad'
);

-- 2. Tabla PELICULA
CREATE TABLE pelicula (
    id_pelicula INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(100),
    duracion INT,
    genero VARCHAR(50),
    precio_base DOUBLE
);

-- 3. Tabla CLIENTE
CREATE TABLE cliente (
    dni VARCHAR(9) PRIMARY KEY,
    nombre VARCHAR(50),
    apellidos VARCHAR(100),
    email VARCHAR(100),
    contrasena VARCHAR(255) -- Cambiado a VARCHAR para facilitar tu Java
);

-- 4. Tabla SESION (Relaciona Pelicula y Sala con IDs numéricos)
CREATE TABLE sesion (
    id_sesion INT AUTO_INCREMENT PRIMARY KEY,
    fecha DATE,
    hora_inicio TIME,
    hora_fin TIME,
    id_sala INT,
    id_pelicula INT,
    espectadores INT DEFAULT 0,
    precio_sesion DOUBLE,
    CONSTRAINT fk_sesion_sala FOREIGN KEY (id_sala) REFERENCES sala(id_sala),
    CONSTRAINT fk_sesion_pelicula FOREIGN KEY (id_pelicula) REFERENCES pelicula(id_pelicula)
);

-- 5. Tabla COMPRA
CREATE TABLE compra (
    id_compra INT AUTO_INCREMENT PRIMARY KEY,
    fecha_compra DATETIME DEFAULT CURRENT_TIMESTAMP,
    precio_total DOUBLE,
    descuento_aplicado DOUBLE,
    dni_cliente VARCHAR(9),
    CONSTRAINT fk_compra_cliente FOREIGN KEY (dni_cliente) REFERENCES cliente(dni)
);

-- 6. Tabla ENTRADA
CREATE TABLE entrada (
    id_entrada INT AUTO_INCREMENT PRIMARY KEY,
    id_sesion INT,
    id_compra INT,
    numero_personas INT,
    precio DOUBLE,
    descuento DOUBLE,
    CONSTRAINT fk_entrada_sesion FOREIGN KEY (id_sesion) REFERENCES sesion(id_sesion),
    CONSTRAINT fk_entrada_compra FOREIGN KEY (id_compra) REFERENCES compra(id_compra)
);
-- 2. INSERTAR SALAS (Los IDs serán 1, 2 y 3 automáticamente)
INSERT INTO sala (nombre, numero_sillas) VALUES 
('Sala 1 - Grande', 50),    -- ID 1
('Sala 2 - Mediana', 25),    -- ID 2
('Sala 3 - VIP', 5);        -- ID 3 (Poca capacidad para probar "Aforo Completo")

-- 3. INSERTAR PELÍCULAS (Los IDs serán 1, 2 y 3)
INSERT INTO pelicula (titulo, duracion, genero, precio_base) VALUES 
('Gladiator II', 150, 'Accion', 9.50),    -- ID 1
('Wicked', 160, 'Musical', 8.00),         -- ID 2
('Mufasa: El Rey León', 110, 'Infantil', 7.50); -- ID 3

-- 4. INSERTAR SESIONES
-- Nota: Asumimos que Sala 1=ID 1, Sala 2=ID 2, Pelicula 1=ID 1, etc.

-- --- SESIONES PARA "GLADIATOR II" (ID 1) ---
-- Mismo día, diferentes horas y salas
INSERT INTO sesion (fecha, hora_inicio, hora_fin, id_sala, id_pelicula, precio_sesion) VALUES 
('2026-02-20', '17:00:00', '19:30:00', 1, 1, 9.50), -- Sesión 1
('2026-02-20', '20:00:00', '22:30:00', 1, 1, 9.50), -- Sesión 2
('2026-02-20', '22:00:00', '00:30:00', 2, 1, 9.50); -- Sesión 3 (Sala diferente)

-- Día siguiente
INSERT INTO sesion (fecha, hora_inicio, hora_fin, id_sala, id_pelicula, precio_sesion) VALUES 
('2026-02-21', '17:00:00', '19:30:00', 1, 1, 8.50), -- Sesión 1
('2026-02-21', '20:00:00', '22:30:00', 1, 1, 8.50), -- Sesión 2
('2026-02-21', '22:00:00', '00:30:00', 2, 1, 8.50); -- Sesión 3 (Sala diferente)
-- Día siguiente
INSERT INTO sesion (fecha, hora_inicio, hora_fin, id_sala, id_pelicula, precio_sesion) VALUES 
('2026-02-22', '17:00:00', '19:30:00', 1, 1, 8.50), -- Sesión 1
('2026-02-22', '20:00:00', '22:30:00', 1, 1, 8.50), -- Sesión 2
('2026-02-22', '22:00:00', '00:30:00', 2, 1, 8.50); -- Sesión 3 (Sala diferente)

-- --- SESIONES PARA "WICKED" (ID 2) ---
INSERT INTO sesion (fecha, hora_inicio, hora_fin, id_sala, id_pelicula, precio_sesion) VALUES 
('2026-02-21', '16:00:00', '18:40:00', 2, 2, 8.00),
('2026-02-21', '19:00:00', '21:40:00', 2, 2, 8.00);
-- dia siguiente
INSERT INTO sesion (fecha, hora_inicio, hora_fin, id_sala, id_pelicula, precio_sesion) VALUES
('2026-02-22', '16:00:00', '18:40:00', 2, 2, 8.00),
('2026-02-22', '19:00:00', '21:40:00', 2, 2, 8.00);
-- dia siguiente
INSERT INTO sesion (fecha, hora_inicio, hora_fin, id_sala, id_pelicula, precio_sesion) VALUES
('2026-02-23', '16:00:00', '18:40:00', 2, 2, 8.00),
('2026-02-23', '19:00:00', '21:40:00', 2, 2, 8.00);

-- --- SESIONES PARA "MUFASA" (ID 3) en Sala VIP (ID 3) ---
-- Esta sala solo tiene 10 sitios, ideal para probar tu código de "No hay sitio"
INSERT INTO sesion (fecha, hora_inicio, hora_fin, id_sala, id_pelicula, precio_sesion) VALUES 
('2026-02-20', '16:00:00', '17:50:00', 3, 3, 12.00), -- VIP es más caro
('2026-02-20', '16:00:00', '17:50:00', 3, 3, 12.00);
-- dia siguiente
INSERT INTO sesion (fecha, hora_inicio, hora_fin, id_sala, id_pelicula, precio_sesion) VALUES 
('2026-02-21', '16:00:00', '17:50:00', 3, 3, 12.00), -- VIP es más caro
('2026-02-21', '16:00:00', '17:50:00', 3, 3, 12.00);
-- dia siguiente
INSERT INTO sesion (fecha, hora_inicio, hora_fin, id_sala, id_pelicula, precio_sesion) VALUES 
('2026-02-22', '16:00:00', '17:50:00', 3, 3, 12.00), -- VIP es más caro
('2026-02-22', '16:00:00', '17:50:00', 3, 3, 12.00);
-- CLIENTES FALSOS PARA PROBAR LOS REGISTROS
INSERT INTO cliente (dni, nombre, apellidos, email, contrasena) VALUES
('00000000A', 'Silvia', 'Ochoa', 'sil@example.com', '1234');




