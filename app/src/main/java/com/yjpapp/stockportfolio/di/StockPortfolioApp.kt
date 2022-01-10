package com.yjpapp.stockportfolio.di

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.koinApplication

class StockPortfolioApp: Application() {

    override fun onCreate() {
        super.onCreate()
        /**
         * Koin
         */
        startKoin {
            androidLogger()
            androidContext(this@StockPortfolioApp)
            //ViewModel
            modules(loginViewModel)
            modules(myStockViewModel)
            modules(myViewModel)
            modules(incomeNoteViewModel)
            modules(memoReadWriteViewModel)
            modules(memoListViewModel)
            //Common
            modules(preferenceController)
        }

        //다크모드 비활성화 처리
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}