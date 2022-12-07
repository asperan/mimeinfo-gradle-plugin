# mimeinfo-gradle-plugin
[![Maven Central](https://img.shields.io/maven-central/v/io.github.asperan/mimeinfo-gradle-plugin.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.asperan%22%20AND%20a:%22mimeinfo-gradle-plugin%22)

This gradle plugin allows to update the Linux shared MIME type database with a dsl.

For technical details, read the [Shared MIME-info database specification](https://specifications.freedesktop.org/shared-mime-info-spec/shared-mime-info-spec-latest.html).

## Unsupported features
* Nested `TreeMatch`es
* `Icon`s installation

## Usage
### Basic example
`settings.gradle.kts`:
```kotlin
plugins {
    id("io.github.asperan.mimeinfo-gradle-plugin")
}
```

`build.gradle.kts`:
```kotlin
import io.github.asperan.mimeinfo.mime.MimeTypeSpecs
mimeinfos {
    mimeinfo("./my-custom-mimetype.xml") {
        mimetype(MimeTypeSpecs.Type.MimeClass.TEXT, "x-custom-text") {
            comment("My custom text type")
            glob("*.cstxt")
            globDeleteAll = true
        }
    }
}
```

### Example full usage
This example just shows every piece of the configuration, it does not produce a useful MIME type.
```kotlin
import io.github.asperan.mimeinfo.mime.MimeTypeSpecs
import io.github.asperan.mimeinfo.mime.Match
mimeinfos {
    mimeinfo("./my-custom-mimetype.xml") {
        mimetype(MimeTypeSpecs.Type.MimeClass.TEXT, "x-custom-text") {
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
```

### A more real example
`settings.gradle.kts`:
```kotlin
plugins {
    id("io.github.asperan.mimeinfo-gradle-plugin")
}
```

`build.gradle.kts`:
```kotlin
import io.github.asperan.mimeinfo.mime.MimeTypeSpecs
mimeinfos {
    mimeinfo("./drawio-mime.xml") {
        mimetype(MimeTypeSpecs.Type.MimeClass.APPLICATION, "x-drawio-file") {
            comment("Drawio diagram file")
            glob("*.drawio")
            globDeleteAll = true
        }
    }
}
```

## License
This code is distributed under the [Mozilla Public License 2.0](LICENSE).
