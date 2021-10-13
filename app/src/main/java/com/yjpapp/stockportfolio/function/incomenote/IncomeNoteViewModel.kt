package com.yjpapp.stockportfolio.function.incomenote

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.yjpapp.stockportfolio.model.request.ReqIncomeNoteInfo
import com.yjpapp.stockportfolio.model.response.RespIncomeNoteInfo
import com.yjpapp.stockportfolio.model.response.RespTotalGainIncomeNoteData
import com.yjpapp.stockportfolio.repository.IncomeNoteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class IncomeNoteViewModel(
    private val incomeNoteRepository: IncomeNoteRepository
) : ViewModel() {
    var editMode = false
    var incomeNoteId = -1
    var page = 1
    var initStartYYYYMMDD = listOf<String>()
    var initEndYYYYMMDD = listOf<String>()
    val totalGainIncomeNoteData = MutableLiveData<RespTotalGainIncomeNoteData>()
    val incomeNoteDeletedPosition = MutableLiveData<Int>()
    val incomeNoteModifySuccess = MutableLiveData<Boolean>()
    val incomeNoteAddSuccess = MutableLiveData<Boolean>()
    val totalIncomeNoteList = arrayListOf<RespIncomeNoteInfo.IncomeNoteList>()
    val incomeNoteListLiveData = MutableLiveData<ArrayList<RespIncomeNoteInfo.IncomeNoteList>>()

    fun requestGetIncomeNote(context: Context, page: Int) {
        val params = HashMap<String, String>()
        params["page"] = page.toString()
        params["size"] = "20"
        params["startDate"] = makeDateString(initStartYYYYMMDD)
        params["endDate"] = makeDateString(initEndYYYYMMDD)
        viewModelScope.launch {
            val result = incomeNoteRepository.requestGetIncomeNote(context, params)
            result?.let {
                if (it.isSuccessful) {
                    it.body()?.income_note?.forEach {
                        totalIncomeNoteList.add(it)
                    }
                    incomeNoteListLiveData.value = totalIncomeNoteList
                }
            }
        }
    }
    fun getIncomeNoteListPagingData(mContext: Context, startDate: String, endDate: String): Flow<PagingData<RespIncomeNoteInfo.IncomeNoteList>> {
        return incomeNoteRepository.getIncomeNoteListByPaging(mContext, startDate, endDate).cachedIn(viewModelScope)
    }

    fun requestTotalGain(context: Context) {
        if (initStartYYYYMMDD.size == 3 && initEndYYYYMMDD.size == 3) {
            val params = hashMapOf<String, String>()
            params["startDate"] = makeDateString(initStartYYYYMMDD)
            params["endDate"] = makeDateString(initEndYYYYMMDD)
            CoroutineScope(Dispatchers.Main).launch {
                val result = incomeNoteRepository.requestTotalGain(context, params)
                result?.let {
                    if (it.isSuccessful) {
                        it.body()?.let {
                            totalGainIncomeNoteData.value = it
                        }
                    }
                }
            }
        }
    }

    fun requestDeleteIncomeNote(context: Context, id: Int, position: Int) {
        viewModelScope.launch {
            val result = incomeNoteRepository.requestDeleteIncomeNote(context, id)
            result?.let {
                if (it.isSuccessful) {
                    incomeNoteDeletedPosition.value = position
                }
            }
        }
    }

    fun requestModifyIncomeNote(context: Context, reqIncomeNoteInfo: ReqIncomeNoteInfo) {
        viewModelScope.launch {
            val result = incomeNoteRepository.requestPutIncomeNote(context, reqIncomeNoteInfo)
            result?.let {
                if (it.isSuccessful) {
                    incomeNoteModifySuccess.value = true
                }
            }
        }
    }

    fun requestAddIncomeNote(context: Context, reqIncomeNoteInfo: ReqIncomeNoteInfo) {
        viewModelScope.launch {
            val result = incomeNoteRepository.requestPostIncomeNote(context, reqIncomeNoteInfo)
            result?.let {
                if (it.isSuccessful) {
                    incomeNoteAddSuccess.value = true
                }
            }
        }
    }

    fun makeDateString(dateList: List<String>): String {
        val result = StringBuilder()
        if (dateList.size == 3) {
            dateList.forEachIndexed { index, s ->
                result.append(s)
                if (index != 2) {
                    result.append("-")
                }
            }
        }
        return result.toString()
    }
}