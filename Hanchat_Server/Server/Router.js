/*
  웹 서버 라우팅
  서버의 미들웨어 설정
  라우트 경로는 Route 폴더에 모음
*/


const express = require('express');
const bodyParser = require('body-parser');

class Router{
  constructor(app, Functions){

    const GCPVisionImageUploadPath = Functions.getGCPVisionImageUploadPath();

    this.logsetting(app, Functions);
    this.middlewaresetting(app, Functions);
    this.Routing(app, Functions);



    //일정 시간마다 테스트 실행
    setInterval(()=>{
      Functions.connecterTest();
    }, 1200000);

  }


//이 라우터를 거치는 연결에 대한 설정
  middlewaresetting(app, Functions){
    app.use(bodyParser.json({limit: '10mb', extended: true}));
    app.use(bodyParser.urlencoded({limit: '10mb', extended: true}));


    app.use('/upload', express.static('upload'));
    const multer = require('multer');
    const storage = multer.diskStorage({
      destination: (req, file, cb) => {
        //console.log(uploadpath);
        cb(null, Functions.getGCPVisionImageUploadPath());
      },
      filename: (req, file, cb) => {
        cb(null, file.originalname);
      }
    });
    Functions.setmulter( multer({storage : storage}));
  }

//이 라우터를 거칠때 로그 출력
  logsetting(app, Functions){
    app.use((req, res, next)=>{
      console.log('\n');
      Functions.printtime();
      process.stdout.write('/');
      next();
    });
  }

//라우팅
  Routing(app, Functions){
    app.all('/', (req,res) =>{
      res.redirect('/net');
    });


    let appRoute = require('./Route/AppRoute.js')(Functions);
    app.use('/apptest', appRoute);
    app.use('/appRoute', appRoute);

    let account = require('./Route/Account.js')(Functions);
    app.use('/account', account);

    //app.use('/net', require('./Routes/net.js')(Functions));
  }

}

//다른 곳에서 require를 할때 실행되는 부분
module.exports = (rootDirName, Portnumber, callback)=>{
  Functions = require('./Functions.js')(rootDirName);

  const app = express();
  let router = new Router(app, Functions);
  Functions.connecterTest().then(()=>{
    app.listen(Portnumber, callback);
  }).catch(err =>{
    console.log(err);
    throw err;
  });
};
