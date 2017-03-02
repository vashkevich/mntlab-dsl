
	job("MNTLAB-aslesarenka-main-build-job") {
   // scm {
      //  github 'MNT-Lab/mntlab-dsl'
   // }
scm {
        github("${giturl}", "${studname}")
      }
     parameters {
        choiceParam('BRANCH_NAME', ['aslesarenka', 'master'])
    }
    
     parameters {
        activeChoiceReactiveParam('BUILDS_TRIGGER') {
            choiceType('CHECKBOX')
            groovyScript {
                script('return ["MNTLAB-aslesarenka-child1-build-job", "MNTLAB-aslesarenka-child2-build-job", "MNTLAB-aslesarenka-child3-build-job", "MNTLAB-aslesarenka-child4-build-job"]'
                      )
                }          
           }   
        }
		
    scm {
        github("${giturl}", "${studname}")
      }
    steps {
	   downstreamParameterized {
			trigger('${BUILDS_TRIGGER}') {
				//condition('UNSTABLE_OR_BETTER')
				parameters {
					predefinedProp('BRANCH_NAME', '$BRANCH_NAME')
				}
			}
		}
	}
    
    for (i in 1..4) {
    
    job("MNTLAB-aslesarenka-child${i}-build-job") {
    
    scm {
        github 'MNT-Lab/mntlab-dsl'
    }

     parameters {
        choiceParam('BRANCH_NAME', ['aslesarenka', 'master'])
    }   
    
    steps {
         shell('chmod +x script.sh')
        shell('./script.sh')    
        shell('tar cvzf ${BRANCH_NAME}_dsl_script.tar.gz jobs.groovy script.sh')
        shell('bash script.sh > output.txt')  
       }        
     }
    }
  } 


  
