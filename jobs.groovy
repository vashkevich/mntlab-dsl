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
                script("return ["choice1", "choice2"]")
                       //def getTags = ("git ls-remote -t -h https://github.com/MNT-Lab/mntlab-dsl.git").execute();def brnchList = ["pheraska"];def hd = getTags.text.readLines().collect {it.split()[1].replaceAll("refs/heads/", "")}.unique();hd.each{ brnchList.push(it)};return brnchList.unique();')
                       }
        }
    }
}
