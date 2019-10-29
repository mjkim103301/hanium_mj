const DB = require('./Database_Connecter');
let sql;
let values;
let fetchtime;


// ####서치
// 1. 장소 키워드 할만한 데이터가 DB에 없어요 설계 추가해주세요!
// 2. groupsize가 뭐하는 필드에요?
// 3. 필드에 따라 update함수 분할 가능한데 필요한거 뭐뭐인지?
// 4. useringroup에 managerpid는 뭐에요?

class Query{
  constructor(DBConfig){
    this.db = new DB(DBConfig);
  }
  //유닛 로그
    UnitLog(action, unit_pid, affected_unit_pid, target_id){
      sql = 'INSERT INTO UnitLog(log_time, action_id, unit_pid, affected_unit_pid, target_id)';
      sql = sql + 'VALUES(now(), $1, $2, $3, $4)';
      values = [action, unit_pid, affected_unit_pid, target_id];

      this.db.query(sql, values).then(res=>{
        console.log('log : ', res);
      }).catch(err=>{
        console.log('log error : ', err);
      });
    }

  //유닛 로그
  UnitLog(action, unit_pid, affected_unit_pid, target_id){
    sql = 'INSERT INTO UnitLog(log_time, action_id, unit_pid, affected_unit_pid, target_id)';
    sql = sql + 'VALUES(now(), $1, $2, $3, $4)';
    values = [action, unit_pid, affected_unit_pid, target_id];

    this.db.query(sql, values).then(res=>{
      console.log('log : ', res);
    }).catch(err=>{
      console.log('log error : ', err);
    });
  }

  createSchedule(title, category, starttime, endtime, memo, repeattype){
  sql = "INSERT INTO schedule(title, category, starttime, endtime, memo, repeattype) VALUES(?,?,?)";
  values = [title, category, starttime, endtime, memo, repeattype];
     // INSERT INTO schedule(unit_pid, title, category, starttime, endtime, memo, repeattype) values (1002,'스케쥴2','c','2019-10-26 10:00:00','2019-10-26 12:00:00','메모 시험음 메모',null);
    sql = "INSERT INTO schedule(unit_pid, title, category, starttime, endtime, memo, repeattype) VALUES(?,?,?,?,?,?,?)";
    values = [unit_pid, title, category, starttime, endtime, memo, repeattype];

    this.db.query(sql, values)
    .then(res =>{
    console.log(res);
    })
    .catch(err =>{
    console.log(err);
  });
  }
   deleteSchedule(id, unit_pid){
    sql = "DELETE FROM schedule WHERE (id=? AND unit_pid=?)";
    values = [id,unit_pid];

    this.db.query(sql, values)
    .then(res =>{
    console.log(res);
    })
    .catch(err =>{
    console.log(err);
  });
  }
  // ####  title, category, starttime, endtime, memo, repeattype 분할가능
   updateSchedule (id, unit_pid, title, category, starttime, endtime, memo, repeattype){
  sql = "UPDATE schedule SET title=?, category=?, starttime=?, endtime=?, memo=?, repeattype=? WHERE (id=? AND unit_pid=?)";
  values = [title, category, starttime, endtime, memo, repeattype, id, unit_pid];

  this.db.query(sql, values)
    .then(res =>{
    console.log(res);
    })
    .catch(err =>{
    console.log(err);
  });
  }

  selectSchedule(unit_pid){
      //SELECT * FROM schedule where unit_pid= 1002;
    sql = "SELECT * FROM schedule where unit_pid=?";
    value = [unit_pid];


    this.db.query(sql, values)
      .then(res =>{
      console.log(res);
      })
      .catch(err =>{
      console.log(err);
    });
  }

//수정 시작
  //로그인토큰이 유효한지 검사
  async authLoginToken(user_pid, logintoken){
    sql = `SELECT logintoken = $1 as isvalid FROM usertable WHERE user_pid = $2`;
    value = [logintoken, user_pid];
    let result = await this.db.query(sql, value);

    return result.rows[0].isvalid;
  }

