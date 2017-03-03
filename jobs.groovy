/* Jenkins. Task9. 
DSL script for automated creation Jenkins jobs: 1 main and 4 child. */

// Creating main job
job("MNTLAB-mkuzniatsou-main-build") {
  	
// Getting project from Github	
    scm {
        github('MNT-Lab/mntlab-dsl', '$BRANCH_NAME')
	}

	parameters {

// Setting predefine string parameter BRANCH_NAME which has two options: {student} (by default) and master	
        activeChoiceParam('BRANCH_NAME')
			{
                description('Allows to choose branch from repository')
                choiceType('SINGLE_SELECT')
                groovyScript
                {
                script('["mkuzniatsou", "master"]')
                }
            }
			
// Choosing jobs is executed by checkboxes 			
		activeChoiceParam('jobs') 
			{
				choiceType('CHECKBOX')
				groovyScript {
				script ('''
                    			def jobsArray = []
                    			for (i = 1; i < 5; i++){ 
						jobsArray.push("MNTLAB-mkuzniatsou-child${i}-build-job")
                    			}
                    			return jobsArray''')
				}
			}
		}

//Creating a trigger for launching 4 child jobs.	
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


// Creating 4 child jobs
for (i = 1; i < 5; i++) {
job("MNTLAB-mkuzniatsou-child${i}-build-job") {

	scm {
		github 'MNT-Lab/mntlab-dsl','${BRANCH_NAME}'
	}
	
// Setting parameter BRANCH_NAME with list of all branches available in repo.
        parameters{
			activeChoiceReactiveParam('BRANCH_NAME') {
				choiceType('SINGLE_SELECT')
				groovyScript {

					script('''
					def gitURL = "https://github.com/MNT-Lab/mntlab-dsl.git"
					def command = "git ls-remote -h $gitURL"

					def proc = command.execute()
					proc.waitFor()              

					if ( proc.exitValue() != 0 ) {
					   println "Error, ${proc.err.text}"
					}

					def branches = proc.in.text.readLines().collect { 
						it.replaceAll(/[a-z0-9]*\trefs\\/heads\\//, '')
					}
					return branches''')	
					
					fallbackScript('''
					BRANCH_NAME = "mkuzniatsou"''')					
				}
			}
		}

		
        steps {
 
/* Removing old artifacts
Executing the cloned ‘script.sh’ from branch propagated by main job. Saving an output to the file ‘output.txt’
Creating artifact
*/
        shell('''
		rm -rf *.tar.gz
		chmod +x script.sh
        ./script.sh > output.txt
		tar -czf ${BRANCH_NAME}_dsl_script.tar.gz jobs.groovy script.sh'''
		)

// Archiving artifacts
		publishers {
				archiveArtifacts('output.txt')
				archiveArtifacts('*.tar.gz')
        }       
        }
    }
}
