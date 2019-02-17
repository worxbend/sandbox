'use strict'

module.exports = async (fastify, opts) => {
  fastify.get('/init', async (request, reply) => {
    return { root: true }
  })
}
