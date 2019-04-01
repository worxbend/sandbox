'use strict'
const mongoose = require('mongoose')

// TODO: add logic of reading of connection url from configuration file.

module.exports = async function (configuration) {
  return new Promise((resolve, reject) => {
    console.log('Initialization of mongoose configuration')
    let host = configuration['mongo.host']
    let port = configuration['mongo.port']
    let dbName = configuration['mongo.db']
    let connectionUrl = `mongodb://${host}:${port}/${dbName}`
    mongoose.connect(connectionUrl, {
      useNewUrlParser: true
    })
    let connection = mongoose.connection
    connection.on('error', e => reject(e))
    connection.once('open', () => {
      console.debug('Connected!')
      resolve('connect')
    })
  })
}
