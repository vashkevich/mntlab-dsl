job('DSL-Tutorial-2-Test') {
    scm {
        git('https://github.com/MNT-Lab/mntlab-dsl')
    }
    triggers {
        scm('H/15 * * * *')
    }
   // steps {
    //    maven('-e clean test')
    //}
}