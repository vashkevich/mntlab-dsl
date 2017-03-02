//Creating main build
job("MNTLAB-mkuzniatsou-main-build") {
  	
	
    scm {
        github('MNT-Lab/mntlab-dsl', '$BRANCH_NAME')
    }
	
    parameters {
		gitParam('BRANCH_NAME') {
            type('BRANCH')
            defaultValue('origin/mkuzniatsou')
        }
		
    activeChoiceParam('jobs') {
            choiceType('CHECKBOX')
            groovyScript {
                script('return ["MNTLAB-mkuzniatsou-child1-build-job", "MNTLAB-mkuzniatsou-child2-build-job", "MNTLAB-mkuzniatsou-child3-build-job", "MNTLAB-mkuzniatsou-child4-build-job"]')
            }
    	}
    }

	
	steps {
        downstreamParameterized {
            trigger('$jobs') {
                block {
                    buildStepFailure('FAILURE')
                    failure('FAILURE')
                    unstable('UNSTABLE')
                }
                parameters {
                    predefinedProp('BRANCH_NAME', '$BRANCH_NAME')
                }
            }            
        }
    }
}



for (i in 1..4) {
  job("MNTLAB-mkuzniatsou-child${i}-build-job") {

	scm {
		github 'MNT-Lab/mntlab-dsl','${BRANCH_NAME}'
	}
	
	parameters{
        activeChoiceReactiveParam('BRANCH_NAME') {
            choiceType('SINGLE_SELECT')
            groovyScript {
                script('["mkuzniatsou", "master"]')
            }
        }
    }




	steps {
	shell('''
	rm -rf *.tar.gz
	chmod +x script.sh
        ./script.sh > output.txt
	BRANCH_NAME=$(echo $BRANCH_NAME | cut -c 8-)
        tar -cfv ${BRANCH_NAME}_dsl_script.tar.gz jobs.groovy script.sh'''
	)

	publishers {
        archiveArtifacts('output.txt')
        archiveArtifacts('*.tar.gz')
			}
		}
	}
}
