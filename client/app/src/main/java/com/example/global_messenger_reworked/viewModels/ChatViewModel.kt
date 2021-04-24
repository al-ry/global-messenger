package com.example.global_messenger_reworked.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.global_messenger_reworked.data.models.Message
import com.example.global_messenger_reworked.repository.MainRepository
import com.example.global_messenger_reworked.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
        private val repository: MainRepository
) : ViewModel() {

    val messageHistory = MutableLiveData<Resource<List<Message>>>()

    fun getMessageHistory(friendPhone: String) = viewModelScope.launch {
        messageHistory.postValue(Resource.loading(null))
        repository.getMessageHistory("89021089168", friendPhone).let {
            if (it.isSuccessful){
                messageHistory.postValue(Resource.success(it.body()))
            } else {
                messageHistory.postValue(Resource.error(it.errorBody().toString(), null))
            }
        }
    }

}