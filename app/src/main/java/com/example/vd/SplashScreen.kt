package com.example.vd
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreen : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        getSupportActionBar()?.hide()

        val animat: Animation = AnimationUtils.loadAnimation(this,R.anim.splashanim)
        val animatv: Animation = AnimationUtils.loadAnimation(this,R.anim.splashanim)
        splashimg.setImageResource(R.drawable.img1)
        splashimg.animation = animat

        sptv.animation = animatv
        sptv2.animation = animatv

        Handler().postDelayed(
            {

                startActivity(Intent(this,MainActivity::class.java))
                finish()

            },2000 )


    }


    override fun onBackPressed() {

    }
}