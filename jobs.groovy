def gitURL = "https://github.com/MNT-Lab/mntlab-dsl.git"

def myJob = freeStyleJob('MNTLAB-akutsko-main-build-job'){
	parameters {
        activeChoiceParam('BRANCH_NAME') {
            description('You can choose name of branch from GitHub repository')
            filterable()
            choiceType('SINGLE_SELECT')
            groovyScript {
                script(
			def command = "git ls-remote -h ${gitURL}"
                        def proc = command.execute()
                        proc.waitFor()              
                        if ( proc.exitValue() != 0 ) {
                                 println "Error, ${proc.err.text}"
                                 System.exit(-1)}
                                 def branches = proc.in.text.readLines().collect { 
                                 it.replaceAll(/[a-z0-9]*\trefs\/heads\//, '')}
                                 def name = branches.findAll { item -> item.contains('akutsko') || item.contains('master')}
                                 name.each { println it }

		)
                fallbackScript('"fallback choice"')
            }
        }
	activeChoiceParam('BUILD_TRIGGER') {
            description('You can choose jobs for works')
            filterable()
            choiceType('CHECK_BOXES')
            groovyScript {
                script('
			return [
			'MNTLAB-akutsko-main-build-job',
			'MNTLAB-akutsko-child1-build-job', 
			'MNTLAB-akutsko-child2-build-job',
			'MNTLAB-akutsko-child3-build-job', 
			'MNTLAB-akutsko-child4-build-job'
			]
		')
                fallbackScript('"fallback choice"')
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
