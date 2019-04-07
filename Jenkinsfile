pipeline {
  agent none
  stages {
    stage('error') {
      steps {
        waitUntil() {
          echo 'Hello world'
        }

        timestamps() {
          echo 'Finish'
        }

      }
    }
  }
}