package org.d3if0108.mobpro1_assessment2.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val nama: String,
    val usia: String,
    val jeniskelamin: String,
    val gejala: String,
    val diagnosis: String,
    val tanggal: String,
    val statuskesehatan: String
)
