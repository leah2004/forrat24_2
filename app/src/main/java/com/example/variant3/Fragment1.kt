package com.example.variant3

import android.annotation.SuppressLint
import android.content.Context
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
 * Use the [Fragment1.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment1 : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var navController: NavController
    private lateinit var date: CalendarView
    private lateinit var task: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)

            val calendar = Calendar.getInstance()
            date.minDate = calendar.timeInMillis
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_1, container, false)

        // Инициализация элементов управления
        date = view.findViewById(R.id.calendarView)
        task = view.findViewById(R.id.ed_name)

        // Переменная для хранения выбранной даты
        var datet: String = ""

        // Установка слушателя для CalendarView
        date.setOnDateChangeListener { _, year, month, dayOfMonth ->
            // Обработка выбранной даты
            datet = "$dayOfMonth/${month + 1}/$year"
        }


        // Установка слушателя для кнопки
        view.findViewById<Button>(R.id.button_ok).setOnClickListener {
            val taskt = task.text.toString()
            if (datet.isNotEmpty() && taskt.isNotEmpty()) {
                    // Сохранение данных в SharedPreferences
                    saveData(datet, taskt)
                    // Навигация к следующему фрагменту
                    navController.navigate(R.id.fragment2)
            } else {
                // Можно вывести сообщение об ошибке
                Toast.makeText(
                    requireContext(),
                    "Пожалуйста, заполните все поля",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        return view
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
    private fun saveData(date: String, task: String) {
        val sharedPreferences = requireActivity().getSharedPreferences("history_list", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("date", date)
        editor.putString("task", task)
        editor.apply()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Fragment1.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Fragment1().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}