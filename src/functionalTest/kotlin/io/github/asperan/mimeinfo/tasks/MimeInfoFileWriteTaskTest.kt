/*
Copyright (c) 2022, Alex Speranza

This Source Code Form is subject to the terms of the Mozilla Public
License, v. 2.0. If a copy of the MPL was not distributed with this
file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package io.github.asperan.mimeinfo.tasks

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import org.gradle.testkit.runner.TaskOutcome
import org.gradle.testkit.runner.UnexpectedBuildFailure
import kotlin.io.path.writeText

class MimeInfoFileWriteTaskTest : TaskFunctionalTest({
    val taskName = "writeMimeInfoFiles"

    "A simple MimeInfo is constructed" {
        val mimeInfoFileName = "./sample-mimeinfo.xml"
        settingsFile.writeText(
            """
            plugins {
                id("io.github.asperan.mimeinfo-gradle-plugin")
            }
            """.trimIndent()
        )
        buildFile.writeText(
            """    
            mimeinfos {
                mimeinfo("$mimeInfoFileName") { }
            }
            """.trimIndent()
        )

        val result = runTask(taskName)

        // Verify the result
        result.tasks.all { it.outcome == TaskOutcome.SUCCESS }.shouldBeTrue()
        val mimeInfoFileText = projectDir.resolve(mimeInfoFileName).toFile().readText()
        val expectedText = """
            <?xml version="1.0"?>
            <mime-info xmlns='http://www.freedesktop.org/standards/shared-mime-info'>
            
            </mime-info>
        """.trimIndent()
        mimeInfoFileText shouldBe expectedText
    }

    "A MimeInfo with a MimeType is constructed" {
        val mimeInfoFileName = "./sample-mimeinfo.xml"
        settingsFile.writeText(
            """
            plugins {
                id("io.github.asperan.mimeinfo-gradle-plugin")
            }
            """.trimIndent()
        )
        buildFile.writeText(
            """
            import io.github.asperan.mimeinfo.mime.MimeTypeSpecs
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

        val result = runTask(taskName)

        // Verify the result
        result.tasks.all { it.outcome == TaskOutcome.SUCCESS }.shouldBeTrue()
        val mimeInfoFileText = projectDir.resolve(mimeInfoFileName).toFile().readText()
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
        settingsFile.writeText(
            """
            plugins {
                id("io.github.asperan.mimeinfo-gradle-plugin")
            }
            """.trimIndent()
        )
        buildFile.writeText(
            """
            import io.github.asperan.mimeinfo.mime.MimeTypeSpecs
            import io.github.asperan.mimeinfo.mime.Match
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
                        treemagic {
                            treematch("/first/path") {
                                matchCase = true
                            }
                        }
                    }
                }
            }
            """.trimIndent()
        )

        val result = runTask(taskName)

        // Verify the result
        result.tasks.all { it.outcome == TaskOutcome.SUCCESS }.shouldBeTrue()
        val mimeInfoFileText = projectDir.resolve(mimeInfoFileName).toFile().readText()
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
                    <treemagic priority="50">
                        <treematch path="/first/path" match-case="true">

                        </treematch>
                    </treemagic>
                </mime-type>
            </mime-info>
        """.trimIndent().replace("    ", "\t")
        mimeInfoFileText shouldBe expectedText
    }

    "Creating a TreeMatch inside a TreeMatch should fail" {
        val mimeInfoFileName = "./sample-mimeinfo.xml"
        settingsFile.writeText(
            """
            plugins {
                id("io.github.asperan.mimeinfo-gradle-plugin")
            }
            """.trimIndent()
        )
        buildFile.writeText(
            """
            import io.github.asperan.mimeinfo.mime.MimeTypeSpecs
            import io.github.asperan.mimeinfo.mime.Match
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
                        treemagic {
                            treematch("/first/path") {
                                matchCase = true
                                treematch("/second/path") {
                                
                                }
                            }
                        }
                    }
                }
            }
            """.trimIndent()
        )

        shouldThrow<UnexpectedBuildFailure> {
            runTask(taskName)
        }
    }
})
