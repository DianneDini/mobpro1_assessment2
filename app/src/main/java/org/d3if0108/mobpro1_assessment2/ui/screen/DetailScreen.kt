package org.d3if0108.mobpro1_assessment2.ui.screen

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3if0108.mobpro1_assessment2.R
import org.d3if0108.mobpro1_assessment2.database.UserDb
import org.d3if0108.mobpro1_assessment2.ui.theme.Mobpro1_assessment2Theme
import org.d3if0108.mobpro1_assessment2.util.ViewModelFactory

const val KEY_ID_CATATAN = "idUser"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavHostController, id: Long? = null) {

    val context = LocalContext.current
    val db = UserDb.getInstance(context)
    val factory = ViewModelFactory(db.dao)
    val viewModel: DetailViewModel = viewModel(factory = factory)

    var nama by remember { mutableStateOf("") }
    var usia by remember { mutableStateOf("") }
    var jeniskelamin by remember { mutableStateOf("") }
    var gejala by remember { mutableStateOf("") }
    var diagnosis by remember { mutableStateOf("") }
    var statuskesehatan by remember { mutableStateOf("") }

    val selectedGenderIndex = remember { mutableIntStateOf(-1) }
    val selectedHealthStatusIndex = remember { mutableIntStateOf(-1) }


    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        if (id == null) return@LaunchedEffect
        val data = viewModel.getUser(id) ?: return@LaunchedEffect
        if (data != null) {
            nama = data.nama
            usia = data.usia
            gejala = data.gejala
            diagnosis = data.diagnosis
            jeniskelamin = data.jeniskelamin
            statuskesehatan = data.statuskesehatan
            selectedGenderIndex.value = getSelectedOptionIndex(jeniskelamin)
            selectedHealthStatusIndex.value = getSelectedHealthStatusIndex(statuskesehatan)
        }
    }

    Scaffold (
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.kembali),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                title = {
                    if (id == null)
                        Text(text = stringResource(id = R.string.tambah_catatan))
                    else
                        Text(text = stringResource(id = R.string.edit_catatan))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                actions = {
                    IconButton(onClick = {
                        if (nama.isEmpty() || usia.isEmpty() || gejala.isEmpty() || diagnosis.isEmpty()) {
                            Toast.makeText(context, R.string.invalid, Toast.LENGTH_LONG).show()
                            return@IconButton
                        }

                        jeniskelamin = when (selectedGenderIndex.value) {
                            0 -> "Pria"
                            1 -> "Wanita"
                            else -> ""
                        }
                        statuskesehatan = when (selectedHealthStatusIndex.value) {
                            0 -> "Sembuh"
                            1 -> "Membaik"
                            2 -> "Stabil"
                            3 -> "Perlu Perawatan Lanjutan"
                            4 -> "Kritis"
                            else -> ""
                        }

                        if (jeniskelamin.isEmpty() || statuskesehatan.isEmpty()) {
                            Toast.makeText(context, R.string.invalid, Toast.LENGTH_LONG).show()
                            return@IconButton
                        }

                        if (id == null) {
                            viewModel.insert(nama, usia, jeniskelamin, gejala, diagnosis, statuskesehatan)
                        } else {
                            viewModel.update(id, nama, usia, jeniskelamin, gejala, diagnosis, statuskesehatan)
                        }
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = stringResource(R.string.simpan),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    if (id != null) {
                        DeleteAction { showDialog = true}
                        DisplayAlertDialog(
                            openDialog = showDialog,
                            onDismissRequest = {showDialog = false }) {
                            showDialog = false
                            viewModel.delete(id)
                            navController.popBackStack()
                        }
                    }
                }
            )
        }
    ){ padding ->
        FormUser(
            name = nama,
            onNameChange = {nama = it},
            age = usia,
            onageChange = {usia = it},
            gejala = gejala,
            ongejalaChange = {gejala = it},
            diagnosis = diagnosis,
            ondiagnosisChange = {diagnosis = it},
            selectedGenderIndex = selectedGenderIndex,
            selectedHealthStatusIndex = selectedHealthStatusIndex,
            modifier = Modifier.padding(padding)
        )
    }
}

private fun getSelectedOptionIndex(jeniskelamin: String): Int {
    return when (jeniskelamin) {
        "Pria" -> 0
        "Wanita" -> 1
        else -> -1
    }
}

private fun getSelectedHealthStatusIndex(statuskesehatan: String): Int {
    return when (statuskesehatan) {
        "Sembuh" -> 0
        "Membaik" -> 1
        "Stabil" -> 2
        "Perlu Perawatan Lanjutan" -> 3
        "Kritis" -> 4
        else -> -1
    }
}

@Composable
fun DeleteAction(delete: () -> Unit) {

    var expanded by remember { mutableStateOf(false) }

    IconButton(onClick = { expanded = true }) {
        Icon(
            imageVector = Icons.Filled.MoreVert ,
            contentDescription = stringResource(R.string.lainnya),
            tint = MaterialTheme.colorScheme.primary
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = {
                    Text(text = stringResource(id = R.string.hapus))
                },
                onClick = {
                    expanded = false
                    delete()
                }
            )

        }
    }
}

@Composable
fun FormUser(
    name: String, onNameChange: (String) -> Unit,
    age: String, onageChange: (String) -> Unit,
    gejala: String, ongejalaChange: (String) -> Unit,
    diagnosis: String, ondiagnosisChange: (String) -> Unit,
    modifier: Modifier,
    selectedGenderIndex: MutableIntState,
    selectedHealthStatusIndex: MutableIntState
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = { onNameChange(it) },
            label = { Text(text = stringResource(id = R.string.nama)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = age,
            onValueChange = { onageChange(it) },
            label = { Text(text = stringResource(id = R.string.usia)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Gray, shape = RoundedCornerShape(4.dp)),
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            val genderOptions = stringArrayResource(id = R.array.gender_options)
            genderOptions.forEachIndexed { index, option ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    RadioButton(
                        selected = index == selectedGenderIndex.value,
                        onClick = { selectedGenderIndex.value = index },
                        colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
                    )
                    Text(
                        text = option,
                        modifier = Modifier.padding(start = 8.dp),
                    )
                }
            }
        }
        OutlinedTextField(
            value = gejala,
            onValueChange = { ongejalaChange(it) },
            label = { Text(text = stringResource(id = R.string.gejala)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = diagnosis,
            onValueChange = { ondiagnosisChange(it) },
            label = { Text(text = stringResource(id = R.string.diagnosis)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Gray, shape = RoundedCornerShape(4.dp)),
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            val healthStatusOptions = stringArrayResource(id = R.array.health_status_options)
            healthStatusOptions.forEachIndexed { index, option ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    RadioButton(
                        selected = index == selectedHealthStatusIndex.value,
                        onClick = { selectedHealthStatusIndex.value = index },
                        colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
                    )
                    Text(
                        text = option,
                        modifier = Modifier.padding(start = 8.dp),
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun  DetailScreenPreview() {
    Mobpro1_assessment2Theme {
        DetailScreen(rememberNavController())
    }
}