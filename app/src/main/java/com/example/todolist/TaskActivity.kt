package com.example.todolist

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_task.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*

const val DB_NAME = "todo.db"
class TaskActivity : AppCompatActivity(),View.OnClickListener {

    lateinit var myCalender:Calendar // this object holds the date month year
    lateinit var dateSetListener:DatePickerDialog.OnDateSetListener // interface that open the dialog
    lateinit var timeSetListener: TimePickerDialog.OnTimeSetListener  // interface that open the dialog of time

    var finalDate = 0L
    var finalTime = 0L
    private val labls = arrayListOf<String>("Personal","Business","Insurance","Shopping","Banking")
    val db by lazy{
        AppDatabase.getDatabase(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        dateEdt.setOnClickListener(this)
        timeEdt.setOnClickListener(this)
        saveBtn.setOnClickListener(this)
        setupSpinner()
    }
    private fun setupSpinner(){
         val adapter = ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,labls)
        labls.sort()
        spinnerCategory.adapter = adapter
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.dateEdt->{
                setListener()
            }
            R.id.timeEdt->{
                 setTimeListener()
            }
            R.id.saveBtn->{
                saveTodo()
            }
        }
    }
    private fun saveTodo(){
        val category = spinnerCategory.selectedItem.toString()
        val title = titleInpLay.editText?.text.toString()
        val description = taskInpLay.editText?.text.toString()

        // coroutines
        GlobalScope.launch(Dispatchers.Main) {
               var id = withContext(Dispatchers.IO){
                   return@withContext db.todoDao().inserTask(
                       TodoModel(
                           title,
                           description,
                           category,
                           finalDate,
                           finalTime
                       )
                   )
               }
            finish()
        }
    }
    private fun setTimeListener(){
         myCalender = Calendar.getInstance()
        timeSetListener = TimePickerDialog.OnTimeSetListener{_: TimePicker,hourOfDay:Int,min:Int->
              myCalender.set(Calendar.HOUR_OF_DAY,hourOfDay)
            myCalender.set(Calendar.MINUTE,min)
            updateTime()
        }
        val timePickerDialog = TimePickerDialog(
            this,
            timeSetListener,
            myCalender.get(Calendar.HOUR_OF_DAY),
            myCalender.get(Calendar.MINUTE),
            false
        )
        timePickerDialog.show()
    }
    private fun updateTime()
    {
        val myformat = "h:mm a"   //hour min am/pm
        val sdf = SimpleDateFormat(myformat)
        finalTime = myCalender.time.time
        timeEdt.setText(sdf.format(myCalender.time))

    }
    private fun setListener(){
        myCalender = Calendar.getInstance()
        // these parameter are selected from the dialog
        dateSetListener = DatePickerDialog.OnDateSetListener{ _: DatePicker,year:Int,month:Int,dayOfMonth:Int ->
            myCalender.set(Calendar.YEAR,year)
            myCalender.set(Calendar.MONTH,month)
            myCalender.set(Calendar.DAY_OF_MONTH,dayOfMonth)
            updateDate()
        }
        // this will set the data in the dialog
        val datePickerDialog = DatePickerDialog(
            this,
            dateSetListener,
            myCalender.get(Calendar.YEAR),
            myCalender.get(Calendar.MONTH),
            myCalender.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() // this will set the minimum time to current time only
        datePickerDialog.show()
    }
    private fun updateDate(){
        //mon, 5 jan 2020 // EEE-> day d-> day MMM-> month yyyy-> year
        val myformat = "EEE, d MMM yyyy"
        val sdf = SimpleDateFormat(myformat)
        finalDate = myCalender.time.time
        dateEdt.setText(sdf.format(myCalender.time))
        timeInpLay.visibility = View.VISIBLE
    }

}
