student = 'abilun'
job('MNTLAB-' + student + '-main-build-job') {
    scm {
        github 'MNT-Lab/mntlab-dsl'
    }
    parameters {
    	gitParam('BRANCH_NAME') {
        	type('BRANCH')    
        }
        activeChoiceReactiveParam('BUILD_TRIGGER') {
            choiceType('CHECKBOX')
            groovyScript {
                script('["MNTLAB-artyom-bilun-child1-build-job", "MNTLAB-artyom-bilun-child2-build-job", "MNTLAB-artyom-bilun-child3-build-job", "MNTLAB-artyom-bilun-child4-build-job"]')
            }
    	}
    }
        
    steps {
        downstreamParameterized {
            trigger('$BUILD_TRIGGER') {
                parameters {
                    predefinedProp('BRANCH_NAME', '$BRANCH_NAME')
                }
            }
        }
    }     
}

for (i = 1; i <5; i++) {
	job('MNTLAB-' + student + '-child' + i + '-build-job') {
    	scm {
        	github('MNT-Lab/mntlab-dsl', '$BRANCH_NAME')
        }
        parameters {
            activeChoiceReactiveParam('BRANCH_NAME') {
                    choiceType('SINGLE_SELECT')
                    groovyScript {
                        script('["abilun", "master"]')
                    }
            }
        }
        steps {
            shell('''
					cd $WORKSPACE
                    tar -czvf $BRANCH_NAME_dsl_script.tar.gz jobs.groovy
                    bash script.sh > output.txt
				'''
          	)
        }
        
        publishers {
        	archiveArtifacts('*.tar.gz')
        }
    }
}

