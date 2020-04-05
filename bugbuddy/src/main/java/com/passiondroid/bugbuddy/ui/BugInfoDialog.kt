package com.passiondroid.bugbuddy.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.passiondroid.bugbuddy.R
import kotlinx.android.synthetic.main.activity_bug_info.*

/**
 * Created by Arif Khan on 01/03/20.
 */
class BugInfoDialog : DialogFragment() {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_Material_Light_Dialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_bug_info, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setClickListeners()
    }

    private fun setClickListeners() {
        val link = arguments?.getString("LINK")
        link?.let {
            openBtn.setOnClickListener {
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(link)
                startActivity(i)
                dismiss()
            }

            shareBtn.setOnClickListener {
                val shareIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_STREAM, link)
                    type = "text/plain"
                    dismiss()
                }
                startActivity(Intent.createChooser(shareIntent, resources.getText(R.string.send_to)))
            }

            copyBtn.setOnClickListener {
                val clipboard = activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("Issue Link", link)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(activity,"Link copied",Toast.LENGTH_SHORT).show()
                dismiss()
            }
        }
    }


}