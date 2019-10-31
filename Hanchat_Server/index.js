const Portnumber = 55252;

require('./Server/Router.js')(__dirname, Portnumber, () => console.log(`Server start at : ${Portnumber}`));
