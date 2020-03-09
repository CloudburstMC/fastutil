pipeline {
    agent any
    tools {
        maven 'Maven 3'
        jdk 'Java 8'
    }
    stages {
        stage ('Build') {
            steps {
                sh 'chmod +x makeSources.sh'
                sh './makeSources.sh'
                sh 'mvn -T 1C clean package'
            }
        }

        stage ('Deploy') {
            when {
                branch 'master';
            }
            steps {
                sh 'mvn -T 1C source:jar deploy -DskipTests'
            }
        }
    }

    post {
        always {
            deleteDir()
        }
    }
}