pipeline{
    agent any
    tools{
      maven 'Maven 3.3.9'
      jdk 'jdk17'
    }

    stages {
        stage('Build'){
            steps {
                sh "mvn clean install -DskipTests"
            }
        }
        stage('Test'){
            steps{
                sh "mvn test"
            }
        }
        stage('Deploy') {
            steps {
                sh "mvn jar:jar deploy:deploy"
            }
        }
    }

}