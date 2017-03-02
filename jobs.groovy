def gitURL = "https://github.com/MNT-Lab/mntlab-dsl.git"

//Groovy script for 
//def command = "git ls-remote -h ${gitURL}"
//def proc = command.execute()
//proc.waitFor()              
//if ( proc.exitValue() != 0 ) {
//println "Error, ${proc.err.text}"
//System.exit(-1)}
//def branches = proc.in.text.readLines().collect { 
//it.replaceAll(/[a-z0-9]*\trefs\/heads\//, '')}
//def branche = branches.findAll { item -> item.contains('akutsko') || item.contains('master')}
//branche.each { println it }

//Groovy script for main job
def myJob = freeStyleJob('MNTLAB-akutsko-main-build-job'){
	parameters {
        activeChoiceParam('BRANCH_NAME') {
            description('You can choose name of branch from GitHub repository')
            filterable()
            choiceType('SINGLE_SELECT')
            groovyScript {
                script("""
//Groovy script for 
def command = "git ls-remote -h ${gitURL}"
def proc = command.execute()
proc.waitFor()
if ( proc.exitValue() != 0 ) {
println "Error, ${proc.err.text}"
System.exit(-1)}
def branches = proc.in.text.readLines().collect {
it.replaceAll(/[a-z0-9]*\trefs\/heads\//, '')}
def branche = branches.findAll { item -> item.contains('akutsko') || item.contains('master')}
branche.each { println it }
return branche
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
