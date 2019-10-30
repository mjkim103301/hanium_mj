const dialogflow = require('dialogflow');

class Dialogflow_Connecter {
  constructor (projectId, config){
  this.projectId = projectId;

    this.sessionClient = new dialogflow.SessionsClient(config);
    console.log('Set Dialogflowapi...');
  }

  async sendtoDialogflow(text, sessionId){
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
    let answer = r.queryResult;
    let result = {
      result : true,
      intent : answer.intent.displayName,
      answer : answer.fulfillmentText,
      params : answer.parameters.fields
    };
    console.log('result : ', result);
    return result;
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
