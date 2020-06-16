package com.shortstack.hackertracker.di

import androidx.work.WorkManager
import com.firebase.jobdispatcher.FirebaseJobDispatcher
import com.firebase.jobdispatcher.GooglePlayDriver
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.shortstack.hackertracker.database.ConferenceUser
import com.shortstack.hackertracker.database.DatabaseManager
import com.shortstack.hackertracker.database.ReminderManager
import com.shortstack.hackertracker.ui.themes.ThemesManager
import com.shortstack.hackertracker.utilities.Analytics
import com.shortstack.hackertracker.utilities.NotificationHelper
import com.shortstack.hackertracker.utilities.Storage
import org.koin.dsl.module

val appModule = module {

    single { Storage(get(), get()) }
    single { NotificationHelper(get()) }
    single {
        GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()
    }
    single { FirebaseJobDispatcher(GooglePlayDriver(get())) }

    single { ThemesManager() }

    single { Analytics(get()) }
    single { WorkManager.getInstance() }
    single { ReminderManager(get(), get()) }

    // Firebase
    single { FirebaseFirestore.getInstance() }
    single { FirebaseStorage.getInstance() }
    single { FirebaseAuth.getInstance() }

    // Database
    single { DatabaseManager(get()) }
    single { ConferenceUser(get(), get()) }

}