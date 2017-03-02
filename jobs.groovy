job('MNTLAB-pheraska-main-build-job') 
{
    scm {
        github('MNT-Lab/mntlab-dsl', 'pheraska')
    }
    parameters {
        activeChoiceParam('BRANCH_NAME') {
            description('Allows to choose branch from repository')
            choiceType('SINGLE_SELECT')
            groovyScript {
                script('return ["pheraska","master"];')
	    }
        }
	activeChoiceParam('BUILDS_TRIGGER') {
            description('Allows to choose branch from repository')
            choiceType('CHECKBOX')
            groovyScript {
                script('def jobsNamesArray = [];for (i = 1; i < 5; i++){ jobsNamesArray.push("MNTLAB-pheraska-child${i}-build-job");};return jobsNamesArray;')
	    }
        }
    }        
}
