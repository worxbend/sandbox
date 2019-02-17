'use strict'

const fp = require('fastify-plugin')

module.exports = fp(async (fastify, opts) => {
  fastify.decorate('mongoConf', (arg) => {
    return {}
  })
})

// eslint-disable-next-line no-unused-vars
function loadMongoConfiguration () {
  // TODO: add implementation of loading configuration of mongodb from
  // configuration service (inter-service communication).
  // PASS
}

// eslint-disable-next-line no-unused-vars
function parseConfiguration (json) {
  // PASS
}

// eslint-disable-next-line no-unused-vars
function printMongoConfiguration (json) {
  // PASS
}

// eslint-disable-next-line no-unused-vars
function validateConfiguration (json) {
  // TODO: add validation schema for mongo configuration json.
  // Choose validation tools for validation by json-schema.
  // PASS
}
