job("MNTLAB-rvashkevich-main-build-job") {
  scm {
    github('MNT-Lab/mntlab-dsl','rvashkevich')
  }
	parameters {
		// Branches drop-down menu (main job)
		activeChoiceParam('BRANCH_NAME') {
        		description('User selection from multiple choices')
        		choiceType('SINGLE_SELECT')
        			groovyScript {
        				script('return["origin/rvashkevich", "master"]')
				}
		}
	}
		// checkbox menu (main menu)
		activeChoiceReactiveParam('BUILDS_TRIGGER') {
            		choiceType('CHECKBOX')
            		groovyScript {
                		script('return ["MNTLAB-rvashkevich-child1-build-job", "MNTLAB-rvashkevich-child2-build-job", "MNTLAB-rvashkevich-child3-build-job", "MNTLAB-rvashkevich-child4-build-job"]')
                	}          
		}   
	//check child job build status, transfer it to the mail job
  	steps {
    		downstreamParameterized {
      			trigger('$BUILDS_TRIGGER') {
        			block {
          				buildStepFailure('FAILURE')
          				failure('FAILURE')
          				unstable('UNSTABLE')
				}
			//predefined downstreams variable transfered to triggered job
        		parameters {
          		predefinedProp('BRANCH_NAME', '$BRANCH_NAME')
			}
			}
		}
	}
  // cycle for child jobs creation
  for (i in 1..4) {

    job("MNTLAB-rvashkevich-child${i}-build-job") {

      scm {
        github('MNT-Lab/mntlab-dsl','rvashkevich')
      }
    
      // Branch name choice parameter for each child job	
      parameters {
	gitParameterDefinition {
      	name('BRANCH_NAME')
      	type('BRANCH')
      	branch('rvashkevich')
      	defaultValue('rvashkevich')
      	selectedValue('DEFAULT')

      	description('')
      	branchFilter('')
      	tagFilter('')
      	sortMode('NONE')
      	useRepository('')
      	quickFilterEnabled(false)
		  }
      }    
      // Set correct format to branch, tar artifacts
      steps {
        shell(''' rm -rf *.tar.gz
		BRANCH_NAME_SHORT=$(echo $BRANCH_NAME | cut -c 8-)
		bash script.sh > output.txt
		tar -czvf ${BRANCH_NAME_SHORT}_dsl_script.tar.gz jobs.groovy script.sh
		echo $BRANCH_NAME''')
	// Archive artifacts	    
	publishers {
			archiveArtifacts('output.txt')
			archiveArtifacts('*.tar.gz')
			}
       }
      } 
  }
}
