
  job('MNTLAB-yskrabkou-main-build-job') {
        scm {
            git("git://github.com/MNT-Lab/mntlab-dsl.git")
        }
         triggers {
        scm('H/15 * * * *')
    }
        steps {
            //maven("test -Dproject.name=${project}/${branchName}")
        }
    }