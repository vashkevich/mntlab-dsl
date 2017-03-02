def gitURL="git@github.com:MNT-Lab/mntlab-dsl.git"
def myJob = 'aslesarenka'job("MNTLAB-${student}-main-build-job"){
parameters{

gitParam('BRANCH_NAME')
 {	type('BRANCH')	}

activeChoiceReactiveParam('TRIGGERED_JOB_NAMES') {
description('Choose from multiple Jobs')
            filterable()
            choiceType('CHECK_BOXES')
            groovyScript {
script '( [ return
'MNTLAB-aslesarenka-main-build-job'
'MNTLAB-aslesarenka-child1-build-job'
'MNTLAB-aslesarenka-child2-build-job'
'MNTLAB-aslesarenka-child3-build-job'
'MNTLAB-aslesarenka-child4-build-job'
]') }
              
                fallbackScript('"wrong choice"')
            }
        }
    }
}


scm {	git(giturl, "\${BRANCH_NAME}")	}
	
publishers {	
downstreamParameterized {	
trigger('${TRIGGERED_JOB_NAMES}') {	
condition('UNSTABLE_OR_BETTER')	
parameters {	
predefinedProp('BRANCH_NAME', '$BRANCH_NAME')	}
	}
	}	
}
}
for(i in 1..4) {	
job("MNTLAB-${student}-child${i}-build-job") {
	scm {	git(giturl, "\${BRANCH_NAME}" )	}	
parameters {	
stringParam("BRANCH_NAME", 'origin/aslesarenka') }	
steps {	shell('echo \$BRANCH_NAME')	
shell('sh script.sh > output.txt')	
shell('tar -cvzf ${BRANCH_NAME//\/}_dsl_script.tar.gz script.sh jobs.groovy')	}	publishers {	
archiveArtifacts('${BRANCH_NAME//\/}_dsl_script.tar.gz')	
}	
}
}
















job('Main Build') {
    publishers {
        downstreamParameterized {
            trigger('${CHECK_BOXES}') {
                condition('UNSTABLE_OR_BETTER')
                parameters {
                    currentBuild()
                }
            }
        }
    }
}
