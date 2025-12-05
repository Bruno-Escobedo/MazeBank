import { User } from "../models/user.model.js";
import bcrypt from "bcrypt";
import jwt from "jsonwebtoken";

export const register = async (req, res) => {
  try {
    const { nombre, correo, password } = req.body;

    const hashed = await bcrypt.hash(password, 10);

    await User.create({ nombre, correo, password: hashed });

    res.json({ message: "Usuario registrado correctamente" });
  } catch (err) {
    res.status(500).json({ error: "Error en el registro" });
  }
};

export const login = async (req, res) => {
  try {
    const { correo, password } = req.body;

    const user = await User.findByEmail(correo);
    if (!user) return res.status(400).json({ error: "Usuario no existe" });

    const valid = await bcrypt.compare(password, user.password);
    if (!valid) return res.status(400).json({ error: "Contraseña incorrecta" });

    const token = jwt.sign(
      { id: user.id, correo: user.correo },
      process.env.JWT_SECRET,
      { expiresIn: "24h" }
    );

    res.json({ token, user });
  } catch (err) {
    res.status(500).json({ error: "Error en el login" });
  }
};