  //pid로 유저가 있는지 검사
  async authUserForPid(user_pid){
    sql = `SELECT user_pid = ${user_pid} as isvalid FROM usertable WHERE user_pid = ${user_pid}`;
    let result = await this.db.query(sql);

    return result.rows[0].isvalid;
  }

  selectScheduleByKeyword(unit_pid, keyword){
    // SELECT * FROM schedule where (unit_pid= 1002 and (memo ~'^.*시험.*$' or title ~ '^.*시험.*$'));
    let reg = '^.*'+keyword+'.*$';
    sql = "SELECT * FROM schedule where (unit_pid = ? and (title ~ ? or category ~ ? or group ~ ? or memo ~ ?))";
    // 키워드 = 제목 / 장소(####) / 카테고리 / 그룹 / 메모
    value = [unit_pid, reg, reg, reg, reg];

    this.db.query(sql, values)
      .then(res =>{
      console.log(res);
      })
      .catch(err =>{
      console.log(err);
    });
  }
  //그룹과의 관계 반환
  async authUserinGroup(group_pid, user_pid){
    sql = `SELECT grade FROM useringroup WHERE (group_pid = $1 and user_pid = $2)`;
    value = [group_pid, user_pid];
    let result = await this.db.query(sql, value);
    let ans = {
      grade : 'N'
    };
    if(result.rows != null)
      ans.grade = result.rows[0].grade;
    return ans;
  }

  //유저 만들기 (로깅) - user_pid 반환
  async createUser(){
    try{
      await this.db.query('BEGIN');
      sql = "INSERT INTO usertable VALUES(default) RETURNING user_pid";
      let result = await this.db.query(sql);
      let pid = result.rows[0].user_pid;
      this.UnitLog('CUS', pid, pid, null);
      await this.db.query('COMMIT');
      return result.rows[0].user_pid;
    } catch(e){
      await this.db.query('ROLLBACK');
      throw e;
    }
  }
  //로그인 토큰 갱신
  async updateUserLoginToken(user_pid, new_logintoken){
    sql = `UPDATE usertable SET logintoken = '${new_logintoken}' WHERE user_pid=${user_pid}`;
    let result = await this.db.query(sql);
    return new_logintoken;
  }
  //id로 salts 얻기
  async getSalts(id){
    sql = `SELECT * FROM userlogin WHERE id = ${id}`;
    let result = await this.db.query(sql);
    if(result.rows == null){
      return { result : false, reason : 'id not found' };
    }
    let r = result.rows[0];
    let ans = {
      result : true,
      salt1 : r.salt1,
      salt2 : r.salt2,
      salt3 : r.salt3,
      salt4 : r.salt4,
      salt5 : r.salt5
    };
    return ans;
  }

  //그룹 만들기 (로깅) -  group_pid 반환
  async createGroup(host_pid, ispublic){
    try{
      await this.db.query('BEGIN');
      sql = `INSERT INTO grouptable(group_pid, groupsize, grouphost, ispublic)`;
      sql += `VALUES(default, default, $1, $2) RETURNING group_pid`;
      value = [host_pid, ispublic];
      let result = await this.db.query(sql, value);
      await this.db.query('COMMIT');
      return result.rows[0].group_pid;

    } catch(e){
      await this.db.query('ROLLBACK');
      throw e;
    }
  }


  //유닛 업데이트
  updateUnitProfile(pid, name, picture, explanation){
    sql = "UPDATE unit SET ";
    if(name != null) sql += `name = ${name} `;
    if(picture != null) sql += `picture = ${picture} `;
    if(explanation != null) sql += `explanation = ${explanation} `;
    sql += "WHERE pid=$1";
    values = [pid];
    return this.db.query(sql, values);
  }

