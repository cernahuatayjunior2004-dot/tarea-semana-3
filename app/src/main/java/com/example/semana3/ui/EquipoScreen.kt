package com.example.semana3.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.semana3.data.Equipo
import com.example.semana3.viewmodel.EquipoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EquipoScreen(viewModel: EquipoViewModel = viewModel()) {
    val equipos by viewModel.equipos.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    var nombre by remember { mutableStateOf("") }
    var pais by remember { mutableStateOf("") }
    var editingEquipo by remember { mutableStateOf<Equipo?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Gestión de Equipos") }) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Formulario
                TextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre del Equipo") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = pais,
                    onValueChange = { pais = it },
                    label = { Text("País") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        if (editingEquipo == null) {
                            viewModel.addEquipo(nombre, pais)
                        } else {
                            viewModel.updateEquipo(editingEquipo!!.id!!, nombre, pais)
                            editingEquipo = null
                        }
                        nombre = ""
                        pais = ""
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading && nombre.isNotBlank() && pais.isNotBlank()
                ) {
                    Text(if (editingEquipo == null) "Agregar Equipo" else "Actualizar Equipo")
                }

                if (editingEquipo != null) {
                    TextButton(
                        onClick = {
                            editingEquipo = null
                            nombre = ""
                            pais = ""
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("Cancelar Edición")
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Lista
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(equipos) { equipo ->
                        EquipoItem(
                            equipo = equipo,
                            onDelete = { viewModel.deleteEquipo(equipo.id!!) },
                            onEdit = {
                                editingEquipo = equipo
                                nombre = equipo.nombre
                                pais = equipo.pais
                            },
                            enabled = !isLoading
                        )
                    }
                }
            }

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
fun EquipoItem(equipo: Equipo, onDelete: () -> Unit, onEdit: () -> Unit, enabled: Boolean = true) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = equipo.nombre, style = MaterialTheme.typography.titleMedium)
                Text(text = equipo.pais, style = MaterialTheme.typography.bodySmall)
            }
            Row {
                IconButton(onClick = onEdit, enabled = enabled) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar")
                }
                IconButton(onClick = onDelete, enabled = enabled) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                }
            }
        }
    }
}
