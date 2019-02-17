'use strict'

const path = require('path')
const AutoLoad = require('fastify-autoload')
const PluginRegistry = require('./configuration/plugin-registry')
const initMongo = require('./mongoose-init')

module.exports = async (fastify, opts) => {
  // Initialization of mongo-client.
  await initMongo(opts)
  // This loads all plugins defined in plugins
  // those should be support plugins that are reused
  // through your application
  fastify.register(PluginRegistry)

  // This loads all plugins defined in services
  // define your routes in one of these
  // TODO: replace with currated routes loading.
  // Encapsulate current way of auto-loading in separate components to provide possibility of versioning and grouping of route-configuration
  fastify.register(AutoLoad, {
    dir: path.join(__dirname, 'services'),
    options: Object.assign({}, opts)
  })

  // Make sure to call next when done
}
