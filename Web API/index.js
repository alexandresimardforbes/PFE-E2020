const express = require('express');
const path = require('path');
const exphbs = require('express-handlebars')
const logger = require('./middleware/logger')

const app = express();

//TODO finish adding Handlebars middleware
app.engine('handlebars', exphbs({defaultLayout: 'main'}));
app.set('view engine', 'handlebars');

//Body parser middleware
app.use(express.json());
app.use(express.urlencoded({ extended: false}));

//Init middleware
app.use(logger);

// Set static folder maybe for swagger
app.use(express.static(path.join(__dirname, 'public')));

//Channels api routes
app.use('/api/channels', require('./routes/tests/channels'));

const PORT = process.env.PORT || 5000;
app.listen(PORT, () => console.log('Server running'));



