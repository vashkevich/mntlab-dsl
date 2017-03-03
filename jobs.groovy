//main job creation
//tetrs
job("MNTLAB-ikhamiakou-main-build-job") {
    parameters {
        //ActivechoiceReactiveParam provides ability to choose child jobs that should be executed (by checkboxes)   
        activeChoiceReactiveParam('jobs') {
            choiceType('CHECKBOX')
            groovyScript {
                script('return ["MNTLAB-ikhamiakou-child1-build-job", "MNTLAB-ikhamiakou-child2-build-job", "MNTLAB-ikhamiakou-child3-build-job", "MNTLAB-ikhamiakou-child4-build-job"]')
            }
            
        }
        //ActivechoiceReactiveParam section provides ability to choose jobs should be executed (by checkboxes)
        //and predefine string parameter BRANCH_NAME which has two options: {student} (by default) and master
        activeChoiceReactiveParam('BRANCH_NAME') {
            choiceType('SINGLE_SELECT')
            groovyScript {
                script('return ["origin/ikhamiakou", "origin/master"]')
            }
        }

    }
    //downloading selected branch    
    scm {
        github('MNT-Lab/mntlab-dsl', '$BRANCH_NAME')
    }
    
    steps {
        //downstreamParameterized section: "This main job is required to trigger the rest four from one place"        
        downstreamParameterized {
            trigger('$jobs') {
                    //block section:"The main job should wait until all child jobs are executed and should be failing
                    //if even one of the triggered jobs is failed."
                    block {
                        //main job fails if any step in child jobs fails
                        buildStepFailure('FAILURE')
                        //main job fails if any child job fails
                        failure('FAILURE')
                        unstable('UNSTABLE')
                    }
                    //parameters section with predefined props downstreams variable to any triggered job
                    parameters {
                    predefinedProp('BRANCH_NAME', '$BRANCH_NAME')
                    
                    }
            }
       
        
         }
    
    
    }

}
//creating N child jobs 
for (i in 1..4) {
job("MNTLAB-ikhamiakou-child${i}-build-job") {
    
    parameters{
        //gitparam: "a) Each job has choice parameter BRANCH_NAME
        //with list of all branches available in repo git@github.com:MNT-Lab/mntlab-dsl.git"
        gitParam('BRANCH_NAME'){
            type('BRANCH')
            defaultValue('origin/ikhamiakou')
        }
    }
    scm {
        github('MNT-Lab/mntlab-dsl', '${BRANCH_NAME}')
    }
    //build steps
    steps {
        //shell commands
            //rm -rf *.tar.gz - removes any existing tar.gz in the current workspace
            //chmod +x - allows to execute downloaded script
            //BRANCH_NAME=$(echo $BRANCH_NAME | cut -c 8-) - remove "origin/" from $BRANCH_NAME
            //bash script.sh > output.txt - runs script and redirect output 
            //tar - creating tar.gz file 
            //echo - shows message
        shell('''
                
                rm -rf *.tar.gz
                chmod +x script.sh
                BRANCH_NAME=$(echo $BRANCH_NAME | cut -c 8-)
                bash script.sh > output.txt
                tar -czvf ${BRANCH_NAME}_dsl_script.tar.gz jobs.groovy script.sh
                echo "Scrip.sh output saved in output.txt. Artifacts craeted." '''
            )         
//archiving output.txt and any existing tar.gz files
        publishers {
        archiveArtifacts('*.tar.gz')
        archiveArtifacts('output.txt')

        }
    }
    }   

}
//eof
