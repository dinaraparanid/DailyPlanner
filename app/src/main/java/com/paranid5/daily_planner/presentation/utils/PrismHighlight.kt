package com.paranid5.daily_planner.presentation.utils

import io.noties.prism4j.annotations.PrismBundle

@PrismBundle(
    include = [
        "c", "clike", "clojure", "cpp", "csharp", "css", "css-extras", "dart",
        "git", "go", "groovy", "java", "javascript", "json", "kotlin", "latex",
        "markdown", "markup", "python", "scala", "sql", "swift", "yaml"
    ],
    grammarLocatorClassName = ".LangSyntaxHighlighter"
)
class PrismHighlight