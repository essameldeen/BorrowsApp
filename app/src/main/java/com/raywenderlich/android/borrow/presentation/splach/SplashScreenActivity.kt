package com.raywenderlich.android.borrow.presentation.splach

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import com.raywenderlich.android.borrow.R
import com.raywenderlich.android.borrow.presentation.home.MainActivity

class SplashScreenActivity : AppCompatActivity() {

  companion object {
    const val SPLASH_SCREEN_DURATION = 2000L
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    // make the activity full screen
    requestWindowFeature(Window.FEATURE_NO_TITLE)
    supportActionBar?.hide()
    window.setFlags(
      WindowManager.LayoutParams.FLAG_FULLSCREEN,
      WindowManager.LayoutParams.FLAG_FULLSCREEN)

    setContentView(R.layout.activity_splash_screen)

    Handler().postDelayed({

      startActivity(Intent(this, MainActivity::class.java))
      overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
      finish()

    }, SPLASH_SCREEN_DURATION)
  }
}
