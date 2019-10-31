/*
  사용자의 인증에 필요한 도구모음
*/

class AuthManager{
  constructor(Database_Connecter){
    this.db = Database_Connecter;
  }

  getdb(){
    return this.db;
  }

//로그인토큰이 유효한지 검사
  async authLoginToken(user_pid, logintoken){
    let sql = `SELECT logintoken = $1 as isvalid FROM usertable WHERE user_pid = $2`;
    let value = [logintoken, user_pid];
    let result = await this.db.query(sql, value);

    return result.rows[0].isvalid;
  }

//그룹과의 관계 반환
  async authUserinGroup(group_pid, user_pid){
    let sql = `SELECT grade FROM useringroup WHERE (group_pid = $1 and user_pid = $2)`;
    let value = [group_pid, user_pid];
    let result = await this.db.query(sql, value);

    return result.rows[0].grade;
  }


}


module.exports = (Database_Connecter) =>{
  return new AuthManager(Database_Connecter);
};
