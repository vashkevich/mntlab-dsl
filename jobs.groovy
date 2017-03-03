def giturl = "https://github.com/MNT-Lab/mntlab-dsl.git"
def studname = "akaminski"


//create master branch
job("MNTLAB-${studname}-main-build")
{      parameters{
		gitParameterDefinition { 
		             name('BRANCH_NAME') 
		             type('BRANCH')
		              branch('origin/akaminski')
		              defaultValue('origin/akaminski')
		              selectedValue('DEFAULT')
		              description('')
		              branchFilter('')
		              tagFilter('')
		              sortMode('NONE')
		              useRepository('')
		              quickFilterEnabled(false)            }
		
		activeChoiceReactiveParam('TRIGGERED_JOB_NAMES'){
			choiceType('CHECKBOX')
			groovyScript {
				script('["MNTLAB-akaminski-child1-build-job", "MNTLAB-akaminski-child2-build-job", "MNTLAB-akaminski-child3-build-job", "MNTLAB-akaminski-child4-build-job"]')
                		}

        			}	

        	}	

		
	description ("Build main job")
      	scm {
          git{
		remote { url("${giturl}")}
		branch("\${BRANCH_NAME}")
    	    }
	    }
	publishers {
        downstreamParameterized {
            trigger('${TRIGGERED_JOB_NAMES}') {
                condition('UNSTABLE_OR_BETTER')
                parameters {predefinedProp('BRANCH_NAME', '$BRANCH_NAME') }
            }
        }
    }
}





for (number in 1..4){
  job("MNTLAB-${studname}-child${number}-build-job")
      {
      description("Builds child${number}")
      parameters { stringParam("BRANCH_NAME")}
      scm {
          git{
		remote { url("${giturl}")}
		branch("${BRANCH_NAME}")
    	    }}
	steps { shell ('''BRANCH_NAME=$(echo $BRANCH_NAME | cut -c 8-)
			 sh script.sh > output.txt
			 tar -czf ${BRANCH_NAME}_dsl_script.tar.gz script.sh jobs.groovy output.txt
			 	 ''')
    	}
	publishers{
		archiveArtifacts('*.tar.gz')	
		}
}
} 
