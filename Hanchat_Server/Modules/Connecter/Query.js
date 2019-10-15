const DB = require('./Database_Connecter');


class Query{
  constructor(DBConfig){
    this.db = new DB(DBConfig);
  }

  dd(){
    this.db.query("Select now()", null)
    .then(res =>{
      console.log(res);
    })
    .catch(err =>{
      console.log(err);
    });
  }
}


const path = require('path');
const Database_ConfigPath = path.join(__dirname, '..', 'Data/JSON/PostgreSQL_testDB_key.json');

var q = new Query(Database_ConfigPath);

q.dd();

module.exports = Query;
