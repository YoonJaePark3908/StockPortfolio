package com.yjpapp.stockportfolio.function

import android.os.Bundle
import android.view.View
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.plusAssign
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.yjpapp.data.localdb.preference.PrefKey
import com.yjpapp.stockportfolio.BuildConfig
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.base.BaseActivity
import com.yjpapp.stockportfolio.databinding.ActivityMainBinding
import com.yjpapp.stockportfolio.util.KeepStateNavigator
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main
 * 디자인 패턴 : MVP
 * @author Yoon Jae-park
 * @since 2020.12
 */
@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
//        initData()
//        initLayout()
        setContent {
            MainScreen(
                viewModel = viewModel
            )
        }
    }

    private fun initData() {
        viewModel.setPreference(PrefKey.KEY_BACK_BUTTON_APP_CLOSE, "false")
        binding.lifecycleOwner = this
    }

    private fun initLayout() {
        binding.apply {
            setSupportActionBar(toolbarMainActivity)
        }
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        val navHostFragment = supportFragmentManager.findFragmentById(binding.consMainActivityFragment.id) as NavHostFragment

        val navController = navHostFragment.navController
        //Custom Navigator 추가
        val navigator = KeepStateNavigator(this, navHostFragment.childFragmentManager, binding.consMainActivityFragment.id)
        navController.navigatorProvider += navigator
        navController.setGraph(R.navigation.nav_main)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.myStockFragment -> {
                    binding.txtMainActivityTitle.text = getString(R.string.MyStockFragment_Title)
                }
                R.id.incomeNoteFragment -> {
                    binding.txtMainActivityTitle.text = getString(R.string.IncomeNoteFragment_Title)
                }
                R.id.memoListFragment -> {
                    binding.txtMainActivityTitle.text = getString(R.string.MemoListFragment_Title)
                }
                R.id.myFragment -> {
                    binding.txtMainActivityTitle.text = getString(R.string.MyFragment_Title)
                }
            }
        }
        binding.bottomNavigationView.setupWithNavController(navController)

        AdView(this).apply {
            adSize = AdSize.BANNER
            adUnitId = BuildConfig.AD_MOB_ID
        }

        MobileAds.initialize(this) {}
        val adRequest = AdRequest.Builder().build()

        binding.adView.loadAd(adRequest)
    }

    override fun onBackPressed() {
        viewModel.runBackPressAppCloseEvent(this)
    }


    fun startLoadingAnimation() {
        binding.viewMasking.visibility = View.VISIBLE
        binding.ivLoading.visibility = View.VISIBLE
        binding.ivLoading.startAnimation()
    }

    fun stopLoadingAnimation() {
        binding.viewMasking.visibility = View.GONE
        binding.ivLoading.visibility = View.GONE
        binding.ivLoading.stopAnimation()
    }
}