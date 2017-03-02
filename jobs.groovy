job("MNTLAB-hvysotski-main-build-job") {
    scm {
        github ('MNT-Lab/mntlab-dsl', '*/${BRANCH_NAME}')
    }
	
   // triggers {
   //     scm 'H * * * *'
   // }
	
     parameters {
        choiceParam('BRANCH_NAME', ['hvysotski', 'master'])
                }
        parameters {
                activeChoiceReactiveParam('BUILDS_TRIGGER') {
                choiceType('CHECKBOX')
                groovyScript {
                script('return ["MNTLAB-hvysotski-child1-build-job", "MNTLAB-hvysotski-child2-build-job", "MNTLAB-hvysotski-child3-build-job", "MNTLAB-hvysotski-child4-build-job"]')
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

 job("MNTLAB-hvysotski-child${i}-build-job") {

 scm {
     github 'MNT-Lab/mntlab-dsl','${BRANCH_NAME}'
 }
// triggers {
  //   scm 'H * * * *'
// }
  parameters {
        gitParameterDefinition{
            name('BRANCH_NAME')
            type('BRANCH')
            defaultValue('hvysotski')
            selectedValue('DEFAULT')
            branch('origin/hvysotski')
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
     shell('./script.sh >> output.txt')
     shell('tar cvzf ${BRANCH_NAME}_dsl_script.tar.gz jobs.groovy script.sh')
    }
      publishers {
     archiveArtifacts('${BRANCH_NAME}_dsl_script.tar.gz, output.txt')
     }
   }
 }
