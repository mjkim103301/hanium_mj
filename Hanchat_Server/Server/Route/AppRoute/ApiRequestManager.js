
/*
  ApiRequest의 함수 구현
*/

class ApiRequestManager{
  constructor(Functions){
    this.Functions = Functions;
  }

//Dialogflow 호출
  async chatbot(pid, text){
    const sessionId = `hanchat-${pid}`;
    const r = await this.Functions.sendToDialogflow(text, sessionId);

    let answer = r.queryResult;
    let result = {
      intent : answer.intent.displayName,
      answer : answer.fulfillmentText,
      params : answer.parameters.fields
    };

    return result;
  }

//GCPVision 호출
  async vision(base64data){
    const r = await this.Functions.sendToVision(base64data);
    let answer = r.textAnnotations;
    let result = {
      description : answer[0].description
    };

    return result;
  }
}

module.exports = (Functions) =>{
  return new ApiRequestManager(Functions);
};
