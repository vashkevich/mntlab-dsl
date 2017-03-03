def giturl = 'https://github.com/MNT-Lab/mntlab-dsl.git'def student = 'sivanchic'job("MNTLAB-${student}-main-build-job") {	parameters{		activeChoiceParam('BRANCH_NAME') {			description('branch name')			choiceType('SINGLE_SELECT')			groovyScript {				script('''					def command = "git ls-remote -h https://github.com/MNT-Lab/mntlab-dsl.git"					def proc = command.execute()					def branches = proc.in.text.readLines().collect { it.replaceAll(/[a-z0-9]*\trefs\\/heads\\//, '') }					def name = branches.findAll { item -> item.contains('sivanchic') || item.contains('master') }					name.each { println it }				''')				fallbackScript(BRANCH_NAME = "sivanchic")			}		}		activeChoiceParam('TRIGGERED_JOB_NAMES') {			choiceType('CHECKBOX')			groovyScript {				script('["MNTLAB-${student}-child1-build-job", "MNTLAB-${student}-child2-build-job", "MNTLAB-${student}-child3-build-job", "MNTLAB-${student}-child4-build-job"]')			}		}	}	scm {		git(giturl, "\${BRANCH_NAME}")	}	steps {		downstreamParameterized {			trigger('$TRIGGERED_JOB_NAMES') {				block {					buildStepFailure('FAILURE')					failure('FAILURE')					unstable('UNSTABLE')				}				parameters {					predefinedProp('BRANCH_NAME', '$BRANCH_NAME')				}			}		}	}}for(i in 1..4) {	job("MNTLAB-${student}-child${i}-build-job") {		scm {			git(giturl, "\${BRANCH_NAME}" )		}		parameters {			activeChoiceParam('BRANCH_NAME') {				description('branch name')				choiceType('SINGLE_SELECT')				groovyScript {					script('''						def command = "git ls-remote -h https://github.com/MNT-Lab/mntlab-dsl.git"						def proc = command.execute()						def branches = proc.in.text.readLines().collect { it.replaceAll(/[a-z0-9]*\trefs\\/heads\\//, '') }						branches.each { println it }					''')					fallbackScript(BRANCH_NAME = "sivanchic")				}			}		}		steps {			shell('''				mkdir -p artifacts				tar -czvf artifacts/${BRANCH_NAME}_dsl_script.tar.gz script.sh jobs.groovy                		sh script.sh > artifacts/output.txt			''')		}		publishers {			archiveArtifacts('artifacts/*')               	}	}}