const express = require('express');
const mongoose = require('mongoose');
const path = require('path');
const port = 3000;
const fetch = require('node-fetch'); // You can change this to 8080 for Cloud Shell
//const fetch = require('node-fetch');


const app = express();
// app.use(express.static(__dirname));
app.use(express.static(path.join(__dirname, 'src', 'main', 'resources', 'static')));
app.use(express.urlencoded({ extended: true }));

// ===== Use Atlas connection string here =====
const uri = 'mongodb+srv://javafinal:123newpassword@cluster0.uclz2xt.mongodb.net/?appName=Cluster0';

mongoose.connect(uri);

// ============================================

const db = mongoose.connection;
db.once('open', () => {
    console.log("MongoDB Atlas works");
});

// ... rest of your code ...
const userSchema = new mongoose.Schema({
    username: String,
    password: String
});

const Users = mongoose.model("data", userSchema);

app.get('/', (req, res) => {
    res.sendFile(path.join(__dirname, 'src', 'main', 'resources', 'static', 'login.html'));


});


app.post('/ai/generate', express.json(), async (req, res) => {
  try {
    const parts = req.body;

    const response = await fetch('https://8080-cs-283706971453-default.cs-us-east1-dogs.cloudshell.dev/?authuser=0', { // use your Spring port
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(parts)
    });

    if (!response.ok) {
      const text = await response.text();
      return res.status(response.status).send(text);
    }

    const text = await response.text();
    res.send(text);
  } catch (err) {
    console.error('Proxy /ai/generate error:', err);
    res.status(500).send('Backend AI error');
  }
});



app.post('/logout', (req, res) => {
    req.session.destroy(() => {
        res.redirect('/login.html');
    });
});


app.post('/post', async (req, res) => {
    const { username, password } = req.body;
    const user = new Users({ username, password });
    await user.save();
    console.log(user);
    res.sendFile(path.join(__dirname, 'src', 'main', 'resources', 'static', 'index.html'));
});

app.listen(port, () => {
    console.log("Server started on port " + port);
});

