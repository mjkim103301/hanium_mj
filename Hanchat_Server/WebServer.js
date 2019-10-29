const express = require('express');
const bodyParser = require('body-parser');

const Functions = require('./Modules/Functions.js');

//Connecter.query('Select * from tester', (err, rows, fields)=>{
  //console.log(rows);
//});

class app{
  constructor(){
    const app = express();
    app.use('/upload', express.static('upload'));
    app.use((req, res, next)=>{
      console.log('\n');
      Functions.printtime();
      process.stdout.write('/');
      next();
    });

    app.use(bodyParser.json({limit: '10mb', extended: true}));
    app.use(bodyParser.urlencoded({limit: '10mb', extended: true}));

    app.use('/apptest', require('./Routes/apptest.js')(Functions));
    app.use('/net', require('./Routes/net.js')(Functions));


    app.all('/', (req,res) =>{
      res.redirect('/net');
    });


    this.app = app;
  }


  listen(Portnumber, callback){
    Functions.test(()=>{
      this.app.listen(Portnumber, callback);
      Functions.printtime();
    });
  }

}



const application = new app();



//throw new Error('test error');
module.exports = application;
