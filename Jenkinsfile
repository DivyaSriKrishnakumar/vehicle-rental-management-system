pipeline {
    agent any

    tools {
        maven 'Maven3'
    }

    stages {

        stage('Build') {
            steps {
                bat 'mvn clean package'
            }
        }

        stage('Build Docker Image') {
            steps {
                bat 'docker build -t vehiclerental-app .'
            }
        }

        stage('Run Docker Container') {
            steps {
                bat 'docker stop vehiclerental-container || exit 0'
                bat 'docker rm vehiclerental-container || exit 0'
                bat 'docker run -d --name vehiclerental-container -p 9090:8080 vehiclerental-app'
            }
        }
    }

    post {
        success {
            echo 'CI/CD Pipeline executed successfully!'
        }

        failure {
            echo 'Pipeline failed!'
        }
    }
}