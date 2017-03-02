def getTags = ("git ls-remote -t -h https://github.com/MNT-Lab/mntlab-dsl.git").execute()
def branchesList = ["pheraska"]

def hd = getTags.text.readLines().collect {it.split()[1].replaceAll('refs/heads/', '')  }.unique()

hd.each
{
    branchesList << it
}

return branchesList.unique()
