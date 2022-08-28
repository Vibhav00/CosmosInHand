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
        viewModel = (activity as CosmosActivity).viewmodel
        tts = TextToSpeech(this.context, this)
        val descriptionDesc = args.description
        val valueofimg = args.url
        val imgnext = args.urlpre
        Log.e("vibav-desc", descriptionDesc)
        Log.e("vibav-valofimg", valueofimg)
        Log.e("imgenext", imgnext)
        Log.e("check", valueofimg)
        item = DatabaseItem(0, valueofimg, descriptionDesc)
        btn_desc.setOnClickListener {
            //  item?.let { it1 ->
            viewModel.saveIntoDatabase(item)
            Log.e("aman", item.toString())
        }
        tv_desc.setOnClickListener {
            speakOut(tv_desc.text.toString())
        }


        if (valueofimg != "abc")
            Glide.with(this).load(valueofimg).into(iv_desc)
        else {
            viewModel.getImageList(imgnext)
            viewModel.imagelist.observe(viewLifecycleOwner, Observer {


                when (it) {


                    is Resource.Success -> {

                        Log.e("vibahvimage", it.data.toString())
                        // val s=it.data?.get(0)
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
                        Log.e("vkvkv", "error")
                    }
                    is Resource.Loding -> {

                    }

                }


            })


        }
        //
        tv_desc.text = descriptionDesc
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts!!.setLanguage(Locale("en", "IN"))
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("vk", "not supported")
            }

        } else {
            Log.e("vk", "inititalization failed ")
        }
    }

    private fun speakOut(text: String) {
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    override fun onDestroy() {
        super.onDestroy()
        tts?.stop()
    }
}