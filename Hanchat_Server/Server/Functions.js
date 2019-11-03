/*
  라우터에서 사용하는 기능들의 모음
  간단한 기능은 구현해놓지만 복잡한 기능은 하위 모듈에서 구현
  하지만 되도록 함수를 여러번 호출하지 않게 하위 모듈들의 함수를 랩핑? 해놓을것 - Tools
  하위 모듈들은 Funtion 폴더에 모음
*/

const fs = require('fs');
const path = require('path');
const moment = require('moment');
require('moment-timezone');

moment.tz.setDefault("Asia/Seoul");

class Functions{
  constructor(rootDirName){
    this.rootDirName = rootDirName;
    this.DataProvider = require("./Function/DataProvider.js")(`${rootDirName}/Data`);
    this.Connecter = require('./Function/Connecter.js')(this.DataProvider);
    this.UploadPath = this.DataProvider.getData('UploadPath');
    this.multer = null;
  }

//getter
  getDataMap(){
    return this.DataProvider;
  }
  getDataProvider(){
    return this.DataProvider;
  }
  getConnecter(){
    return this.Connecter;
  }
  getGCPVisionImageUploadPath(){
    return this.UploadPath;
  }
//end getter

//Connceter
  getDialogflowConnecter(){
    return this.Connecter.getDialogflow();
  }
  getGCPVisionConnecter(){
    return this.Connecter.getGCPVision();
  }
  getDatabaseConnecter(){
    return this.Connecter.getDatabaseConnecter();
  }

  async sendToDialogflow(text, sessionId){
    return this.getDialogflowConnecter().sendToDialogflow(text, sessionId);
  }
  async sendToVision(base64data){
    return this.getGCPVisionConnecter().sendToVision(base64data);
  }
  async query(sql, values){
    return this.getDatabaseConnecter().query(sql, values);
  }
//end Connecter


  setmulter(multername){
    this.multer = multername;
  }


// //API Connecter
//   async Dialogflow(req, res, resultcallback, errorcallback){
//     const body = req.body;
//     console.log('request : ', body);
//     const text = body.text;
//     const sessionId = "testid";
//     if(text == "" || text == undefined){
//       throw new Error("text nothing");
//     }
//
//     return await this.Connecter.getDialogflow().sendtoDialogflow(text, sessionId);
//   }
//
//   async Visionapi(req, res, resultcallback, errorcallback){
//     const body = req.body;
//     console.log(decodeURI(body.text));
//
//     const file = req.file;
//     console.log('file -');
//     console.log(req.file);
//
//     if(file == undefined){
//       throw new Error("file not found");
//     }
//
//     const filepath = uploadpath + file.filename;
//     const filedata = fs.readFileSync(filepath);
//
//     var encodeddata = Buffer.from(filedata).toString('base64');
//
//     return await this.Connecter.getGCPVision().sendtoVision(encodeddata);
//
//   }
//end API Connecter

//Return Req
  returnResults(res, result){
    result.result = true;
    res.send(JSON.stringify(result));
  }

  returnFailure(res, err){
    res.send(JSON.stringify({ result : false, err : err }));
  }
//end Return Req

//unitLog 에 기록
  async UnitLog(action, unit_pid, affected_unit_pid, target_id){
    let sql = 'INSERT INTO UnitLog(log_time, action_id, unit_pid, affected_unit_pid, target_id)';
    sql += 'VALUES(now(), $1, $2, $3, $4)';
    let values = [action, unit_pid, affected_unit_pid, target_id];
    await this.getDatabaseConnecter().query(sql, values);
  }

//비동기 작업 대신 돌려주는 함수
//응답할 곳, 실행시킬 함수의 주인, 실행시킬 함수,
// 매개변수(배열로), 실패시 응답할 에러 메시지, 성공시 실행시킬 함수( nullable)
  asyncFuncExecutor(res, manager, func, args, errmsg, successfunc){
    func.apply(manager, args).then(r =>{
      console.log('result : ', r);
      if(successfunc == null){
        r.result = true;
        res.send(JSON.stringify(r));
      }
      else{
        successfunc(r);
      }
    }).catch(e =>{
      console.log('err : ', e);
      res.send(JSON.stringify({ result : false, err : errmsg }));
    });
  }


//Tools
//콘솔에 시간 출력
  printtime(){
    console.log(moment().format('YYYY-MM-DD HH:mm:ss'));
  }
//라우터를 거칠때 콘솔에 시간 출력하게 설정
  logRouter(router, routerName){
    router.use((req, res, next)=>{
      process.stdout.write(`${routerName}/`);
      next();
    });
  }

//연결 테스트
  async connecterTest(){
    console.log();
    this.printtime();
    console.log('test start');
    const re1 = await this.sendToDialogflow('안녕', 'start-id');
    console.log('Dialogflow Connected');

    var data = DataProvider.getData('GCPVisionTestData');
    const re2 = await this.sendToVision(data);
    console.log('Visionapi Connected');

    const re3 = await this.query('select now()');
    console.log('Database Connected');
  }

//길이만큼의 랜덤 문자열 생성
  getRandomString(length){
    var chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz";
    var randomstring = '';
    for (var i=0; i<length; i++) {
      var rnum = Math.floor(Math.random() * chars.length);
      randomstring += chars.substring(rnum,rnum+1);
    }
    return randomstring;
  }

//루트에서의 경로로 require
  rootRequire(modulename){
    return require(path.join(this.rootDirName, modulename));
  }

//루트에서의 경로 얻기
  getRootPath(pathname){
    return path.join(this.rootDirName, pathname);
  }
//end Tools

}


module.exports = (rootDirName) =>{
  return new Functions(rootDirName);
};
