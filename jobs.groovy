job("MNTLAB-imanzhulin-main-build-job") {
    scm {
        github('MNT-Lab/mntlab-dsl','imanzhulin')
    }
     parameters {
      /* activeChoiceParam('BRANCH_NAME') {
           description('Allows user choose from multiple choices')
            choiceType('SINGLE_SELECT')
            groovyScript {
                script('return["imanzhulin", "master"];')
                           }
        }
        */
	  gitParameterDefinition {
              name('BRANCH_NAME')
              type('BRANCH')
              branch('imanzhulin')
              defaultValue('imanzhulin')
              selectedValue('DEFAULT')

              description('')
              branchFilter('')
              tagFilter('')
              sortMode('NONE')
              useRepository('')
              quickFilterEnabled(false)
            }
	     
	     
	     
	     
	     
        activeChoiceReactiveParam('BUILDS_TRIGGER') {
            choiceType('CHECKBOX')
            groovyScript {
                script('return ["MNTLAB-imanzhulin-child1-build-job", "MNTLAB-imanzhulin-child2-build-job", "MNTLAB-imanzhulin-child3-build-job", "MNTLAB-imanzhulin-child4-build-job"]')
                }          
           }   
     }
    steps {
	   downstreamParameterized {
			trigger('$BUILDS_TRIGGER') {
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
    
    for (i in 1..4) {
    
    job("MNTLAB-imanzhulin-child${i}-build-job") {
    
    scm {
        github('MNT-Lab/mntlab-dsl','imanzhulin')
    }

     parameters {
        	 activeChoiceParam('BRANCH_NAME') {
            description('Allows user choose from multiple choices')
            choiceType('SINGLE_SELECT')
            groovyScript {
                script('return["origin/imanzhulin", "master"]')
                           }
        }
    }   
    
    steps {
        shell('''
BRANCH_NAME=$(echo $BRANCH_NAME | cut -c 8-)
tar -czvf ${BRANCH_NAME}_dsl_script.tar.gz jobs.groovy script.sh
bash script.sh > output.txt ''')
        }
  
	/*shell('chmod +x script.sh')
        shell('./script.sh')    
        shell('tar cvzf ${BRANCH_NAME}_dsl_script.tar.gz jobs.groovy script.sh')
        shell('bash script.sh > output.txt')*/  
       }        
     }
    }
