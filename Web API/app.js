const express = require('express');
const path = require('path');
const exphbs = require('express-handlebars');
const logger = require('./middleware/logger');
const swaggerJsDoc = require('swagger-jsdoc');
const swaggerUi = require('swagger-ui-express');
const channels = require('./models/Channels');

const app = express();

const swaggerOptions = {
    definition: {
      openapi: '3.0.0', 
      info: {
        title: 'Web api for NOAN mockup', 
        version: '1.0.0',
      },
    },
    // Path to the API docs
    apis: ['./routes/channels/*.js'],
  };

const swaggerDocs = swaggerJsDoc(swaggerOptions);

app.use('/api-docs', swaggerUi.serve, swaggerUi.setup(swaggerDocs));

//TODO finish adding Handlebars middleware
app.engine('handlebars', exphbs({defaultLayout: 'main'}));
app.set('view engine', 'handlebars');

//Body parser middleware
app.use(express.json());
app.use(express.urlencoded({ extended: false}));

//Init middleware
app.use(logger);

//Channels api routes
app.use('/api/channels', require('./routes/channels/channels'));

const PORT = process.env.PORT || 5000;
app.listen(PORT, () => console.log('Server running'));

module.exports = app;