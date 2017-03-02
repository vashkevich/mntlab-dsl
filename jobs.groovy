        job("MNTLAB-mburakouski-main-build-job") {
    scm {
        github ('MNT-Lab/mntlab-dsl', '*/${BRANCH_NAME}')
	    }
   // triggers {
   //     scm 'H * * * *'
   // }
     parameters {
        choiceParam('BRANCH_NAME', ['mburakouski', 'master'])
                        }
        parameters {
                activeChoiceReactiveParam('BUILDS_TRIGGER') {
                choiceType('CHECKBOX')
                groovyScript {
                script('return ["MNTLAB-mburakouski-child1-build-job", "MNTLAB-mburakouski-child2-build-job", "MNTLAB-mburakouski-child3-build-job", "MNTLAB-mburakouski-child4-build-job"]')
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
  

parameters
        {
            activeChoiceParam('BRANCH_NAME')
          	{
                choiceType('SINGLE_SELECT')
                groovyScript
                	{
                    script('def getTags = ("git ls-remote -t -h https://github.com/MNT-Lab/mntlab-dsl.git").execute();def brnchList = ["mburakouski"];def hd = getTags.text.readLines().collect {it.split()[1].replaceAll("refs/heads/", "")}.unique();hd.each{ brnchList.push(it);};return brnchList.unique();')
                	}
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

