import express from "express";
import cors from "cors";
import bodyParser from "body-parser";
import mysql from "mysql2/promise";
import bcrypt from "bcrypt";

const app = express();

// Configuración CORS para Android y Hotspot
app.use(cors({
    origin: "*",
    methods: ["GET", "POST", "PUT", "DELETE"],
    credentials: false,
    allowedHeaders: ["Content-Type", "Authorization"]
}));

app.use(bodyParser.json());

let db;

async function connectDB() {
    try {
        db = await mysql.createConnection({
            host: "localhost",
            user: "root",
            password: "Panchitoo1",
            database: "banco"
        });
        console.log("✅ Conectado a la base de datos MariaDB");
    } catch (error) {
        console.error("❌ Error conectando a la BD:", error);
        process.exit(1);
    }
}

connectDB();

// RUTA DE REGISTRO - ACTUALIZADA para coincidir con tus data models
app.post("/register", async (req, res) => {
    try {
        console.log("📥 Recibiendo solicitud de registro:", req.body);
        
        const { nombre, correo, telefono, password, tipoTarjeta } = req.body;
        
        if (!nombre || !correo || !telefono || !password) {
            return res.status(400).json({ 
                mensaje: "Todos los campos son requeridos", 
                usuario: null,
                error: "Datos incompletos" 
            });
        }

        const [existe] = await db.execute("SELECT id FROM usuarios WHERE correo = ?", [correo]);
        if (existe.length > 0) {
            return res.status(400).json({ 
                mensaje: "El correo ya está registrado", 
                usuario: null,
                error: "Correo duplicado" 
            });
        }

        const passHasheada = await bcrypt.hash(password, 10);
        
        const [result] = await db.execute(
            "INSERT INTO usuarios (nombre, correo, telefono, password, tipo_tarjeta) VALUES (?, ?, ?, ?, ?)", 
            [nombre, correo, telefono, passHasheada, tipoTarjeta || "standard"]
        );

        console.log("✅ Usuario creado exitosamente:", { 
            id: result.insertId, 
            nombre, 
            correo, 
            tipoTarjeta: tipoTarjeta || "standard" 
        });

        res.json({ 
            mensaje: "Usuario creado exitosamente", 
            usuario: { 
                id: result.insertId,
                nombre, 
                correo, 
                telefono 
            },
            error: null
        });
        
    } catch (err) {
        console.error("💥 Error en registro:", err);
        res.status(500).json({ 
            mensaje: "Error en el servidor", 
            usuario: null,
            error: err.message 
        });
    }
});

// RUTA DE LOGIN - ACTUALIZADA para coincidir con tus data models
app.post("/login", async (req, res) => {
    try {
        console.log("📥 Recibiendo solicitud de login:", { 
            correo: req.body.correo, 
            password: req.body.password ? "***" : "empty" 
        });
        
        const { correo, password } = req.body;
        
        if (!correo || !password) {
            return res.status(400).json({ 
                mensaje: "Correo y contraseña son requeridos", 
                usuario: null,
                error: "Credenciales vacías" 
            });
        }

        const [rows] = await db.query("SELECT * FROM usuarios WHERE correo = ?", [correo]);
        if (rows.length === 0) {
            console.log("❌ Usuario no encontrado:", correo);
            return res.status(400).json({ 
                mensaje: "Usuario no encontrado", 
                usuario: null,
                error: "Credenciales inválidas" 
            });
        }

        const user = rows[0];
        const passCorrecta = await bcrypt.compare(password, user.password);
        if (!passCorrecta) {
            console.log("❌ Contraseña incorrecta para:", correo);
            return res.status(400).json({ 
                mensaje: "Contraseña incorrecta", 
                usuario: null,
                error: "Credenciales inválidas" 
            });
        }

        // Remover password del response
        const { password: _, ...userWithoutPassword } = user;
        
        console.log("✅ Login exitoso para:", correo);
        
        res.json({ 
            mensaje: "Login correcto", 
            usuario: userWithoutPassword,
            error: null
        });
        
    } catch (error) {
        console.error("💥 Error en login:", error);
        res.status(500).json({ 
            mensaje: "Error en el servidor", 
            usuario: null,
            error: error.message 
        });
    }
});

