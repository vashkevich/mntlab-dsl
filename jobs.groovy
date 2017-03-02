def giturl = 'https://github.com/MNT-Lab/mntlab-dsl.git'
def student = 'sivanchic'

job("MNTLAB-${student}-main-build-job") {
	parameters {
		gitParam('BRANCH_NAME') {
			type('BRANCH')
		}


	}

	activeChoiceReactiveParam('CHK') {
                choiceType('CHECKBOX')
                groovyScript {
                        script('return ["MNTLAB-${student}-child1-build-job", "MNTLAB-${student}-child2-build-job", "MNTLAB-${student}-child3-build-job", "MNTLAB-${student}-child4-buil$
                }
        }

	scm {
		git(giturl, "\${BRANCH_NAME}")
	}

	steps {
		downstreamParameterized {
			trigger('$CHK') {
				parameters {
					predefinedProp('BRANCH_NAME', '$BRANCH_NAME')
				}
			}
		}
	}

}

for(i in 1..4) {
	job("MNTLAB-${student}-child${i}-build-job") {
		scm {
			git(giturl, "\${BRANCH_NAME}" )
		}
	}
}
