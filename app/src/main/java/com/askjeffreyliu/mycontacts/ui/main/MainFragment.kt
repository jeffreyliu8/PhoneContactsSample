package com.askjeffreyliu.mycontacts.ui.main


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.askjeffreyliu.mycontacts.R
import com.askjeffreyliu.mycontacts.adapter.MyAdapter
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
        private const val PERMISSIONS_REQUEST_CODE = 123
        private val PERMISSIONS = arrayOf(Manifest.permission.READ_CONTACTS)
    }

    private lateinit var viewModel: MainViewModel


    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var mAdapter: MyAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUIListeners()
        initList()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.contactsLiveData.observe(viewLifecycleOwner, {
            mAdapter.updateList(it)
        })
    }

    private fun initUIListeners() {
        permissionButton.setOnClickListener {
            showSystemPermissionRequestWindow()
        }
    }

    private fun initList() {
        viewManager = LinearLayoutManager(requireContext())
        mAdapter = MyAdapter {
            Logger.d(it.name)
        }
        viewAdapter = mAdapter

        contactRecyclerView.apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter
        }
    }

    private fun hasPermissionAndShowList(hasPermission: Boolean) {
        if (hasPermission) {
            permissionButton.visibility = View.GONE
            viewModel.fetchContacts()
        } else {
            permissionButton.visibility = View.VISIBLE
        }
    }

    override fun onStart() {
        super.onStart()
        tryGetPermissionAndExecuteTask()
    }

    private fun tryGetPermissionAndExecuteTask() {
        // check if we have the permission
        if (!hasPermissions(requireContext(), PERMISSIONS[0])) {
            // if not, we explain why we need it first
            AlertDialog.Builder(requireContext())
                .setTitle("Permission Request")
                .setMessage("With these permissions, we can operate the peripherals")
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    showSystemPermissionRequestWindow()
                }
                .show()
            hasPermissionAndShowList(false)
        } else { // we already have permission
            hasPermissionAndShowList(true)
        }
    }

    private fun hasPermissions(context: Context, vararg permissions: String): Boolean =
        permissions.all {
            ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

    private fun showSystemPermissionRequestWindow() {
        // Should we show an explanation?
//        if (ActivityCompat.shouldShowRequestPermissionRationale(
//                requireActivity(),
//                PERMISSIONS[0]
//            )
//        ) {
//
//            textView.setText("Got rejected the first time, and now we need this permission again")
//            // Show an explanation to the user *asynchronously* -- don't block
//            // this thread waiting for the user's response! After the user
//            // sees the explanation, try again to request the permission.
//        }

        requestPermissions(
            PERMISSIONS,
            PERMISSIONS_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSIONS_REQUEST_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() &&
                    allPermissionsCheckGranted(grantResults)
                ) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    // add your logic here
                    hasPermissionAndShowList(true)
                } else {
                    Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_LONG).show()
                    hasPermissionAndShowList(false)
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }
        }// other 'case' lines to check for other
        // permissions this app might request
    }

    private fun allPermissionsCheckGranted(grantResults: IntArray): Boolean {
        for (grantResult in grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }
}