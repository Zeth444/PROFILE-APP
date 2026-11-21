-- SQLite (relacional). Se ejecuta automáticamente al iniciar el servidor.
CREATE TABLE IF NOT EXISTS users (
  id            INTEGER PRIMARY KEY AUTOINCREMENT,
  full_name     TEXT NOT NULL,
  email         TEXT NOT NULL UNIQUE,
  password_hash TEXT NOT NULL,
  created_at    TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP
);
-- Nota: NO existe columna para la foto de perfil. La imagen nunca se guarda.
