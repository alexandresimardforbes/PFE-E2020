const express = require('express');
const router = express.Router();
const uuid = require('uuid');
const channels = require('../../Channels');

// GET all channels
router.get('/', (req, res) => res.json(channels));

// GET single channel
router.get('/:channelNumber', (req, res) => {
    const found = channels.some(channel => channel.channelNumber === req.params.channelNumber);
    
    if(found) {
        res.json(channels.filter(channel => channel.channelNumber === req.params.channelNumber))
    } else {
        res.status(400).json({msg: 'No channel ' + req.params.channelNumber})
    } 
});

//POST
router.post('/', (req, res) => {
    const addedChannel = {
        id: uuid.v4(),
        channelNumber: req.body.channelNumber,
        name: req.body.name,
        genre: req.body.genre
    }

    if(!addedChannel.channelNumber || !addedChannel.name) {
        res.status(400).json({ msg: 'Channel number and channel name needed'})
    } else {
        channels.push(addedChannel);
    }
    res.json(channels);
});

//PUT modify a channel
router.put('/:channelNumber', (req, res) => {
    const found = channels.some(member => member.channelNumber === req.params.channelNumber);
    
    if(found) {
        const modChannel = req.body;
        channels.forEach(channel => {
            if(channel.channelNumber === req.params.channelNumber) {
                channel.name = modChannel.name ? modChannel.name : req.body.name;
                channel.genre = modChannel.genre ? modChannel.genre : req.body.genre;
                res.json({ msg: 'the channel as been modified.', channel})
            }
        });

    } else {
        res.status(400).json({msg: 'No channel ' + req.params.channelNumber})
    } 
});

//DELETE remove a channel
router.delete('/:channelNumber', (req, res) => {

    const found = channels.some(channel => channel.channelNumber === req.params.channelNumber);
    if(found) {
        res.json({ msg: 'Channel removed', channels : channels.filter(channel => channel.channelNumber !== req.params.channelNumber)})
    } else {
        res.status(400).json({msg: 'No channel ' + req.params.channelNumber})
    } 
});

module.exports = router;