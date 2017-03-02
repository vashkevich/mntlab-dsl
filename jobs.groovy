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
                  script('def used_jobs = ["MNTLAB-ac-child1-build-job", "MNTLAB-ac-child2-build-job", "MNTLAB-ac-child3-build-job", "MNTLAB-ac-child4-build-job", "MNTLAB-ac-child5-build-job"] \n return used_jobs')
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
          shell(readFileFromWorkspace('script.sh'))
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
      

      // build step
        steps { 
          shell(readFileFromWorkspace('script.sh'))
        }
      }
    }
  }
