def giturl = "https://github.com/MNT-Lab/mntlab-dsl.git"
def studname = "akaminski"


//create master branch
job("MNTLAB-${studname}-main-build")
{       parameters {
		gitParam('BRANCH_NAME') { type('BRANCH') defaultValue("${studname}") }
		activeChoiceParam('CHOICE-1') {
            description('Allows user choose from multiple choices')
            filterable()
            choiceType('SINGLE_SELECT')
            groovyScript {
                script('["choice1", "choice2"]')
                fallbackScript('"fallback choice"')
            }
        }

		}
	description ("Build main job")
      scm {
          git{
		remote { url("${giturl}")}
		branch("$studname")
    	    }
	}
	publishers {
        downstreamParameterized {
            trigger('MNTLAB-akaminski-child1-build-job,MNTLAB-akaminski-child2-build-job,MNTLAB-akaminski-child3-build-job,MNTLAB-akaminski-child4-build-job') {
                condition('UNSTABLE_OR_BETTER')
                parameters {predefinedProp('BRANCH_NAME', '$BRANCH_NAME') }
            }
        }
    }
}





for (number in 1..4){
  job("MNTLAB-${studname}-child${number}-build-job")
      {
      description("Builds child${number}")
      parameters { stringParam("BRANCH_NAME")}
      scm {
          git{
		remote { url("${giturl}")}
		branch("$studname")
    	    }
	steps { shell "sh script.sh "}
    	}
	
}
} 
