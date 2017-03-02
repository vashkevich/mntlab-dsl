job('MNTLAB-ikhamiakou-main-build-job') {
    
    parameters {
        activeChoiceReactiveParam('jobs') {
            choiceType('CHECKBOX')
            groovyScript {
                script('return ["MNTLAB-ikhamiakou-child1-build-job", "MNTLAB-ikhamiakou-child2-build-job", "MNTLAB-ikhamiakou-child3-build-job", "MNTLAB-ikhamiakou-child4-build-job"]')
            }
            
        }
    }
    scm {
        git('https://github.com/MNT-Lab/mntlab-dsl.git')
    }
    
    steps {
        downstreamParameterized {
            trigger('$jobs') {
                block {
                    buildStepFailure('FAILURE')
                    failure('FAILURE')
                    unstable('UNSTABLE')
                }
                parameters {
                    predefinedProp('BRANCH_NAME', '$BRANCH_NAME')
                    
                }
        }
       
        
    }
    
    
}

job('MNTLAB-ikhamiakou-child1-build-job') {
    
    parameters {
        stringParam('BRANCH_NAME')
        
    }
    
    
    steps {
        shell('rm -rf $WORKSPACE/*')
        shell('BRANCH_NAME=$(echo $BRANCH_NAME | cut -c 8-'))
        shell('wget https://raw.githubusercontent.com/MNT-Lab/mntlab-dsl/master/script.sh')        
        shell('chmod +x script.sh')
        shell('./script.sh>>output.txt')
        shell('tar -zcvf $BRANCH_NAME.tar.gz $WORKSPACE/script.sh')
    
        publishers {
        archiveArtifacts('*')
        }
    }
    
    
}

job('MNTLAB-ikhamiakou-child2-build-job') {
    
    parameters {
        stringParam('BRANCH_NAME')
        
    }
    
    
    steps {
        shell('rm -rf $WORKSPACE/*')
        shell('BRANCH_NAME=$(echo $BRANCH_NAME | cut -c 8-'))
        shell('wget https://raw.githubusercontent.com/MNT-Lab/mntlab-dsl/master/script.sh')        
        shell('chmod +x script.sh')
        shell('./script.sh>>output.txt')
        shell('tar -zcvf $BRANCH_NAME.tar.gz $WORKSPACE/script.sh')        
        publishers {
        archiveArtifacts('*')
        }

    }
    
    
}

job('MNTLAB-ikhamiakou-child3-build-job') {
    
    parameters {
        stringParam('BRANCH_NAME')
        
    }
    
    
    steps {
        shell('rm -rf $WORKSPACE/*')
        shell('BRANCH_NAME=$(echo $BRANCH_NAME | cut -c 8-'))
        shell('wget https://raw.githubusercontent.com/MNT-Lab/mntlab-dsl/master/script.sh')        
        shell('chmod +x script.sh')
        shell('./script.sh>>output.txt')
        shell('tar -zcvf $BRANCH_NAME.tar.gz $WORKSPACE/script.sh')
    
        publishers {
        archiveArtifacts('*')
        }

    }
    
    
}

job('MNTLAB-ikhamiakou-child4-build-job') {
    
    parameters {
        stringParam('BRANCH_NAME')
        
    }
    
    
    steps {
        shell('rm -rf $WORKSPACE/*')
        shell('BRANCH_NAME=$(echo $BRANCH_NAME | cut -c 8-'))
        shell('wget https://raw.githubusercontent.com/MNT-Lab/mntlab-dsl/master/script.sh')        
        shell('chmod +x script.sh')
        shell('./script.sh>>output.txt')
        shell('tar -zcvf $BRANCH_NAME.tar.gz $WORKSPACE/script.sh')
    
        publishers {
        archiveArtifacts('*')
        
        }

    }
    
}    
}