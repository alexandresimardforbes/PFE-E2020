const chai = require('chai');
const chaiHttp = require('chai-http');
const server = require('../app');
const app = require('../app');
const channels = require('../models/Channels');

chai.should();
chai.use(chaiHttp);


describe('Channels API', () => {

    //Test GET route
    describe('GET /api/channels', () => {
        it('It should GET all the channels', (done) => {
            chai.request(server)
                .get("/api/channels")
                .end((err, response) => {
                    response.should.have.status(200);
                    response.body.should.a('array');
                    response.body.length.should.be.eq(channels.length);
            done();
            });
        });

        it('It should NOT GET all the channels', (done) => {
            chai.request(server)
                .get("/api/wrongchannelurl")
                .end((err, response) => {
                    response.should.have.status(404);
            done();
            });
        });
    });

    //Test GET (by channel number) route
    describe('GET /api/channels/:channelNumber', () => {
        it('It should GET a channel by channel number', (done) => {
            const channelNumber = 33;
            chai.request(server)
                .get("/api/channels/" + channelNumber)
                .end((err, response) => {
                    response.should.have.status(200);
                    response.body.should.a('object');
                    response.body.should.have.property('channelNumber');
                    response.body.should.have.property('name');
                    response.body.should.have.property('genre');
            done();
            });
        });

        it('It should GET a channel by channel number', (done) => {
            const channelNumber = 133;
            chai.request(server)
                .get("/api/channels/" + channelNumber)
                .end((err, response) => {
                    response.should.have.status(404);
                    response.text.should.be.eq('No channel ' + channelNumber)
            done();
            });
        });
    });

    //Test POST route
    describe('POST /api/channels', () => {
        it('It should POST a new channel', (done) => {
            const channelLenght = channels.length;
            const channel = {
                channelNumber: '66',
                name: 'HTARW',
                genre: 'Religion'
            };
            chai.request(server)
                .post("/api/channels")
                .send(channel)
                .end((err, response) => {
                    response.should.have.status(201);
                    response.body.should.a('array');
                    response.body.length.should.be.eq(channelLenght + 1);
            done();
            });
        });

        it('It should NOT POST a new channel', (done) => {
            const channelLenght = channels.length;
            const channel = {     
                channelNumber: '99',
                genre: 'Travel'
            };
            chai.request(server)
                .post("/api/channels")
                .send(channel)
                .end((err, response) => {
                    response.should.have.status(400); 
                    response.text.should.be.eq('Channel number and channel name needed');
                done();
            });
        });
    });
});