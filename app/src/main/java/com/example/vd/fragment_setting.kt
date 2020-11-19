package com.example.vd

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.vd.Apiconfig.APIconfigure
import com.example.vd.GalleryUploadFile.MyAPI
import com.example.vd.GalleryUploadFile.UploadRequestBody
import com.example.vd.GalleryUploadFile.UploadResponse
import com.example.vd.GalleryUploadFile.getFileName
import com.irozon.alertview.AlertActionStyle
import com.irozon.alertview.AlertStyle
import com.irozon.alertview.AlertView
import com.irozon.alertview.objects.AlertAction
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_setting.*
import kotlinx.android.synthetic.main.fragment_setting.Bankname
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.io.*


class fragment_setting : Fragment(),UploadRequestBody.UploadCallback {

    private var selectedImageUri: Uri? = null
    lateinit var imgURL:String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

            shimmer_view_containerst.startShimmerAnimation()

        val Getsharepref: SharedPreferences = this.activity!!.getSharedPreferences("LoginUserDetails",0)
        val id=Getsharepref.getString("user_id","").toString()


        getProfile(id)

        image_view.setOnClickListener {
            openImageChooser()
        }
        button_upload.setOnClickListener {
            uploadImage()
        }


        savebtn.setOnClickListener {
            if (Sname.text.isNullOrEmpty()){
                Sname.setError("Please enter name")
            }
            else if (Scontact.text.isNullOrEmpty()){
                Scontact.setError("Please enter Contact")
            }
            else if (Saddress.text.isNullOrEmpty()){
                Saddress.setError("Please enter Address")
            }
            else if (SEmail.text.isNullOrEmpty()){
                SEmail.setError("Please enter Email")
            }
            else if (Bankname.text.isNullOrEmpty()){
                Bankname.setError("Please enter Address")
            }
            else if (SAccountNo.text.isNullOrEmpty()){
                SAccountNo.setError("Please enter Address")
            }
            else if (SIFSCcode.text.isNullOrEmpty()){
                SIFSCcode.setError("Please enter Address")
            }
            else if (SAccountHolderName.text.isNullOrEmpty()){
                SAccountHolderName.setError("Please enter Address")
            }
            else{

                shimmer_view_containerst.visibility = View.VISIBLE
                shimmer_view_containerst.startShimmerAnimation()

                saveProfileDetails(id)
            }

        }

    }


    private fun saveProfileDetails(_uid:String) {

        val jsonobj = JSONObject()
        jsonobj.put("full_name",Sname.text)
        jsonobj.put("email",SEmail.text)
        jsonobj.put("address",Saddress.text)
        jsonobj.put("phone_no",Scontact.text)
        jsonobj.put("bankName",Bankname.text)
        jsonobj.put("account_no",SAccountNo.text)
        jsonobj.put("ifsc_code",SIFSCcode.text)
        jsonobj.put("account_holder_name",SAccountHolderName.text)
        jsonobj.put("profile_picture",imgURL)
        
        val jsonOBJFinal = JSONObject()
        jsonOBJFinal.put("uid",_uid)
        jsonOBJFinal.put("data",jsonobj)


        Log.d("SETTING-JSONOBJECT",jsonOBJFinal.toString())

        val API = APIconfigure()
        val que = Volley.newRequestQueue(context)

        val req = JsonObjectRequest(Request.Method.PUT,API.BASEURL+API.PROFILEDIT,jsonOBJFinal, Response.Listener { response ->try {
            Log.d("EDITsuccess","REQUEST GET")
            Log.d("EDITafterProfilefetch",response.toString())

            shimmer_view_containerst.stopShimmerAnimation()
            shimmer_view_containerst.visibility = View.GONE
            //Glide.with(context).asBitmap().load(profileurl).into(profile_image)


        } catch (e: JSONException) {
            e.printStackTrace()
        }
        },
            Response.ErrorListener {

                Toast.makeText(context,"Check Your Connection Or try Again Later".toString(), Toast.LENGTH_LONG).show()
                Log.d("ERROR","REQUEST FAILD")
            })

        que.add(req)
        req.setRetryPolicy(DefaultRetryPolicy(10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))

    }


    fun getProfile(Userid:String){

        val url = "https://vk-backend.herokuapp.com/users/c_profile?uid=${Userid}"
        val que = Volley.newRequestQueue(context)

        val req = JsonObjectRequest(Request.Method.GET,url,null, Response.Listener { response ->try {
            Log.d("success","REQUEST GET")
            Log.d("Profile fetch",response.toString())

            shimmer_view_containerst.stopShimmerAnimation()
            shimmer_view_containerst.visibility = View.GONE

            val payment_status = response["payment_status"]
            val wallet = response["wallet"]
            val lifetimeEarnings = response["lifetimeEarnings"]
            val contactno = response["phone_no"]
            val profileurl = response["profile_picture"]
            val fullname = response["full_name"]
            val address = response["address"]
            val email = response["email"]

            imgURL=profileurl.toString()

            val bankName = response["bankName"]
            val accountNo = response["account_no"]
            val Ifsccode = response["ifsc_code"]
            val Accountholdername = response["account_holder_name"]

            Glide.with(context).asBitmap().load(imgURL).into(image_view)

            Sname.setText(fullname.toString())
            Scontact.setText(contactno.toString())
            Saddress.setText(address.toString())
            SEmail.setText(email.toString())

            SAccountHolderName.setText(Accountholdername.toString())
            SAccountNo.setText(accountNo.toString())
            SIFSCcode.setText(Ifsccode.toString())
            Bankname.setText(bankName.toString())



        } catch (e: JSONException) {
            e.printStackTrace()
        }
        },
            Response.ErrorListener {

                Toast.makeText(context,"Check Your Connection Or try Again Later".toString(), Toast.LENGTH_LONG).show()
                Log.d("ERROR","REQUEST FAILD")
            })

        que.add(req)
        req.setRetryPolicy(DefaultRetryPolicy(10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))


    }



    private fun openImageChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE_PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_PICK_IMAGE -> {
                    selectedImageUri = data?.data
                    image_view.setImageURI(selectedImageUri)
                }
            }
        }
    }

    private fun uploadImage() {

        if (selectedImageUri == null) {
            val alert = AlertView("ALERT....", "Please choose image by Click the image", AlertStyle.DIALOG)
            alert.addAction(AlertAction("ok", AlertActionStyle.DEFAULT, { action -> }))
            alert.show(activity as AppCompatActivity)
            return
        }

        progress_bar.visibility = View.VISIBLE
        val parcelFileDescriptor =  context?.contentResolver?.openFileDescriptor(selectedImageUri!!, "r", null)
            ?: return

        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file = File(activity?.cacheDir,activity?.contentResolver?.getFileName(selectedImageUri!!)
        )
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)


        progress_bar.progress = 0

        val body = UploadRequestBody(file, "image", callback = this)

        //LoginApiFunCall(MultipartBody.Part.createFormData("image",file.name,body))

        MyAPI().uploadImage(
            MultipartBody.Part.createFormData(
                "image",
                file.name,
                body
            ),
            RequestBody.create(MediaType.parse("multipart/form-data"), "json")
        ).enqueue(object : Callback<UploadResponse> {
            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {

                val alert = AlertView("Error", "An error Occured Please Try again Later", AlertStyle.DIALOG)
                alert.addAction(AlertAction("ok", AlertActionStyle.DEFAULT, { action -> }))
                alert.show(activity as AppCompatActivity)
                //layout_root.snackbar(t.message!!)
                progress_bar.progress = 0
            }

            override fun onResponse(
                call: Call<UploadResponse>,
                response: retrofit2.Response<UploadResponse>
            ) {
                response.body()?.let {
                    //layout_root.snackbar(it.toString())
                    progress_bar.progress = 100
                    imgURL = it.url
                    val alert = AlertView("Profile Uploaded", "Image is Uploaded Click Save button to save", AlertStyle.DIALOG)
                    alert.addAction(AlertAction("ok", AlertActionStyle.DEFAULT, { action -> }))
                    alert.show(activity as AppCompatActivity)
                    Log.d("result",it.toString())
                    progress_bar.visibility=View.GONE
                }
            }
        })

    }

    override fun onProgressUpdate(percentage: Int) {
        progress_bar.progress = percentage
    }

    companion object {
        const val REQUEST_CODE_PICK_IMAGE = 101
    }



}