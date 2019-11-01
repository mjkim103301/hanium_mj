/*
  계정에 관련된 요청을 담당하는 라우터
*/




class Account{
  constructor(Functions, account){
    Functions.logRouter(account, 'account');
    const Manager = require('./AccountManager')(Functions);

    //로그인 토큰 얻기에서 응답까지
    const getLoginToken = (response, user_pid) => {
      Functions.asyncFuncExecutor(response, Manager, Manager.getLoginToken,
      [user_pid], 'get logintoken failed');
      // Manager.getLoginToken(user_pid).then(res=>{
      //   console.log('getloginToken : ');
      //   let result = {
      //     result : true,
      //     pid : user_pid,
      //     logintoken : res.logintoken
      //   };
      //   console.log('result : ', result);
      //   Functions.returnResults(response, result);
      // }).catch(err=>{
      //   console.log(err);
      //   Functions.returnFailure(response, 'cannot get logintoken');
      // });
    };

    //로그인 - pid, logintoken 반환
    account.post('/login', (req, response) =>{
      const body = req.body;
      console.log('login : ');
      console.log('request : ', body);

      //id와 비밀번호로 로그인
      if(body.pid == null){
        let id = body.id;
        let password = body.password;

        Manager.loginWithId(id, password).then(res =>{
          if(res.result){
            getLoginToken(response, res.pid);
          }
          else{
            Functions.returnFailure(response, 'password is not valid');
          }
        }).catch(err=>{
          console.log(err);
          Functions.returnFailure(response, 'id not found');
        });
      }
      //pid와 이전 로그인 토큰으로 로그인
      else{
        let pid = body.pid;
        let logintoken = body.logintoken;

        Functions.asyncFuncExecutor(response, Manager, Manager.loginWithPid, [pid, logintoken],
        'pid not found', (r)=>{
          if(r.result){
            getLoginToken(response, r.pid);
          }
          else{
            Functions.returnFailure(response, 'logintoken is not valid');
          }
        });

        // Manager.loginWithPid(pid, logintoken).then(loginresult =>{
        //   if(loginresult.result){
        //     getLoginToken(response, loginresult.pid);
        //   }
        //   else{
        //     Functions.returnFailure(response, 'logintoken is not valid');
        //   }
        //
        // }).catch(err=>{
        //   console.log(err);
        //   Functions.returnFailure(response, 'pid not found');
        // });
      }

    });

    //새로운 유저를 만들고 pid, logintoken 반환
    account.post('/createUser', (req, response) =>{
      console.log('createUser : ');
      const body = req.body;
      console.log('request : ', body);

      Functions.asyncFuncExecutor(response, Manager, Manager.createUser, null,
         'user create failed');

    });

    //salts 얻기 - salts 반환
    account.post('/getsalts', (req, response) =>{
      console.log('getsalts : ');
      console.log('request : ', req.body);
      const id = req.body.id;


      Manager.getSalts(id).then(result => {
        console.log('result : ', result);
        Functions.returnResults(response, result);
      }).catch(err =>{
        Functions.returnFailure(response, 'id not found');
      });
    });

  }





}

module.exports = function(Functions){
  const express = require('express');
  const account = express.Router();
  new Account(Functions, account);
  return account;
};
