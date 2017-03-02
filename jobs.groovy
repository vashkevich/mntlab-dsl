def giturl = "https://github.com/MNT-Lab/mntlab-dsl.git"
def studname = "acherlyonok"
//create master branch

  for (number in 1..1){
    job("MNTLAB-${studname}-main${number}-build-job") {
      description("Builds main${number}")
      
      // scm git
      scm {
        git {
          remote { 
            url("${giturl}")
          }
        }
      } 
      triggers { 
       scm 'H/5 * * * *' 
      }

      // build step
      steps { 
        shell "echo 'hello'"
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
      }

      // build step
      steps { 
        shell "echo 'hello'"
      }
    }
  } 
