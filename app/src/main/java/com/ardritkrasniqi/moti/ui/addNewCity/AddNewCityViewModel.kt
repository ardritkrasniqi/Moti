package com.ardritkrasniqi.moti.ui.addNewCity

import android.app.Application
import androidx.lifecycle.*
import com.ardritkrasniqi.moti.database.getDatabase
import com.ardritkrasniqi.moti.network.CityData
import com.ardritkrasniqi.moti.network.CityDataList
import com.ardritkrasniqi.moti.network.GeoDbNetwork
import com.ardritkrasniqi.moti.repository.WeatherRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class AddNewCityViewModel(application: Application) : AndroidViewModel(application) {
    private val weatherRepository = WeatherRepository(getDatabase(application))

    private var _status = MutableLiveData<String>()
    val status: LiveData<String>
        get() = _status

    private var _cityByPrefixLiveData = MutableLiveData<CityDataList>()
    val cityByPrefixLiveData: LiveData<CityDataList>
        get() = _cityByPrefixLiveData

    fun getCityByPrefix(prefix: String) {
        viewModelScope.launch {
            try {
                val cityPrefixDeferred = GeoDbNetwork.geoDbService.getCityByPrefix(prefix)
                _cityByPrefixLiveData.value = cityPrefixDeferred.await()
            } catch (e: Exception){
                _status.value = e.localizedMessage
            }
        }
    }


    /**
     * Factory method to construct viewmodel with parameter( in this case the application parameter)
     * paterna "factory" per te konstruktu viewmodelin me parameter ne kete rast na duhet konteksti i aplikactionit per te initilizu DB
     */
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AddNewCityViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AddNewCityViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}