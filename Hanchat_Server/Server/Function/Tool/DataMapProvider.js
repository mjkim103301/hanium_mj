
const DataMapFile_path = require('Data/Datamap.json');

class DataMapProvider{
  constructor(){
    const fs = require('fs');
    this.data = JSON.parse(fs.readFileSync(DataMapFile_path));
  }
  getDialogflowKey(){
    return this.data.Dialogflow_key;
  }

  getGCPVisionKey(){
    return this.data.GCPVision_key;
  }

  getDatabaseConfig(){
    return this.data.Database_Config;
  }

  getGCPVisionFilePath(){
    return this.data.UploadPath;
  }

  getGCPVisionTestDataPath(){
    return this.data.GCPVisionTestData;
  }

  getDialogflowProjectId(){
    return this.map.Dialogflow_ProjectId;
  }

}
