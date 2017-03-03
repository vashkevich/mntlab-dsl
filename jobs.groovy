def giturl = "https://github.com/MNT-Lab/mntlab-dsl.git"
def studname = "akaminski"


//create master branch
job("MNTLAB-${studname}-main-build")
{      parameters{
		activeChoiceReactiveParam('BRANCH_NAME') {
		  description('You can choose name of branch from GitHub repository')
		  choiceType('SINGLE_SELECT')
		  groovyScript {
		    script('''def command = "git ls-remote -h https://github.com/MNT-Lab/mntlab-dsl.git"
			      def proc = command.execute()
			      //def branches = proc.in.text.readLines().collect {	it.replaceAll(/[a-z0-9]*\trefs\\/heads\\//, '')}
			      def name = branches.findAll { item -> item.contains('akaminski') || item.contains('master')}
			      name.each { println it }''')
			      fallbackScript(BRANCH_NAME = "akaminski")
			      }
		  }
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
		branch("\${BRANCH_NAME}")
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
