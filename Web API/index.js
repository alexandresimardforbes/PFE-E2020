const express = require('express');
const path = require('path');
const logger = require('./middleware/logger')

const app = express();

const channels = require('./Channels');
const { filter } = require('./Channels');

const PORT = process.env.PORT || 5000;
app.listen(PORT, () => console.log('Server running'));

//Init middleware
app.use(logger);

// Set static folder maybe for swagger
app.use(express.static(path.join(__dirname, 'public')));

// Gets all channels
app.get('/api/channels', (req, res) => res.json(channels));

// Get single channel
app.get('/api/channels/:id', (req, res) => {
    res.json(channels.filter(channel => channel.id === parseInt(req.params.id)))
});


