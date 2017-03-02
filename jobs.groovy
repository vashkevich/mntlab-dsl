for (i = 1; i < 5; i++)
{
    job('MNTLAB-pheraska-child' + i + '-build-job') 
    {
        parameters
        {
            activeChoiceParam('BRANCH_NAME')
	        {
                description('Allows to choose branch from repository')
                choiceType('SINGLE_SELECT')
                groovyScript
                {
                    script('def getTags = ("git ls-remote -t -h https://github.com/MNT-Lab/mntlab-dsl.git").execute();def brnchList = ["pheraska"];def hd = getTags.text.readLines().collect {it.split()[1].replaceAll("refs/heads/", "")}.unique();hd.each{ brnchList.push(it);};return brnchList.unique();')
                }
            }
        }

        scm
        {
            github('MNT-Lab/mntlab-dsl', '${BRANCH_NAME}')
        }

        steps
        {
           shell('chmod +x script.sh')
           shell('./script.sh > output.txt')
           shell('tar -czf ${BRANCH_NAME}_dsl_script.tar.gz jobs.groovy script.sh')
        }

        publishers
        { 
            archiveArtifacts('output.txt')
            archiveArtifacts('${BRANCH_NAME}_dsl_script.tar.gz')
        }
    }
}

job('MNTLAB-pheraska-main-build-job') 
{
    scm
    {
        github('MNT-Lab/mntlab-dsl', 'pheraska')
    }
	
    parameters
    {
        activeChoiceParam('BRANCH_NAME') 
	    {
            description('Allows to choose branch from repository')
            choiceType('SINGLE_SELECT')
            groovyScript
	        {
                script('return ["pheraska","master"];')
	        }
        }
	    
        activeChoiceParam('BUILDS_TRIGGER')
	    {
            description('Allows to choose branch from repository')
            choiceType('CHECKBOX')
            groovyScript
            {
                script('def jobsNamesArray = [];for (i = 1; i < 5; i++){ jobsNamesArray.push("MNTLAB-pheraska-child${i}-build-job");};return jobsNamesArray;')
            } 
        }
        
        steps
        {   downstreamParameterized
            {
                trigger('$BUILDS_TRIGGER') 
                {
                    parameters
                    {
                        predefinedProp('BRANCH_NAME', '$BRANCH_NAME');
                    }
                }
            }
        }          
    }
}
