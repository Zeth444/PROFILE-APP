const router = require('express').Router();
const verifyToken = require('../middleware/auth.middleware');
const user = require('../controllers/user.controller');

router.get('/me', verifyToken, user.me);

module.exports = router;
