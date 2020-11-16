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
import androidx.fragment.app.Fragment
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_setting.*
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException


class fragment_setting : Fragment() {


    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    var encodeImageString: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        val Getsharepref: SharedPreferences = this.activity!!.getSharedPreferences("LoginUserDetails",0)
        val id=Getsharepref.getString("user_id","").toString()


        getProfile(id)


        Sprofile_image.setOnClickListener {

            launchGalleryAndSelectImage()

        }

        savebtn.setOnClickListener {

           saveProfileDetails(id)

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
        
        val jsonOBJFinal = JSONObject()
        jsonOBJFinal.put("uid",_uid)
        jsonOBJFinal.put("data",jsonobj)


        Log.d("JSONOBJECT",jsonOBJFinal.toString())

        val url = "https://vk-backend.herokuapp.com/users/c_profileEdit"
        val que = Volley.newRequestQueue(context)

        val req = JsonObjectRequest(Request.Method.PUT,url,jsonOBJFinal, Response.Listener { response ->try {
            Log.d("EDITsuccess","REQUEST GET")
            Log.d("EDITafterProfilefetch",response.toString())



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

    }

    fun getProfile(Userid:String){

        val url = "https://vk-backend.herokuapp.com/users/c_profile?uid=${Userid}"
        val que = Volley.newRequestQueue(context)

        val req = JsonObjectRequest(Request.Method.GET,url,null, Response.Listener { response ->try {
            Log.d("success","REQUEST GET")
            Log.d("Profile fetch",response.toString())


            val payment_status = response["payment_status"]
            val wallet = response["wallet"]
            val lifetimeEarnings = response["lifetimeEarnings"]
            val contactno = response["phone_no"]
            val profileurl = response["profile_picture"]
            val fullname = response["full_name"]
            val address = response["address"]
            val email = response["email"]

            val bankName = response["bankName"]
            val accountNo = response["account_no"]
            val Ifsccode = response["ifsc_code"]
            val Accountholdername = response["account_holder_name"]

            //Glide.with(context).asBitmap().load(profileurl).into(profile_image)

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

    }




    private fun launchGalleryAndSelectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if(data == null || data.data == null){
                return
            }

            filePath = data.data


            Toast.makeText(this.context, filePath?.path,Toast.LENGTH_LONG).show()
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(this.activity?.contentResolver, filePath)

                Sprofile_image.setImageBitmap(bitmap)


            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }




    fun LoginApiFunCall(uri:String){

        val url = "https://vk-backend.herokuapp.com/users/c_upload_photo"
        val que = Volley.newRequestQueue(this.context)

        val jsonobj = JSONObject()
        jsonobj.put("image",uri)
        Log.d("JSONOBJECT",jsonobj.toString())

        val req = VolleyMultipartRequest(Request.Method.POST,url, Response.Listener { response ->try {
            Log.d("success","REQUEST GET")
            Log.d("successFetchData",response.toString())

            Toast.makeText(this.context,"WELCOME BACK...", Toast.LENGTH_LONG).show()


        } catch (e: JSONException) {
            e.printStackTrace()
        }

        }, Response.ErrorListener {

            Log.d("ERRORinGetImgURL",it.toString())
            Log.d("ERRORinGetImgURL","REQUEST FAILD")

        })

        que.add(req)
        req.setRetryPolicy(
            DefaultRetryPolicy(
                120000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
        )


    }



}