package com.passiondroid.bugbuddy.ui

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.passiondroid.bugbuddy.BugBuddy
import com.passiondroid.bugbuddy.BugBuddy.IMAGE_PATH
import com.passiondroid.bugbuddy.R
import com.passiondroid.bugbuddy.data.models.IssueRequest
import com.passiondroid.imageeditorlib.ImageEditor
import kotlinx.android.synthetic.main.activity_bug_report.*

/**
 * Created by Arif Khan on 26/02/20.
 */
class BugReportActivity : AppCompatActivity() {

    private lateinit var viewModel: BugReportViewModel
    //TODO: Remove ProgressDialog with some cool animation
    private var progressDialog: ProgressDialog? = null
    private var imagePath = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bug_report)
        setSupportActionBar(toolbar)
        title = getString(R.string.report)
        imagePath = intent.extras.let {
            it?.getString(IMAGE_PATH,"").orEmpty()
        }

        viewModel = ViewModelProvider(this).get(BugReportViewModel::class.java)
        setUpView()

        observeLiveData()
        viewModel.getIssueMetaData()
    }

    private fun setUpView() {
        Glide.with(this).load(imagePath).into(imageView)
        imageView.setOnClickListener {
            ImageEditor.Builder(this, imagePath).open()
        }
        //TODO: Enable submit only when all fields are entered
        submitButton.setOnClickListener {
            val meta = viewModel.isssueMetaLiveData.value
            meta?.let {
                val projectId = meta.projects?.get(spinnerProject.selectedItemId.toInt())?.id
                val typeId = meta.projects?.get(spinnerProject.selectedItemId.toInt())?.issuetypes?.get(spinnerIssueType.selectedItemId.toInt())?.id
                val request = IssueRequest(IssueRequest.Fields(
                    titleET.text.toString(),
                    IssueRequest.Fields.Issuetype(typeId),
                    IssueRequest.Fields.Project(projectId),
                    descET.text.toString()
                ))
                viewModel.createIssue(request,imagePath)
                progressDialog = ProgressDialog(this)
            }
        }
    }

    private fun observeLiveData() {
        viewModel.isssueMetaLiveData.observe(this, Observer {issueMeta ->
            val projectAdapter = ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item)
            issueMeta.projects?.forEach {
                projectAdapter.add(it.name)
            }
            spinnerProject.adapter = projectAdapter
            projectAdapter.notifyDataSetChanged()
            progressProject.visibility = View.GONE

            //TODO: Set Issue type based on project
            // If the user selects Task or Subtask then get the supertask id
            val typeAdapter = ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item)
            issueMeta.projects?.forEach {
                it.issuetypes?.forEach { type ->
                    typeAdapter.add(type.name)
                }
            }
            spinnerIssueType.adapter = typeAdapter
            typeAdapter.notifyDataSetChanged()
            progressType.visibility = View.GONE
        })

        viewModel.issueStateLiveData.observe(this, Observer {
            when(it!!){
                BugReportViewModel.IssueState.CreatingIssue -> {
                    progressDialog?.setMessage("Creating Jira Ticket...")
                    progressDialog?.show()
                }
                BugReportViewModel.IssueState.IssueCreationFailed -> {
                    progressDialog?.dismiss()
                    AlertDialog.Builder(this)
                        .setMessage("Some error occured. Please try again or check the logs. Thanks")
                        .show()
                }
                BugReportViewModel.IssueState.UploadingAttachment -> { }
                BugReportViewModel.IssueState.UploadingAttachmentFailed -> {
                    progressDialog?.dismiss()
                    AlertDialog.Builder(this)
                        .setMessage("Issue created but due to some error image not uploaded. Please check logs. Thanks")
                        .setNeutralButton("Ok") { _, _ ->
                            val jiraLink = BugBuddy.getJiraEndpoint() + "browse/${viewModel.issueResponse!!.key}"
                            val dialog = BugInfoDialog()
                            val bundle = Bundle()
                            bundle.putString("LINK", jiraLink)
                            dialog.arguments = bundle
                            dialog.show(supportFragmentManager, "Dialog")
                        }
                        .show()
                }
                BugReportViewModel.IssueState.IssueCreated -> {
                    progressDialog?.dismiss()
                    val jiraLink = BugBuddy.getJiraEndpoint() + "browse/${viewModel.issueResponse!!.key}"
                    val dialog = BugInfoDialog()
                    val bundle = Bundle()
                    bundle.putString("LINK", jiraLink)
                    dialog.arguments = bundle
                    dialog.show(supportFragmentManager, "Dialog")
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            ImageEditor.RC_IMAGE_EDITOR ->
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val path = data.getStringExtra(ImageEditor.EXTRA_EDITED_PATH)
                    imageView.setImageBitmap(BitmapFactory.decodeFile(path))
                    imagePath = path.orEmpty()
                }
        }
    }

}