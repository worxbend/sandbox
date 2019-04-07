pipeline {
  agent none
  stages {
    stage('Test::printMessage') {
      steps {
        echo 'Finished!!!'
      }
    }
    stage('Exit') {
      steps {
        sleep(time: 3, unit: 'MINUTES')
        timestamps() {
          sleep 20
        }

      }
    }
  }
}