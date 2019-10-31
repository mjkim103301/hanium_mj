/*
  앱에서의 모든 호출을 담당하는 라우터
  AppRoute 폴더에 모음
*/

class AppRoute{
  constructor(Functions, appRoute){

    Functions.logRouter(appRoute, 'appRoute');

    const AuthManager = require('./AuthManager.js')(Functions.getConnecter().getDatabaseConnecter());
    this.middlewaresetting(appRoute, AuthManager);
    this.Routing(appRoute, Functions);



  }

  Routing(appRoute, Functions){
    appRoute.use('/account', require('./AppRoute/Account.js')(Functions));
    appRoute.use('/apiRequest', require('./AppRoute/ApiRequest.js')(Functions));
  }

//모든 요청 전에 먼저 로그인 유효성 검사
  middlewaresetting(app, AuthManager){
    app.use((req, res, next) =>{
      process.stdout.write(' [auth logintoken ');
      const pid = req.body.pid;
      const logintoken = req.body.logintoken;
      process.stdout.write(`{${pid}} `);
      AuthManager.authLoginToken(pid, logintoken).then((result)=>{
        if(result){
          process.stdout.write('success] ');
          next();
        }
        else{
          console.log('failed] : invalid logintoken');
        }
      }).catch(err =>{
        console.log('failed] : pid not found');
      });
    });
  }

}


module.exports = function(Functions){
  const express = require('express');
  const appRoute = express.Router();
  new AppRoute(Functions, appRoute);
  return appRoute;
};
