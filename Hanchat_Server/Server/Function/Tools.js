
const moment = require('moment');
require('moment-timezone');
const fs = require('fs');
const path = require('path');

moment.tz.setDefault("Asia/Seoul");

class Tools{
  constructor(){
    this.DataMapProvider = require("./Tool/DataMapProvider.js");
  }

  printtime(){
    console.log(moment().format('YYYY-MM-DD HH:mm:ss'));
  }

  async test(Connecter){
    const re1 = await Connecter.sendtoDialogflow('안녕', 'start-id');
    console.log('Dialogflow Connected');

    var data = fs.readFileSync(this.DataPathProvider.getGCPVisionTestDataPath(),'utf-8');
    const re2 = await Connecter.sendtoVision(data);
    console.log('Visionapi Connected');

  }

  getRandomString(length){
    var chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz";
    var randomstring = '';
    for (var i=0; i<length; i++) {
      var rnum = Math.floor(Math.random() * chars.length);
      randomstring += chars.substring(rnum,rnum+1);
    }
    return randomstring;
  }

  googleApiKeytoConfig(KeyFilePath){
    const keyfile = JSON.parse(fs.readFileSync(KeyFilePath));

    let privateKey = keyfile.private_key;
    let clientEmail = keyfile.client_email;

    let config = {
      credentials: {
        private_key: privateKey,
        client_email: clientEmail,
      }
    };

    return config;
  }

  getDataMap(){
    return this.DataMapProvider;
  }
}

module.exports = () =>{
  return new Tools();
};
