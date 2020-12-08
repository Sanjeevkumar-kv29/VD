package com.example.vd

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.vd.Adapter.giftRVAdapter
import com.example.vd.Apiconfig.APIconfigure
import kotlinx.android.synthetic.main.activity_signgetgift.*
import org.json.JSONException
import java.util.ArrayList

class signgetgift : AppCompatActivity() {

    var UsersignupDatamap = hashMapOf<String, Any?>()

    private val giftNAME = ArrayList<String>()
    private val giftimgURL = ArrayList<String>()
    private val giftdiscription = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signgetgift)
        getSupportActionBar()?.hide()

        UsersignupDatamap = intent.getSerializableExtra("UserSignupData") as HashMap<String, Any?>

        getGifts()
    }



    fun getGifts(){


        val API = APIconfigure()
        val que = Volley.newRequestQueue(this)

        val req = JsonArrayRequest(
            Request.Method.GET,API.BASEURL+API.GETALLGIFTS,null, Response.Listener { response ->try {

                for (i in 0..response.length()-1) {
                    val responseOBJ = response.getJSONObject(i)
                    giftNAME.add(responseOBJ.getString("giftName"))
                    giftimgURL.add(responseOBJ.getString("giftImage"))
                    giftdiscription.add(responseOBJ.getString("giftDescription"))

                    Log.d("responseVal",response.getJSONObject(i).toString())
                }

                giftRecyclerView()

                Log.d("success","gift list get")
                //Log.d("Profile fetch",response.toString())


            } catch (e: JSONException) {
                e.printStackTrace()
            }
            },
            Response.ErrorListener {
                Toast.makeText(this,"Check Your Connection Or try Again Later".toString(), Toast.LENGTH_LONG).show()
                Log.d("ERROR","REQUEST FAILD")
            })

        que.add(req)
        req.setRetryPolicy(
            DefaultRetryPolicy(12000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        )

    }

    private fun giftRecyclerView() {
        // Log.d(TAG, "initRecyclerView: init recyclerview")
        val layoutManager = GridLayoutManager(this, 2)
        giftsRV.layoutManager = layoutManager
        val adapter = giftRVAdapter(this,giftNAME,giftimgURL,giftdiscription,UsersignupDatamap)
        giftsRV.adapter = adapter
    }


}