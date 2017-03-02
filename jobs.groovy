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
            script('return ["MNTLAB-child1-build-job", "MNTLAB-' + mybranch + '-child2-build-job", "MNTLAB-abilun-' + mybranch + '-build-job", "MNTLAB-' + mybranch + '-child4-build-job"]')
            }
    	}

    	 } 

    	 scm
        {
          github('MNT-Lab/mntlab-dsl', '$BRANCH_NAME')
        }
    }