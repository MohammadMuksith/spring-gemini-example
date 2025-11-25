const express = require('express');
const mongoose = require('mongoose');
const path = require('path');
const port = 8080; // You can change this to 8080 for Cloud Shell

const app = express();
app.use(express.static(__dirname));
app.use(express.urlencoded({ extended: true }));

// ===== Use Atlas connection string here =====
const uri = 'mongodb+srv://javafinal:sharan%23070401@cluster0.uclz2xt.mongodb.net/students?retryWrites=true&w=majority';

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

app.post('/post', async (req, res) => {
    const { username, password } = req.body;
    const user = new Users({ username, password });
    await user.save();
    console.log(user);
    res.sendFile(path.join(__dirname, 'index.html'));
});

app.listen(port, () => {
    console.log("Server started on port " + port);
});