  //유저 프로필 업데이트 (로깅)
  async updateUserProfile(user_pid, name, picture, explanation){
    try{
      await this.db.query('BEGIN');
      let result = await this.updateUnitProfile(user_pid, name, picture, explanation);
      this.UnitLog('UUS', user_pid, user_pid, null);
      await this.db.query('COMMIT');
      return result;
    } catch(e){
      await this.db.query('ROLLBACK');
      throw e;
    }

  }
  //그룹 프로필 업데이트 (로깅)
  async updateGroupProfile(user_pid, group_pid, name, picture, explanation){
    try{
      await this.db.query('BEGIN');
      let result = await this.updateUnitProfile(group_pid, name, picture, explanation);
      this.UnitLog('UGR', user_pid, group_pid, null);
      await this.db.query('COMMIT');
      return result;
    } catch(e){
      await this.db.query('ROLLBACK');
      throw e;
    }
  }
  //그룹 호스트 변경
  async updateGrouphost(group_pid, old_host_pid, new_host_pid){
    //sql = `UPDATE grouptable SET grouphost`;
  }
  //그룹 공개범위 변경
  async updateGroupPublic(group_pid, ispublic){
    sql = `UPDATE grouptable SET ispublic = $1 WHERE group_pid = $2`;
    values = [ispublic, group_pid];

    return this.db.query(sql, values);
  }
//끝

  deleteUser(user_pid){
     //DELETE FROM usertable WHERE user_pid=1001;
     //DELETE FROM unit WHERE pid=1001;
    sql = "DELETE FROM usertable WHERE user_pid=?";
    values = [user_pid];

    this.db.query(sql, values)
    .then(res =>{
    console.log(res);
    })
    .catch(err =>{
    console.log(err);
  });

    deleteUnit(user_pid);
  }

  // ####  name, picture, explanation 분할가능
  // unit과 join 처리 가능한지 확인

  updateUser(user_pid, name, picture, explanation){

      sql = "UPDATE usertable SET fetchtime=now() WHERE user_pid=?"
      values = [user_pid];

      this.db.query(sql, values)
      .then(res =>{
      console.log(res);
      })
      .catch(err =>{
      console.log(err);
    });
      updateUnit(user_pid, name, picture, explanation);
    }
  selectUser(user_pid){
    sql = "SELECT * FROM usertable WHERE user_pid=?";
    values = [user_pid];

    this.db.query(sql, values)
    .then(res =>{
    console.log(res);
    })
    .catch(err =>{
    console.log(err);
  });
  }




   createGroup(groupsize, grouphost, ispublic, name, picture, explanation){

     //INSERT INTO grouptable(groupsize, grouphost, ispublic) VALUES(1,1001,false);
     //UPDATE unit SET name='그룹이름', picture='그룹사진', explanation ='그룹설명' WHERE pid=1002;
     //INSERT INTO useringroup(group_pid, user_pid, grade, groupcolor) VALUES(1002,1001,'H','White');
     sql = "INSERT INTO grouptable(groupsize, grouphost, ispublic) VALUES(?,?,?)";
     values = [groupsize, grouphost, ispublic];

    this.db.query(sql, values)
    .then(res =>{
    console.log(res);
    })
    .catch(err =>{
    console.log(err);
  });
    updateUnit(group_pid, name, picture, explanation);
    //호스트의 권한 생성
    createUserGroup(group_pid, user_pid, 'H', groupcolor);
  }
   deleteGroup(group_pid){

     //DELETE FROM grouptable WHERE group_pid=1002;
     //DELETE FROM unit WHERE pid=1002;
     sql = "DELETE FROM grouptable WHERE group_pid=?";
     values = [group_pid];

    this.db.query(sql, values)
    .then(res =>{
    console.log(res);
    })
    .catch(err =>{
    console.log(err);
  });
    deleteUnit(group_pid);
  }
  // #### groupsize, grouphost, ispublic, name, picture, explanation 분할가능
  // unit과 join 처리 가능한지 확인
   updateGroup(group_pid, groupsize, grouphost, ispublic, name, picture, explanation) {
     sql = "UPDATE grouptable SET groupsize=?, grouphost=?, ispublic =? WHERE group_pid=?"
     values = [groupsize, grouphost, ispublic, group_pid];

    this.db.query(sql, values)
    .then(res =>{
    console.log(res);
    })
    .catch(err =>{
    console.log(err);
  });

    updateUnit(group_pid, name, picture, explanation);
  }
    selectGroup(){

    }




