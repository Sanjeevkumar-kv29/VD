package com.example.vd

import android.app.PendingIntent.getActivity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.navigation.NavigationView
import com.irozon.alertview.AlertActionStyle
import com.irozon.alertview.AlertStyle
import com.irozon.alertview.AlertView
import com.irozon.alertview.objects.AlertAction
import kotlinx.android.synthetic.main.activity_home.*
import java.lang.Exception

class HomeActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {


    var walletstatus = "false"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        getSupportActionBar()?.hide()

        val extras: Bundle? = this.getIntent().getExtras()
        if (extras != null) {
            val walletstatus = extras.getString("walletstatus")
            addFragment(fragment_wallet())

        } else {
            println("Null")
        }




        nav_menu.setNavigationItemSelectedListener(this)
        displayScreen(-1)

    }

    override fun onBackPressed() {
        val alert = AlertView("Confirm Quit", "Are you Really Want to Exit", AlertStyle.BOTTOM_SHEET)
        alert.addAction(AlertAction("No", AlertActionStyle.DEFAULT, { action -> }))
        alert.addAction(AlertAction("Yes", AlertActionStyle.POSITIVE, { action -> finish()}))
        alert.show(this)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.navi_menu, menu)
        return false
    }



    fun displayScreen(id: Int){

        when (id){/* R.id.navi_search -> {addFragment(fragment_search())
                Toast.makeText(this,"Search ", Toast.LENGTH_LONG).show()}*/

            R.id.navi_wallet -> {
                try {
                    addFragment(fragment_wallet())
                    Toast.makeText(this,"wallet ", Toast.LENGTH_LONG).show()
                }catch (e: Exception){
                    Log.d("fragloaderror",e.toString())
                }}

            R.id.navi_profile -> {
                try {
                    addFragment(fragment_profile())
                    Toast.makeText(this,"profile ", Toast.LENGTH_LONG).show()
                }catch (e: Exception){
                    Log.d("fragloaderror",e.toString())
                }}


            R.id.navi_setting -> {
                try {addFragment(fragment_setting())
                Toast.makeText(this,"setting ", Toast.LENGTH_LONG).show()
            }catch (e: Exception){
                Log.d("fragloaderror",e.toString())
            }}

            R.id.navi_logout -> {

                val alert = AlertView("Confirm Logout ", "Are you Really Want to Logout", AlertStyle.BOTTOM_SHEET)
                alert.addAction(AlertAction("No", AlertActionStyle.DEFAULT, { action -> }))
                alert.addAction(AlertAction("Yes", AlertActionStyle.POSITIVE, { action ->
                    val sharepref: SharedPreferences = getSharedPreferences("LoginUserDetails",0)
                    val editor: SharedPreferences.Editor = sharepref.edit()
                    editor.clear()
                    editor.commit()
                startActivity(Intent(this,MainActivity::class.java))
                    finish()}))

                alert.show(this)
            }

            //R.id.navi_donate -> {addFragment(fragment_donation())
               // Toast.makeText(this,"navi donate", Toast.LENGTH_LONG).show()}
                //supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment_donation()).commit()

            R.id.navi_tree -> {
                try {addFragment(fragment_tree())
                Toast.makeText(this,"tree ", Toast.LENGTH_LONG).show()
            }catch (e: Exception){
                Log.d("fragloaderror",e.toString())
            }}

        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {displayScreen(item.itemId)

        return true }


    private fun addFragment(fragment: Fragment){
        fragment_container.removeAllViews()
        val fragmentManager: androidx.fragment.app.FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.fragment_container, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

}