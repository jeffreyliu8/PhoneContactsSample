package com.askjeffreyliu.mycontacts.ui.main


import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.askjeffreyliu.mycontacts.R
import com.askjeffreyliu.mycontacts.model.ResourceState
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
        viewModel.emailsLiveData.observe(viewLifecycleOwner, { resource ->
            when (resource.state) {
                ResourceState.LOADING -> {
                    emailButton.visibility = View.INVISIBLE
                    emailTextView.visibility = View.INVISIBLE
                    emailProgress.visibility = View.VISIBLE
                }
                ResourceState.SUCCESS -> {
                    emailProgress.visibility = View.INVISIBLE
                    if (resource.data.isNullOrEmpty()) {
                        emailButton.visibility = View.INVISIBLE
                        emailTextView.visibility = View.INVISIBLE
                    } else {
                        emailTextView.text = resource.data[0]
                        emailButton.visibility = View.VISIBLE
                        emailTextView.visibility = View.VISIBLE
                        emailButton.setOnClickListener {
                            sendEmail(resource.data[0])
                        }
                    }
                }
            }
        })
        viewModel.phonesLiveData.observe(viewLifecycleOwner, { resource ->
            when (resource.state) {
                ResourceState.LOADING -> {
                    callButton.visibility = View.INVISIBLE
                    phoneTextView.visibility = View.INVISIBLE
                    phoneProgress.visibility = View.VISIBLE
                }
                ResourceState.SUCCESS -> {
                    phoneProgress.visibility = View.INVISIBLE
                    if (resource.data.isNullOrEmpty()) {
                        callButton.visibility = View.INVISIBLE
                        phoneTextView.visibility = View.INVISIBLE
                    } else {
                        phoneTextView.text = resource.data[0]
                        callButton.visibility = View.VISIBLE
                        phoneTextView.visibility = View.VISIBLE
                        callButton.setOnClickListener {
                            val intent =
                                Intent(
                                    Intent.ACTION_DIAL,
                                    Uri.fromParts("tel", resource.data[0], null)
                                )
                            startActivity(intent)
                        }
                    }
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

    private fun sendEmail(email: String) {
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.data = Uri.parse("mailto:")
        emailIntent.type = "message/rfc822"
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."))
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(
                requireContext(),
                "There is no email client installed.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}