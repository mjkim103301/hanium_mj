


module.exports = function(Functions){
  const express = require('express');
  const account = express.Router();

  apptest.use((req, res, next)=>{
    process.stdout.write('account/');
    next();
  });

  apptest.post('/login', (req, res) =>{
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
          res.send(JSON.stringify({result : false}));
          return;
        }

        Functions.getQuery().getUserLoginToken(pid).then(res=>{
          let result = {
            result : true,
            logintoken : res
          };
          res.send(JSON.stringify(result));
        }).catch(err=>{
          res.send(JSON.stringify({result:false}));
        });

      }).catch(err=>{
        console.log(err);
        let result = {
          result : false
        };
        res.send(JSON.stringify(result));
      });
    }

    res.json(body);
  });

  apptest.Post('/createuser', (req, res) =>{
    //새로운 유저를 만들고 pid 반환
    const body = req.body;
    Functions.getQuery().createUser().then(res=>{
      console.log(res.rows);
      let pid = res.rows.user_pid;
      let result = {
        result : true,
        pid : pid
      };
      res.send(JSON.stringify(result));
    }).catch(err =>{
      let result = {
        result : false
      };
      res.send(JSON.stringify(result));
      console.log(err);
    });
  });

  apptest.Post('/getsalts', (req, res) =>{
    const id = req.body.id;
    Functions.getQuery().selectUserLogin(id).then(res => {
      console.log(res);
      let result = {
        result : true,
        //
      };
    }).catch(err =>{

    })
  });

  return account;
}
