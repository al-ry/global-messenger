const expect = require('chai').expect;
const User = require('../../models/user')
var db = require('../../db/database')
describe('Test User Model', function() {
    describe('Make new user', function() {
    const newUser = new User('name', 'telephone', 'password')
      it('should construct new user ', ()=> {
        expect(newUser.name).to.equal('name');
        expect(newUser.telephone).to.equal('telephone');
        expect(newUser.password).to.equal('password');
      });
      it('can find some user ', ()=> {
        User.Find('q').then((result) => {
            //expect(result.name).to.equal('q');
            //expect(result.telephone).to.equal('q');
        })
      });

    });
  });