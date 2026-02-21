pipeline {
  agent any

  tools {
    jdk 'jdk17'
    maven 'maven3'
    nodejs 'node18'
  }

  stages {
    stage('Backend Build') {
      steps {
        sh 'mvn -B -DskipTests clean package'
      }
    }

    stage('Frontend Build') {
      steps {
        dir('frontend') {
          sh 'npm ci'
          sh 'npm run build'
        }
      }
    }

    stage('Backend Tests') {
      steps {
        sh 'mvn -B test'
      }
    }
  }

  post {
    always {
      archiveArtifacts artifacts: '**/target/*.jar, frontend/dist/**', allowEmptyArchive: true
      junit allowEmptyResults: true, testResults: '**/surefire-reports/*.xml'
    }
  }
}
