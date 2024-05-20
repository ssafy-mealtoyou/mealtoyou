pipeline {
    agent any

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
                    dir('./AI') {
                        sh 'pwd'
                        sh 'docker stop ai-service || true'
                        sh 'docker rmi -f ai-service:latest'
                        sh 'docker rm -f ai-service'
                        sh 'docker build -t ai-service .'
                        sh "docker run -d --name ai-service --env-file .deploy --network=infra ai-service"
                    }
                }
            }
        }
    }
}
