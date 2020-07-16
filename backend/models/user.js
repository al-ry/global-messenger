

module.exports = class User{
 
    constructor(name, telephone, password){
        this.name = name
        this.password = password
        this.telephone = telephone
    }
    SaveUser(){
        users.push(this);
    }
}
