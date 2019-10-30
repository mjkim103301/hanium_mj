const vision = require('@google-cloud/vision');


class GCPVision_Connecter{
  constructor (config){
    this.client = new vision.ImageAnnotatorClient(config);
    console.log('Set Visionapi...');
  }

  async sendtoVision(encodingtext){
    const request = {
      image: {
        content: encodingtext
      },
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
