def giturl = "https://github.com/MNT-Lab/mntlab-dsl.git"
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

          //gitParam('BRANCH_NAME'){
          //  type('BRANCH')
          //  defaultValue('origin/acherlyonok')
          //  SelectedValue('DEFAULT')
          //  SelectedValue.DEFAULT
            gitParameterDefinition {
              name('BRANCH_NAME')
              type('BRANCH')
              branch('origin/acherlyonok')
              defaultValue('origin/acherlyonok')
              selectedValue('DEFAULT')

              description('')
              branchFilter('')
              tagFilter('')
              sortMode('NONE')
              useRepository('')
              quickFilterEnabled(false)
            }
          //}
      }


      // scm git
      scm {
        git {
          remote { 
            url("${giturl}")
          }
        }
        // build step
        steps {
          downstreamParameterized {
           trigger('$BUILD_TRIGGERS') {
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
      triggers {
        scm 'H/5 * * * *'
      }

    }
  }


  for (number in 1..4){
    job("MNTLAB-${studname}-child${number}-build-job") {
      description("Builds child${number}")
      
      // scm git
      scm {
        git {
          remote {
            url("${giturl}")
          }
        }
      
      parameters {
          activeChoiceReactiveParam('BRANCH_NAME') {
                  choiceType('SINGLE_SELECT')
                  groovyScript {
                    script('["origin-abilun", "origin-master"]')
                  }
          }
      }

      // build step
        steps {
          shell('''
BRANCH_NAME=$(echo $BRANCH_NAME | cut -c 8-)
tar -czvf ${BRANCH_NAME}_dsl_script.tar.gz jobs.groovy
bash script.sh > output.txt ''')
          //shell(readFileFromWorkspace('script.sh'))
        }
      }
    }
  }
