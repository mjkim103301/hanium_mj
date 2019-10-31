/*
  구글 클라우드 Vision 과의 연결 설정
*/

const vision = require('@google-cloud/vision');


class GCPVision_Connecter{
  constructor (key){
    const config = require('./googleApiKeytoConfig.js')(key);
    this.client = new vision.ImageAnnotatorClient(config);
    console.log('Set GCPVision');
  }

  async sendToVision(encodingtext){
    const request = {
      image: {
        content: encodingtext
      }
    };

    const [r] = await this.client.textDetection(request);
    let result = r.textAnnotations;
    return result;
  }
}

/*
var testmodule = new Vision('Data/NewAgent-TextDetection.json');
testmodule.sendtoVision("ghghghghg").then(r =>{
  console.log(r);
}).catch(err=>{
  console.log(err);
})
*/

module.exports= GCPVision_Connecter;
