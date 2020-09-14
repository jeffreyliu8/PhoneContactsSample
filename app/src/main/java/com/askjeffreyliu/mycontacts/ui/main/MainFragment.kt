package com.askjeffreyliu.mycontacts.ui.main


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.askjeffreyliu.mycontacts.R
import com.askjeffreyliu.mycontacts.adapter.MyAdapter
import com.askjeffreyliu.mycontacts.model.MyContact
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel


    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initList()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel
    }


    private fun initList() {
        val testList = mutableListOf<MyContact>()
        testList.add(MyContact("asdf", "asdfsadf"))
        testList.add(MyContact("asdfas", "asdfdfsadf"))

        viewManager = LinearLayoutManager(requireContext())
        viewAdapter = MyAdapter {
            Logger.d(it.name)
        }.apply {
            updateList(testList)
        }

        recyclerView.apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter

        }
    }
}