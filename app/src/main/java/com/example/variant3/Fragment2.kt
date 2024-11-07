package com.example.variant3

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment2.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment2 : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var navController: NavController
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var date: TextView
    private lateinit var task: TextView
    private lateinit var yet: EditText

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
        val view = inflater.inflate(R.layout.fragment_2, container, false)

        yet = view.findViewById(R.id.ed_yet)
        date = view.findViewById(R.id.date)
        task = view.findViewById(R.id.task)

        sharedPreferences = requireActivity().getSharedPreferences("history_list", Context.MODE_PRIVATE)

        val dateValue= sharedPreferences.getString("date", null)
        val taskValue = sharedPreferences.getString("task", null)

        date.text = dateValue
        task.text = taskValue

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()

        view.findViewById<Button>(R.id.button_f2).setOnClickListener {
            val yet = yet.text.toString()
            if (yet.isNotEmpty()) {

                val task = Task(
                    date.text.toString(),
                    task.text.toString(),
                    yet,
                )
                Fragment3.addHis(requireContext(), task)
                saveData(yet)
                navController.navigate(R.id.fragment3)

            }
            else {
                Toast.makeText(requireContext(), "Введите подробную информацию", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveData(yet: String) {
        sharedPreferences = requireActivity().getSharedPreferences("history_list", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("yet", yet)
        editor.apply()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Fragment2.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Fragment2().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}