package com.example.testdemo.coroutine.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.testdemo.R

class CoroutineFragment : Fragment() {

    companion object {
        fun newInstance() = CoroutineFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        val tv = view?.findViewById<TextView>(R.id.tv)
        viewModel.result.observe(viewLifecycleOwner) {
            tv?.text = "fan $it"
        }
        viewModel.success("on Create View")


    }

}