def giturl = "https://github.com/MNT-Lab/mntlab-dsl.git"
def studname = "akaminski"
//create master branch
job("MNTLAB-${studname}-main-build")
{
	description ("Build main job")
}





for (number in 1..4){
  job("MNTLAB-${studname}-child${number}-build-job")
      {
      description("Builds child${number}")
    	scm {
          git("${giturl}"," ${studname}")
    	    }
	steps { shell"echo "hello""}
    	
	}
} 
