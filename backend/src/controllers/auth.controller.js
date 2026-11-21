const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');
const User = require('../models/user.model');

const EMAIL_REGEX = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

exports.register = async (req, res, next) => {
  try {
    const { fullName, email, password } = req.body;

    if (!fullName || !email || !password)
      return res.status(400).json({ message: 'Todos los campos son obligatorios' });
    if (!EMAIL_REGEX.test(email))
      return res.status(400).json({ message: 'Correo electrónico inválido' });
    if (password.length < 6)
      return res.status(400).json({ message: 'La contraseña debe tener al menos 6 caracteres' });

    const normalizedEmail = email.trim().toLowerCase();
    if (await User.findByEmail(normalizedEmail))
      return res.status(409).json({ message: 'Ya existe un usuario con ese correo' });

    const passwordHash = await bcrypt.hash(password, 10);
    const user = await User.create({ fullName: fullName.trim(), email: normalizedEmail, passwordHash });

    res.status(201).json({ message: 'Usuario registrado correctamente', user });
  } catch (err) { next(err); }
};

exports.login = async (req, res, next) => {
  try {
    const { email, password } = req.body;
    if (!email || !password)
      return res.status(400).json({ message: 'Correo y contraseña son obligatorios' });

    const row = await User.findByEmail(email.trim().toLowerCase());
    const valid = row && (await bcrypt.compare(password, row.password_hash));
    if (!valid) return res.status(401).json({ message: 'Credenciales incorrectas' });

    const token = jwt.sign({ id: row.id, email: row.email }, process.env.JWT_SECRET, {
      expiresIn: process.env.JWT_EXPIRES_IN || '2h',
    });

    res.json({ message: 'Login exitoso', token, user: User.toUser(row) });
  } catch (err) { next(err); }
};
