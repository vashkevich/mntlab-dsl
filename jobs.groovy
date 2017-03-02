job('MNTLAB-mburakouski-main-build-job') {
    description 'Build main job.'
    scm {
        github 'MNT-Lab/mntlab-dsl'
        }
    parameters {
        activeChoiceReactiveParam('BRANCH_NAME') {
            choiceType('CHECKBOX')
            groovyScript {
                script('["choice1", "choice2"]')
                fallbackScript('"fallback choice"')
            }
         }
    }

job('MNTLAB-mburakouski-child1-build-job') {
    description 'Build child job.'
    scm {
        github 'MNT-Lab/mntlab-dsl'
        }
    
    parameters {
        activeChoiceReactiveParam('BRANCH_NAME') {
            choiceType('SINGLE_SELECT')
            groovyScript {
                script('["choice1", "choice2"]')
                fallbackScript('"fallback choice"')
            }
            referencedParameter('BOOLEAN-PARAM-1')
            referencedParameter('BOOLEAN-PARAM-2')
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




