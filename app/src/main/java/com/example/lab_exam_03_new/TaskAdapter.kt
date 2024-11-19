import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lab_exam_03_new.R

class TaskAdapter(
    private val tasks: MutableList<String>,
    private val editTask: (Int) -> Unit,
    private val deleteTask: (Int) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.taskName.text = task

        holder.editIcon.setOnClickListener {
            editTask(position)
        }

        holder.deleteIcon.setOnClickListener {
            deleteTask(position)
        }
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskName: TextView = itemView.findViewById(R.id.tv_task_name)
        val editIcon: ImageView = itemView.findViewById(R.id.iv_edit_task)
        val deleteIcon: ImageView = itemView.findViewById(R.id.iv_delete_task)
        val checkBox: CheckBox = itemView.findViewById(R.id.cb_task1)
    }
}
