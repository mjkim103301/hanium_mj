/*
  Account 라우터의 기능 모음
*/


class AccountManager{
  constructor(Functions){
    this.Functions = Functions;
    this.db = Functions.getDatabaseConnecter();
  }

//id와 비번으로 로그인 - result, pid 반환
  async loginWithId(id, password){
    let sql = `SELECT password = $1 as isvalid, user_pid FROM userlogin WHERE user_pid = $2`;
    let values = [password, id];
    let res = await this.db.query(sql, values);
    let ans = {
      result : res.rows[0].isvalid,
      pid : res.rows[0].user_pid
    };

    return ans;
  }

//pid와 로그인 토큰으로 로그인 - result, pid 반환
  async loginWithPid(pid, logintoken){
    let sql = `SELECT logintoken = $1 as isvalid FROM usertable WHERE user_pid = $2`;
    let values = [logintoken, pid];
    let result = await this.db.query(sql, values);

    let ans = {
      result : result.rows[0].isvalid,
      pid : pid
    };

    return ans;
  }

//유저 생성 - pid, logintoken 반환 (로깅)
  async createUser(){
    try{
      await this.db.query('BEGIN');
      let sql = "INSERT INTO usertable VALUES(default) RETURNING user_pid";
      let result = await this.db.query(sql);
      let pid = result.rows[0].user_pid;

      //로그 생성
      await this.Functions.UnitLog('CUS', pid, pid, null);

      //새 로그인토큰 생성
      let logintokenres = await this.getLoginToken(pid);

      let answer = {
        pid : pid,
        logintoken : logintokenres.logintoken
      };
      await this.db.query('COMMIT');

      return answer;
    } catch(e){
      await this.db.query('ROLLBACK');
      throw e;
    }


  }

//로그인 토큰 갱신 - logintoken만 반환
  async getLoginToken(user_pid){
    const new_logintoken = this.Functions.getRandomString(14);
    let sql = `UPDATE usertable SET logintoken = '${new_logintoken}' WHERE user_pid=${user_pid}`;
    let result = await this.db.query(sql);
    let ans = {
      pid : user_pid,
      logintoken : new_logintoken
    };
    return ans;
  }

//id에 맞는 salt 얻기 - salt 5개 반환
  async getsalts(){
    let sql = `SELECT * FROM userlogin WHERE id = ${id}`;
    let result = await this.db.query(sql);
    let ans = {
      salt1 : r.salt1,
      salt2 : r.salt2,
      salt3 : r.salt3,
      salt4 : r.salt4,
      salt5 : r.salt5
    };
    return ans;
  }
}


module.exports = (Functions)=>{
  return new AccountManager(Functions);
};
