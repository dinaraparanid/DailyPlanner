package com.paranid5.daily_planner.presentation.utils.ext

import android.content.Context
import io.noties.markwon.Markwon
import io.noties.markwon.ext.latex.JLatexMathPlugin
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin
import io.noties.markwon.ext.tables.TablePlugin
import io.noties.markwon.ext.tasklist.TaskListPlugin

fun DefaultMarkwon(context: Context) = Markwon
    .builder(context)
    .usePlugins(
        listOf(
            StrikethroughPlugin.create(),
            JLatexMathPlugin.create(13F),
            TablePlugin.create(context),
            TaskListPlugin.create(context),
        )
    )
    .build()