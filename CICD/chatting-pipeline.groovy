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
                    dir('BE/chatting-service') {
                        sh 'bash gradlew build -x test'
                        sh 'cp -r build/libs/ ../../../chatting-server/'
                    }
                    dir('../chatting-server') {
                        sh 'docker stop chatting-server || true'
                        sh 'docker rmi -f chatting-server:latest'
                        sh 'docker rm -f chatting-server'
                        sh 'docker build -t chatting-server .'
                        sh "docker run -d --name chatting-server --network=infra chatting-server"
                    }
                }
            }
        }
    }
}
