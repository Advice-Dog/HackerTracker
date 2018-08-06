package com.shortstack.hackertracker.ui.workshops

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.shortstack.hackertracker.App
import com.shortstack.hackertracker.Resource
import com.shortstack.hackertracker.database.DatabaseManager
import com.shortstack.hackertracker.models.DatabaseEvent
import javax.inject.Inject

/**
 * Created by Chris on 05/08/18.
 */
class WorkshopViewModel : ViewModel() {

    @Inject
    lateinit var database: DatabaseManager

    private val result = MediatorLiveData<Resource<List<DatabaseEvent>>>()

    init {
        App.application.component.inject(this)
    }

    val workshops: LiveData<Resource<List<DatabaseEvent>>>
        get() {
            val conference = database.conferenceLiveData
            return Transformations.switchMap(conference) { id ->
                result.value = Resource.loading(null)

                if (id != null) {
                    result.addSource(database.getWorkshops(id.conference)) {
                        result.value = Resource.success(it)
                    }
                } else {
                    result.value = Resource.init(null)
                }
                return@switchMap result
            }
        }
}