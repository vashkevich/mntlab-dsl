for (i in 1..4)
{
    job('MNTLAB-mburakouski-child' + i + '-build-job')
    {
        parameters
        {
            activeChoiceParam('BRANCH_NAME')
                {
                description('Allows to choose branch from repository')
                choiceType('SINGLE_SELECT')
                groovyScript
                {
                script(readFileFromWorkspace('work.groovy'))
                }
            }
        }

        scm
        {
            github('MNT-Lab/mntlab-dsl', '${BRANCH_NAME}')
        }

        steps
        {
           shell('''chmod +x script.sh
                 ./script.sh > output.txt
                 tar -czf ${BRANCH_NAME}_dsl_script.tar.gz jobs.groovy script.sh''')
        }

        publishers
        {
            archiveArtifacts('output.txt')
            archiveArtifacts('${BRANCH_NAME}_dsl_script.tar.gz')
        }
    }
}
job("MNTLAB-mburakouski-main-build-job") {
    scm {
        github ('MNT-Lab/mntlab-dsl', '*/${BRANCH_NAME}')
    }

   // triggers {
   //     scm 'H * * * *'
   // }

     parameters {
        choiceParam('BRANCH_NAME', ['mburakouski', 'master'])
                }
        parameters {
                activeChoiceReactiveParam('BUILDS_TRIGGER') {
                choiceType('CHECKBOX')
                groovyScript {
                script('return ["MNTLAB-mburakouski-child1-build-job", "MNTLAB-mburakouski-child2-build-job", "MNTLAB-mburakouski-child3-build-job", "MNTLAB-mburakouski-child4-build-job"]')
                }
             }
           }
        steps {
        downstreamParameterized {
            trigger('$BUILDS_TRIGGER'){
                block {
                        //main job fails if any step in child jobs fails
                        buildStepFailure('FAILURE')
                        //main job fails if any child job fails
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
