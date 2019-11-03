/*
  각종 잡다한 도구들의 모음
  삭제 예정
*/


const moment = require('moment');
require('moment-timezone');
const fs = require('fs');
const path = require('path');

moment.tz.setDefault("Asia/Seoul");

class Tools{
  constructor(rootDirName){
    this.rootDirName = rootDirName;
  }

//시간 출력
  printtime(){
    console.log(moment().format('YYYY-MM-DD HH:mm:ss'));
  }

//커넥터 테스트
  async test(Connecter, DataProvider){
    console.log('\n test start');
    const re1 = await Connecter.getDialogflow().sendToDialogflow('안녕', 'start-id');
    console.log('Dialogflow Connected');

    var data = DataProvider.getData('GCPVisionTestData');
    const re2 = await Connecter.getGCPVision().sendToVision(data);
    console.log('Visionapi Connected');

    const re3 = await Connecter.getDatabaseConnecter().query('select now()');
    console.log('Database Connected');
  }

//무작위 문자열 생성
  getRandomString(length){
    var chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz";
    var randomstring = '';
    for (var i=0; i<length; i++) {
      var rnum = Math.floor(Math.random() * chars.length);
      randomstring += chars.substring(rnum,rnum+1);
    }
    return randomstring;
  }




//루트로부터의 디렉터리로 require
  rootRequire(modulename){
    return require(path.join(this.rootDirName, modulename));
  }

//루트로부터의 디렉터리 경로
  getRootPath(pathname){
    return path.join(this.rootDirName, pathname);
  }

//라우트 경로 콘솔에 로깅
  logRouter(router, routerName){
    router.use((req, res, next)=>{
      process.stdout.write(`${routerName}/`);
      next();
    });
  }
}

module.exports = (rootDirName) =>{
  return new Tools(rootDirName);
};
