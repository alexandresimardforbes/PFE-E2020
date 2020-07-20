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

        it('It should POST a new channel without a genre', (done) => {
            const channelLenght = channels.length;
            const channel = {
                channelNumber: '89',
                name: 'RSQ',
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

        it('It should NOT POST a new channel cause the name is missing', (done) => {
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

        it('It should NOT POST a new channel cause the channel number is missing', (done) => {
            const channelLenght = channels.length;
            const channel = {
                name: 'VVT',
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

        it('It should NOT POST a new channel cause the channel number and the name are missing', (done) => {
            const channelLenght = channels.length;
            const channel = {
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

    //Test PUT route
    describe('PUT /api/channels/:channelNumber', () => {
        it('It should PUT (modify) a new channel', (done) => {
            const channelNumber = 33;
            const channelMod = {
                name: 'SPORTFM',
                genre: 'Sport radio'
            };
            chai.request(server)
                .put("/api/channels/" + channelNumber)
                .send(channelMod)
                .end((err, response) => {
                    response.should.have.status(201);
                    response.body.should.a('object');
                    response.body.should.have.property('msg').eq('the channel as been modified.');
                    response.body.should.have.property('channel').property('name').eq('SPORTFM');
                    response.body.should.have.property('channel').property('genre').eq('Sport radio');
                    done();
                });
        });

        it('It should NOT PUT (modify) a new channel because of a wrong channel number', (done) => {
            const channelNumber = 777;
            const channelMod = {
                name: 'SPORTFM',
                genre: 'Sport radio'
            };
            chai.request(server)
                .put("/api/channels/" + channelNumber)
                .send(channelMod)
                .end((err, response) => {
                    response.should.have.status(404);
                    response.text.should.be.eq('No channel ' + channelNumber)
                    done();
                });
        });
    });

    //Test DELETE (by channel number) route
    describe('DELETE /api/channels/:channelNumber', () => {
        it('It should DELETE a channel by channel number', (done) => {
            const channelNumber = 33;
            chai.request(server)
                .delete("/api/channels/" + channelNumber)
                .end((err, response) => {
                    response.should.have.status(201);
                    done();
                });
        });

        it('It should not DELETE a channel by channel number', (done) => {
            const channelNumber = 111;
            chai.request(server)
                .delete("/api/channels/" + channelNumber)
                .end((err, response) => {
                    response.should.have.status(404);
                    response.text.should.be.eq('No channel ' + channelNumber)
                    done();
                });
        });
    });
});