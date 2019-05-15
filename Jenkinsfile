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
        sleep(time: 20, unit: 'SECONDS')
        timestamps() {
          sleep(time: 1000, unit: 'MILLISECONDS')
        }

        echo 'Finish'
      }
    }
  }
}