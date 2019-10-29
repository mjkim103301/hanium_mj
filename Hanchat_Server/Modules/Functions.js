const Connecter = require('./Connecter/Connecter.js');

const moment = require('moment');
require('moment-timezone');
moment.tz.setDefault("Asia/Seoul");

const fs = require('fs');
const path = require('path');

const uploadpath = path.join(__dirname, '..', 'upload/');


class Functions{
  constructor(){
    this.uploadpath = uploadpath;
    const multer = require('multer');
    const storage = multer.diskStorage({
      destination: function(req, file, cb){
        //console.log(uploadpath);
        cb(null, uploadpath);
      },
      filename: function(req, file, cb){
        cb(null, file.originalname);
      }
    });
    this.upload = multer({storage : storage});
  }

  printtime(){
    console.log(moment().format('YYYY-MM-DD HH:mm:ss'));
  }

  test(callback){
    Connecter.test2().then(()=>{
      callback();
    }).catch((err)=>{
      console.log(err);
    });
  }


  Dialogflow(req, res, resultcallback, errorcallback){
    const body = req.body;

    const text = body.text;
    if(text == "" || text == undefined){
      errorcallback(req, res, "send nothing");
      return;
    }

    Connecter.sendtoDialogflow(text, 'test').then((r)=>{
      let answer = r.queryResult;
      let result = {
        result : true,
        intent : answer.intent.displayName,
        answer : answer.fulfillmentText,
        params : answer.parameters.fields
      };
      console.log('result : ', result);
      resultcallback(req, res, result);
    })
    .catch((err)=>{
      console.log(err);
      let result = {
        result : false,
        error : err
      };
      errorcallback(req, res, result);
    });
  }


  Visionapi(req, res, resultcallback, errorcallback){
    const body = req.body;
    console.log('body -');
    console.log(body);
    console.log('text -');
    console.log(decodeURI(body.text));

    const file = req.file;
    console.log('file -');
    console.log(req.file);

    if(file == undefined){
      errorcallback(req, res, 'file not found');
      return;
    }

    const filepath = this.uploadpath + file.filename;
    console.log('filepath : ' + filepath);
    const filedata = fs.readFileSync(filepath);

    var encodeddata = Buffer.from(filedata).toString('base64');

    Connecter.sendtoVision(encodeddata).then((r)=>{
      let result = r.textAnnotations;
      console.log(result);

      resultcallback(req, res, result);
      var no = Math.floor(Math.random() * (100000));
      var name = result.fulfillmentText;
      //Connecter.query(`insert into tester values(${no}, "${name}");`,(err, rows, fields)=>{

      //});
    })
    .catch((err)=>{
      console.log(err);
      errorcallback(req, res, err);
    });

  }


  getQuery(){
    return Connecter.getQuery();
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

}


module.exports = new Functions();
