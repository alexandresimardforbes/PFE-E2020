const chai = require('chai');
const chaiHttp = require('chai-http');
const server = require('../app');
const app = require('../app');
const channels = require('../models/Channels');

chai.should();
chai.use(chaiHttp);

describe('noanmockup API test', () => {

    //Test POST route
    describe('POST /api/noanmockup', () => {
        it('It should POST a customer request ', (done) => {
            chai.request(server)
                .post("/api/noanmockup")
                .end((err, response) => {
                    response.should.have.status(201);
                    response.text.should.be.eq('We receive your request');
                    done();
                });
        });
    });
});