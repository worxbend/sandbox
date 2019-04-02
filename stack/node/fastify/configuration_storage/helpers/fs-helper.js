'use strict'
const fse = require('fs-extra')
const path = require('path')
const AdmZip = require('adm-zip')

class FileSystemHelper {
  /**
   * Write file.
   * @param {String} dirPath Absolute destination path.
   * @param {String} filename  File name with extension.
   * @param {Buffer} data  Data for writing.
   * @returns Object which contains file metadata.
   */
  static async writeFile(dirPath, filename, data) {
    await FileSystemHelper._writeFile(dirPath, filename, data)
  }

  static async writeZipFile(dirPath, zipFileName, {
    innerFileName,
    buffer
  }) {
    let newZipFile = new AdmZip()
    newZipFile.addFile(innerFileName, buffer)
    let createdZipBuffer = newZipFile.toBuffer()
    newZipFile.writeZip(dirPath, zipFileName)
    return createdZipBuffer
  }

  static _writeFile(dirPath, filename, buffer) {
    return new Promise((resolve, reject) => {
      fse.outputJSON()
    })
  }

  static _readFile(filePath) {
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

  static _deleteFile(filePath) {
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

  static _deleteDirectory(dirPath) {
    return new Promise((resolve, reject) => {
      fs.
    })
  }
}

module.exports = {
  FileSystemHelper
}