def giturl = 'https://github.com/MNT-Lab/mntlab-dsl.git'
def student = 'sivanchic'

job("MNTLAB-${student}-main-build-job") {
	parameters {
		gitParam('BRANCH_NAME') {
			type('BRANCH')
		}
	}
	scm {
		git(giturl, "\${BRANCH_NAME}")
	}
}

for(i in 1..4) {
	job("MNTLAB-${student}-child${i}-build-job") {
		scm {
			git(giturl, "\${BRANCH_NAME}" )
		}
	}
}
