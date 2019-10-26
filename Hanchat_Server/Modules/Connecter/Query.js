const DB = require('./Database_Connecter');
let sql;
let values;
let fetchtime;

class Query{
  ructor(DBConfig){
    this.db = new DB(DBConfig);
  }

  createFetchTime(){
    //empty 데이터 베이스에서 만들어줄까봐 따로 코딩안했는데 필요하면 할것
  }
   createSchedule(title, category, starttime, endtime, memo, repeattype){
    sql = "INSERT INTO schedule(title, category, starttime, endtime, memo, repeattype) VALUES(?,?,?)";
    values = [title, category, starttime, endtime, memo, repeattype];

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
  // selectSchedule(tag){
  //   switch(tag) {
  //     case 0 :
  //       selectScheduleByTitle(title);
  //       break;
  //     case 1 :
  //       selectScheduleByTitle();
  //       break;
  //     case 2 :
  //       selectScheduleByTitle();
  //       break;
  //     case 3 :
  //       selectScheduleByTitle();
  //       break;
  //     default :
  //     //전체를 들고오는 쿼리
  //       break;
  //
  //   }
  // }
  // selectScheduleByTitle(title){
  //   sql = "SELECT * FROM schedule WHERE title = ?";
  //   value = title;
  //
  //   this.db.query(sql, values)
  //     .then(res =>{
  //     console.log(res);
  //     })
  //     .catch(err =>{
  //     console.log(err);
  //     })
  // }

  // 테이블에 값이 없어서 일단 비워둠
   createUser(name, picture, explanation){
    fetchtime = createFetchTime();
    sql = "INSERT INTO usertable(fetchtime) VALUES(?) ";
    values = [fetchtime];

    this.db.query(sql, values)
    .then(res =>{
    console.log(res);
    })
    .catch(err =>{
    console.log(err);
    })

    createUnit(name, picture, explanation, 'SELECT user_pid');
  }
   deleteUser(user_pid){
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
    fetchtime = createFetchTime();
    sql = "UPDATE usertable SET fetchtime=? WHERE user_pid=?"
    values = [fetchtime, user_pid];

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

  }

   createGroup(groupsize, grouphost, ispublic, name, picture, explanation){
     sql = "INSERT INTO grouptable(groupsize, grouphost, ispublic) VALUES(?,?,?)";
     values = [groupsize, grouphost, ispublic];

    this.db.query(sql, values)
    .then(res =>{
    console.log(res);
    })
    .catch(err =>{
    console.log(err);
    })
    createUnit(name, picture, explanation, 'SELECT group_pid');
  }
   deleteGroup(group_pid){
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
  createUnit(pid, name, picture, explanation){
    sql = "INSERT INTO unit(pid, name, picture, explanation) VALUES(?,?,?,?) ";
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
  selectUnit(){

  }


   createUserGroup(group_pid, user_pid, grade, groupcolor) {
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
     sql = "INSERT INTO postcomment(group_pid, post_id, write_user_pid, writetime, modifytime, contents) VALUESVALUES(?,?,?,?,?,?)";
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
}

module.exports = Query;
