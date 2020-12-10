package com.example.vd

import android.R.attr.password
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.vd.Adapter.childsRVAdapter
import com.example.vd.Apiconfig.APIconfigure
import com.irozon.alertview.AlertActionStyle
import com.irozon.alertview.AlertStyle
import com.irozon.alertview.AlertView
import com.irozon.alertview.objects.AlertAction
import kotlinx.android.synthetic.main.fragment_tree.*
import org.json.JSONArray
import java.util.ArrayList


class fragment_tree : Fragment() {

    private val childNAME = ArrayList<String>()
    private val childimgURL = ArrayList<String>()
    private val mrefferalmobno = ArrayList<String>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tree, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        shimmer_view_container.startShimmerAnimation()

        val Getsharepref: SharedPreferences = this.activity!!.getSharedPreferences("LoginUserDetails",0)
        val refcode=Getsharepref.getString("refferal_code","")
        val token=Getsharepref.getString("accessToken","")

        childNAME.clear()
        childimgURL.clear()
        mrefferalmobno.clear()

        getchild(refcode!!.toString(), token.toString())

    }


    fun getchild(parentid:String,token:String){

        val MyRequestQueue = Volley.newRequestQueue(context)
        val API = APIconfigure() // <----enter your post url here

        val MyStringRequest: StringRequest = object : StringRequest( Method.POST,API.BASEURL+API.GETCHILD, Response.Listener {
                //This code is executed if the server responds, whether or not the response contains data.
                //The String 'response' contains the server's response.
            val response = JSONArray(it)
            for (i in 0..response.length()-1) {
                val responseOBJ = response.getJSONObject(i)
                childNAME.add(responseOBJ["full_name"].toString())
                childimgURL.add(responseOBJ["profile_picture"].toString())
                mrefferalmobno.add(responseOBJ["phone_no"].toString())
                //println(responseOBJ["phone_no"])
                Log.d("responseVal",response.getJSONObject(i).toString())
            }
            shimmer_view_container.stopShimmerAnimation()
            shimmer_view_container.visibility = View.GONE
            CatICONRecyclerView()
            Log.d("Childs",it)

            },
            Response.ErrorListener
            {
                if ( it.networkResponse.statusCode == 401){

                       val sharepref: SharedPreferences = activity!!.getSharedPreferences("LoginUserDetails",0)
                        val editor: SharedPreferences.Editor = sharepref.edit()
                        editor.clear()
                        editor.commit()
                        startActivity(Intent(activity,MainActivity::class.java))
                        activity?.finish()
                }
                //This code is executed if there is an error.
            }) {

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["x-auth-token"] = token
                return headers
            }

            override fun getParams(): Map<String, String> {
                val MyData: MutableMap<String, String> = HashMap()
                MyData["parentID"] = parentid
                return MyData
            }
        }
        MyRequestQueue.add(MyStringRequest)
        MyStringRequest.setRetryPolicy(DefaultRetryPolicy(10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))
    }

    private fun CatICONRecyclerView() {
        // Log.d(TAG, "initRecyclerView: init recyclerview")
        val layoutManager = GridLayoutManager(context, 2)
        childsRV.layoutManager = layoutManager
        val adapter = childsRVAdapter(context,childNAME,childimgURL,mrefferalmobno)
        childsRV.adapter = adapter
    }

    override fun onDestroy() {


        try {
            val fragmentManager: androidx.fragment.app.FragmentManager = activity!!.supportFragmentManager
            val ft: FragmentTransaction = fragmentManager.beginTransaction()
            ft.remove(this)
            ft.commit()
        } catch (e: Exception) {
        }
        super.onDestroy()


    }
}
