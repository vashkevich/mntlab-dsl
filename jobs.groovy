job('MNTLAB-{amatveenko}-main-build-job'){
  parameters {
    gitParam('BRANCH_NAME') {
            type('BRANCH')
            defaultValue('amatveenko')
            sortMode('ASCENDING')
    }
  }
    
    scm {
        github('MNT-Lab/mntlab-dsl')
    }


  steps {
    downstreamParameterized {
     		trigger('MNTLAB-{amatveenko}-child1-build-job'){
      			parameters {
  	                  predefinedProp('BRANCH_NAME', '$BRANCH_NAME')
      			}
      	}
 	  }
  }
}


for (i = 1; i <2; i++) {
job('MNTLAB-{amatveenko}-child' + i + '-build-job') {
parameters {
        stringParam('BRANCH_NAME')
}
    scm {
        github('MNT-Lab/mntlab-dsl', '$BRANCH_NAME')
    }
  steps {
        shell('''
          cd $WORKSPACE
          bash script.sh > output.txt
          tar -czvf *_dsl_script.tar.gz jobs.groovy
          ls -lh
          echo $BRANCH_NAME
          ''')
	}
          publishers {
          archiveArtifacts('*_dsl_script.tar.gz')
          }
}
}