  updateUnit(pid, name, picture, explanation){
    sql = "UPDATE unit SET name=?, picture=?, explanation =? WHERE group_pid=?";
    values = [name, picture, explanation, pid];

    this.db.query(sql, values)
    .then(res =>{
    console.log(res);
    })
    .catch(err =>{
    console.log(err);
  });
  }
  deleteUnit(pid){
   sql = "DELETE FROM unit WHERE pid=?";
   values = [pid];

   this.db.query(sql, values)
   .then(res =>{
   console.log(res);
   })
   .catch(err =>{
   console.log(err);
 });
 }




   createUserGroup(group_pid, user_pid, grade, groupcolor) {
     //INSERT INTO useringroup(group_pid, user_pid, grade, groupcolor) VALUES(1002,1003,'M','White');
    sql = "INSERT INTO useringroup(group_pid, user_pid, grade, groupcolor) VALUES(?,?,?,?)";
    values = [group_pid, user_pid, grade, groupcolor];

    this.db.query(sql, values)
    .then(res =>{
    console.log(res);
    })
    .catch(err =>{
    console.log(err);
  });
  }
   deleteUserGroup(group_pid, user_pid) {

     //DELETE FROM useringroup WHERE (group_pid =1003 AND user_pid =1004);
    sql = "DELETE FROM useringroup WHERE (group_pid =? AND user_pid =?)";
    values = [group_pid, user_pid];

    this.db.query(sql, values)
    .then(res =>{
    console.log(res);
    })
    .catch(err =>{
    console.log(err);
  });
  }

  // #### grade, groupcolor 분할가능
   updateUserGroup(group_pid, user_pid, grade, groupcolor) {

     //UPDATE useringroup SET grade ='M', groupcolor ='Black' WHERE (group_pid =1003 AND user_pid =1004);
    sql = "UPDATE useringroup SET grade =?, groupcolor =? WHERE (group_pid =? AND user_pid =?)"
    values = [grade, groupcolor, group_pid, user_pid];

    this.db.query(sql, values)
    .then(res =>{
    console.log(res);
    })
    .catch(err =>{
    console.log(err);
  });
  }
  selectGroupMember(group_pid){
    //SELECT * FROM useringroup WHERE group_pid =1003;
        sql = "SELECT * FROM useringroup WHERE group_pid =?"
        values = [group_pid];

        db.query(sql, values)
        .then(res =>{
        console.log(res);
        })
        .catch(err =>{
        console.log(err);
        });
  }
  selectGroupforUser(user_pid){
    //SELECT * FROM useringroup WHERE user_pid =1002;
        sql = "SELECT * FROM useringroup WHERE user_pid =?"
        values = [user_pid];

        db.query(sql, values)
        .then(res =>{
        console.log(res);
        })
        .catch(err =>{
        console.log(err);
        });
  }
  selectHostGroup(group_pid){
    //SELECT * FROM useringroup WHERE (group_pid =1003 AND grade ='H');
        sql = "SELECT * FROM useringroup WHERE (group_pid =? AND grade ='H')"
        values = [group_pid];

        db.query(sql, values)
        .then(res =>{
        console.log(res);
        })
        .catch(err =>{
        console.log(err);
        });
  }



