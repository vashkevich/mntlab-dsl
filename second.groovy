import groovy.io.FileType

def listhuist = []
listhuist.add("hvysotski")

def gitURL = "https://github.com/MNT-Lab/mntlab-dsl"
def command = "git ls-remote -h $gitURL"

def proc = command.execute()
proc.waitFor()

if ( proc.exitValue() != 0 ) {
   println "Error, ${proc.err.text}"
   System.exit()
}

branches = proc.in.text.readLines().collect {
  it.replaceAll(/[a-z0-9]*\trefs\/heads\//, '')}


listhuist.addAll(branches)
listhuist.unique()

listhuist.each {
println it
}
