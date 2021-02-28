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
                rtMavenDeployer (
                        id: "maven-deployer",
                        serverId: "opencollab-artifactory",
                        releaseRepo: "maven-releases",
                        snapshotRepo: "maven-snapshots"
                )
                rtMavenResolver (
                        id: "maven-resolver",
                        serverId: "opencollab-artifactory",
                        releaseRepo: "release",
                        snapshotRepo: "snapshot"
                )
                rtMavenRun (
                        pom: 'pom.xml',
                        goals: 'javadoc:jar source:jar install -T 1C -DskipTests',
                        deployerId: "maven-deployer",
                        resolverId: "maven-resolver"
                )
                rtPublishBuildInfo (
                        serverId: "opencollab-artifactory"
                )
            }
        }
    }

    post {
        always {
            deleteDir()
        }
    }
}