package com.example.cosmosinhand.ui.fragment

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.cosmosinhand.R
import com.example.cosmosinhand.models.database.DatabaseItem
import com.example.cosmosinhand.ui.CosmosActivity
import com.example.cosmosinhand.ui.CosmosViewModel
import kotlinx.android.synthetic.main.fragment_desc.*
import com.example.cosmosinhand.util.Resource
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import java.util.*


class DescFragment : Fragment(R.layout.fragment_desc), TextToSpeech.OnInitListener {
    val args: DescFragmentArgs by navArgs()
    var tts: TextToSpeech? = null
    lateinit var viewModel: CosmosViewModel
    lateinit var item: DatabaseItem
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // initialising the viewModel
        viewModel = (activity as CosmosActivity).viewmodel

        //initialising text to speech
        tts = TextToSpeech(this.context, this)

        // collectin all the arguments comming from the previous fragment
        val descriptionDesc = args.description
        val valueofimg = args.url
        val imgnext = args.urlpre

        // initialising the data item
        item = DatabaseItem(0, valueofimg, descriptionDesc)

        // implementing the function of the button to save the item to the database
        btn_desc.setOnClickListener {
            viewModel.saveIntoDatabase(item)
        }

        // implementing the function of the text to speech on textview is clicked
        tv_desc.setOnClickListener {
            speakOut(tv_desc.text.toString())
        }


        //checking the default value of (valueofimg)
        // if it is not that value
        // that means control jumps from Apod fragment
        // else control jumps from iavl fragment and
        // accodingly setting the image in the image view
        if (valueofimg != "abc")
            Glide.with(this).load(valueofimg).into(iv_desc)
        else {
            viewModel.getImageList(imgnext)
            viewModel.imagelist.observe(viewLifecycleOwner, Observer {
                when (it) {
                    is Resource.Success -> {

                        var i = 0;
                        for (name in it.data!!) {
                            if (name.contains(".jpg") || name.contains(".png")) {
                                Glide.with(this).load(name).override(1080, 720)
                                    .into(iv_desc)
                                i++;
                                item.ure = name
                            }

                        }
                        if (i == 0) {
                            Glide.with(this)
                                .load("https://images-assets.nasa.gov/image/PIA18906/PIA18906~orig.jpg")
                                .into(iv_desc)
                        }

                    }

                    is Resource.Error -> {
                        Log.e("Error", "error occurred in desc fragment in parsing the result")
                    }
                    is Resource.Loding -> {
                        Log.e("Error", "imgage is still Loading ")
                    }

                }


            })


        }
        tv_desc.text = descriptionDesc
    }

    // initialising text to speech
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts!!.setLanguage(Locale("en", "IN"))
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("vk", "not supported")
            }

        } else {
            Log.e("Error", "inititalization failed ")
        }
    }

    // setting up text to speech
    private fun speakOut(text: String) {
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    // stopping the text to speech when fragment is destroyed
    override fun onDestroy() {
        super.onDestroy()
        tts?.stop()
    }
}