package com.example.antkysport.ViewModel

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.antkysport.Network.AuthResponse
import com.example.antkysport.Respository.AuthRespository
import kotlinx.coroutines.launch
import retrofit2.Response

class AuthViewModel: ViewModel() {
    private val respository = AuthRespository()

    var authToken : String? = null
    var errorsMessage: String? = null

    fun registerUser(name: String,username:String,email: String,password:String, onSuccess:()->Unit,onError: (String)->Unit,context: Context){
        viewModelScope.launch {
            val respone = respository.registerUser(name,username,email,password)
            handleResponse(respone,onSuccess,onError,context)
        }
    }

    fun loginUser(email: String,password: String,onSuccess: () -> Unit,onError: (String) -> Unit,context: Context){
        viewModelScope.launch {
            val response = respository.loginUser(email,password)
            handleResponse(response,onSuccess,onError,context)
        }
    }

    private fun handleResponse(response: Response<AuthResponse>, onSuccess: () -> Unit, onError: (String) -> Unit, context: Context){
        if(response.isSuccessful){
            authToken = response.body()?.token
            if(authToken != null){
                val sharedPreferences = context.getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                val expirationTime = System.currentTimeMillis() + 36000000 // 5 phuts
                with(sharedPreferences.edit()){
                    putString("userToken",authToken)
                    putLong("tokenExpiration",expirationTime)
                    apply() // hoặc commit() để lưu ngay lập tucs
                }

            }
            Log.d("check", "handleResponse: $authToken")
            onSuccess()
        }else{
            errorsMessage = "Lỗi: ${response.message()}"
            onError(errorsMessage?: "Unknown Error")
        }
    }
}
