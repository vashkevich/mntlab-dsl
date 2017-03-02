/* Written by Siarhei Hreben
   DevOps Lab 2017	*/

// Script for branch choice for main job
def script_list_main_branches = '''def main_list_branch = ["shreben","master"];
return main_list_branch;
'''

// Script for branch choice for child jobs
def script_list_git_branches = """def git_ls = [ "/bin/bash", "-c", "git ls-remote --heads https://github.com/MNT-Lab/mntlab-dsl.git | awk '{print \\\$2}' | sort -r -V | sed 's@refs/heads/@@'" ];
def process = git_ls.execute();
process.waitFor();

def result = process.in.text.tokenize("\\n");

def child_list_branch = ["shreben"];
for(i in result) {
  if (i != "shreben") {
    child_list_branch.add(i)
  }
}
return child_list_branch;
"""

// Script to list jobs
def script_list_child_jobs = '''def child_jobs_list = [];
(1..4).each { child_jobs_list << "MNTLAB-SHREBEN-child\${it}-build-job" };
return child_jobs_list;
'''


// Define child jobs list
def child_jobs_list = []
(1..4).each { it ->
	child_jobs_list << "MNTLAB-SHREBEN-child${it}-build-job"
}


// Defining child jobs
child_jobs_list.each {
	freeStyleJob(it) {
	    parameters {
     		activeChoiceParam('BRANCH_NAME') {
		description('Choose branch')
               	choiceType('SINGLE_SELECT')
	        groovyScript {
        	script(script_list_git_branches)
	        }
		}
	    }
	    scm {
		git{
	            remote {
			name('origin')
			url('https://github.com/MNT-Lab/mntlab-dsl.git')
			}
		    branch('$BRANCH_NAME')
		    }
		}
	    steps {
		shell('echo "Job $JOB_NAME is running..."')
		shell('chmod +x script.sh')
	        shell('./script.sh > output.txt')
		shell('tar -czf ${BRANCH_NAME}_dsl_script.tar.gz jobs.groovy script.sh')
	    }
	    publishers {
	        archiveArtifacts('output.txt')
		archiveArtifacts('${BRANCH_NAME}_dsl_script.tar.gz')
	    }
	}

}



// Define main job
multiJob('MNTLAB-SHREBEN-main-build-job') {
    parameters {
                activeChoiceParam('BRANCH_NAME') {
                description('Choose branch')
                choiceType('SINGLE_SELECT')
                groovyScript {
                script(script_list_main_branches)
                }
		}
                activeChoiceParam('BUILDS_TRIGGERS') {
                description('Choose jobs to build')
                choiceType('CHECKBOX')
                groovyScript {
                script(script_list_child_jobs)
                }
		}
	}
    steps {
	triggerBuilder {
		configs {
			blockableBuildTriggerConfig {
				projects('$BUILDS_TRIGGERS')
				block {
					failureThreshold('failure') 
					buildStepFailureThreshold('never')
					unstableThreshold('never')
				}
				
			}
		}
	}
   }
}

