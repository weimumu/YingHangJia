var app = require('../server');
var request = require("supertest").agent(app.default.listen()); // trick

describe('Hello', function() {
  it('should say hello to xiaoming', function (done) {
    request
      .get('/api/user')
      .expect(200, 'hello xiaoming', done);
  });
});
