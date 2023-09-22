package com.paranid5.daily_planner.presentation.utils.ext

import android.content.Context
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.paranid5.daily_planner.presentation.utils.LangSyntaxHighlighter
import io.noties.markwon.Markwon
import io.noties.markwon.editor.MarkwonEditor
import io.noties.markwon.editor.MarkwonEditorTextWatcher
import io.noties.markwon.ext.latex.JLatexMathPlugin
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin
import io.noties.markwon.ext.tables.TablePlugin
import io.noties.markwon.ext.tasklist.TaskListPlugin
import io.noties.markwon.html.HtmlPlugin
import io.noties.markwon.image.ImagesPlugin
import io.noties.markwon.syntax.Prism4jThemeDarkula
import io.noties.markwon.syntax.SyntaxHighlightPlugin
import io.noties.prism4j.Prism4j
import java.util.concurrent.Executors

fun EditText.initMarkwonEditor(context: Context) {
    val markwon = Markwon
        .builder(context)
        .usePlugins(
            listOf(
                StrikethroughPlugin.create(),
                JLatexMathPlugin.create(13F),
                TablePlugin.create(context),
                TaskListPlugin.create(context),
                HtmlPlugin.create(),
                ImagesPlugin.create(),
                SyntaxHighlightPlugin.create(
                    Prism4j(LangSyntaxHighlighter()),
                    Prism4jThemeDarkula.create(ContextCompat.getColor(context, android.R.color.transparent))
                )
            )
        )
        .build()

    val editor = MarkwonEditor.create(markwon)

    addTextChangedListener(
        MarkwonEditorTextWatcher.withPreRender(
            editor,
            Executors.newCachedThreadPool(),
            this
        )
    )
}