package com.example.dessertclicker.ui

import androidx.lifecycle.ViewModel
import com.example.dessertclicker.data.Datasource
import com.example.dessertclicker.model.Dessert
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DessertViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(DessertUiState())
    val uiState: StateFlow<DessertUiState> = _uiState.asStateFlow()

    init {
        initDessertState()
    }

    private fun initDessertState() {
        _uiState.value = DessertUiState(
            revenue = 0,
            dessertSold = 0,
            currentDessertPrice = determineDessertToShow().price,
            currentDessertImage = determineDessertToShow().imageId
        )
    }

    fun dessertClicked() {
        _uiState.update { currentState ->
            var currentRevenue = currentState.revenue
            currentRevenue += currentState.currentDessertPrice
            currentState.copy(
                revenue = currentRevenue,
                dessertSold = currentState.dessertSold.inc(),
                currentDessertPrice = determineDessertToShow().price,
                currentDessertImage = determineDessertToShow().imageId
            )
        }
    }

    private fun determineDessertToShow(): Dessert {
        val desserts = Datasource.dessertList
        val dessertsSold = _uiState.value.dessertSold
        var dessertToShow = Datasource.dessertList.first()
        for (dessert in desserts) {
            if (dessertsSold >= dessert.startProductionAmount) {
                dessertToShow = dessert
            } else {
                // The list of desserts is sorted by startProductionAmount. As you sell more desserts,
                // you'll start producing more expensive desserts as determined by startProductionAmount
                // We know to break as soon as we see a dessert who's "startProductionAmount" is greater
                // than the amount sold.
                break
            }
        }

        return dessertToShow
    }

    /**
     * Solution from GitHub
     */

    //fun onDessertClicked() {
    //    _uiState.update { cupcakeUiState ->
    //        val dessertsSold = cupcakeUiState.dessertSold + 1
    //        val nextDessertIndex = determineDessertIndex(dessertsSold)
    //        cupcakeUiState.copy(
    //            currentDessertIndex = nextDessertIndex,
    //            revenue = cupcakeUiState.revenue + cupcakeUiState.currentDessertPrice,
    //            dessertsSold = dessertsSold,
    //            currentDessertImageId = dessertList[nextDessertIndex].imageId,
    //            currentDessertPrice = dessertList[nextDessertIndex].price
    //        )
    //    }
    //}

    //private fun determineDessertIndex(dessertsSold: Int): Int {
    //    var dessertIndex = 0
    //    for (index in dessertList.indices) {
    //        if (dessertsSold >= dessertList[index].startProductionAmount) {
    //            dessertIndex = index
    //        } else {
    //            // The list of desserts is sorted by startProductionAmount. As you sell more
    //            // desserts, you'll start producing more expensive desserts as determined by
    //            // startProductionAmount. We know to break as soon as we see a dessert who's
    //            // "startProductionAmount" is greater than the amount sold.
    //            break
    //        }
    //    }
    //    return dessertIndex
    //}

}