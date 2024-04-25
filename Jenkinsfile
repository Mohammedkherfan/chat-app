@Library('jenkins-shared-libraries@openjdk17-alpine')_
pipeline{
  agent { label 'CI'}
  environment {
    CI_SVC_URL = 'https://localhost:8080/'
    APP = 'chat-app'
    Test_Name = 'chat-app-service'
    mvn_extra = "-Dspring.profiles.active=local"
    profile_STG_Name = 'dev'
    openJDK_Version = 17
  }

  stages {
    stage('test: Integration test'){
      steps {
        CompileIntegrationTestsWithJDKVersionOption(mvn_extra, openJDK_Version)
      }
    }
    stage ('Next level ENV deployments') {
      when {
        expression {
          return env.GIT_BRANCH == "main"
        }
      }
      stages {
          stage ('deploy to dev'){
              steps {
                DeployToSTGWithJDKVersionOption(APP,env.BUILD_NUMBER,NAMESPACE, openJDK_Version)
              }
          }
          stage ('test: Functional and Security on dev') {
                parallel {
                  stage ('Functional testing') {
                    steps {
                      FunctionalTest(Test_Name)
                    }
                  }
                  stage ('Web End2End') {
                    steps {
                     EndToEndTest(webEnd2End,profile_STG_Name)
                    }
                  }
                }
          }
    }
  }
  post {
      always {
      sh 'docker stop  db_${port} || echo "done"'
    }
    failure {
      SlackNotifier('FAILURE',env.JOB_NAME,env.BUILD_URL)
      }
  }
  
}
