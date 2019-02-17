'use strict'
const mongoose = require('mongoose')

// TODO: add logic of reading of connection url from configuration file.

module.exports = async function (configuration) {
  return new Promise((resolve, reject) => {
    console.log('Initialization of mongoose configuration')
    mongoose.connect('mongodb://localhost:27017/mydb', { useNewUrlParser: true })
    let connection = mongoose.connection
    connection.on('error', e => reject(e))
    connection.once('open', () => {
      console.debug('Connected!')
      resolve('connect')
    })
  })
}