   createGrouppost(group_pid, write_user_pid, linkedschedule_id, contents) {

     //INSERT INTO grouppost(group_pid, write_user_pid, linkedschedule_id, writetime, modifytime, contents) VALUES(1003, 1002, 12, now(), now(), 'content');
    sql = "INSERT INTO grouppost(group_pid, write_user_pid, linkedschedule_id, writetime, modifytime, contents) VALUES(?,?,?,now(), now(),?)";
    values = [group_pid, write_user_pid, linkedschedule_id, contents ];

    this.db.query(sql, values)
    .then(res =>{
    console.log(res);
    })
    .catch(err =>{
    console.log(err);
  });
  }
   deleteGrouppost(group_pid, id) {
     sql = "DELETE FROM grouppost WHERE (group_pid =? AND id =?)";
     values = [group_pid, id];

    this.db.query(sql, values)
    .then(res =>{
    console.log(res);
    })
    .catch(err =>{
    console.log(err);
  });
  }
  // #### linkedschedule_id, contents 분할가능
   updateGrouppost(group_pid, id, linkedschedule_id, contents) {
     //UPDATE grouppost SET linkedschedule_id =12, modifytime=now(), contents='contnt' WHERE (group_pid =1003 AND id =2);
     sql = "UPDATE grouppost SET linkedschedule_id =?, modifytime=now(), contents=? WHERE (group_pid =? AND id =?)"
     values = [linkedschedule_id, contents, group_pid, id];

    this.db.query(sql, values)
    .then(res =>{
    console.log(res);
    })
    .catch(err =>{
    console.log(err);
});
  }
  selectGrouppost(group_pid){

    //SELECT * FROM grouppost WHERE group_pid =1003;
    sql = "SELECT * FROM grouppost WHERE group_pid =?"
    values = [group_pid];

    db.query(sql, values)
    .then(res =>{
    console.log(res);
    })
    .catch(err =>{
    console.log(err);
    });
  }




   createPostcomment(group_pid, post_id, write_user_pid, contents) {
     // INSERT INTO postcomment(group_pid, post_id, write_user_pid, writetime, modifytime, contents) VALUES(1003,2,1004,now(),now(),'댓글');
     sql = "INSERT INTO postcomment(group_pid, post_id, write_user_pid, writetime, modifytime, contents) VALUES(?,?,?,now(),now(),?)";
     values = [group_pid, post_id, write_user_pid, contents];

    this.db.query(sql, values)
    .then(res =>{
    console.log(res);
    })
    .catch(err =>{
    console.log(err);
});
  }
   deletePostcomment(group_pid, post_id, id) {

     //DELETE FROM postcomment WHERE (group_pid =1003 AND post_id=2 AND id =3);
     sql = "DELETE FROM postcomment WHERE (group_pid =? AND post_id=? AND id =?)";
     values = [group_pid, post_id, id];

    this.db.query(sql, values)
    .then(res =>{
    console.log(res);
    })
    .catch(err =>{
    console.log(err);
  });
  }

   updatePostcomment(group_pid, post_id, id, contents) {
     sql = "UPDATE postcomment SET modifytime=now(), contents=? WHERE (group_pid =? AND post_id=? AND id =?)"
     values = [contents, group_pid, post_id, id];

    db.query(sql, values)
    .then(res =>{
    console.log(res);
    })
    .catch(err =>{
    console.log(err);
  });
  }
  selectPostcomment(group_pid, post_id){

    ////SELECT * FROM postcomment WHERE (group_pid =1003 AND post_id=2);
    sql = "SELECT * FROM postcomment WHERE (group_pid =? AND post_id=?)"
    values = [group_pid, post_id];

    db.query(sql, values)
    .then(res =>{
    console.log(res);
    })
    .catch(err =>{
    console.log(err);
    });

  }

//스케줄 입력전에 필수로 있어야 함
  createCategoty(unit_pid, name, schedulecolor) {

    //INSERT INTO category(unit_pid, name, schedulecolor) values (1002,'c','White');
   sql = "INSERT INTO category(unit_pid, name, schedulecolor) values (?,?,?)";
   values = [unit_pid, name, schedulecolor];

   this.db.query(sql, values)
   .then(res =>{
   console.log(res);
   })
   .catch(err =>{
   console.log(err);
  });
 }
}

module.exports = Query;
