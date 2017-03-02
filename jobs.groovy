def giturl = "https://github.com/MNT-Lab/mntlab-dsl.git"
def studname = "acherlyonok"
//create master branch

    job("MNTLAB-${studname}-main-build-job") {
      description("Builds main${number}")
      
      // scm git
      scm {
        git {
          remote { 
            url("${giturl}")
          }
        }
        // build step
        steps { 
          shell(readFileFromWorkspace('script.sh'))
        } 
      }
      
      triggers { 
      scm 'H/5 * * * *' 
      }
    }

  for (number in 1..4){
    job("MNTLAB-${studname}-child${number}-build-job") {
      description("Builds child${number}")
      
      // scm git
      scm {
        git {
          remote { 
            url("${giturl}")
          }
        }
      

      // build step
        steps { 
          shell "echo 'hello'"
        } 
      }
    }
  }
