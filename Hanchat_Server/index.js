var app = require('./Server/Router.js');
const Portnumber = 55252;

app.listen(__dirname, Portnumber, () => console.log(`Server start at : ${Portnumber}`));
