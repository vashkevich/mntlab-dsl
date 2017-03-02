job("MNTLAB-mburakouski-main-build-job") {
    scm {
        github ('MNT-Lab/mntlab-dsl', '*/${BRANCH_NAME}')
            }
    triggers {
       scm 'H * * * *'
            }
    parameters {
        choiceParam('BRANCH_NAME', ['mburakouski', 'master'])
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
            parameters {
                    predefinedProp('BRANCH_NAME', '$BRANCH_NAME')
                        }
                }
        }
    }
}

for (i in 1..4) {

job("MNTLAB-mburakouski-child${i}-build-job") {

 scm {
   github 'MNT-Lab/mntlab-dsl','*/${BRANCH_NAME}'
     }
triggers {
     scm 'H * * * *'
         }


parameters {
        gitParameterDefinition{
            name('BRANCH_NAME')
            type('BRANCH')
            defaultValue('mburakouski')
            selectedValue('DEFAULT')
            branch('origin/mburakouski')
            description('')
            branchFilter('')
            tagFilter('')
            sortMode('NONE')
            useRepository('')
            quickFilterEnabled(false)
                }
         }

 steps {
     shell('touch output.txt')
     shell('chmod +x script.sh')
     shell('./script.sh > output.txt')
     shell('tar cvzf ${BRANCH_NAME}_dsl_script.tar.gz jobs.groovy script.sh')
          }
      publishers {
     archiveArtifacts('${BRANCH_NAME}_dsl_script.tar.gz, output.txt')
         }
     }
 }
