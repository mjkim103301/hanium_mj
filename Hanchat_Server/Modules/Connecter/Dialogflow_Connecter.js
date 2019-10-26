const dialogflow = require('dialogflow');

class Dialogflow {
  constructor (projectId, config){
  this.projectId = projectId;

    this.sessionClient = new dialogflow.SessionsClient(config);
    console.log('Set Dialogflowapi...');
  }

  // sendtoDialogflow (text, sessionId){
  //   console.log(text);
  //   const sessionPath = this.sessionClient.sessionPath(this.projectId, sessionId);
  //
  //   const request = {
  //     session: sessionPath,
  //     queryInput: {
  //       text: {
  //         text: text,
  //         languageCode: 'ko-KR'
  //       }
  //     }
  //   };
  //   console.log(2);
  //   return this.sessionClient.detectIntent(request);
  // }

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
    const [result] = await this.sessionClient.detectIntent(request);
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


module.exports = Dialogflow;
