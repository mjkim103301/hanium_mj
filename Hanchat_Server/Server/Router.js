/*
  웹 서버 라우팅
  서버의 미들웨어 설정
  라우트 경로는 Route 폴더에 모음
*/


const express = require('express');
const bodyParser = require('body-parser');

class Router{
  constructor(Functions){
    const app = express();

    const GCPVisionImageUploadPath = Functions.getGCPVisionImageUploadPath();

    this.multersetting(app, Functions);
    this.middlewaresetting(app, Functions);
    this.logsetting(app);


    app.all('/', (req,res) =>{
      res.redirect('/net');
    });

  }

  multersetting(app, Functions){
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

  middlewaresetting(app, Functions){
    app.use(bodyParser.json({limit: '10mb', extended: true}));
    app.use(bodyParser.urlencoded({limit: '10mb', extended: true}));

    let appRoute = require('./Route/AppRoute.js')(Functions);
    app.use('/apptest', appRoute);
    app.use('/appRoute', appRoute);

    let account = require('./Route/Account.js')(Functions);
    app.use('./account', account);


    //app.use('/net', require('./Routes/net.js')(Functions));

  }

  logsetting(app){
    app.use((req, res, next)=>{
      console.log('\n');
      this.Functions.printtime();
      process.stdout.write('/');
      next();
    });
  }

  getFunctions(){
    return this.Functions;
  }
}

module.exports = (rootDirName, Portnumber, callback)=>{
  Functions = require('./Functions.js')(rootDirName);
  let router = new Router(Functions);
  Functions.connecterTest().then(()=>{
    callback();
    return router;
  }).catch(err =>{
    console.log(err);
    throw err;
  });
};
