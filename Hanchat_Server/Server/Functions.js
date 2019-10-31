/*
  라우터에서 사용하는 기능들의 모음
  간단한 기능은 구현해놓지만 복잡한 기능은 하위 모듈에서 구현
  하지만 되도록 함수를 여러번 호출하지 않게 하위 모듈들의 함수를 랩핑? 해놓을것 - Tools
  하위 모듈들은 Funtion 폴더에 모음
*/


const fs = require('fs');
const path = require('path');

class Functions{
  constructor(rootDirName){
    this.Tools = require('./Function/Tools.js')(rootDirName);
    this.Tools.printtime();
    this.DataProvider = require("./Function/DataProvider.js")(`${rootDirName}/Data`);
    this.Connecter = require('./Function/Connecter.js')(this.DataProvider);
    this.UploadPath = this.DataProvider.getData('UploadPath');
    this.multer = null;
  }

//getter
  getTools(){
    return this.Tools;
  }
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
    res.send(JSON.stringify(result));
  }

  returnFailure(res, err){
    res.send(JSON.stringify({ result : false, err : err }));
  }
//end Return Req

//unitLog 에 기록
  async unitLog(action, unit_pid, affected_unit_pid, target_id){
    sql = 'INSERT INTO UnitLog(log_time, action_id, unit_pid, affected_unit_pid, target_id)';
    sql = sql + 'VALUES(now(), $1, $2, $3, $4)';
    values = [action, unit_pid, affected_unit_pid, target_id];
    await this.getDatabaseConnecter().query(sql, values);
  }

  asyncFuncExecutor(res, func, args, errmsg, successfunc){
    func.apply(null, args).then(r =>{
      console.log('result : ', r);
      if(successfunc == null){
        res.send(JSON.stringify(result));
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
  printtime(){
    this.Tools.printtime();
  }
  logRouter(router, routerName){
    this.Tools.logRouter(router, routerName);
  }
  async connecterTest(){
    return await this.Tools.test(this.Connecter, this.DataProvider);
  }
  getRandomString(length){
    return this.Tools.getRandomString(length);
  }
  googleApiKeytoConfig(KeyFilePath){
    return this.Tools.googleApiKeytoConfig(KeyFilePath);
  }
  rootRequire(modulename){
    return this.Tools.rootRequire(modulename);
  }
  getRootPath(pathname){
    return this.Tools.getRootPath(pathname);
  }
//end Tools

}

function dd(){
  let d =arguments.length;
}
module.exports = (rootDirName) =>{
  return new Functions(rootDirName);
};
