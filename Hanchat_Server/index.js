/*
  실행시킬 스크립트
  포트번호를 지정하고 서버 실행
*/

const Portnumber = 55252;

require('./Server/Router.js')(__dirname, Portnumber, () => console.log(`Server start at : ${Portnumber}`));
