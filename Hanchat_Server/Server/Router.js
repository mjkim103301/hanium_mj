
class Router{
  constructor(rootDirName){
    const express = require('express');
    const bodyParser = require('body-parser');
    const app = express();

    this.Functions = require('./Functions.js')(rootDirName);

    this.multersetting(app);
    this.middlewaresetting(app);
    this.logsetting(app);


    app.all('/', (req,res) =>{
      res.redirect('/net');
    });

  }

  multersetting(app){
    app.use('/upload', express.static('upload'));
    this.uploadpath = uploadpath;
    const multer = require('multer');
    const storage = multer.diskStorage({
      destination: function(req, file, cb){
        //console.log(uploadpath);
        cb(null, uploadpath);
      },
      filename: function(req, file, cb){
        cb(null, file.originalname);
      }
    });
    this.upload = multer({storage : storage});
  }

  middlewaresetting(app){
    app.use(bodyParser.json({limit: '10mb', extended: true}));
    app.use(bodyParser.urlencoded({limit: '10mb', extended: true}));

    appRoute = require('./Routes/appRoute.js')(this.Functions);
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
  router.getFunctions().test().then(()=>{
    callback();
    Functions.printtime();
    return router;
  }).catch(err =>{
    console.log(err);
    throw err;
  });
};
