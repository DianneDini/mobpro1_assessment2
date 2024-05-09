package org.d3if0108.mobpro1_assessment2.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3if0108.mobpro1_assessment2.database.UserDao
import org.d3if0108.mobpro1_assessment2.model.User
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailViewModel(private val dao: UserDao) : ViewModel() {

    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)

    fun insert(nama: String, usia: String, jeniskelamin: String, gejala: String, diagnosis: String, statuskesehatan: String) {
        val user = User(
            tanggal = formatter.format(Date()),
            nama = nama,
            usia = usia,
            jeniskelamin = jeniskelamin,
            gejala = gejala,
            diagnosis = diagnosis,
            statuskesehatan = statuskesehatan
        )

        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(user)
        }
    }

    suspend fun getUser(id: Long): User? {
        return dao.getCatatanById(id)
    }

    fun update(id: Long, nama: String, usia: String, jeniskelamin: String, gejala: String, diagnosis: String, statuskesehatan: String) {
        val user = User(
            id = id,
            tanggal = formatter.format(Date()),
            nama = nama,
            usia = usia,
            jeniskelamin = jeniskelamin,
            gejala = gejala,
            diagnosis = diagnosis,
            statuskesehatan = statuskesehatan
        )
        viewModelScope.launch(Dispatchers.IO) {
            dao.update(user)
        }
    }

    fun delete(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteById(id)
        }
    }

}