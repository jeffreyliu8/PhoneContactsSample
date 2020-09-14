package com.askjeffreyliu.mycontacts.ui.main


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.askjeffreyliu.mycontacts.R
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.detail_fragment.*


class DetailFragment : Fragment() {

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.detail_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.emailsLiveData.observe(viewLifecycleOwner, {
            if (it.isEmpty()) {
                emailButton.visibility = View.GONE
                emailTextView.visibility = View.GONE
            } else {
                emailTextView.text = it[0]
                emailButton.visibility = View.VISIBLE
                emailTextView.visibility = View.VISIBLE
            }
        })
        viewModel.phonesLiveData.observe(viewLifecycleOwner, {
            if (it.isEmpty()) {
                callButton.visibility = View.GONE
                phoneTextView.visibility = View.GONE
            } else {
                phoneTextView.text = it[0]
                callButton.visibility = View.VISIBLE
                phoneTextView.visibility = View.VISIBLE
            }
        })

        val item by navArgs<DetailFragmentArgs>()

        nameTextView.text = item.name
//        phoneTextView.text = item.phone
        if (item.star) {
            starView.visibility = View.VISIBLE
        } else {
            starView.visibility = View.GONE
        }
//            emailTextView.text = item.email

        val options = RequestOptions()
            .centerCrop()
            .error(R.drawable.ic_baseline_account_circle_24)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .priority(Priority.HIGH)

        Glide.with(requireContext())
            .load(item.photo)
            .apply(options)
            .into(avatarImageView)

        viewModel.fetchEmails(item.contactId)
        viewModel.fetchPhones(item.contactId)
    }
}