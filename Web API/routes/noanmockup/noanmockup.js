const express = require('../../node_modules/express');
const router = express.Router();
const { response } = require('../../node_modules/express');

router.post('/', (req, res) => {
    res.send("We receive your request");  
});

module.exports = router;