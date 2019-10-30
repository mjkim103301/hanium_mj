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

class AppRoute{
  constructor(Functions){
    this.Functions = Functions;
    const express = require('express');
    const appRoute = express.Router();

    this.logsetting(appRoute);
    this.Routing(appRoute);

    appRoute.post('/chatbot', (req, res) =>{

      console.log('chatbot : ');
      this.Functions.Dialogflow(req, res).then(result =>{
        this.Functions.returnResults(res, result);
      }.catch(err=>{
        this.Functions.returnFailure(res, err);
      }));

    });

    appRoute.post('/image', Functions.upload.single('userimage'), (req, res) =>{
        console.log('image : \n');
        Functions.Visionapi(req, res).then( r =>{
          if(r == null){
            this.Functions.returnFailure(res, "error");
          }
          else{
            //res.send(r[0].description);
            let result = {
              result : true,
              description : r[0].description
            };
            this.Functions.returnResults(res, result);
          }

        }.catch(err=>{
          this.Functions.returnFailure(res, err);
        }));
    });
  }

  logsetting(appRoute){
    appRoute.use((req, res, next)=>{
      process.stdout.write('appRoute/');
      next();
    });
  }

  Routing(appRoute){
    appRoute.use('/account', require('./AppRoute/Account.js')(Functions));
  }
}


module.exports = function(Functions){
  return new AppRoute(Functions);
};
