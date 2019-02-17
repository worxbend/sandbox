'use strict'

const fp = require('fastify-plugin')

module.exports = fp(async (fastify, opts) => {
  fastify.register(require('../plugins/support'))
  fastify.register(require('../plugins/utils/database/mongo-conf'))
  // Healthcheck API for fastify. Returning a default healthcheck
  // route mapped to '/health' that only reply to a GET request
  fastify.register(require('fastify-healthcheck'))

  printRoutes(fastify)
})

function printRoutes (serverInstanceRef) {
  // Prints all fastify routes before server will be started.
  serverInstanceRef.ready(() => {
    const routes = serverInstanceRef.printRoutes()
    console.log(`Available Routes:\n${routes}`)
  })
}
