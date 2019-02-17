'use strict'
const fs = require('fs')
const path = require('path')
const makeDir = require('make-dir')
const AdmZip = require('adm-zip')

class FileSystemHelper {
  /**
   * Write file.
   * @param dirPath {String} Absolute destination path.
   * @param filename {String} File name with extension.
   * @param data {Buffer} Data for writing.
   * @returns Object which contains file metadata.
   */
  static async writeFile (dirPath, filename, data) {
    await makeDir(dirPath)
    await FileSystemHelper._writeFile(dirPath, filename, data)
  }

  static async writeZipFile (dirPath, zipFileName, { innerFileName, buffer }) {
    let newZipFile = new AdmZip()
    newZipFile.addFile(innerFileName, buffer)
    let createdZipBuffer = newZipFile.toBuffer()
    newZipFile.writeZip(dirPath, zipFileName)
    return createdZipBuffer
  }

  static _writeFile (dirPath, filename, buffer) {
    return new Promise((resolve, reject) => {
      fs.writeFile(path.join(dirPath, filename), buffer, 'utf8', err => {
        if (err) {
          reject(err)
        } else {
          resolve()
        }
      })
    })
  }

  static _readFile (filePath) {
    return new Promise((resolve, reject) => {
      fs.readFile(filePath, (err, data) => {
        if (err) {
          reject(err)
        } else {
          resolve(data)
        }
      })
    })
  }

  static _deleteFile (filePath) {
    return new Promise((resolve, reject) => {
      fs.unlink(filePath, (err) => {
        if (err) {
          reject(err)
        } else {
          resolve(true)
        }
      })
    })
  }
}

module.exports = {
  FileSystemHelper
}
