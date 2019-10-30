//const sql = require('mysql');
const fs = require('fs');
const {Client} = require('pg');


class Database_Connecter {
  constructor(ConfigPath){

    this.client = new Client(JSON.parse(fs.readFileSync(ConfigPath)));
    this.client.connect();
    this.client.query('select now()', (err, res) =>{
      console.log(err, res.rows[0]);
      console.log('Database connected');
    });


    //연결 지속을 위한 반복 요청
    setInterval(()=>{
      this.client.query('select now()', (err, res)=>{
        console.log(err, res.rows[0]);
      });
    }, 1200000);

  }

  async query(sql, values){
    let res;
    res = await this.client.query(sql, values);
    return res;
  }
}

module.exports = Database_Connecter;
