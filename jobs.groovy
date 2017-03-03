def giturl = "MNT-Lab/mntlab-dsl"
def studname = "acherlyonok"
//create master branch

  for (number in 1..2){
    job("MNTLAB-${studname}-main-build-job") {
      description("Builds main${number}")
      
      // This project is parameterized: activeChoiceReactiveParam
      parameters {
        activeChoiceReactiveParam('BUILDS_TRIGGER') {
          description('Allows user choose from multiple choices')
          choiceType('CHECKBOX')
          groovyScript {
            script('def used_jobs = ["MNTLAB-acherlyonok-child1-build-job", "MNTLAB-acherlyonok-child2-build-job", "MNTLAB-acherlyonok-child3-build-job", "MNTLAB-acherlyonok-child4-build-job"] \n return used_jobs')
          }
        }
          
        // show branches
        activeChoiceReactiveParam('BRANCH_NAME') {
                choiceType('SINGLE_SELECT')
                groovyScript {
                  script('return ["origin/acherlyonok", "origin/master"]')
                }
        }
        /*
        gitParameterDefinition {
          name('BRANCH_NAME')
          type('BRANCH')
          branch('acherlyonok')
          defaultValue('acherlyonok')
          selectedValue('DEFAULT')

          description('')
          branchFilter('')
          tagFilter('')
          sortMode('NONE')
          useRepository('')
          quickFilterEnabled(false)
        }
        */
      }


      // scm git url, branch
      scm {
        github('${giturl}', '$BRANCH_NAME')
      }
        // build step
      steps {
        downstreamParameterized {
          trigger('$BUILDS_TRIGGER') {
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
    
      triggers {
        scm 'H/5 * * * *'
      }

      wrappers {
        timestamps()
      }

    }
  }
  for (number in 1..4){
    job("MNTLAB-${studname}-child${number}-build-job") {
      description("Builds child${number}")
      
      // scm git
      scm {
        github('${giturl}', '$BRANCH_NAME')
      }
      
      parameters {
        gitParameterDefinition {
          name('BRANCH_NAME')
          type('BRANCH')
          branch('acherlyonok')
          defaultValue('acherlyonok')
          selectedValue('DEFAULT')

          description('')
          branchFilter('')
          tagFilter('')
          sortMode('NONE')
          useRepository('')
          quickFilterEnabled(false)
        }
        /*
        activeChoiceReactiveParam('BRANCH_NAME') {
                choiceType('SINGLE_SELECT')
                groovyScript {
                  script('return ["origin/acherlyonok", "origin/acherlyonok"]')
                }
        }
        */
      }

      // build step
        steps {
          shell('''
BRANCH_NAME=$(echo $BRANCH_NAME | cut -c 8-)
tar -czvf ${BRANCH_NAME}_dsl_script.tar.gz jobs.groovy script.sh
bash script.sh > output.txt ''')
        }

      wrappers {
        timestamps()
      }
    }
  }
