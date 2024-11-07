package com.example.variant3

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment3.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment3 : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var sharedPreferences: SharedPreferences
    private var currentPosition: Int = -1
    private lateinit var navController: NavController
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: Adapter
    private var historyList: MutableList<Task> = mutableListOf()
    private lateinit var data2: CalendarView
    private lateinit var task2: EditText

    private lateinit var yet2: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_3, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        data2 = view.findViewById(R.id.calendarView)
        task2 = view.findViewById(R.id.task2)
        yet2 = view.findViewById(R.id.yet2)
        var datet: String = ""

// Установка слушателя для CalendarView
        data2.setOnDateChangeListener { _, year, month, dayOfMonth ->
            // Обработка выбранной даты
            datet = "$dayOfMonth/${month + 1}/$year"
        }

// Инициализация navController
        navController = findNavController()

// Инициализация RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

// Инициализация адаптера
        adapter = Adapter(historyList) { position ->

            currentPosition = position
            val selectedItem = historyList[position]

            val dateParts = selectedItem.date.split("/")
            if (dateParts.size == 3) {
                data2.setDate(Calendar.getInstance().apply {
                    set(Calendar.YEAR, dateParts[2].toInt())
                    set(Calendar.MONTH, dateParts[1].toInt() - 1) // Месяцы начинаются с 0
                    set(Calendar.DAY_OF_MONTH, dateParts[0].toInt())
                }.timeInMillis, true, true)
            }
            task2.setText(selectedItem.task)
            yet2.setText(selectedItem.yet)
        }

// Установка адаптера для RecyclerView
        recyclerView.adapter = adapter

// Загрузка истории
        loadHistory()

// Установка слушателя для кнопки редактирования
        view.findViewById<Button>(R.id.editBut).setOnClickListener {
            if (task2.text.toString().isNotEmpty() && yet2.text.toString().isNotEmpty()) {

                    if (currentPosition != -1) {
                        val newItem = Task(
                            datet.toString(),
                            task2.text.toString(),
                            yet2.text.toString()
                        )
                        updateItem(currentPosition, newItem)
                    }
            } else {
                Toast.makeText(
                    requireContext(),
                    "Пожалуйста, заполните все поля",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

// Установка слушателя для кнопки возврата
        view.findViewById<Button>(R.id.button_return).setOnClickListener {
            navController.navigate(R.id.fragment1)
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun loadHistory() {
        sharedPreferences = requireActivity().getSharedPreferences("history_list", Context.MODE_PRIVATE)
        val json = sharedPreferences.getString("history_list", null)
        if (json != null) {
            val type = object : TypeToken<List<Task>>() {}.type
            val savedHis: List<Task> = Gson().fromJson(json, type)
            historyList.clear()
            historyList.addAll(savedHis)
            adapter.notifyDataSetChanged()
        }
    }

    fun isValidDate(dateString: String, dateFormat: String = "dd.MM.yyyy"): Boolean {
        val sdf = SimpleDateFormat(dateFormat, Locale.getDefault())
        sdf.isLenient = false
        return try {
            sdf.parse(dateString) != null
        } catch (e: ParseException) {
            false
        }
    }

    fun updateItem(position: Int, newItem: Task) {
        historyList[position] = newItem
        adapter.notifyItemChanged(position)
        saveHistory()
    }

    private fun saveHistory() {
        val sharedPreferences = requireActivity().getSharedPreferences("history_list", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val json = Gson().toJson(historyList)
        editor.putString("history_list", json)
        editor.apply()
    }

    companion object {
        fun addHis(context: Context, task: Task) {
            val sharedPreferences =
                context.getSharedPreferences("history_list", Context.MODE_PRIVATE)
            val json = sharedPreferences.getString("history_list", null)
            val taskList = if (json != null) {
                val type = object : TypeToken<MutableList<Task>>() {}.type
                val savedTasks: MutableList<Task> = Gson().fromJson(json, type)
                savedTasks
            } else {
                mutableListOf()
            }
            taskList.add(task)
            sharedPreferences.edit().putString("history_list", Gson().toJson(taskList)).apply()
        }
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Fragment3().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}