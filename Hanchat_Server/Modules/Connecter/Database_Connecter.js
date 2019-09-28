const sql = require('mysql');
const fs = require('fs');


class Database {
  constructor(ConfigPath){
    const Config = JSON.parse(fs.readFileSync(ConfigPath));
    this.connection = sql.createConnection(Config);

    //this.connection.connect();
    //console.log('Database Connected');

/*
    this.connection.query('Select * from tester', (err, rows, fields)=>{
      console.log('rows : ', rows);
    });

/*
    connection.query('insert into tester values(5234, "name22")', (err, rows, fields)=>{
      console.log('err : ', err);
      console.log('rows : ', rows);
      console.log('fields : ', fields);
    });
    */
    //connection.end();
  }

  async query(query, callback){
    return this.connection.query(query, callback);
  }
}



module.exports = Database;
