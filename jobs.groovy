def giturl = "https://github.com/MNT-Lab/mntlab-dsl.git"
def studname = "akaminski"
//create master branch
job("MNTLAB-${studname}-main-build")
{
		
}






for (number in 1..4){
  job("MNTLAB-${studname}-child${number}-build-job")
      {
      description("Builds child${number}")
    	scm {
          github("${giturl}")
    	    }
	steps { shell"sh script.sh" }
    	
	}
} 
