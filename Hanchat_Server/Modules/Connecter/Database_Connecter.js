//const sql = require('mysql');
const fs = require('fs');
const {Client} = require('pg');


class Database {
  constructor(ConfigPath){
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

    this.client = new Client(JSON.parse(fs.readFileSync(ConfigPath)));

    console.log(JSON.parse(fs.readFileSync(ConfigPath)));
    this.client.connect();

    console.log('Database connected');
    setInterval(()=>{
      this.client.query('select now()', (err, res)=>{
        console.log(err, res);
      });
    }, 1200000);
  }

  async query(sql, values){
    let res;
    //this.client.connect();
    res = await this.client.query(sql, values);
    //this.client.end();

    return res;
  }
}

module.exports = Database;
