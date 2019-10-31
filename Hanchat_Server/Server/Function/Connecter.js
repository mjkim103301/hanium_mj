/*
  외부와의 연결을 설정
  하위 모듈들은 Connecter 폴더에 모음
*/


class Connecter {
  constructor(DataProvider) {
    console.log('connecting...');
    //필요한 정보들
    const Dialogflow_ProjectId = DataProvider.getData("Dialogflow_ProjectId");
    const Dialogflow_key = DataProvider.getData("Dialogflow_key");
    const GCPVision_key = DataProvider.getData("GCPVision_key");
    const Database_Config = DataProvider.getData("Database_Config");

    //사용하는 모듈들
    const Dialogflow_Connecter = require('./Connecter/Dialogflow_Connecter.js');
    const GCPVision_Connecter = require('./Connecter/GCPVision_Connecter.js');
    const Database_Connecter = require('./Connecter/Database_Connecter.js');

    //모듈들 생성
    this.Dialogflow = new Dialogflow_Connecter(Dialogflow_ProjectId, Dialogflow_key);
    this.GCPVision = new GCPVision_Connecter(GCPVision_key);
    this.Database = new Database_Connecter(Database_Config);

  }

  getDialogflow(){
    return this.Dialogflow;
  }

  getGCPVision(){
    return this.GCPVision;
  }

  getDatabaseConnecter(){
    return this.Database;
  }
}

module.exports = (DataPathProvider) =>{
  return new Connecter(DataPathProvider);
};
