def gitURI = "MNT-Lab/mntlab-dsl"
//def BRANCH_NAME = "akutsko"

//Groovy script for main job
def myJob = freeStyleJob('MNTLAB-akutsko-main-build-job'){
def BRANCH_NAME = "akutsko"
	parameters {
        activeChoiceParam('BRANCH_NAME') {
            description('You can choose name of branch from GitHub repository')
            filterable()
            choiceType('SINGLE_SELECT')
            groovyScript {
                script("""
		def command = "git ls-remote -h https://github.com/MNT-Lab/mntlab-dsl.git"
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
        github("${gitURI}", "${BRANCH_NAME}")
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
              predefinedProp('BRANCH_NAME', '${BRANCH_NAME}')
            }
          }
        }
      }
    
      triggers {
        scm 'H/5 * * * *'
      }
}
println "main job was created"
  for (number in 1..4){
    job("MNTLAB-akutsko-child${number}-build-job") {
      description("Builds child${number}")
      
		parameters {
        activeChoiceParam('BRANCH_NAME') {
            description('You can choose name of branch from GitHub repository')
            filterable()
            choiceType('SINGLE_SELECT')
            groovyScript {
                script("""
                def command = "git ls-remote -h https://github.com/MNT-Lab/mntlab-dsl.git"
                def proc = command.execute()

                def branches = proc.in.text.readLines().collect { 
                it.replaceAll(/[a-z0-9]*\trefs\\/heads\\//, '')}

                branches.each { println it }
                """)
}
}
}
// scm git
      scm {
        github("${gitURI}", "${BRANCH_NAME}")
      }
// build step
        steps {
          shell("""
		cat script.sh > output.txt
		tar -czvf ${BRANCH_NAME}_dsl_script.tar.gz jobs.groovy script.sh
	  """)
        }
	}
    }    
