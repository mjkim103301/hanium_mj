


module.exports = function(Functions){
  const express = require('express');
  const account = express.Router();

  account.use((req, res, next)=>{
    process.stdout.write('account/');
    next();
  });

  account.post('/login', (req, response) =>{
    //로그인 확인 후 로그인 토큰 반환
    const body = req.body;
    console.log('login : ');
    console.log(body);

    if(body.pid == null){
      let id = body.id;
      let password = body.password;

    }
    else{
      let pid = body.pid;
      Functions.getQuery().selectUserFromPid(pid).then(res =>{
        console.log(res);
        if(res.rows == null){
          response.send(JSON.stringify({result : false}));
          return;
        }

        let new_logintoken = Functions.getRandomString(14);
        Functions.getQuery().getUserLoginToken(pid, new_logintoken).then(res=>{
          let result = {
            result : true,
            logintoken : new_logintoken
          };
          response.send(JSON.stringify(result));
          console.log(result);
        }).catch(err=>{
          console.log(err);
          response.send(JSON.stringify({result:false}));
        });

      }).catch(err=>{
        console.log(err);
        let result = {
          result : false
        };
        response.send(JSON.stringify(result));
      });
    }

  });

  account.post('/createuser', (req, response) =>{
    //새로운 유저를 만들고 pid 반환
    console.log('createuser : ');
    const body = req.body;
    Functions.getQuery().createUser().then(queryResult=>{
      let pid = queryResult.rows[0].user_pid;
      let result = {
        result : true,
        pid : pid
      };
      console.log(result);
      response.send(JSON.stringify(result));
    }).catch(err =>{
      let result = {
        result : false
      };
      response.send(JSON.stringify(result));
      console.log(err);
    });
  });

  account.post('/getsalts', (req, res) =>{
    const id = req.body.id;
    Functions.getQuery().selectUserLogin(id).then(res => {
      console.log(res);
      let result = {
        result : true,
        //
      };
    }).catch(err =>{

    });
  });

  return account;
};
