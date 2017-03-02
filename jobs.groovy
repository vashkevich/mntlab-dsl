job('MNTLAB-mburakouski-main-build-job') {
    description 'Build main job.'
    scm {
        github 'MNT-Lab/mntlab-dsl'
        }
    parameters {
        activeChoiceReactiveParam('BRANCH_NAME') {
            choiceType('CHECKBOX')
            groovyScript {
                script('["MNTLAB-mburakouski-child1-build-job", "MNTLAB-mburakouski-child2-build-job", "MNTLAB-mburakouski-child1-build-job", "MNTLAB-mburakouski-child2-build-job"]')
              }
         }
    }

    for  (i in 1..4){
    
    job("MNTLAB-mburakouski-child${i}-build-job") {
    description 'Build and test the app.'
    scm {
        github 'MNT-Lab/mntlab-dsl'
        }
    parameters {
       activeChoiceReactiveParam('BRANCH_NAME') {
          choiceType('SINGLE_SELECT')
          groovyScript {
            script('["choice1", "choice2"]')
          }
        }
    }
}
    
    triggers {
        scm('* * * * *')
    }
    steps {
        shell('chmod +x script.sh')
        shell('./script.sh')
		shell('tar cvzf ${BRANCH_NAME}_dsl_script.tar.gz jobs.groovy script.sh')
        shell('touch output.txt')
        shell('cat $JENKINS_HOME/jobs/$JOB_NAME/builds/lastSuccessfulBuild/log > output.txt')
    }
    publishers {
        archiveArtifacts('${BRANCH_NAME}_dsl_script.tar.gz')
    }
}

}

