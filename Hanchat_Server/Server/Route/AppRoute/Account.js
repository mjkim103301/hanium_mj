
class Account{
  constructor(Functions, account){
    this.Functions = Functions;

    this.Functions.logRouter(account, 'account');

    // this.AccountManager = require('./AccountManager')(Functions);

        //로그인 - pid, logintoken 반환
        account.post('/login', (req, response) =>{
          //로그인 확인 후 로그인 토큰 반환
          const body = req.body;
          console.log('login : ');
          console.log('request : ', body);

          //id와 비밀번호로 로그인
          if(body.pid == null){
            let id = body.id;
            let password = body.password;
            Functions.getQuery().authUserForSignin(id, password).then(res =>{
              if(res.isvaild){
                this.getLoginToken(req, response, res.user_pid);
              }
              else{
                returnFailure(response, 'id not found');
              }
            }).catch(err=>{
              console.log(err);
              returnFailure(response, 'error');
            });
          }
          else{
            let pid = body.pid;
            Functions.getQuery().authUserForPid(pid).then(res =>{
              if(res){
                this.getLoginToken(req, response, pid);
              }
              else{
                returnFailure(response, 'pid not found');
              }

            }).catch(err=>{
              console.log(err);
              returnFailure(response, 'error');
            });
          }

        });

        //새로운 유저를 만들고 pid 반환
        account.post('/createuser', (req, response) =>{
          console.log('createuser : ');
          const body = req.body;
          console.log('request : ', body);
          Functions.getQuery().createUser().then(res=>{
            let result = {
              result : true,
              pid : res
            };
            console.log('result : ', result);
            returnResults(response, result);
          }).catch(err =>{
            returnFailure(response, 'user create failed');
            console.log(err);
          });
        });

        //salts 얻기 - salts 반환
        account.post('/getsalts', (req, response) =>{
          const id = req.body.id;
          Functions.getQuery().getSalts(id).then(res => {
            console.log(res);
            returnResults(response, res);
          }).catch(err =>{
            returnFailure(response, 'cannot get salts');
          });
        });

  }

  getLoginToken(req, response, user_pid){
    // let new_logintoken = this.Functions.getRandomString(14);
    // this.Functions.getQuery().updateUserLoginToken(user_pid, new_logintoken).then(res=>{
    //   let result = {
    //     result : true,
    //     pid : user_pid,
    //     logintoken : new_logintoken
    //   };
    //   returnResults(response, result);
    //   console.log('result : ', result);
    // }).catch(err=>{
    //   console.log(err);
    //   returnFailure(response, 'cannot get logintoken');
    // });
  }

  login(){

  }



}

module.exports = function(Functions){
  const express = require('express');
  const account = express.Router();
  new Account(Functions, account);
  return account;
};
