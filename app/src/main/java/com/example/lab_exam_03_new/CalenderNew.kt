package com.example.yourapp

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.lab_exam_03_new.R
import com.example.lab_exam_03_new.ReminderReceiver
import java.util.*

class CalenderNew : Fragment() {

    private lateinit var datePicker: DatePicker
    private lateinit var timePicker: TimePicker
    private lateinit var notesEditText: EditText
    private lateinit var setReminderButton: Button
    private lateinit var remindersListView: ListView

    private val reminders = mutableListOf<String>()
    private lateinit var remindersAdapter: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calender_new, container, false)

        datePicker = view.findViewById(R.id.datePicker)
        timePicker = view.findViewById(R.id.timePicker)
        notesEditText = view.findViewById(R.id.notesEditText)
        setReminderButton = view.findViewById(R.id.setReminderButton)
        remindersListView = view.findViewById(R.id.remindersListView)

        remindersAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, reminders)
        remindersListView.adapter = remindersAdapter

        // Load saved reminders
        loadReminders()

        setReminderButton.setOnClickListener {
            setReminder()
        }

        return view
    }

    private fun setReminder() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !canScheduleExactAlarms()) {
            // Prompt user to enable the permission in settings
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
            startActivity(intent)
            return
        }

        val calendar = Calendar.getInstance().apply {
            set(datePicker.year, datePicker.month, datePicker.dayOfMonth, timePicker.hour, timePicker.minute, 0)
        }

        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), ReminderReceiver::class.java).apply {
            putExtra("REMINDER_MESSAGE", notesEditText.text.toString())
        }

        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            System.currentTimeMillis().toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmClockInfo = AlarmManager.AlarmClockInfo(calendar.timeInMillis, pendingIntent)
        alarmManager.setAlarmClock(alarmClockInfo, pendingIntent)

        val reminderText = "${notesEditText.text} at ${calendar.time}"
        reminders.add(reminderText)
        remindersAdapter.notifyDataSetChanged()
        saveReminder(reminderText)

        notesEditText.text.clear()

        Toast.makeText(requireContext(), "Reminder set successfully!", Toast.LENGTH_SHORT).show()
    }



    // Check if the app can schedule exact alarms
    private fun canScheduleExactAlarms(): Boolean {
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else {
            true // No need to check for older versions
        }
    }

    // Save reminders to SharedPreferences
    private fun saveReminder(reminder: String) {
        val sharedPreferences =
            requireContext().getSharedPreferences("Reminders", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val existingReminders =
            sharedPreferences.getStringSet("remindersList", mutableSetOf()) ?: mutableSetOf()
        existingReminders.add(reminder)

        editor.putStringSet("remindersList", existingReminders)
        editor.apply()
    }

    // Load reminders from SharedPreferences
    private fun loadReminders() {
        val sharedPreferences =
            requireContext().getSharedPreferences("Reminders", Context.MODE_PRIVATE)
        val savedReminders =
            sharedPreferences.getStringSet("remindersList", mutableSetOf()) ?: return

        reminders.addAll(savedReminders)
        remindersAdapter.notifyDataSetChanged()
    }
}
