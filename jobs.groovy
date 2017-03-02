job("MNTLAB-mburakouski-main-build-job") {
    scm {
        github 'MNT-Lab/mntlab-dsl'
    }
    triggers {
        scm 'H * * * *'
    }
     parameters {
        choiceParam('BRANCH_NAME', ['mburakouski', 'master'])
    }
    
     parameters {
        activeChoiceReactiveParam('BUILDS_TRIGGER') {
            choiceType('CHECKBOX')
            groovyScript {
                script('return ["MNTLAB-mburakouski-child1-build-job", "MNTLAB-mburakouski-child2-build-job", "MNTLAB-mburakouski-child3-build-job", "MNTLAB-mburakouski-child4-build-job"]'
                      )
                }          
           }   
        }
    publishers {
	   downstreamParameterized {
			trigger('${BUILDS_TRIGGER}') {
				condition('UNSTABLE_OR_BETTER')
				parameters {
					predefinedProp('BRANCH_NAME', '$BRANCH_NAME')
				}
			}
		}
	}
    
    for (i in 1..4) {
    
    job("MNTLAB-mburakouski-child${i}-build-job") {
    
    scm {
        github 'MNT-Lab/mntlab-dsl'
    }
    triggers {
        scm 'H * * * *'
    }
     parameters {
        choiceParam('BRANCH_NAME', ['mburakouski', 'master'])
    }   
    
    steps {
        shell('chmod +x script.sh')
        shell('./script.sh')
        shell('tar cvzf ${BRANCH_NAME}_dsl_script.tar.gz jobs.groovy script.sh')
        shell('touch output.txt')   
       }
     }
    }
  } 
