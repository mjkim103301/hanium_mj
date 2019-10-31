/*
  웹 서버 라우팅
  서버의 미들웨어 설정
  라우트 경로는 Route 폴더에 모음
*/


const express = require('express');
const bodyParser = require('body-parser');

class Router{
  constructor(rootDirName){
    const app = express();

    this.Functions = require('./Functions.js')(rootDirName);
    this.uploadpath = this.Functions.getGCPVisionImageUploadPath();

    this.multersetting(app);
    this.middlewaresetting(app);
    this.logsetting(app);


    app.all('/', (req,res) =>{
      res.redirect('/net');
    });

  }

  multersetting(app){
    app.use('/upload', express.static('upload'));
    const multer = require('multer');
    const storage = multer.diskStorage({
      destination: function(req, file, cb){
        //console.log(uploadpath);
        console.log('uploadpath : ', Router.this.uploadpath);
        cb(null, Router.this.uploadpath);
      },
      filename: function(req, file, cb){
        cb(null, file.originalname);
      }
    });
    this.upload = multer({storage : storage});
    this.Functions.setmulter(this.upload);
  }

  middlewaresetting(app){
    app.use(bodyParser.json({limit: '10mb', extended: true}));
    app.use(bodyParser.urlencoded({limit: '10mb', extended: true}));

    let appRoute = require('./Route/AppRoute.js')(this.Functions);
    app.use('/apptest', appRoute);
    app.use('/appRoute', appRoute);
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
  let router = new Router(rootDirName);
  router.getFunctions().connecterTest().then(()=>{
    callback();
    router.getFunctions().printtime();
    return router;
  }).catch(err =>{
    console.log(err);
    throw err;
  });
};
