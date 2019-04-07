pipeline {
  agent none
  stages {
    stage('Test::printMessage') {
      parallel {
        stage('Test::printMessage') {
          steps {
            echo 'Finished!!!'
          }
        }
        stage('Create workspace') {
          steps {
            ws(dir: 'kzonix') {
              sh 'touch project_info.json'
            }

            node(label: 'kzonix') {
              fileExists 'project_info.json'
            }

          }
        }
      }
    }
  }
}