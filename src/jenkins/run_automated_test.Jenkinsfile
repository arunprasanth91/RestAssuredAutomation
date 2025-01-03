// stage run - we checkout code and run packs
// stage copy reports - we compress output reports
// stage download from artifactory - we download prev results
// stage upload to artifactory - we upload current reports
// stage compare results - we compare prev vs current results
// stage send email - we publish the result of comparison.


def env
def runConfig
def test

echo "please input and trigger in 600 mins"
    timeout(time: 600, unit: "MINUTES") {
        pipeline_parameters = input(
            id: 'pipeline_parameters',
            message: 'please select/enter values for following parameters',
            parameters: [
                        choice(defaultValue: '', choices: 'SIT \n UAT', description:'choose env to run', name:'test_env'),
                        string(defaultValue: '', description: 'Enter run config', name: 'run_config')
                        choice(defaultValue: '', choices: 'JiraAPITest \n LibraryAPIAPITest \n SampleTest', description:'choose test class name',
                        name:'test_class')
                        ])
    }

node('linux') {
    def mvnHome
    def jdkHome

    stage('Run') {
    startTime = getTime();

    echo "======================= start time # ${startTime} =========================="
    echo "========================== Jenkins execution is in progress pls check logging.txt for full output ========="
    env = pipeline_parameters['test_env']
    runConfig = pipeline_parameters['run_config']
    test = pipeline_parameters['test_class']
    cleanWs()
    checkoutRegressionCode()

    jdkHome = installTool "jdk1.8.0_91"
    mvnHome = installTool "apache-maven-3.5.2"

    withEnv(["JAVA_HOME=$jdkHome", "MVN_HOME=$mvnHome"]) {

    if(test.contains('SampleTest')){
        sh "${MVN_HOME}/bin/mvn test -Dtest=${test} -Pgeneric-runner -Dmaven.test.failure.ignore ${env} ${runConfig} & >> logging.txt"
    }
    else if(test.contains("JiraAPITest")) {
        // say we need to login to server to fetch any value to pass as parameter to test
       def server_param;
        withCredentials(bindings: [sshUserPrivateKey(credentialsId: "${KEY_NAME}", keyFileVariable: "JENKINS_KEY")]) {
            def propertyName = "server_param"
            def sshCommand = "ssh -i ${JENKINS_KEY} ${server_IP}"
            def configPath = "/data/personal/properties/api.server.properties"
            def currentProperty = sh([script: "$(sshCommand) 'grep '^${propertyName}=' ${configPath}'", returnStdout: true]).tokenize("\n")
            server_param = currentProperty[0].split('@')[1]
                }
        sh "${MVN_HOME}/bin/mvn test -Dtest=${test} -Pgeneric-runner -Dmaven.test.failure.ignore ${env} ${runConfig} ${server_param} & >> logging.txt"
            }
        } else {
                sh "${MVN_HOME}/bin/mvn test -Dtest=${test} -Pgeneric-runner -Dmaven.test.failure.ignore ${env} & >> logging.txt"
        }

    }
}

def checkoutRegressionCode(){
checkout([$class: 'GitSCM', branches : [[name: 'main']], doGenerateSubmoduleConfiguration: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId:
                                                    'pipeline-readonly', url: 'https://jenkins.com/scm/automation/restAssured_Automation.git']]])

}