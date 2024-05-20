pipeline {
    agent any
    tools {
        gradle "gradle"
    }

    options {
        disableConcurrentBuilds()
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'develop', credentialsId: 'test', url: 'https://lab.ssafy.com/s10-final/S10P31A102.git'
                script {
                    def branchName = sh(script: "git rev-parse --abbrev-ref HEAD", returnStdout: true).trim()
                    if (branchName != 'develop') {
                        echo "This job only runs on the master branch. Current branch is ${branchName}. Aborting the build."
                        currentBuild.result = 'ABORTED'
                        return
                    }
                }
            }
        }

        stage('Build and Deploy') {
            steps {
                script {
                    dir('BE/food-service') {
                        sh 'bash gradlew build -x test'
                        sh 'cp -r build/libs/ ../../../food-server/'
                    }
                    dir('../food-server') {
                        sh 'docker stop food-server || true'
                        sh 'docker rmi -f food-server:latest'
                        sh 'docker rm -f food-server'
                        sh 'docker build -t food-server .'
                        sh "docker run -d --name food-server --network=infra food-server"
                    }
                }
            }
        }
    }
}
