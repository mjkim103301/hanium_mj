const dialogflow = require('./Connecter/Dialogflow_Connecter.js');
const gcpvision = require('./Connecter/GCPVision_Connecter.js');
const database = require('./Connecter/Database_Connecter.js');


class Connecter {
  constructor(Tools) {
    console.log('connecting...');
    const DataMapProvider = Tools.getDataMap();
    const Dialogflow_ProjectId = DataMapProvider.getDialogflowProjectId();
    const Dialogflow_keyfilePath = DataMapProvider.getDialogflowKey();
    const GCPVision_keyfilePath = DataMapProvider.getGCPVisionKey();
    const Database_Config = DataMapProvider.getDatabaseConfig();
    this.Dialogflowapi = new dialogflow(Dialogflow_ProjectId, Tools.googleApiKeytoConfig(Dialogflow_keyfilePath));
    this.Visionapi = new gcpvision(Tools.googleApiKeytoConfig(GCPVision_keyfilePath));
    this.Database = new database(Database_Config);

  }

  async sendtoVision(encodedimage){
    const result = await this.Visionapi.sendtoVision(encodedimage);
    return result;
  }

  async sendtoDialogflow(text, sessionId){
    const result = await this.Dialogflowapi.sendtoDialogflow(text, sessionId);
    return result;
  }

  async test2(){
    const re1 = await this.sendtoDialogflow('안녕', 'start-id');
    const fs = require('fs');
    var data = fs.readFileSync(path.join(__dirname,'..','Data','TestImage','Testdata2.txt'),'utf-8');
    const re2 = await this.sendtoVision(data);

    if(re1.queryResult.action == 'input.welcome'){
      console.log('Dialogflow Connected');
    }
    else{
      throw new Error('Dialogflow Connection failed');
    }
    if(re2.error == null){
      console.log('Visionapi Connected');
    }
    else{
      console.log(re2.error);
      throw new Error('Visionapi Connection failed');
    }

    return;

  }

  getDatabaseConnecter(){
    return this.Database;
  }
}

module.exports = (DataPathProvider) =>{
  return new Connecter(DataPathProvider);
};

/*
ddd = new Connecter('hello');

ddd.sendtoDialogflow('안녕', 'test').then(r =>{
  console.log(r);
})
.catch(err =>{
  console.log(err);
});
*/
