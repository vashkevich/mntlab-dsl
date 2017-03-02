def gitURL = "https://github.com/MNT-Lab/mntlab-dsl.git"
def studname = "mnikolayev"

  for (number in 1..4){
    job("MNTLAB-${studname}-main-build-job") {
      description("Builds mai


parameters {
        choiceParam('BRANCH_NAME', ['mnikolayev', 'master'])
                }

      parameters {
          activeChoiceReactiveParam('BUILDS_TRIGGER') {
              choiceType('CHECKBOX') 
              groovyScript {
                  script('return ["MNTLAB-$studname-parent-dsl-job", "MNTLAB-$studname-child1-dsl-job", "MNTLAB-$studname-child2-dsl-job", "MNTLAB-$studname-child3-dsl-job", "MNTLAB-$studname-child4-dsl-job"]')
              }
          }

          gitParameterDefinition {
              name('BRANCH_NAME')
              type('BRANCH')
              branch('origin/mnikolayev')
              defaultValue('origin/${studname}')  
              selectedValue('DEFAULT')
              sortMode('NONE')                              //что это вообще?
              quickFilterEnabled(false)           
              description('')                               //не работает без этого 
              branchFilter('')                              //
              tagFilter('')                                 //
              useRepository('')                             //
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
        scm 'H * * * *'
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
      parameters {
          activeChoiceReactiveParam('BRANCH_NAME') {
                  choiceType('CHECKBOX')
                  groovyScript {
                    script('["origin-mnikolayev", "origin-mnikolayev"]')
                      }}}
          steps {
          shell('''
BRANCH_NAME=$(echo $BRANCH_NAME | cut -c 8-)
tar -czvf ${BRANCH_NAME}_dsl_script.tar.gz jobs.groovy script.sh
bash script.sh > output.txt ''')
          shell(readFileFromWorkspace('script.sh'))
}}}