// RUTA PARA OBTENER DATOS DEL DASHBOARD
app.get("/dashboard/:userId", async (req, res) => {
    try {
        const userId = req.params.userId;
        
        const [userRows] = await db.query(
            "SELECT id, nombre, correo, telefono, tipo_tarjeta FROM usuarios WHERE id = ?", 
            [userId]
        );
        
        if (userRows.length === 0) {
            return res.status(404).json({ 
                mensaje: "Usuario no encontrado",
                error: "Usuario no existe" 
            });
        }

        const user = userRows[0];
        
        // Datos de ejemplo para el dashboard
        const dashboardData = {
            usuario: {
                id: user.id,
                nombre: user.nombre,
                correo: user.correo,
                telefono: user.telefono,
                saldo: 15000.75,
                cuenta: "1234 5678 9012 3456",
                fechaRegistro: "2024-01-15",
                tarjetaAsignada: user.tipo_tarjeta || "Débito"
            },
            tarjetas: [
                {
                    numero: "**** **** **** 1234",
                    tipo: "Débito",
                    subtipo: user.tipo_tarjeta || "Clásica",
                    fechaVencimiento: "12/26",
                    cvv: "***",
                    saldo: 15000.75,
                    nombre: `Maze ${user.tipo_tarjeta || "Clásica"}`,
                    color: "#2196F3"
                }
            ],
            inversiones: [
                {
                    id: 1,
                    nombre: "Fondo Maze Growth",
                    montoInvertido: 5000.00,
                    rendimiento: 250.50,
                    rendimientoPorcentaje: 5.01,
                    fechaInicio: "2024-02-01"
                }
            ],
            transacciones: [
                {
                    id: 1,
                    tipo: "transferencia",
                    monto: -500.00,
                    destinatario: "Juan Pérez",
                    fecha: "2024-03-15",
                    descripcion: "Transferencia a cuenta externa"
                },
                {
                    id: 2,
                    tipo: "deposito", 
                    monto: 1500.00,
                    destinatario: null,
                    fecha: "2024-03-10",
                    descripcion: "Depósito de nómina"
                }
            ],
            usuariosFrecuentes: [
                {
                    nombre: "Ana García",
                    cuenta: "9876 5432 1098 7654",
                    banco: "Maze Bank"
                },
                {
                    nombre: "Carlos López", 
                    cuenta: "5555 6666 7777 8888",
                    banco: "Otro Banco"
                }
            ]
        };

        res.json(dashboardData);
        
    } catch (error) {
        console.error("💥 Error obteniendo dashboard:", error);
        res.status(500).json({ 
            mensaje: "Error en el servidor",
            error: error.message 
        });
    }
});

// RUTA DE PRUEBA
app.get("/", (req, res) => {
    res.json({ 
        mensaje: "Backend Maze Bank funcionando ✔",
        timestamp: new Date().toISOString(),
        endpoints: {
            register: "POST /register",
            login: "POST /login", 
            dashboard: "GET /dashboard/:userId",
            usuarios: "GET /usuarios"
        }
    });
});

// RUTA PARA OBTENER USUARIOS
app.get("/usuarios", async (req, res) => {
    try {
        const [rows] = await db.query("SELECT id, nombre, correo, telefono, tipo_tarjeta FROM usuarios");
        res.json({
            total: rows.length,
            usuarios: rows
        });
    } catch (error) {
        res.status(500).json({ 
            error: error.message 
        });
    }
});

const PORT = 3000;
app.listen(PORT, "0.0.0.0", () => {
    console.log(`🚀 Servidor ejecutándose en:`);
    console.log(`   📱 Local: http://localhost:${PORT}`);
    console.log(`   🔥 Hotspot: http://192.168.1.73:${PORT}`);
    console.log(`   🔧 Endpoints disponibles:`);
    console.log(`      📍 GET  / - Estado del servidor`);
    console.log(`      📍 POST /register - Registrar usuario`);
    console.log(`      📍 POST /login - Iniciar sesión`);
    console.log(`      📍 GET  /dashboard/:userId - Datos del dashboard`);
    console.log(`      📍 GET  /usuarios - Listar usuarios`);
    console.log(`\n📡 Conecta tu Android al hotspot "MazeBank" y usa:`);
    console.log(`   📲 http://192.168.137.140:${PORT}`);
});