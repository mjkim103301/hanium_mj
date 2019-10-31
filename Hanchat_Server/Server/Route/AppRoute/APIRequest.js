

class APIRequest{
  constructor(Functions, apiRequest){
    this.Functions = Functions;

    this.Functions.logRouter(apiRequest, 'apiRequest');
  }


}


module.exports = (Functions) =>{
  const express = require('express');
  const apiRequest = express.Router();
  new APIRequest(Functions, apiRequest);
  return apiRequest;
};
