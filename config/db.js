const mongoose = require('mongoose');

const local = "mongodb+srv://admin1:zqwCqU4zTl2IZOyX@cluster0.d13ikfs.mongodb.net/PH27669";

const connect = async () => {
    try {
        await mongoose.connect(local);
        console.log('Connect success');
    } catch (error) {
        console.error('Connection to MongoDB failed:', error);
    }
}

module.exports = { connect };
