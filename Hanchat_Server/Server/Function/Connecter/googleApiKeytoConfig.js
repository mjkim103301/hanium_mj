
//구글 api의 keyfile로 config 생성
module.exports = (key) => {
  const keydata = JSON.parse(key);

  let privateKey = keydata.private_key;
  let clientEmail = keydata.client_email;

  let config = {
    credentials: {
      private_key: privateKey,
      client_email: clientEmail,
    }
  };

  return config;
};
