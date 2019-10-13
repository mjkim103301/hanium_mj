const DB = require('./Database_Connecter');


class Query{
  constructor(DBConfig){
    this.db = new DB(DBConfig);
  }
}

module.exports = Query;
