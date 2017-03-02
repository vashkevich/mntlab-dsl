def gitURL = "https://github.com/MNT-Lab/mntlab-dsl.git"
def studname = "mnikolayev"
job("MNTLAB-mnikolayev-main-build-job") {

    scm {
        github ('MNT-Lab/mntlab-dsl', '*/${BRANCH_NAME}')
    }
  
  
     parameters {
        choiceParam('BRANCH_NAME', ['mnikolayev', 'master'])
                }



        parameters {
                activeChoiceReactiveParam('BUILDS_TRIGGER') {
                choiceType('CHECKBOX')
                groovyScript {
                script('return ["MNTLAB-mnikolayev-child1-build-job", "MNTLAB-mnikolayev-child2-build-job", "MNTLAB-mnikolayev-child3-build-job", "MNTLAB-mnikolayev-child4-build-job"]')
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



for (number in 1..4) {

 job("MNTLAB-mnikolayev-child${number}-build-job") {

 scm {
     github 'MNT-Lab/mntlab-dsl','${BRANCH_NAME}'
 }
  parameters {
        gitParameterDefinition{
            name('BRANCH_NAME')
            type('BRANCH')
            defaultValue('mnikolayev')
            selectedValue('DEFAULT')
            branch('origin/mnikolayev')
            description('')
            branchFilter('')
            tagFilter('')
            sortMode('NONE')
            useRepository('')
            quickFilterEnabled(false)
        }
    }

 steps {
     shell('bash script.sh >> output.txt')
     shell('tar cvzf ${BRANCH_NAME}_dsl_script.tar.gz jobs.groovy script.sh')
    }
      publishers {
     archiveArtifacts('${BRANCH_NAME}_dsl_script.tar.gz, output.txt')
     }
   }
 }
