package com.soumyadip.otpforwardapp.presentation

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.soumyadip.otpforwardapp.R
import com.soumyadip.otpforwardapp.databinding.ActivityMainBinding
import com.soumyadip.otpforwardapp.databinding.AlertDialogPermissionNeededBinding
import com.soumyadip.otpforwardapp.domain.repository.OTPForwardRepository
import com.soumyadip.otpforwardapp.presentation.ui.adapters.MainViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var viewPager2: ViewPager2

    //private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var highlighterFrame: View
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private var _dialog: AlertDialog? = null
    private val dialog: AlertDialog get() = _dialog!!

    private var _dialogBinding: AlertDialogPermissionNeededBinding? = null
    private val dialogBinding get() = _dialogBinding!!


    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionMap ->
        if (permissionMap.any { !it.value }) onPermissionDenied(permissionMap)
    }

    private val requiredPermissions = arrayOf(
        Manifest.permission.RECEIVE_SMS,
        Manifest.permission.READ_SMS,
        Manifest.permission.SEND_SMS,
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.POST_NOTIFICATIONS
    )

    @Inject
    lateinit var otpForwardRepositary: OTPForwardRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //installSplashScreen()
        enableEdgeToEdge()
        _binding = ActivityMainBinding.inflate(layoutInflater)
        _dialogBinding = AlertDialogPermissionNeededBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupWindowInsets()
        setupBottomNavigationView()
        setupViewPager()
        highlighterFrame = findViewById(R.id.main_activity_highlighter_frame)
        requestPermission()
    }

    private fun requestPermission() {
        permissionLauncher.launch(requiredPermissions)
    }

    private fun onPermissionDenied(permissionMap: Map<String, Boolean>) {

        val deniedPermission = permissionMap.mapNotNull {
            if (it.value) null
            else it.key
        }
        val shouldRequestAgain = deniedPermission.any { !shouldShowRequestPermissionRationale(it) }
        updatePermissionDeniedList(permissionMap)
        updateAlertDialog(shouldRequestAgain)
    }

    private fun updatePermissionDeniedList(permissionMap: Map<String, Boolean>) {
        dialogBinding.permissionDialogReceiveSms.visibility =
            if (permissionMap[Manifest.permission.RECEIVE_SMS] == true) View.GONE else View.VISIBLE
        dialogBinding.permissionDialogReadSms.visibility =
            if (permissionMap[Manifest.permission.READ_SMS] == true) View.GONE else View.VISIBLE
        dialogBinding.permissionDialogSendSms.visibility =
            if (permissionMap[Manifest.permission.SEND_SMS] == true) View.GONE else View.VISIBLE
        dialogBinding.permissionDialogReadContacts.visibility =
            if (permissionMap[Manifest.permission.READ_CONTACTS] == true) View.GONE else View.VISIBLE
        dialogBinding.permissionDialogNotification.visibility =
            if (permissionMap[Manifest.permission.POST_NOTIFICATIONS] == true) View.GONE else View.VISIBLE

    }

    private fun setupDialog() {
        _dialog = AlertDialog.Builder(this).setView(dialogBinding.root).create()
    }

    private fun updateAlertDialog(shouldRequestAgain: Boolean) {
        if (_dialog == null) setupDialog()

        if (!shouldRequestAgain) {
            dialogBinding.permissionDialogButton.text = "Okay!"
            dialogBinding.permissionDialogButton.setOnClickListener { dialog.dismiss() }
            dialog.setOnDismissListener {
                permissionLauncher.launch(requiredPermissions)
            }
        } else {
            dialogBinding.permissionDialogButton.text = "Go to Settings And Allow Permission!"
            dialogBinding.permissionDialogButton.setOnClickListener {
                goToSettings()
            }
            dialog.setOnDismissListener {
                Toast.makeText(
                    this,
                    "You need to allow permissions to use this app",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }

        dialog.show()
    }

    private fun goToSettings() {
        Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", packageName, null)
        ).also(::startActivity)

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        _dialogBinding = null
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupViewPager() {
        viewPager2 = findViewById(R.id.main_activity_viewpager)
        viewPager2.adapter = MainViewPagerAdapter(this)
        viewPager2.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        viewPager2.offscreenPageLimit = 1

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                val viewPagerWidth = viewPager2.width
                val percent = (position + positionOffset) / viewPager2.adapter!!.itemCount
                val highlighterFrameX = (percent * viewPagerWidth).toInt()
                highlighterFrame.translationX = highlighterFrameX.toFloat()
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == 0) binding.mainActivityStartFrame.performClick()
                if (position == 1) binding.mainActivityHomeFrame.performClick()
            }
        })
    }

    private fun setupBottomNavigationView() {
//        bottomNavigationView = findViewById(R.id.main_activity_bottom_nav_view)
//        bottomNavigationView.setOnItemSelectedListener { item ->
//            when (item.itemId) {
//
//                R.id.menu_start_pause -> viewPager2.currentItem = 0
//                R.id.menu_home -> viewPager2.currentItem = 1
//            }
//            true
//        }

        val homeMenu = binding.mainActivityHomeFrame
        val startStopMenu = binding.mainActivityStartFrame

        startStopMenu.setOnClickListener {
            viewPager2.currentItem = 0
            binding.mainActivityStartSelected.visibility = View.VISIBLE
            binding.mainActivityStartUnselected.visibility = View.GONE
            binding.mainActivityHomeSelected.visibility = View.GONE
            binding.mainActivityHomeUnselected.visibility = View.VISIBLE
        }

        homeMenu.setOnClickListener {
            viewPager2.currentItem = 1
            binding.mainActivityStartSelected.visibility = View.GONE
            binding.mainActivityStartUnselected.visibility = View.VISIBLE
            binding.mainActivityHomeSelected.visibility = View.VISIBLE
            binding.mainActivityHomeUnselected.visibility = View.GONE
        }
    }

}
