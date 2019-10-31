const fs = require('fs');

module.exports = function (KeyFile){
  const keyfile = JSON.parse(fs.readFileSync(KeyFile));

  let privateKey = keyfile.private_key;
  let clientEmail = keyfile.client_email;

  let config = {
    credentials: {
      private_key: privateKey,
      client_email: clientEmail,
    }
  };

  return config;
};
