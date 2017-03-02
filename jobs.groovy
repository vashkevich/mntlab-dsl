def gitURL = "https://github.com/MNT-Lab/mntlab-dsl.git"
def studname = "mnikolayev"
//create master branch

  for (number in 1..2){
    job("MNTLAB-${studname}-main-build-job") {
      description("Builds main${number}")
      
      //activeChoiceReactiveParam 
      parameters {
          activeChoiceReactiveParam('BUILDS_TRIGGER') {
              description('Allows user choose from multiple choices')
              choiceType('CHECKBOX')
              groovyScript {
                  script('def used_jobs = ["MNTLAB-$studname-parent-dsl-job", "MNTLAB-$studname-child1-dsl-job", "MNTLAB-$studname-child2-dsl-job", "MNTLAB-$studname-child3-dsl-job", "MNTLAB-$studname-child4-dsl-job"] \n return used_jobs')
              }
          }

          gitParameterDefinition {
              name('BRANCH_NAME')
              type('BRANCH')
              branch('origin/${studname}')
              defaultValue('origin/${studname}')  
              selectedValue('DEFAULT')
              sortMode('NONE')                    //что это?
              quickFilterEnabled(false)           //
              description('')   //не работает без этого 
              branchFilter('')  //
              tagFilter('')     //
              useRepository('') //
            }
      }

//цепляем с гита
      scm {
        git {
          remote { 

            url("${gitURL}")
          }
        }
        steps {
          shell(readFileFromWorkspace('script.sh'))
        }
      }
      triggers {
        scm '*/10 * * * *'
      }

    }
  }

//исполняем шелл скрипт
  for (number in 1..4){
    job("MNTLAB-${studname}-child${number}-build-job") {
      description("Builds child${number}")
      scm {
        git {
          remote {
            url("${gitURL}")
          }
        }
        steps { 
          shell(readFileFromWorkspace('script.sh'))
        }
      }
    }
  }