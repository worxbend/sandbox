// next.config.js
const { PHASE_DEVELOPMENT_SERVER } = require('next/constants')
module.exports = (phase, { defaultConfig }) => {
  if (phase === PHASE_DEVELOPMENT_SERVER) {
    return {
      generateEtags: false,
      onDemandEntries: {
        // period (in ms) where the server will keep pages in the buffer
        maxInactiveAge: 25 * 1000,
        // number of pages that should be kept simultaneously without being disposed
        pagesBufferLength: 2
      },
      pageExtensions: ['jsx', 'js']
    }
  }

  return {
    /* config options for all phases except development here */
  }
}
