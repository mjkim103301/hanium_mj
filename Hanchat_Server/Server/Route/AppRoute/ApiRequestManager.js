

class ApiRequestManager{
  constructor(Functions){
    this.Functions = Functions;
  }

//Dialogflow 호출
  async chatbot(pid, text){
    const sessionId = `hanchat-${pid}`;
    const r = this.Functions.sendToDialogflow(text,sessionId);

    let answer = r.queryResult;
    let result = {
      result : true,
      intent : answer.intent.displayName,
      answer : answer.fulfillmentText,
      params : answer.parameters.fields
    };

    return result;
  }

//GCPVision 호출
  async vision(base64data){
    const r = this.Functions.sendToVision(base64data);
    let answer = r.textAnnotations;
    let result = {
      result : true,
      description : answer[0].description
    };

    return result;
  }
}

module.exports = (Functions) =>{
  return new ApiRequestManager(Functions);
};
