const DB = require('./Database_Connecter');
let sql;
let values;
let fetchtime;


// ####서치
class Query{
  constructor(DBConfig){
    this.db = new DB(DBConfig);
  }

   createSchedule(unit_pid, title, category, starttime, endtime, memo, repeattype){
// INSERT INTO schedule(unit_pid, title, category, starttime, endtime, memo, repeattype) values (1002,'스케쥴2','c','2019-10-26 10:00:00','2019-10-26 12:00:00','메모 시험음 메모',null);
    sql = "INSERT INTO schedule(unit_pid, title, category, starttime, endtime, memo, repeattype) VALUES(?,?,?,?,?,?,?)";
    values = [unit_pid, title, category, starttime, endtime, memo, repeattype];

    this.db.query(sql, values)
    .then(res =>{
    console.log(res);
    })
    .catch(err =>{
    console.log(err);
    })
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
    })
  }
   updateSchedule (id, unit_pid, title, category, starttime, endtime, memo, repeattype){
  sql = "UPDATE schedule SET title=?, category=?, starttime=?, endtime=?, memo=?, repeattype=? WHERE (id=? AND unit_pid=?)";
  values = [title, category, starttime, endtime, memo, repeattype, id, unit_pid];

  this.db.query(sql, values)
    .then(res =>{
    console.log(res);
    })
    .catch(err =>{
    console.log(err);
    })
  }

  selectSchedule(unit_pid){
      //SELECT * FROM schedule where unit_pid= 1002;
    sql = "SELECT * FROM schedule where unit_pid= unit_pid=?";
    value = [unit_pid];


    this.db.query(sql, values)
      .then(res =>{
      console.log(res);
      })
      .catch(err =>{
      console.log(err);
      })
  }
  selectScheduleByTime(unit_pid, starttime, endtime){
    //
    sql = "SELECT * FROM schedule WHERE (unit_pid = ? AND endtime BETWEEN to_timestamp('2019-10-26 01:00:00' , 'YYYY-MM-DD HH24:MI:SS') AND to_timestamp('2019-10-26 07:43:00', 'YYYY-MM-DD HH24:MI:SS'))";
    value = [unit_pid, starttime, endtime];


    this.db.query(sql, values)
      .then(res =>{
      console.log(res);
      })
      .catch(err =>{
      console.log(err);
      })
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
      })
  }




  createUser(name, picture, explanation){
    //INSERT INTO usertable(fetchtime) VALUES(now());
    //UPDATE unit SET name='이름', picture='tmp', explanation ='설명' WHERE pid=1001;
     sql = "INSERT INTO usertable(fetchtime) VALUES(?)";
     values = ['now()'];

    this.db.query(sql, values)
    .then(res =>{
    console.log(res);
    })
    .catch(err =>{
    console.log(err);
    })

    updateUnit(user_pid, name, picture, explanation);
  }
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
    })
    deleteUnit(user_pid);
  }
  updateUser(user_pid, name, picture, explanation){

      sql = "UPDATE usertable SET fetchtime=? WHERE user_pid=?"
      values = ['now()', user_pid];

      this.db.query(sql, values)
      .then(res =>{
      console.log(res);
      })
      .catch(err =>{
      console.log(err);
      })
      updateUnit(user_pid, name, picture, explanation);
    }
  selectUser(){
    sql = "SELECT user_pid FROM USER WHERE
    //조건
    ";
    value = [starttime, starttime, endtime];

    this.db.query(sql, values)
      .then(res =>{
      console.log(res);
      })
      .catch(err =>{
      console.log(err);
      })
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
    })
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
    })
    deleteUnit(group_pid);
  }
   updateGroup(group_pid, groupsize, grouphost, ispublic, name, picture, explanation) {
     sql = "UPDATE grouptable SET groupsize=?, grouphost=?, ispublic =? WHERE group_pid=?"
     values = [groupsize, grouphost, ispublic, group_pid];

    this.db.query(sql, values)
    .then(res =>{
    console.log(res);
    })
    .catch(err =>{
    console.log(err);
    })

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
    })
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
   })
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
    })
  }
   deleteUserGroup(group_pid, user_pid) {
    sql = "DELETE FROM useringroup WHERE (group_pid =? AND user_pid =?)";
    values = [group_pid, user_pid];

    this.db.query(sql, values)
    .then(res =>{
    console.log(res);
    })
    .catch(err =>{
    console.log(err);
    })
  }
   updateUserGroup(group_pid, user_pid, grade, groupcolor) {
    sql = "UPDATE useringroup SET grade =?, groupcolor =? WHERE (group_pid =? AND user_pid =?)"
    values = [grade, groupcolor, group_pid, user_pid];

    this.db.query(sql, values)
    .then(res =>{
    console.log(res);
    })
    .catch(err =>{
    console.log(err);
    })
  }
  selectUserGroup(){

  }



   createGrouppost(group_pid, write_user_pid, linked_schedule_id, writetime, modifytime, contents) {
    sql = "INSERT INTO grouppost(group_pid, write_user_pid, linked_schedule_id, writetime, modifytime, contents) VALUES(?,?,?,?,?,?)";
    values = [group_pid, write_user_pid, linked_schedule_id, writetime, modifytime, contents ];

    this.db.query(sql, values)
    .then(res =>{
    console.log(res);
    })
    .catch(err =>{
    console.log(err);
    })
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
    })
  }
   updateGrouppost(group_pid, id, write_user_pid, linked_schedule_id, writetime, modifytime, contents) {
     sql = "UPDATE grouppost SET write_user_pid =?, linked_schedule_id =? ,writetime=?, modifytime=?, contents=? WHERE (group_pid =? AND id =?)"
     values = [write_user_pid, linked_schedule_id, writetime, modifytime, contents, group_pid, id];

    this.db.query(sql, values)
    .then(res =>{
    console.log(res);
    })
    .catch(err =>{
    console.log(err);
})
  }
  selectGrouppost(){

  }




   createPostcomment(group_pid, post_id, write_user_pid, writetime, modifytime, contents) {
     sql = "INSERT INTO postcomment(group_pid, post_id, write_user_pid, writetime, modifytime, contents) VALUES(?,?,?,?,?,?)";
     values = [group_pid, post_id, write_user_pid, writetime, modifytime, contents];

    this.db.query(sql, values)
    .then(res =>{
    console.log(res);
    })
    .catch(err =>{
    console.log(err);
})
  }
   deletePostcomment(group_pid, post_id, id) {
     sql = "DELETE FROM postcomment WHERE (group_pid =? AND post_id=? AND id =?)";
     values = [group_pid, post_id, id];

    this.db.query(sql, values)
    .then(res =>{
    console.log(res);
    })
    .catch(err =>{
    console.log(err);
    })
  }
   updatePostcomment(group_pid, post_id, id, write_user_pid, writetime, modifytime, contents) {
     sql = "UPDATE postcomment SET write_user_pid =?, writetime=?, modifytime=?, contents=? WHERE (group_pid =? AND post_id=? AND id =?)"
     values = [write_user_pid, writetime, modifytime, contents, group_pid, post_id, id];

    db.query(sql, values)
    .then(res =>{
    console.log(res);
    })
    .catch(err =>{
    console.log(err);
    })
  }
  selectPostcomment(){

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
   })
 }
}

module.exports = Query;
