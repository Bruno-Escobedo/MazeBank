import { db } from "../config/db.js";

export const User = {
  create: async (data) => {
    const [rows] = await db.query(
      "INSERT INTO usuarios (nombre, correo, password) VALUES (?, ?, ?)",
      [data.nombre, data.correo, data.password]
    );
    return rows;
  },

  findByEmail: async (correo) => {
    const [rows] = await db.query(
      "SELECT * FROM usuarios WHERE correo = ?",
      [correo]
    );
    return rows[0];
  }
};
