var express = require('express'),
app = express(),
port = process.env.PORT || 9999;

app.use(session({
  store: new RedisStore({
    url: config.redisStore.url
  }),
  secret: config.redisStore.secret,
  resave: false,
  saveUnitialized: false
}));
app.use(password.initialize());
app.use(password.session());


app.listen(port);

console.log('gezenti RESTful API server started on: ' + port);
