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
		def branches = proc.in.text.readLines()

		if (branches.findAll { item -> item.contains('akutsko') } )
		{return "akutsko, master"}
		else 
		{return "master"}
		""")
            }
        }
	activeChoiceParam('BUILD_TRIGGER') {
            description('You can choose jobs for works')
            filterable()
            choiceType('CHECKBOX')
            groovyScript {
                script("""
			return["MNTLAB-akutsko-main-build-job", 
			"MNTLAB-akutsko-child1-build-job", 
			"MNTLAB-akutsko-child2-build-job", 
			"MNTLAB-akutsko-child3-build-job", 
			"MNTLAB-akutsko-child4-build-job"]
		""")
            }
        }


    }

	publishers {
        buildPipelineTrigger('${BUILD_TRIGGER}') {
            parameters {
                predefinedProp('BRANCH_NAME', '$BRANCH_NAME')
            }
        }
    }

}
