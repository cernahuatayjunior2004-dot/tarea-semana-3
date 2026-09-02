package com.example.semana3.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.semana3.data.Equipo
import com.example.semana3.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EquipoViewModel : ViewModel() {
    private val _equipos = MutableStateFlow<List<Equipo>>(emptyList())
    val equipos: StateFlow<List<Equipo>> = _equipos

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        fetchEquipos()
    }

    fun fetchEquipos() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                _equipos.value = RetrofitClient.instance.getEquipos()
            } catch (e: Exception) {
                _errorMessage.value = "Error al obtener equipos: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addEquipo(nombre: String, pais: String) {
        if (nombre.isBlank() || pais.isBlank()) {
            _errorMessage.value = "Nombre y país son requeridos"
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                RetrofitClient.instance.createEquipo(Equipo(nombre = nombre, pais = pais))
                fetchEquipos()
            } catch (e: Exception) {
                _errorMessage.value = "Error al agregar equipo: ${e.message}"
                _isLoading.value = false
            }
        }
    }

    fun updateEquipo(id: String, nombre: String, pais: String) {
        if (nombre.isBlank() || pais.isBlank()) {
            _errorMessage.value = "Nombre y país son requeridos"
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                RetrofitClient.instance.updateEquipo(id, Equipo(nombre = nombre, pais = pais))
                fetchEquipos()
            } catch (e: Exception) {
                _errorMessage.value = "Error al actualizar equipo: ${e.message}"
                _isLoading.value = false
            }
        }
    }

    fun deleteEquipo(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                RetrofitClient.instance.deleteEquipo(id)
                fetchEquipos()
            } catch (e: Exception) {
                _errorMessage.value = "Error al eliminar equipo: ${e.message}"
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
