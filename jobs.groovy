job("MNTLAB-hvysotski-main-build-job") {
    scm {
         github ('MNT-Lab/mntlab-dsl', '*/${BRANCH_NAME}')
    }
    triggers {
        scm 'H * * * *'
    }
     parameters {
        choiceParam('BRANCH_NAME', ['hvysotski', 'master'])
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
    
    job("MNTLAB-hvysotski-child${i}-build-job") {
    
    scm {
         github ('MNT-Lab/mntlab-dsl', '*/${BRANCH_NAME}')
    }
    triggers {
        scm 'H * * * *'
    }
     parameters {
        choiceParam('BRANCH_NAME', ['hvysotski', 'master'])
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
