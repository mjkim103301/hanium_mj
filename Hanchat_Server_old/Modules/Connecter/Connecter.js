const dialogflow = require('./Dialogflow_Connecter.js');
const gcpvision = require('./GCPVision_Connecter.js');
const keytoconfig = require('./KeytoConfig.js');

const path = require('path');
// const Query = require('./Database_Connecter.js');
const Query = require('./Query.js');
const Dialogflow_ProjectId = 'hanchat-klyaoq';
const Dialogflow_keyfilePath = path.join(__dirname, '..', 'Data/JSON/APIkey-Dialogflow.json');
const TextDetector_keyfilePath = path.join(__dirname, '..', 'Data/JSON/APIkey-GCPVision.json');

const Database_ConfigPath = path.join(__dirname, '..', 'Data/JSON/PostgreSQL_ServerDB_key.json');
// const Database_ConfigPath = path.join(__dirname, '..', 'Data/JSON/PostgreSQL_testDB_key.json');

//const DB = path.join()

class Connecter {
  constructor() {
    console.log('connecting...');

    this.Dialogflowapi = new dialogflow(Dialogflow_ProjectId, keytoconfig(Dialogflow_keyfilePath));
    this.Visionapi = new gcpvision(keytoconfig(TextDetector_keyfilePath));
    this.Query = new Query(Database_ConfigPath);

  }

  async sendtoVision(encodedimage){
    const result = await this.Visionapi.sendtoVision(encodedimage);
    return result;
  }

  async sendtoDialogflow(text, sessionId){
    const result = await this.Dialogflowapi.sendtoDialogflow(text, sessionId);
    return result;
  }

  getQuery(){
    return this.Query;
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

}

module.exports = new Connecter();

/*
ddd = new Connecter('hello');

ddd.sendtoDialogflow('안녕', 'test').then(r =>{
  console.log(r);
})
.catch(err =>{
  console.log(err);
});
*/
