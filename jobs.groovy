job('MNTLAB-ikhamiakou-main-build-job') {
    
    parameters {
        activeChoiceReactiveParam('jobs') {
            choiceType('CHECKBOX')
            groovyScript {
                script('return ["MNTLAB-ikhamiakou-child1-build-job", "MNTLAB-ikhamiakou-child2-build-job", "MNTLAB-ikhamiakou-child3-build-job", "MNTLAB-ikhamiakou-child4-build-job"]')
            }
            
        }
        gitParam('BRANCH_NAME'){
            type('BRANCH')
        }

    }
    
    scm {
        github('MNT-Lab/mntlab-dsl', '$BRANCH_NAME')
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

}

job('MNTLAB-ikhamiakou-child1-build-job') {
    
    parameters{
        activeChoiceReactiveParam('BRANCH_NAME') {
            choiceType('SINGLE_SELECT')
            groovyScript {
                script('["ikhamiakou", "master"]')
            }
        }
    }
    scm {
        github('MNT-Lab/mntlab-dsl')
    }

    steps {
        shell('''
                BRANCH_NAME=$(echo $BRANCH_NAME | cut -c 8-)
                bash script.sh >> output.txt
                tar -czvf ${BRANCH_NAME}_dsl_script.tar.gz jobs.groovy script.sh output.txt '''
            )         
            
        publishers {
        archiveArtifacts('*')
        }
    }
    }   


job('MNTLAB-ikhamiakou-child2-build-job') {
    
    parameters{
        activeChoiceReactiveParam('BRANCH_NAME') {
            choiceType('SINGLE_SELECT')
            groovyScript {
                script('["ikhamiakou", "master"]')
            }
        }
    }
    scm {
        github('MNT-Lab/mntlab-dsl')
    }

    steps {
        shell('''
                BRANCH_NAME=$(echo $BRANCH_NAME | cut -c 8-)
                bash script.sh >> output.txt
                tar -czvf ${BRANCH_NAME}_dsl_script.tar.gz jobs.groovy script.sh output.txt '''
            )         
            
        publishers {
        archiveArtifacts('*')
        }
    }
    }
    
    


job('MNTLAB-ikhamiakou-child3-build-job') {
    
    parameters{
        activeChoiceReactiveParam('BRANCH_NAME') {
            choiceType('SINGLE_SELECT')
            groovyScript {
                script('["ikhamiakou", "master"]')
            }
        }
    }
    scm {
        github('MNT-Lab/mntlab-dsl')
    }

    steps {
        shell('''
                BRANCH_NAME=$(echo $BRANCH_NAME | cut -c 8-)
                bash script.sh >> output.txt
                tar -czvf ${BRANCH_NAME}_dsl_script.tar.gz jobs.groovy script.sh output.txt '''
            )         
            
        publishers {
        archiveArtifacts('*')
        }
    }
    }
    
    


job('MNTLAB-ikhamiakou-child4-build-job') {
    
    parameters{
        activeChoiceReactiveParam('BRANCH_NAME') {
            choiceType('SINGLE_SELECT')
            groovyScript {
                script('["ikhamiakou", "master"]')
            }
        }
    }
    scm {
        github('MNT-Lab/mntlab-dsl')
    }

    steps {
        shell('''
                BRANCH_NAME=$(echo $BRANCH_NAME | cut -c 8-)
                bash script.sh >> output.txt
                tar -czvf ${BRANCH_NAME}_dsl_script.tar.gz jobs.groovy script.sh output.txt '''
            )         
            
        publishers {
        archiveArtifacts('*')
        }
    }
    }
    