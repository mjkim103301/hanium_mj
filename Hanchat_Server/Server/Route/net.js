const fs = require('fs');
const express = require('express');

const html = require('./htmllayout');
gethtmllayout = html.gethtmllayout;
getimagehtml = html.getimagehtml;
getchatbothtml = html.getchatbothtml;
gettesthtml = html.gettesthtml;


module.exports = function(Functions){
  const net = express.Router();

  net.use((req, res, next)=>{
    process.stdout.write('net/');
    next();
  });

  net.get('/', (req,res) =>{
    res.end(fs.readFileSync(`${__dirname}/tmp_root.html`));
  });


  net.route('/chatbot')
  .post((req, res) =>{
    console.log('chatbot : ');
    Functions.Dialogflow(req, res, (req, res, r)=>{
      res.send(getchatbothtml(JSON.stringify(r)));
    }, (req, res, err)=>{
      res.send(getchatbothtml(JSON.stringify(err)));
    });

  })
  .get((req, res) => {
    console.log('chatbot : ');
    res.end(getchatbothtml(''));
  });


  net.route('/image')
  .post(Functions.upload.single('userimage'), (req, res) =>{
    console.log('image :');
    Functions.Visionapi(req, res, (req, res, r)=>{
      var result = r[0].description;
      res.send(gettesthtml(result));
    }, (req, res, err)=>{
      res.send(gettesthtml(err));
    });

  })
  .get((req, res) =>{
    console.log('imagetest : ');
    res.end(gettesthtml(''));
  });


  return net;
};
