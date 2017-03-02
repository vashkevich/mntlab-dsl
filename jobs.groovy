def giturl = 'https://github.com/MNT-Lab/mntlab-dsl.git'
def student = 'sivanchic'

job("MNTLAB-${student}-main-build-job") {
	parameters {
		gitParam('BRANCH_NAME') {
			type('BRANCH')
		}
	
		activeChoiceReactiveParam('CHILDREN') {
                	choiceType('CHECKBOX')
                	groovyScript {
                        	script('return ["MNTLAB-sivanchic-child1-build-job, MNTLAB-sivanchic-child2-build-job, MNTLAB-sivanchic-child3-build-job, MNTLAB-sivanchic-child4-build-job"]')
                	}
        	}

	}

	scm {
		git(giturl, "\${BRANCH_NAME}")
	}

	publishers {
		downstreamParameterized {
			trigger('CHILDREN') {
				condition('UNSTABLE_OR_BETTER')
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
	parameters {
		stringParam("BRANCH_NAME", 'origin/sivanchic') 
	}

	steps {
		shell('echo \$BRANCH_NAME')
	}

	}
}
