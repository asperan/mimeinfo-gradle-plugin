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
    fun getBuildFile() = getProjectDir().resolve("build.gradle.kts")

    "A simple MimeInfo is constructed" {
        val mimeInfoFileName = "./sample-mimeinfo.xml"
        getBuildFile().writeText(
            """
            plugins {
                id("io.github.asperan.mimeinfo-gradle-plugin")
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

    "A MimeInfo with a MimeType is constructed" {
        val mimeInfoFileName = "./sample-mimeinfo.xml"
        getBuildFile().writeText(
            """
            import io.github.asperan.mimeinfo.mime.MimeTypeSpecs
            
            plugins {
                id("io.github.asperan.mimeinfo-gradle-plugin")
            }
            
            mimeinfos {
                mimeinfo("$mimeInfoFileName") {
                    mimetype(MimeTypeSpecs.Type.MimeClass.TEXT, "custom-text") {
                        glob("*.cstxt")
                        globDeleteAll = true
                    }
                }
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
                <mime-type type="text/custom-text">
                    <glob-deleteall />
                    <glob pattern="*.cstxt"/>
                </mime-type>
            </mime-info>
        """.trimIndent().replace("    ", "\t")
        mimeInfoFileText shouldBe expectedText
    }

    "Full mimeinfo file generation test" {
        val mimeInfoFileName = "./sample-mimeinfo.xml"
        getBuildFile().writeText(
            """
            import io.github.asperan.mimeinfo.mime.MimeTypeSpecs
            import io.github.asperan.mimeinfo.mime.Match
            
            plugins {
                id("io.github.asperan.mimeinfo-gradle-plugin")
            }
            
            mimeinfos {
                mimeinfo("$mimeInfoFileName") {
                    mimetype(MimeTypeSpecs.Type.MimeClass.TEXT, "custom-text") {
                        comment("My custom text type")
                        glob("*.cstxt")
                        globDeleteAll = true
                        magicDeleteAll = true
                        magic {
                            match(Match.Type.STRING, Match.Offset(0u), "0x0000")
                        }
                        alias("text/plain")
                        subclassOf("text/plain")
                        acronym("CSTXT")
                        expandedAcronym("Custom Text")
                        icon("my-custom-icon")
                        rootXml("/path/to/somewhere", "xml-localname")
                    }
                }
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
                <mime-type type="text/custom-text">
                    <comment >
                        My custom text type
                    </comment>
                    <acronym >
                        CSTXT
                    </acronym>
                    <expanded-acronym >
                        Custom Text
                    </expanded-acronym>
                    <sub-class-of type="text/plain"/>
                    <alias type="text/plain"/>
                    <icon name="my-custom-icon"/>
                    <root-XML namespaceURI="/path/to/somewhere" localname="xml-localname"/>
                    <glob-deleteall />
                    <glob pattern="*.cstxt"/>
                    <magic-deleteall />
                    <magic priority="50">
                        <match type="string" offset="0" value="0x0000"/>
                    </magic>
                </mime-type>
            </mime-info>
        """.trimIndent().replace("    ", "\t")
        mimeInfoFileText shouldBe expectedText
    }
})
