package com.makertech.assignmentquantamit.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VBinding :ViewBinding>(layout_id: Int
,private val bindingInflater: (inflater:LayoutInflater) ->VBinding
) :Fragment(layout_id) {
    private var _binding : VBinding? = null

    val binding :VBinding
    get() = _binding as VBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         _binding = bindingInflater.invoke(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observe()
    }

    open fun initViews(){}
    open fun observe(){}

    fun makeToast(text:String)
    {
        Toast.makeText(requireContext(),text,Toast.LENGTH_LONG).show()
    }
}