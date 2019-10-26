function senderrormsg(res, received){
  let Msg;
  if(received == undefined){
    Msg = "null";
  }
  else{
    if(received.length < 25){
      Msg = received;
    }
    else{
      Msg = received.substring(0, 22) + "...";
    }
  }
  res.send(`Error!   your msg : ${Msg} `);
}


module.exports = function(Functions){
  const express = require('express');
  const apptest = express.Router();

  apptest.use((req, res, next)=>{
    process.stdout.write('apptest/');
    next();
  });

  apptest.post('/chatbot', (req, res) =>{
    console.log('chatbot : ');
    Functions.Dialogflow(req, res, (req, res, result)=>{
      res.send(result);

    }, (req, res, err)=>{
      senderrormsg(res, text);
    });


  });

  apptest.post('/image', Functions.upload.single('userimage'), (req, res) =>{
      console.log('image : \n');
      Functions.Visionapi(req, res, (req, res, r)=>{
        if(r == null){
          res.send('글을 인식하지 못했습니다.');
        }
        else{
          res.send(r[0].description);
        }

      }, (req, res, err)=>{
        senderrormsg(res, error);
      });
  });


  apptest.post('/login', (req, res) =>{
    const body = req.body;
    console.log('login : ');
    console.log(body);


    res.json(body);
  });

  apptest.post('/createuser', (req, res) =>{

  });


  apptest.post('/test', Functions.upload.single('userimage'), function(req, res){
    console.log('test : \n');
    Functions.Visionapi(req, res, (req, res, r)=>{
      if(r == null){
        res.end('글을 인식하지 못했습니다.');
      }
      else{
        res.send(r[0].description);
      }

    }, (req, res, err)=>{
      senderrormsg(res, error);
    });
  });


  return apptest;
};
