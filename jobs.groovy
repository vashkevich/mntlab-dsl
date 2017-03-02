def giturl = 'https://github.com/MNT-Lab/mntlab-dsl.git'
def student = 'sivanchic'

job("MNTLAB-${student}-main-build-job") {
        scm {
            git(giturl, "*/${student}" )
        }
    }

for(i in 1..4) {
    job("MNTLAB-${student}-child${i}-build-job") {
        scm {
            git(giturl, "*/${student}" )
        }
    }
}
