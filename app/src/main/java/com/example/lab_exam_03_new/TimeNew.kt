package com.example.lab_exam_03_new

import android.app.Dialog
import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Chronometer
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class TimeNew : Fragment() {

    private var timeSelected: Int = 0
    private var timeCountDown: CountDownTimer? = null
    private var timeProgress = 0
    private var pauseOffset: Long = 0
    private var isStart = true

    private lateinit var progressBar: ProgressBar
    private lateinit var timeLeftTv: TextView
    private lateinit var startBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_time_new, container, false)

        // Initialize views
        progressBar = view.findViewById(R.id.pbTimer)
        timeLeftTv = view.findViewById(R.id.tvTimeLeft)
        startBtn = view.findViewById(R.id.btnPlayPause)
        val addBtn: ImageButton = view.findViewById(R.id.btnAdd)
        val resetBtn: ImageButton = view.findViewById(R.id.ib_reset)
        val addTimeTv: TextView = view.findViewById(R.id.tv_addTime)

        // Set up button click listeners
        addBtn.setOnClickListener { setTimeFunction() }
        startBtn.setOnClickListener { startTimerSetup() }
        resetBtn.setOnClickListener { resetTime() }
        addTimeTv.setOnClickListener { addExtraTime() }

        return view
    }

    private fun addExtraTime() {
        if (timeSelected != 0) {
            timeSelected += 15
            progressBar.max = timeSelected
            timePause()
            startTimer(pauseOffset)
            Toast.makeText(requireContext(), "15 sec added", Toast.LENGTH_SHORT).show()
        }
    }

    private fun resetTime() {
        timeCountDown?.cancel()
        timeProgress = 0
        timeSelected = 0
        pauseOffset = 0
        timeCountDown = null
        startBtn.text = "Start"
        isStart = true
        progressBar.progress = 0
        timeLeftTv.text = "0"
    }

    private fun timePause() {
        timeCountDown?.cancel()
    }

    private fun startTimerSetup() {
        if (timeSelected > timeProgress) {
            if (isStart) {
                startBtn.text = "Pause"
                startTimer(pauseOffset)
                isStart = false
            } else {
                isStart = true
                startBtn.text = "Resume"
                timePause()
            }
        } else {
            Toast.makeText(requireContext(), "Enter Time", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startTimer(pauseOffsetL: Long) {
        progressBar.progress = timeProgress
        timeCountDown = object : CountDownTimer(
            (timeSelected * 1000).toLong() - pauseOffsetL * 1000, 1000
        ) {
            override fun onTick(p0: Long) {
                timeProgress++
                pauseOffset = timeSelected.toLong() - p0 / 1000
                progressBar.progress = timeSelected - timeProgress
                timeLeftTv.text = (timeSelected - timeProgress).toString()
            }

            override fun onFinish() {
                resetTime()
                Toast.makeText(requireContext(), "Times Up!", Toast.LENGTH_SHORT).show()
            }
        }.start()
    }

    private fun setTimeFunction() {
        val timeDialog = Dialog(requireContext())
        timeDialog.setContentView(R.layout.add_dialog)
        val timeSet = timeDialog.findViewById<EditText>(R.id.etGetTime)
        timeDialog.findViewById<Button>(R.id.btnOk).setOnClickListener {
            if (timeSet.text.isEmpty()) {
                Toast.makeText(requireContext(), "Enter Time Duration", Toast.LENGTH_SHORT).show()
            } else {
                resetTime()
                timeLeftTv.text = timeSet.text
                startBtn.text = "Start"
                timeSelected = timeSet.text.toString().toInt()
                progressBar.max = timeSelected
            }
            timeDialog.dismiss()
        }
        timeDialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        timeCountDown?.cancel()
        timeProgress = 0
    }
}
