// test
job('master') {
    scm {
        github 'MNT-Lab/mntlab-dsl'
    }
    parameters {
          gitParam('BRANCH_NAME') {
            type('BRANCH')    
          }
	activeChoiceReactiveParam('BUILD_TRIGGER') {
            choiceType('CHECKBOX')
            groovyScript {
                script('return ["child_1", "child_2", "child_3", "child_4"]')
            }
        }
        
    steps {
        downstreamParameterized {
            trigger('$BUILD_TRIGGER') {
                parameters {
                    predefinedProp('BRANCH_NAME', '$BRANCH_NAME')
                }
            }
        }
    }  
        
}


for (i = 1; i <5; i++) {
	job('child_'+i) {
    	scm {
        	github 'MNT-Lab/mntlab-dsl'
            }    
        }
    }
}
