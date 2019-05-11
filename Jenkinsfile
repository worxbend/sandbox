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
        sleep(time: 3, unit: 'SECONDS')
        timestamps() {
          sleep 1
        }

      }
    }
  }
}
