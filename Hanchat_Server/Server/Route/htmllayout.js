

class layout{
  gethtmllayout(title, head, description, text){
    const html =`
    <!doctype html>
    <html>
      <head>
        <title> ${title} </title>
        <meta charset='utf-8'>
      </head>
      <body>
        <p>
          <h3><a href="/net/"><-</a> ${head} </h3>
        </p>
        <p>
          ${description}
        </p>
        <p>
          ${text}
        </p>
      </body>
    </html>
    `;
    return html;
  }
  getchatbothtml(text){
    return gethtmllayout('Dialogflow test', 'Dialogflow 테스트중', `
    <form action="/net/chatbot" method="post">
      <input type="text" name="text", placeholder="말해보세요"
      size=50>
      <input type="submit">
    </form>
    `, text);
  }
  getimagehtml(text) {
    return gethtmllayout('image test', '이미지 테스트중',`
      <form action="/net/image", method="post">
        <TEXTAREA name="image" cols=70 rows=15 placeholder="base64 encoded image"
        value=""></textarea>
        <br>
        <input type="submit">
      </form>
    ` , text);
  }
  gettesthtml(text){
    return gethtmllayout('image test', '이미지 테스트중', `
    <form action="/net/imagetest" method="post" enctype="multipart/form-data">
        <input type="file" name="userimage">
        <input type="submit" value="텍스트 추출">
    </form>
    `, text);
  }
}


module.exports = new layout();
