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
            defaultValue('origin/ikhamiakou')
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

for (i in 1..4) {
job('MNTLAB-ikhamiakou-child${i}-build-job') {
    
    parameters{
        activeChoiceReactiveParam('BRANCH_NAME') {
            choiceType('SINGLE_SELECT')
            groovyScript {
                script('["origin/ikhamiakou", "origin/master"]')
            }
        }
    }
    scm {
        github('MNT-Lab/mntlab-dsl', '${BRANCH_NAME}')
    }

    steps {
        shell('''
                rm -rf *.tar.gz
                chmod +x script.sh
                BRANCH_NAME=$(echo $BRANCH_NAME | cut -c 8-)
                bash script.sh >> output.txt
                tar -czvf ${BRANCH_NAME}_dsl_script.tar.gz jobs.groovy script.sh '''
            )         
            
        publishers {
        archiveArtifacts('*.tar.gz')
        archiveArtifacts('output.txt')

        }
    }
    }   

}
