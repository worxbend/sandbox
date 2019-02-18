'use strict'

const path = require('path')
const AutoLoad = require('fastify-autoload')
const PluginRegistry = require('./configuration/plugin-registry')
const initMongo = require('./mongoose-init')

module.exports = async (fastify, opts) => {
  // Initialization of mongo-client.
  await initMongo(opts)
  fastify.register(PluginRegistry)
  // TODO: replace with currated routes loading.
  fastify.register(AutoLoad, {
    dir: path.join(__dirname, 'services'),
    options: Object.assign({}, opts)
  })
}
