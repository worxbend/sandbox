properties([
  buildDiscarder(
    logRotator(
      artifactDaysToKeepStr: '2', 
      artifactNumToKeepStr: '2', 
      daysToKeepStr: '2', 
      numToKeepStr: '2',
    ),
  ),
])
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
