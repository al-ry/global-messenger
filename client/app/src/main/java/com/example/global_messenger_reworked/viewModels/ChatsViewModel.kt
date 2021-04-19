package com.example.global_messenger_reworked.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.global_messenger_reworked.data.models.Chat
import com.example.global_messenger_reworked.repository.MainRepository
import com.example.global_messenger_reworked.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatsViewModel @Inject constructor(
        private val repository: MainRepository
) : ViewModel() {

    val chats = MutableLiveData<Resource<List<Chat>>>()

    init {
        getChats()
    }

    private fun getChats() = viewModelScope.launch {
        chats.postValue(Resource.loading(null))
        repository.getChats("89021089168").let {
            if (it.isSuccessful){
                chats.postValue(Resource.success(it.body()))
            } else {
                chats.postValue(Resource.error(it.errorBody().toString(), null))
            }
        }
    }

}