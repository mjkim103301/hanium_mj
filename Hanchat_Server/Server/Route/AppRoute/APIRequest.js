/*
  API 호출 요청의 모음
  Dialogflow, GCPVision
*/

class ApiRequest{
  constructor(Functions, app){
    Functions.logRouter(app, 'apiRequest');

    const Manager = require('./ApiRequestManager.js');


    app.post('/chatbot', (req, res) =>{
      console.log('chatbot : ');
      const body = req.body;
      console.log('request : ', body);

      const text = req.text;
      const pid = req.pid;
      Manager.chatbot(pid, text).then(result =>{
        console.log('result : ', result);
        Functions.returnResults(res, result);
      }).catch(err=>{
        console.log(err);
        Functions.returnFailure(res, 'dialogflow error');
      });


    });


    app.post('/image', Functions.multer.single('userimage'), (req, res) =>{
        console.log('image : ');
        const body = req.body;
        console.log(decodeURI(body.text));

        const file = req.file;
        console.log('file -');
        console.log(req.file);

        if(file == undefined){
          Functions.returnFailure(res, 'file not found');
          return;
        }

        //업로드된 파일 경로
        const filepath = Functions.getGCPVisionImageUploadPath() + file.filename;
        fs.readFile(filepath, (err, data)=>{
          if(err) {
            Functions.returnFailure(res, 'readfile error');
            return;
          }
          var encodeddata = Buffer.from(data).toString('base64');

          //api 호출
          Manager.vision(encodeddata).then(r=>{
            Functions.returnResults(res, r);
          }).catch(err=>{
            Functions.returnFailure(res, 'visionapi error');
          });
        });

        Manager.vision().then( r =>{
          if(r == null){
            Functions.returnFailure(res, "error");
          }
          else{
            //res.send(r[0].description);
            let result = {
              result : true,
              description : r[0].description
            };
            Functions.returnResults(res, result);
          }

        }).catch(err=>{
          this.Functions.returnFailure(res, err);
        });
    });
  }

}



module.exports = (Functions) =>{
  const express = require('express');
  const apiRequest = express.Router();
  new ApiRequest(Functions, apiRequest);
  return apiRequest;
};
