//CREATION OF MASTER JOB
job("MNTLAB-imanzhulin-main-build-job") {
    scm {
        github('MNT-Lab/mntlab-dsl','imanzhulin')
    }
     parameters {
//THIS BLOCK IS FOR DROPDOWN CHECKOUT BRANCH!!! IT IS USED IN OTHER PART OF CODE
//THE SEQUENCE IS IMPORTANT		     
      /* activeChoiceParam('BRANCH_NAME') {
           description('Allows user choose from multiple choices')
            choiceType('SINGLE_SELECT')
            groovyScript {
                script('return["imanzhulin", "master"];')
                           }
        }
        */
	     
//THIS BLOCK IS FOR DROPDOWN ALL STUDENTS BRANCHES!!! IT IS USED IN OTHER PART OF CODE!!!
//THE SEQUENCE IS IMPORTANT	     
	/*  gitParameterDefinition {
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
*/
//DROPDOWN OF BRANCHES (my branch and master) APPEARS IN MAIN JOB		     
	     activeChoiceParam('BRANCH_NAME') {
            description('Allows user choose from multiple choices')
            choiceType('SINGLE_SELECT')
            groovyScript {
                script('return["origin/imanzhulin", "master"]')
                           }
        }
//CHECKBOX OF CHILD JOBS APPEARS IN MAIN JOB	     	     	     
        activeChoiceReactiveParam('BUILDS_TRIGGER') {
            choiceType('CHECKBOX')
            groovyScript {
                script('return ["MNTLAB-imanzhulin-child1-build-job", "MNTLAB-imanzhulin-child2-build-job", "MNTLAB-imanzhulin-child3-build-job", "MNTLAB-imanzhulin-child4-build-job"]')
                }          
           }   
     }
//IF CHILD JOBS FAIL OR ARE UNSTABLE, MAIN JOB FAILS	
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
//LOOP WHICH CREATES CHILD JOBS	
    
    for (count in 1..4) {
    
    job("MNTLAB-imanzhulin-child${count}-build-job") {
    
    scm {
        github('MNT-Lab/mntlab-dsl','imanzhulin')
    }

     parameters {
        	 
/*
activeChoiceParam('BRANCH_NAME') {
            description('Allows user choose from multiple choices')
            choiceType('SINGLE_SELECT')
            groovyScript {
                script('return["origin/imanzhulin", "master"]')
                           }
        }

*/
	   
//DROPDOWN OF ALL STUDENTS' BRANCHES APPEARS IN MAIN JOB
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


    }   
//SCRIPT WHICH RENAMES BRANCH TO THE CORRECT FORMAT
//AND ARCHIVES THE ARTIFACT AFTER DELETING UNNECESSARY	    
    
    steps {
        shell('''
BRANCH_NAME=$(echo $BRANCH_NAME | cut -c 8-)
rm -rf *.tar.gz
tar -czvf ${BRANCH_NAME}_dsl_script.tar.gz jobs.groovy script.sh
bash script.sh > output.txt ''')
        }
//Archives artifacts	    
	publishers {
			archiveArtifacts('output.txt')
			archiveArtifacts('*.tar.gz')
			}
//ANOTHER VARIATION OF SHELL EXECUTION BUT THERE IS SOME PROBLEM  
	/*shell('chmod +x script.sh')
        shell('./script.sh')    
        shell('tar cvzf ${BRANCH_NAME}_dsl_script.tar.gz jobs.groovy script.sh')
        shell('bash script.sh > output.txt')*/  
       }        
     }
    }
