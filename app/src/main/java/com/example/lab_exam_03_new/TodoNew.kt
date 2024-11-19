package com.example.lab_exam_03_new

import TaskAdapter
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class TodoNew : Fragment() {

    private lateinit var taskAdapter: TaskAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var taskList: MutableList<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_todo_new, container, false)

        // Initialize SharedPreferences
        sharedPreferences = requireContext().getSharedPreferences("tasks_pref", Context.MODE_PRIVATE)
        taskList = loadTasks()

        // Set up the RecyclerView and Adapter
        taskAdapter = TaskAdapter(taskList, ::editTask, ::deleteTask)
        val rvTasks = view.findViewById<RecyclerView>(R.id.rv_tasks)
        rvTasks.layoutManager = LinearLayoutManager(context)
        rvTasks.adapter = taskAdapter

        // Add new task using the plus icon
        val ivAddTask = view.findViewById<ImageView>(R.id.iv_add_task)
        ivAddTask.setOnClickListener {
            addNewTask()
        }

        return view
    }

    // Function to add a new task
    private fun addNewTask() {
        val inputTask = EditText(context)

        // Customizing the dialog title
        val title = TextView(context).apply {
            text = "Add Task"
            gravity = Gravity.CENTER
            textSize = 20f
            setTypeface(null, android.graphics.Typeface.BOLD)
            setPadding(0, 30, 0, 30)  // Padding for better spacing
        }

        val dialog = AlertDialog.Builder(requireContext())
            .setCustomTitle(title)  // Set custom title
            .setView(inputTask)
            .setPositiveButton("Add") { dialogInterface, _ ->
                val newTask = inputTask.text.toString()
                if (newTask.isNotEmpty()) {
                    taskList.add(newTask)
                    saveTasks()
                    taskAdapter.notifyItemInserted(taskList.size - 1)
                }
            }
            .setNegativeButton("Cancel", null)
            .create()

        // Customizing the button colors and dialog background color
        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)  // Set "Add" button color
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)  // Set "Cancel" button color

            // Set the dialog background to primaryLightColor
            dialog.window?.setBackgroundDrawableResource(R.color.primaryLightColor)
        }

        dialog.show()
    }

    // Function to edit an existing task
    private fun editTask(position: Int) {
        val inputTask = EditText(context)
        inputTask.setText(taskList[position])

        // Customizing the dialog title
        val title = TextView(context).apply {
            text = "Edit Task"
            gravity = Gravity.CENTER
            textSize = 20f
            setTypeface(null, android.graphics.Typeface.BOLD)
            setPadding(0, 30, 0, 30)
        }

        val dialog = AlertDialog.Builder(requireContext())
            .setCustomTitle(title)
            .setView(inputTask)
            .setPositiveButton("Update") { dialogInterface, _ ->
                val updatedTask = inputTask.text.toString()
                if (updatedTask.isNotEmpty()) {
                    taskList[position] = updatedTask
                    saveTasks()
                    taskAdapter.notifyItemChanged(position)
                }
            }
            .setNegativeButton("Cancel", null)
            .create()

        // Customizing the button colors and dialog background color
        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)

            // Set the dialog background to primaryLightColor
            dialog.window?.setBackgroundDrawableResource(R.color.primaryLightColor)
        }

        dialog.show()
    }

    // Function to delete a task
    private fun deleteTask(position: Int) {
        taskList.removeAt(position)
        saveTasks()
        taskAdapter.notifyItemRemoved(position)
    }

    // Save the tasks to SharedPreferences
    private fun saveTasks() {
        val editor = sharedPreferences.edit()
        editor.putStringSet("tasks", taskList.toSet())
        editor.apply()

        // Update the widget whenever tasks are changed
        val intent = Intent(requireContext(), WidgetTodoProvider::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        val ids = AppWidgetManager.getInstance(requireContext())
            .getAppWidgetIds(ComponentName(requireContext(), WidgetTodoProvider::class.java))
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        requireContext().sendBroadcast(intent)
    }

    // Load the tasks from SharedPreferences
    private fun loadTasks(): MutableList<String> {
        return sharedPreferences.getStringSet("tasks", setOf())?.toMutableList() ?: mutableListOf()
    }
}
