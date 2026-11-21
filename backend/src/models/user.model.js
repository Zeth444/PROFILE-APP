const db = require('../config/db');

// Capa de acceso a datos (solo SQL, sin lógica de negocio)
const toUser = (row) => ({
  id: row.id,
  fullName: row.full_name,
  email: row.email,
  createdAt: row.created_at,
});

function findById(id) {
  const row = db.prepare('SELECT * FROM users WHERE id = ?').get(id);
  return row ? toUser(row) : null;
}

function findByEmail(email) {
  // Devuelve la fila completa (incluye password_hash, solo para login)
  return db.prepare('SELECT * FROM users WHERE email = ?').get(email) || null;
}

function create({ fullName, email, passwordHash }) {
  const result = db
    .prepare('INSERT INTO users (full_name, email, password_hash) VALUES (?, ?, ?)')
    .run(fullName, email, passwordHash);
  return findById(result.lastInsertRowid);
}

module.exports = { create, findByEmail, findById, toUser };
