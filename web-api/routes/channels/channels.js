const express = require('../../node_modules/express');
const router = express.Router();
const uuid = require('../../node_modules/uuid');
const channels = require('../../models/Channels');
const { response } = require('../../node_modules/express');



/**
 * @swagger
 * /api/channels:
 *   get:
 *     summary: Return a list of the channels
 *     responses:
 *       200:
 *         description: Request successful
 */
router.get('/', (req, res) => res.json(channels));

/**
 * @swagger
 * /api/channels/{channelNumber}:
 *   get:
 *     summary: Return a channel 
 *     parameters:
 *      - in: path
 *        name: channelNumber
 *        schema:
 *         type: string
 *        required: true      
 *     responses:
 *       200:
 *         description: Request successful
 */
router.get('/:channelNumber', (req, res) => {
    const found = channels.some(channel => channel.channelNumber === req.params.channelNumber);

    if (found) {
        res.json(channels.find(channel => channel.channelNumber === req.params.channelNumber))
    } else {
        res.status(404).send('No channel ' + req.params.channelNumber)
    }
});

/**
 * @swagger
 *
 * /api/channels:
 *   post:
 *     summary: Create a new channel
 *     requestBody:
 *       required: true
 *       content:   
 *         application/json:
 *           schema:
 *             type: object
 *             properties:
 *               channelNumber:
 *                 type: string
 *               name:
 *                 type: string
 *               genre: 
 *                 type: string
 *     responses:
 *       201:
 *         description: Request successful and a resource has been created
 *       400:
 *         description: The channel number and the channel are required.
 */
router.post('/', (req, res) => {
    const addedChannel = {
        id: uuid.v4(),
        channelNumber: req.body.channelNumber,
        name: req.body.name,
        genre: req.body.genre
    }

    if (!addedChannel.channelNumber || !addedChannel.name) {
        res.status(400).send('Channel number and channel name needed');
    } else {
        channels.push(addedChannel);
        response.status(201);
        res.json(channels);
    }
});

/**
 * @swagger
 *
 * /api/channels/{channelNumber}:
 *   put:
 *     summary: Modify a channel
 *     parameters:
 *      - in: path
 *        name: channelNumber
 *        schema:
 *         type: string
 *        required: true  
 *     requestBody:
 *       required: true
 *       content:   
 *         application/json:
 *           schema:
 *             type: object
 *             properties:
 *               name:
 *                 type: string
 *               genre:
 *                 type: string
 *     responses:
 *       200:
 *         description: Request successful
 *       400:
 *         description: The channel number and the channel are required.
 */
router.put('/:channelNumber', (req, res) => {
    const found = channels.some(member => member.channelNumber === req.params.channelNumber);

    if (found) {
        const modChannel = req.body;
        channels.forEach(channel => {
            if (channel.channelNumber === req.params.channelNumber) {
                channel.name = modChannel.name ? modChannel.name : req.body.name;
                channel.genre = modChannel.genre ? modChannel.genre : req.body.genre;
                res.json({ msg: 'the channel as been modified.', channel })
            }
        });

    } else {
        res.status(404).send('No channel ' + req.params.channelNumber)
    }
});

/**
 * @swagger
 * /api/channels/{channelNumber}:
 *   delete:
 *     summary: Deleta a channel 
 *     parameters:
 *      - in: path
 *        name: channelNumber
 *        schema:
 *         type: string
 *        required: true      
 *     responses:
 *       200:
 *         description: Request successful
 */
router.delete('/:channelNumber', (req, res) => {

    const found = channels.some(channel => channel.channelNumber === req.params.channelNumber);
    if (found) {
        res.json({ msg: 'Channel removed', channels: channels.filter(channel => channel.channelNumber !== req.params.channelNumber) })
    } else {
        res.status(404).send('No channel ' + req.params.channelNumber)
    }
});

module.exports = router;