def gitURL = "https://github.com/MNT-Lab/mntlab-dsl.git"

//Groovy script for main job
def myJob = freeStyleJob('MNTLAB-akutsko-main-build-job'){
	parameters {
        activeChoiceParam('BRANCH_NAME') {
            description('You can choose name of branch from GitHub repository')
            filterable()
            choiceType('SINGLE_SELECT')
            groovyScript {
                script("""
        	def gitURL = "https://github.com/MNT-Lab/mntlab-dsl.git"
		def command = "git ls-remote -h $gitURL"
		def proc = command.execute()

		def branches = proc.in.text.readLines().collect { 
        	it.replaceAll(/[a-z0-9]*\trefs\\/heads\\//, '')}
		def name = branches.findAll { item -> item.contains('akutsko') || item.contains('master')}

		name.each { println it }
		""")
}
	activeChoiceParam('BUILD_TRIGGER') {
            description('You can choose jobs for works')
            filterable()
            choiceType('CHECKBOX')
            groovyScript {
                script("""
			return["MNTLAB-akutsko-child1-build-job", 
			"MNTLAB-akutsko-child2-build-job", 
			"MNTLAB-akutsko-child3-build-job", 
			"MNTLAB-akutsko-child4-build-job"]
		""")
            }
        }


    }
}

// scm git url, branch
      scm {
        github("MNT-Lab/mntlab-dsl", "akutsko")
      }

// build step
      steps {
        downstreamParameterized {
          trigger('$BUILD_TRIGGER') {
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
    
      triggers {
        scm 'H/5 * * * *'
      }

for (number in 1..4){
    def BRANCH_NAME = ${BRANCH_NAME}
    job("MNTLAB-akutsko-child${number}-build-job") {
      description("Builds child${number}")
      
      // scm git
      scm {
        github("MNT-Lab/mntlab-dsl", "${BRANCH_NAME}")
      }
      
      parameters {stringParam('BUILD_TRIGGER', '')}
      parameters {stringParam('BRANCH_NAME', '')}

      // build step
        steps {
          shell("""
                cat script.sh >> output.txt
		tar -czvf ${BRANCH_NAME}_dsl_script.tar.gz jobs.groovy script.sh >> output.txt
	  """)
        }
	}
    }
    
}
