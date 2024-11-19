package com.example.lab_exam_03_new
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.example.lab_exam_03_new.R

class WidgetTodoProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetIds: IntArray?) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        if (context != null && appWidgetManager != null) {
            for (appWidgetId in appWidgetIds!!) {
                val views = RemoteViews(context.packageName, R.layout.widget_todo)

                // Load tasks from SharedPreferences
                val sharedPreferences = context.getSharedPreferences("tasks_pref", Context.MODE_PRIVATE)
                val taskSet = sharedPreferences.getStringSet("tasks", setOf())
                val taskList = taskSet?.toList() ?: listOf()

                // Update TextView with tasks
                val taskText = if (taskList.isEmpty()) {
                    "No upcoming tasks"
                } else {
                    taskList.joinToString(separator = "\n") // Display tasks as a single string
                }
                views.setTextViewText(R.id.tv_widget_tasks, taskText)

                // Update the widget
                appWidgetManager.updateAppWidget(appWidgetId, views)
            }
        }
    }
}
