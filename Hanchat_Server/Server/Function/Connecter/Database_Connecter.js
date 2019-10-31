/*
  데이터베이스와의 연결 설정
*/
const fs = require('fs');
const {Client} = require('pg');


class Database_Connecter {
  constructor(Config){
    this.client = new Client(JSON.parse(Config));
    this.client.connect();

    //연결 지속을 위한 반복 요청
    setInterval(()=>{
      this.client.query('select now()', (err, res)=>{
        console.log(err, res.rows[0]);
      });
    }, 1200000);

    console.log('set Database');
  }

  async query(sql, values){
    return this.client.query(sql, values);
  }
}

module.exports = Database_Connecter;
