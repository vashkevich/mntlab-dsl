job('XXX_MNTLAB-yskrabkou-main-build-job') {
    scm {
        git("git://github.com/MNT-Lab/mntlab-dsl.git", yskrabkou)
    }
    triggers {
        scm('H/15 * * * *')
    }
   // steps {
    //    maven('-e clean test')
    //}
}