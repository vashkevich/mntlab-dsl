def gitURL = "https://github.com/MNT-Lab/mntlab-dsl.git"

def job_names="""
["MNTLAB-akutsko-main-build-job", 
"MNTLAB-akutsko-child1-build-job, 
"MNTLAB-akutsko-child2-build-job, 
"MNTLAB-akutsko-child3-build-job, 
"MNTLAB-akutsko-child4-build-job]
"""

//Groovy script for 
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

//Groovy script for main job
def myJob = freeStyleJob('MNTLAB-akutsko-main-build-job'){
	parameters {
        activeChoiceParam('BRANCH_NAME') {
            description('You can choose name of branch from GitHub repository')
            filterable()
            choiceType('SINGLE_SELECT')
            groovyScript {
                script('["drhr", "srgys"]')
                fallbackScript('"fallback choice"')
            }
        }
	activeChoiceParam('BUILD_TRIGGER') {
            description('You can choose jobs for works')
            filterable()
            choiceType('CHECKBOX')
            groovyScript {
                script('${job_names}')
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
