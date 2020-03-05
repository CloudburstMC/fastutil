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
                sh 'mvn javadoc:aggregate javadoc:jar source:jar deploy -DskipTests'
                step([$class: 'JavadocArchiver',
                        javadocDir: 'target/site/apidocs',
                        keepAll: false])
            }
        }
    }

    post {
        always {
            deleteDir()
        }
    }
}