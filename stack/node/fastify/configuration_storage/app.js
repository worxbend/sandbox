'use strict'

const path = require('path')
const AutoLoad = require('fastify-autoload')
const initMongo = require('./mongoose-init')

module.exports = async (fastify, opts) => {
  // Place here your custom code!
  let configuration = {
    'mongo.host': 'localhost',
    'mongo.port': 21017,
    'mongo.db': 'mydb'
  }
  opts = {
    ...opts,
    ...configuration
  }
  await initMongo(fastify, opts)
  // Do not touch the following lines
  // This loads all plugins defined in plugins
  // those should be support plugins that are reused
  // through your application

  fastify.register(AutoLoad, {
    dir: path.join(__dirname, 'plugins'),
    options: Object.assign({}, opts)
  })

  // This loads all plugins defined in services
  // define your routes in one of these
  fastify.register(AutoLoad, {
    dir: path.join(__dirname, 'services'),
    options: Object.assign({}, opts)
  })
}