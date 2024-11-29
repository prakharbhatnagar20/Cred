package com.example.cred.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cred.model.ApiResponse
import com.example.cred.service.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response


class MyViewModel : ViewModel() {
    // StateFlow to hold the API response
    private val _apiData = MutableStateFlow<ApiResponse?>(null)
    val apiData: StateFlow<ApiResponse?> get() = _apiData

    // StateFlow to hold the loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    // StateFlow to hold any potential error messages
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    // Function to fetch data from the API
    fun fetchData() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.fetchData()
                Log.e("response", "${response}")
                    if (response.items.isNotEmpty()) {
                            _apiData.value = response

                    } else {
                        // Handle unsuccessful response
                        Log.e("MyViewModel", "Failed API response: ${response}")
                    }
            } catch (e: Exception) {
                Log.e("MyViewModel", "Error in fetchData: ${e.message}")
            }
        }
    }
}