/*
  앱 라우터
  AppRoute 폴더에 모음
*/

class AppRoute{
  constructor(Functions, appRoute){
    this.Functions = Functions;

    this.Functions.logRouter(appRoute, 'appRoute');
    this.Routing(appRoute);

    appRoute.post('/chatbot', (req, res) =>{

      console.log('chatbot : ');
      this.Functions.Dialogflow(req, res).then(result =>{
        this.Functions.returnResults(res, result);
      }).catch(err=>{
        this.Functions.returnFailure(res, err);
      });

    });

    appRoute.post('/image', Functions.multer.single('userimage'), (req, res) =>{
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

        }).catch(err=>{
          this.Functions.returnFailure(res, err);
        });
    });
  }

  Routing(appRoute){
    appRoute.use('/account', require('./AppRoute/Account.js')(this.Functions));
    appRoute.use('/apiRequest', require('./AppRoute/APIRequest.js')(this.Functions));
  }


}


module.exports = function(Functions){
  const express = require('express');
  const appRoute = express.Router();
  new AppRoute(Functions, appRoute);
  return appRoute;
};
