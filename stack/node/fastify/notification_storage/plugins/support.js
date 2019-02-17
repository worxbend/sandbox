'use strict'

const fp = require('fastify-plugin')

module.exports = fp(async (fastify, opts) => {
  fastify.decorate('someSupport', (arg) => {
    return 'hugs ' + arg
  })
})
