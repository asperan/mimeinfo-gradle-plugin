package io.github.asperan.mimeinfo.tasks

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import kotlin.io.path.createTempDirectory
import kotlin.io.path.writeText

class MimeInfoFileWriteTaskTest : StringSpec({
    val tempFolder = createTempDirectory()

    fun getProjectDir() = tempFolder
    fun getBuildFile() = getProjectDir().resolve("build.gradle")

    "A simple MimeInfo is constructed" {
        val mimeInfoFileName = "./sample-mimeinfo.xml"
        getBuildFile().writeText(
            """
            plugins {
                id('io.github.asperan.mimeinfo-gradle-plugin')
            }
            
            mimeinfos {
                mimeinfo("$mimeInfoFileName") { }
            }
            """.trimIndent()
        )

        // Run the build
        val runner = GradleRunner.create()
        runner.forwardOutput()
        runner.withPluginClasspath()
        runner.withArguments("writeMimeInfoFiles")
        runner.withProjectDir(getProjectDir().toFile())
        val result = runner.build()

        // Verify the result
        result.tasks.all { it.outcome == TaskOutcome.SUCCESS }.shouldBeTrue()
        val mimeInfoFileText = getProjectDir().resolve(mimeInfoFileName).toFile().readText()
        val expectedText = """
            <?xml version="1.0"?>
            <mime-info xmlns='http://www.freedesktop.org/standards/shared-mime-info'>
            
            </mime-info>
        """.trimIndent()
        mimeInfoFileText shouldBe expectedText
    }
})
