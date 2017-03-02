import hudson.model.FreeStyleProject
import hudson.plugins.git.BranchSpec
import hudson.plugins.git.extensions.impl.RelativeTargetDirectory
import hudson.plugins.git.extensions.impl.SparseCheckoutPath
import hudson.plugins.git.extensions.impl.SparseCheckoutPaths
import hudson.plugins.git.GitSCM
import hudson.tasks.Shell
import hudson.triggers.SCMTrigger
import javaposse.jobdsl.plugin.ExecuteDslScripts
import jenkins.model.Jenkins
import org.jenkinsci.plugins.multiplescms.MultiSCM
import hudson.plugins.ws_cleanup.PreBuildCleanup
import hudson.model.ChoiceParameterDefinition
import hudson.model.ParametersDefinitionProperty
import org.jenkinsci.plugins.envinject.EnvInjectBuildWrapper
import org.jenkinsci.plugins.envinject.EnvInjectJobPropertyInfo


@NonCPS
def job = { name ->
    jenkins = Jenkins.instance

    project = new FreeStyleProject(jenkins, name)
    pollTrigger = new SCMTrigger("*/5 * * * *")
    project.with {
        scm = new MultiSCM({
            ciScm = new GitSCM("git@github.com:MNT-Lab/mntlab-dsl.git")
            ciScm.branches = [new BranchSpec('$BRANCH_NAME')]
            [ciScm]
        }())

        cleanupWrapper = new PreBuildCleanup([], false, "", "")
        
        buildWrappersList.addAll([cleanupWrapper, envinjectWrapper])

        addProperty(new ParametersDefinitionProperty(
            new StringParameterDefinition("BRANCH_NAME", "${env.BRANCH_NAME}", "")))

        dslBuilder = new ExecuteDslScripts()
        dslBuilder.with {
            scriptLocation = new ExecuteDslScripts.ScriptLocation()
            scriptLocation.with { targets = "jobs.groovy" }
        }
        buildersList.addAll([dslBuilder])
        createTransientActions()
        addTrigger(pollTrigger)
    }
    jenkins.add(project, name)
    pollTrigger.start(project, true)
}



node ('master') {
    checkout scm
    job("SEED-mntlab-jobs-manager")
}



