package com.askjeffreyliu.mycontacts.ui.main


import android.content.Intent
import android.net.Uri
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
        viewModel.emailsLiveData.observe(viewLifecycleOwner, { emailList ->
            if (emailList.isEmpty()) {
                emailButton.visibility = View.GONE
                emailTextView.visibility = View.GONE
            } else {
                emailTextView.text = emailList[0]
                emailButton.visibility = View.VISIBLE
                emailTextView.visibility = View.VISIBLE
                emailButton.setOnClickListener {
                    val i = Intent(Intent.ACTION_SEND)
                    i.putExtra(Intent.EXTRA_EMAIL, emailList[0])
                    i.putExtra(Intent.EXTRA_SUBJECT, "Enter subject here")
                    i.type = "message/rfc822"
                    startActivity(Intent.createChooser(i, "Choose an Email client :"))
                }
            }
        })
        viewModel.phonesLiveData.observe(viewLifecycleOwner, { phoneList ->
            if (phoneList.isEmpty()) {
                callButton.visibility = View.GONE
                phoneTextView.visibility = View.GONE
            } else {
                phoneTextView.text = phoneList[0]
                callButton.visibility = View.VISIBLE
                phoneTextView.visibility = View.VISIBLE
                callButton.setOnClickListener {
                    val intent =
                        Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneList[0], null))
                    startActivity(intent)
                }
            }
        })

        val item by navArgs<DetailFragmentArgs>()

        nameTextView.text = item.name
        if (item.star) {
            starView.visibility = View.VISIBLE
        } else {
            starView.visibility = View.GONE
        }

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