job("MNTLAB-mburakouski-main-build-job") {
    scm {

        github ('MNT-Lab/mntlab-dsl', '${BRANCH_NAME}')
    }
   // triggers {
   //     scm 'H * * * *'
   // }
     parameters {
        choiceParam('BRANCH_NAME', ['mburakouski', 'master'])
                        }
        parameters {
     choiceParam('BRANCH_NAME', ['mburakouski', 'master'])
 }
           }
        steps {
        downstreamParameterized {
            trigger('$job'){
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
// triggers {
  //   scm 'H * * * *'
// }
  
 parameters {
        gitParameterDefinition{
            name('BRANCH_NAME')
            type('BRANCH')
            defaultValue('abilun')
            selectedValue('DEFAULT')
            branch('origin/abilun')
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

