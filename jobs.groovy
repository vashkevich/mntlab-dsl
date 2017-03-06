job("MNTLAB-rvashkevich-main-build-job") {
  scm {
    github('MNT-Lab/mntlab-dsl','rvashkevich')
  }
  parameters {
    gitParameterDefinition {
      name('BRANCH_NAME')
      type('BRANCH')
      branch('rvashkevich')
      defaultValue('rvashkevich')
      selectedValue('DEFAULT')

      description('')
      branchFilter('')
      tagFilter('')
      sortMode('NONE')
      useRepository('')
      quickFilterEnabled(false)
    }

    activeChoiceReactiveParam('BUILDS_TRIGGER') {
      choiceType('CHECKBOX')
      groovyScript {
        script('return ["MNTLAB-rvashkevich-child1-build-job", "MNTLAB-rvashkevich-child2-build-job", "MNTLAB-rvashkevich-child3-build-job", "MNTLAB-rvashkevich-child4-build-job"]')
      } 
    } 
  }
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

  for (i in 1..4) {

    job("MNTLAB-rvashkevich-child${i}-build-job") {

      scm {
        github('MNT-Lab/mntlab-dsl','rvashkevich')
      }

      parameters {
        activeChoiceParam('BRANCH_NAME') {
          description('Allows user choose from multiple choices')
          choiceType('SINGLE_SELECT')
          groovyScript {
            script('return["origin/rvashkevich", "master"]')
          }
        }
      } 

      steps {
        shell(''' rm -rf *.tar.gz |
BRANCH_NAME=$(echo $BRANCH_NAME | cut -c 8-)
tar -czvf ${BRANCH_NAME}_dsl_script.tar.gz jobs.groovy script.sh
bash script.sh | tee -a logfile.txt''')
      }
        publishers {
     archiveArtifacts('${BRANCH_NAME}_dsl_script.tar.gz, output.txt')
    } 
  }
}
