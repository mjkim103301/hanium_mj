const fs = require('fs');
const path = require('path');

class Functions{
  constructor(){
    this.Tools = require('./Function/Tools.js')();
    this.UploadPath = this.Tools.getDataMap().getUploadPath();
    this.Connecter = require('./Function/Connecters.js')(this.Tools.getDataMap());
    this.Query = require('./Function/Querys.js')(this.Connecter.getDatabaseConnecter());
  }

//API Connecter
  async Dialogflow(req, res, resultcallback, errorcallback){
    const body = req.body;
    console.log('request : ', body);
    const text = body.text;

    if(text == "" || text == undefined){
      throw new Error("text nothing");
    }

    return Connecter.sendtoDialogflow(text, 'test');
  }

  async Visionapi(req, res, resultcallback, errorcallback){
    const body = req.body;
    console.log(decodeURI(body.text));

    const file = req.file;
    console.log('file -');
    console.log(req.file);

    if(file == undefined){
      throw new Error("file not found");
    }

    const filepath = uploadpath + file.filename;
    const filedata = fs.readFileSync(filepath);

    var encodeddata = Buffer.from(filedata).toString('base64');

    return Connecter.sendtoVision(encodeddata);

  }
//end API Connecter

//Return Req
  returnResults(res, result){
    res.send(JSON.stringify(result));
  }

  returnFailure(res, reason){
    res.send(JSON.stringify({ result : false, reason : reason }));
  }
//end Return Req


  getQuery(){
    return this.query;
  }


//Tools
  printtime(){
    this.Tools.printtime();
  }

  async testConnecter(){
    return await this.Tools.test();
  }

  getRandomString(length){
    return this.Tools.getRandomString(length);
  }

  googleApiKeytoConfig(KeyFilePath){
    return this.Tools.googleApiKeytoConfig(KeyFilePath);
  }

  getDataMap(){
    return this.Tools.getDataMap();
  }
//end Tools

}


module.exports = () =>{
  return new Functions();
};
