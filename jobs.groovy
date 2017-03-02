job("MNTLAB-mburakouski-main-build-job") {
    triggers {
        scm '* * * * *'
   	     }
     parameters {
        choiceParam('BRANCH_NAME', ['mburakouski (default)', 'master'])
                }
    parameters {
        activeChoiceReactiveParam('job') {
            choiceType('CHECKBOX')
            groovyScript {
                script('return ["MNTLAB-mburakouski-child1-build-job", "MNTLAB-mburakouski-child2-build-job", "MNTLAB-mburakouski-child3-build-job", "MNTLAB-mburakouski-child4-build-job"]'
                      )
                }          
           }   
        }
}
    

for  (i in 1..4){
    
    job("MNTLAB-mburakouski-child${i}-build-job") {
    description 'Build and test the app.'
    scm {
        github 'MNT-Lab/mntlab-dsl'
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
