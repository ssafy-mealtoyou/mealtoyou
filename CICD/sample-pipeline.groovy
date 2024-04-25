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
                    dir('BE/sample') {
                        sh 'bash gradlew build -x test'
                        sh 'cp -r build/libs/ ../../../sample-server/'
                    }
                    dir('../sample-server') {
                        sh 'docker stop sample-server || true'
                        sh 'docker rmi -f sample-server:latest'
                        sh 'docker rm -f sample-server'
                        sh 'docker build -t sample-server .'
                        sh "docker run -d --name sample-server --network=infra sample-server"
                    }
                }
            }
        }
    }
}
