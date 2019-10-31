/*
  Dialogflow와의 연결 설정
*/

const dialogflow = require('dialogflow');

class Dialogflow_Connecter {
  constructor (projectId, key){
    this.projectId = projectId;
    let config = require('./googleApiKeytoConfig.js')(key);
    this.sessionClient = new dialogflow.SessionsClient(config);
    console.log('Set Dialogflow');
  }

  async sendToDialogflow(text, sessionId){
    const sessionPath = this.sessionClient.sessionPath(this.projectId, sessionId);
    const request = {
      session: sessionPath,
      queryInput: {
        text: {
          text: text,
          languageCode: 'ko-KR'
        }
      }
    };

    const [r] = await this.sessionClient.detectIntent(request);
    return r;
  }
}


//test
/*
const path = require('path');
const test = new Dialogflow('newagent-fxhlqn', path.join(__dirname, '..', `Data/JSON/newagent-Dialogflow.json`));
test.sendToDialogflow('내일 5시에 회의', 'test-id').then((r) =>{
  console.log(r);
}).catch((err) =>{
  console.log(err);
});
*/


module.exports = Dialogflow_Connecter;
