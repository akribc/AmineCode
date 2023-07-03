pipeline {
    agent any
    options {
        buildDiscarder logRotator( 
                    daysToKeepStr: '7', 
                    numToKeepStr: '10'
            )
    }
    stages {
        stage('Cleanup Workspace') {
            steps {
                cleanWs()
                sh """
                echo "Cleaned Up Workspace For Project when build is done"
                """
                sh("printenv")
            }
        }
        stage('Build') {
            when {
                allOf{
                    expression {env.CHANGE_BRANCH != 'master'}
                    expression {env.CHANGE_BRANCH != 'release/standard'}
                }
            }
            steps {
                git credentialsId: "adria_batch_livraison_credentials", branch: "${env.CHANGE_BRANCH}", url: 'https://ADIB_Amine@bitbucket.org/adib_amine/test-project.git'
            }
        }
        stage('SonarQube analysis') {
            when {
                allOf{
                    expression {env.CHANGE_BRANCH != 'master'}
                    expression {env.CHANGE_BRANCH != 'release/standard'}
                }
            }
            steps {
                withSonarQubeEnv("SonarCloud") {
                    sh "mvn -Dsonar.branch.name=${env.CHANGE_BRANCH} verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar -Dsonar.projectKey=adib_amine_test-project"
                }
            }
        }  
    }   
    post {
        success{
        script{
            try{
                bitbucketPullRequestBuilder(
                    credentialsId: "Bitbucket_Oauth",
                    actionType: "approve",
                    pullRequestId: env.CHANGE_ID,
                    pullRequestLink: env.CHANGE_URL,  
                )
            }catch(Exception e){
                echo e.toString() + "because is aleardy approved"
            }
        }
        }
        failure{
            bitbucketPullRequestBuilder(
                credentialsId: "Bitbucket_Oauth",
                actionType: "decline",
                pullRequestId: env.CHANGE_ID,
                pullRequestLink: env.CHANGE_URL,
                message : 'could not approve your PR'       
            )
        }
    }
}