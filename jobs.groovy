job("MNTLAB-rvashkevich-main-build-job") {
  scm {
    github('MNT-Lab/mntlab-dsl','rvashkevich')
  }
	parameters {
		activeChoiceParam('BRANCH_NAME') {
        		description('Allows user choose from multiple choices')
        		choiceType('SINGLE_SELECT')
        			groovyScript {
        				script('return["origin/rvashkevich", "master"]')
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

    job("MNTLAB-rvashkevich-child${i}-build-job") {

      scm {
        github('MNT-Lab/mntlab-dsl','rvashkevich')
      }
    
		
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
       

      steps {
        shell(''' rm -rf *.tar.gz
BRANCH_NAME_SHORT=$(echo $BRANCH_NAME | cut -c 8-)
bash script.sh > output.txt
tar -czvf ${BRANCH_NAME_SHORT}_dsl_script.tar.gz jobs.groovy script.sh
echo $BRANCH_NAME''')
//Archives artifacts	    
	publishers {
			archiveArtifacts('output.txt')
			archiveArtifacts('*.tar.gz')
			}
  
 /*       publishers {
     archiveArtifacts('${BRANCH_NAME}_dsl_script.tar.gz, output.txt')  
      }*/ 
      }
      } 
  }
}
