/*
  서버에 필요한 각종 데이터 제공
  데이터들은 {DataPath}에 저장
  데이터의 정보는 {DataPath}/Datamap.json 으로 저장
*/

  const fs = require('fs');
const path = require('path');

class DataProvider{
  constructor(DataPath){
    this.dataPath = DataPath;
    const dataMap = JSON.parse(fs.readFileSync(path.join(DataPath, 'Datamap.json')));
    this.data = dataMap.Data;
    this.path = dataMap.Path;
  }

  getData(name){
    let ans = this.data[name];
    if(ans == null){
      ans = fs.readFileSync(path.join(this.dataPath, this.path[name]));
    }
    return ans;
  }
  /*
  getDialogflowKeyPath(){
    return this.Tools.getRootPath(this.data.Dialogflow_key);
  }

  getGCPVisionKeyPath(){
    return this.Tools.getRootPath(this.data.GCPVision_key);
  }

  getDatabaseConfigPath(){
    return this.Tools.getRootPath(this.data.Database_Config);
  }

  getGCPVisionFilePath(){
    return this.Tools.getRootPath(this.data.UploadPath);
  }

  getGCPVisionTestDataPath(){
    return this.Tools.getRootPath(this.data.GCPVisionTestData);
  }

  getDialogflowProjectId(){
    return this.data.Dialogflow_ProjectId;
  }
*/
}

module.exports = (DataPath) =>{
  return new DataProvider(DataPath);
};
