job("MNTLAB-mburakouski-main-build-job") {
    description 'Building one main job and four child'
    scm {
        github 'MNT-Lab/mntlab-dsl'
    }
    triggers {
        scm 'H * * * *'
    }
     parameters {
        choiceParam('BRANCH_NAME', ['hvysotski (default)', 'master'])
    }
    
     parameters {
        activeChoiceReactiveParam('BUILDS_TRIGGER') {
            choiceType('CHECKBOX')
            groovyScript {
                script('return ["MNTLAB-hvysotski-child1-build-job", "MNTLAB-hvysotski-child2-build-job", "MNTLAB-hvysotski-child3-build-job", "MNTLAB-hvysotski-child4-build-job"]'
                      )
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

