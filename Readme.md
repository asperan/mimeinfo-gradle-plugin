# mimeinfo-gradle-plugin
This gradle plugin allows to update the Linux shared MIME type database with a dsl.

## Unsupported features
* Nested `TreeMatch`es
* `Icon`s installation

## Sample Usage
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
        mimetype(MimeTypeSpecs.Type.MimeClass.TEXT, "custom-text") {
            comment("My custom text type")
            glob("*.cstxt")
            globDeleteAll = true
        }
    }
}
```

## Example full usage
```kotlin
import io.github.asperan.mimeinfo.mime.MimeTypeSpecs
import io.github.asperan.mimeinfo.mime.Match
mimeinfos {
    mimeinfo("./my-custom-mimetype.xml") {
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
```

## License
This code is distributed under the [Mozilla Public License 2.0](LICENSE).
