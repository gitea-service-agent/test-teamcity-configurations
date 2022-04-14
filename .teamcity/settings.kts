import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2019_2.projectFeatures.buildReportTab
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.vcs
import jetbrains.buildServer.configs.kotlin.v2019_2.vcs.GitVcsRoot

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2021.2"

project {
    description = "Contains all other projects"

    features {
        buildReportTab {
            id = "PROJECT_EXT_1"
            title = "Code Coverage"
            startPage = "coverage.zip!index.html"
        }
    }

    cleanup {
        baseRule {
            preventDependencyCleanup = false
        }
    }

    subProject(TestTeamcity)
}


object TestTeamcity : Project({
    name = "Test Teamcity2"

    vcsRoot(TestTeamcity_HttpsGithubComGiteaServiceAgentTestTeamcityGitRefsHeadsMain)

    buildType(TestTeamcity_Build)
})

object TestTeamcity_Build : BuildType({
    name = "Build"

    vcs {
        root(TestTeamcity_HttpsGithubComGiteaServiceAgentTestTeamcityGitRefsHeadsMain)
    }

    steps {
        script {
            scriptContent = """echo "hello world""""
        }
    }

    triggers {
        vcs {
        }
    }
})

object TestTeamcity_HttpsGithubComGiteaServiceAgentTestTeamcityGitRefsHeadsMain : GitVcsRoot({
    name = "https://github.com/gitea-service-agent/test-teamcity.git#refs/heads/main"
    url = "https://github.com/gitea-service-agent/test-teamcity.git"
    branch = "refs/heads/main"
    branchSpec = "refs/heads/*"
    authMethod = password {
        userName = "gitea-service-agent"
        password = "credentialsJSON:bbbfd0c7-fd78-4739-a9a2-d462ba74a64b"
    }
})
