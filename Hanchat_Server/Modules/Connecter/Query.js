this.db = const DB = require('./Database_Connecter');

class Query{
  constructor(DBConfig){
    this.db = new DB(DBConfig);
  }

   createSchedule(unit_pid, title, category, starttime, endtime, memo, repeattype){
    const sql = 'INSERT INTO schedule(unit_pid, title, category, starttime, endtime, memo, repeattype) VALUES(?,?,?)';
    const values = [unit_pid, title, category, starttime, endtime, memo, repeattype];

    this.db.query(sql, values)
    .then(res =>{
    console.log(res);
    })
    .catch(err =>{
    console.log(err);
    })
  }

   deleteSchedule(id, unit_pid){
    const sql = "DELETE FROM schedule WHERE (id='?'AND unit_pid='?')";
    const values = ['id','unit_pid'];

    this.db.query(sql, values)
    .then(res =>{
    console.log(res);
    })
    .catch(err =>{
    console.log(err);
    })
  }
   updateSchedule(unit_pid, title, category, starttime, endtime, memo, repeattype){
  var sql = "UPDATE usertable SET title='?', category ='?',starttime='?',endtime='?', memo='?' ,repeattype='?' WHERE (id='?' AND unit_pid='?')"
  const values = ['title', 'category', 'starttime', 'endtime', 'memo', 'repeattype', 'id', 'unit_pid'];

  this.db.query(sql, values)
    .then(res =>{
    console.log(res);
    })
    .catch(err =>{
    console.log(err);
    })
  }



  //[유닛] 유저 추가 삭제 수정 --unit-control action
  // ####내용이 테이블에 존재하지 않아 임시 email, name, password 추가작성
   createUser(){
    const sql = "INSERT INTO usertable(email, name, password, fetchtime) VALUES($1, $2, $3, $4) RETURNING *";
    const values = ['email', 'name', 'password', 'fetchtime'];

    this.db.query(sql, values)
    .then(res =>{
    console.log(res);
    })
    .catch(err =>{
    console.log(err);
    })
  }
   deleteUser(){
    const sql = "DELETE FROM usertable WHERE user_pid='?'";
    const values = ['user_pid'];

    this.db.query(sql, values)
    .then(res =>{
    console.log(res);
    })
    .catch(err =>{
    console.log(err);
    })
  }
   updateUser(){
    var sql = "UPDATE usertable SET email='?', name='?', password ='?',fetchtime='?' WHERE user_pid='?'"
    const values = ['email', 'name', 'password', 'fetchtime', 'user_pid'];

    this.db.query(sql, values)
    .then(res =>{
    console.log(res);
    })
    .catch(err =>{
    console.log(err);
    })
  }

   createGroup(){
    const sql = "INSERT INTO grouptable(groupsize, grouphost, ispublic) VALUES($1, $2, $3) RETURNING *";
    const values = ['groupsize', 'grouphost', 'ispublic'];

    this.db.query(sql, values)
    .then(res =>{
    console.log(res);
    })
    .catch(err =>{
    console.log(err);
    })
  }
   deleteGroup(){
    const sql = "DELETE FROM grouptable WHERE group_pid='?'";
    const values = ['group_pid'];

    this.db.query(sql, values)
    .then(res =>{
    console.log(res);
    })
    .catch(err =>{
    console.log(err);
    })
  }
   updateGroup() {
    var sql = "UPDATE grouptable SET groupsize='?', grouphost='?', ispublic ='?' WHERE group_pid='?'"
    const values = ['groupsize', 'grouphost', 'ispublic', 'group_pid'];

    this.db.query(sql, values)
    .then(res =>{
    console.log(res);
    })
    .catch(err =>{
    console.log(err);
    })
  }

   createUserGroup() {
    //SELECT
    const sql = "INSERT INTO useringroup(group_pid, grade, groupcolor) VALUES($1, $2, $3, $4) RETURNING *";
    const values = ["(SELECT user_pid FROM usertable WHERE user_email = 'test@mail.com' //user_pid"
    'group_pid', 'grade', 'groupcolor'];

    this.db.query(sql, values)
    .then(res =>{
    console.log(res);
    })
    .catch(err =>{
    console.log(err);
    })
  }
   deleteUserGroup() {
        const sql = "DELETE FROM useringroup WHERE (group_pid ='?' AND user_pid ='?')";
        const values = ['group_pid', 'user_pid'];

        this.db.query(sql, values)
    .then(res =>{
    console.log(res);
    })
    .catch(err =>{
    console.log(err);
    })
  }
   updateUserGroup() {
    var sql = "UPDATE useringroup SET grade ='?', groupcolor ='?' WHERE (group_pid ='?' AND user_pid ='?')"
    const values = ['grade', 'groupcolor','group_pid', 'user_pid'];

    this.db.query(sql, values)
    .then(res =>{
    console.log(res);
    })
    .catch(err =>{
    console.log(err);
    })
  }
   createGrouppost() {
    const sql = "INSERT INTO grouppost(group_pid, write_user_pid, linked_schedule_id, writetime, modifytime, contents) VALUES($1, $2, $3, $4, $5, $6) RETURNING *";
    const values = ['group_pid','write_user_pid', 'linked_schedule_id', 'writetime', 'modifytime','contents' ];

    this.db.query(sql, values)
    .then(res =>{
    console.log(res);
    })
    .catch(err =>{
    console.log(err);
    })
  }
   deleteGrouppost() {
    const sql = "DELETE FROM grouppost WHERE (group_pid ='?' AND id ='?')";
    const values = ['group_pid', 'id'];

    this.db.query(sql, values)
    .then(res =>{
    console.log(res);
    })
    .catch(err =>{
    console.log(err);
    })
  }
   updateGrouppost() {
    var sql = "UPDATE grouppost SET write_user_pid ='?', linked_schedule_id ='?' ,writetime='?',modifytime='?',contents='?' WHERE (group_pid ='?' AND id ='?')"
    const values = ['write_user_pid', 'linked_schedule_id','writetime', 'modifytime','contents''group_pid', 'id'];

    this.db.query(sql, values)
    .then(res =>{
    console.log(res);
    })
    .catch(err =>{
    console.log(err);
})
  }
   createPostcomment() {
    const sql = "INSERT INTO postcomment(group_pid, post_id, write_user_pid, writetime, modifytime, contents) VALUES($1, $2, $3, $4, $5, $6) RETURNING *";
    const values = ['group_pid','post_id', 'writetime', 'modifytime','contents' ];

    this.db.query(sql, values)
    .then(res =>{
    console.log(res);
    })
    .catch(err =>{
    console.log(err);
})
  }
   deletePostcomment() {
    //SELECT
    const sql = "DELETE FROM postcomment WHERE (group_pid ='?' AND post_id='?' AND id ='?')";
    const values = ['group_pid', 'post_id', 'id'];

    this.db.query(sql, values)
    .then(res =>{
    console.log(res);
    })
    .catch(err =>{
    console.log(err);
    })
  }
   updatePostcomment() {
    var sql = "UPDATE postcomment SET write_user_pid ='?',writetime='?',modifytime='?',contents='?' WHERE (group_pid ='?' AND post_id='?' AND id ='?')"
    const values = ['write_user_pid', 'writetime', 'modifytime','contents''group_pid','post_id', 'id'];

    db.query(sql, values)
    .then(res =>{
    console.log(res);
    })
    .catch(err =>{
    console.log(err);
    })
  }

}

module.exports = Query;
