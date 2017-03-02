job("MNTLAB-hvysotski-main-build-job") {
    description 'Building one main job and four child'
    scm {
        github 'MNT-Lab/mntlab-dsl'
    }
    triggers {
        scm 'H * * * *'
    }
     parameters {
        choiceParam('BRANCH_NAME', ['hvysotski (default)', 'master'])
    }
    
     parameters {
        activeChoiceReactiveParam('BUILDS_TRIGGER') {
            filterable()
            choiceType('CHECKBOX')
            groovyScript {
                script('return ["MNTLAB-hvysotski-child1-build-job", "MNTLAB-hvysotski-child2-build-job", "MNTLAB-hvysotski-child3-build-job", "MNTLAB-hvysotski-child4-build-job"]'
                      )
                }          
           }   
        }
    
    for (i in 1..4) {
    
    job("MNTLAB-hvysotski-child${i}-build-job") {
    
    scm {
        github 'MNT-Lab/mntlab-dsl'
    }
    triggers {
        scm 'H * * * *'
    }
     parameters {
        choiceParam('BRANCH_NAME', ['hvysotski (default)', 'master'])
    }   
    
    steps {
        shell('chmod +x script.sh')
        shell('./script.sh')
        shell('tar cvzf ${BRANCH_NAME}_dsl_script.tar.gz jobs.groovy script.sh')
        shell('touch output.txt')
        shell('cat $JENKINS_HOME/jobs/$JOB_NAME/builds/lastSuccessfulBuild/log > output.txt')      
       }
     }
    }
  } 
