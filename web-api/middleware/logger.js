const moment = require('moment');

const logger = (req, res, next) => {
    let url = req.protocol + "://" + req.get('host') + req.originalUrl;
    console.log(url + " : " + moment().format());
    next();
};

module.exports = logger;