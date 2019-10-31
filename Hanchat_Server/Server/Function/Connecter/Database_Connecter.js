/*
  데이터베이스와의 연결 설정
*/
const fs = require('fs');
const {Client} = require('pg');


class Database_Connecter {
  constructor(Config){
    console.log(JSON.parse(Config));
    this.client = new Client(JSON.parse(Config));
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
    return await this.client.query(sql, values);
  }
}

module.exports = Database_Connecter;
