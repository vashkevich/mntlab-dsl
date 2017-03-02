mybranch = 'yskrabkou'

job('MNTLAB-' + mybranch + '-main-build-job') 
	{
		parameters
        {
            activeChoiceParam('BRANCH_NAME')
	        {
                description('Allows to choose branch from repository')
                choiceType('SINGLE_SELECT')
                groovyScript
                {
                    script('def getTags = ("git ls-remote -t -h https://github.com/MNT-Lab/mntlab-dsl.git").execute();def branchList  = getTags.text.readLines().collect {it.split()[1].replaceAll("refs/heads/", "")}.unique(); branchList  = branchList .reverse(); return branchList;')
                }
            }
        

         activeChoiceReactiveParam('JOB_SELECT')
          {
         	choiceType('CHECKBOX')
            groovyScript 
            {
            script('return ["MNTLAB-yskrabkou-child1-build-job", "MNTLAB-yskrabkou-child2-build-job", "MNTLAB-yskrabkou-child3-build-job", "MNTLAB-yskrabkou-child4-build-job"]')
            }
    	}

    	 } 

    	 scm
        {
          github('MNT-Lab/mntlab-dsl', '$BRANCH_NAME')
        }

          steps {
        downstreamParameterized {
            trigger('$BUILD_TRIGGER') {
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