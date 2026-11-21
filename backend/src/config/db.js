require('dotenv').config();
const fs = require('fs');
const path = require('path');
const Database = require('better-sqlite3');

const dbPath = path.resolve(process.env.DB_PATH || './database/app.db');
fs.mkdirSync(path.dirname(dbPath), { recursive: true });

const db = new Database(dbPath);
db.pragma('journal_mode = WAL');

// Crea las tablas si no existen (no hay que ejecutar nada a mano)
db.exec(fs.readFileSync(path.join(__dirname, '../../database/schema.sql'), 'utf8'));

module.exports = db;
