const vision = require('@google-cloud/vision');


class Vision{
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

    const [result] = await this.client.textDetection(request);

    return result;

  }
  //
  // sendtoVision(encodingtext){
  //   const request = {
  //     image: {
  //       content: encodingtext
  //     },
  //   };
  //
  //
  //   return this.client.textDetection(request);
  //
  // }
}

//답장
//  '중간고사\n6//7\n6월\n17일\n1\n국어\nG13\n10시 ~ 12시\n수학\n402\nl\nMiperm\nTest
//  \nin. 6\nTam\nN 9am\nEnglrsh\nMath\n/0 to 12\n'

 //3과 같지만 용량이 200kb
//답장
//  '6월\n17일\n6//7\n중간고사\n613\n국어\n7:00 N9:00\n101 N 12A\n402\n수학\nin 6\n
//  M5d-Herm\nTest\nEngirsh\nMath\n9am\nnam\n/0 to 12\n'

/*
 '6월 17일
  6//7
 중간고사
 613
 국어
 7:00 N9:00
 101 N 12A
 402
 수학
 in 6
 M5d-Herm
 Test
 Engirsh
 Math
 9am
 nam
 /0 to 12
 '
*/


/*
var testmodule = new Vision('Data/NewAgent-TextDetection.json');
testmodule.sendtoVision("ghghghghg").then(r =>{
  console.log(r);
}).catch(err=>{
  console.log(err);
})
*/

module.exports=Vision;
