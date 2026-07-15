package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "officers")
data class Officer(
    @PrimaryKey val badgeNumber: String,
    val name: String,
    val rank: String, // Constable, Sergeant, Inspector, Superintendent
    val role: String, // Admin, Officer
    val passwordHash: String, // simple hashed/clear text password for local use
    val dateJoined: Long = System.currentTimeMillis()
) : Serializable

@Entity(tableName = "cases")
data class Case(
    @PrimaryKey val caseNumber: String, // e.g., CASE-2026-0001
    val title: String,
    val description: String,
    val category: String, // Theft, Assault, Fraud, Robbery, Homicide, Other
    val priority: String, // Low, Medium, High
    val status: String, // Active, Under Investigation, Cold Case, Closed
    val dateCreated: Long = System.currentTimeMillis(),
    val timeCreated: String, // HH:mm
    val assignedOfficerBadge: String // badge number of assigned officer
) : Serializable

@Entity(tableName = "statements")
data class Statement(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val caseNumber: String,
    val officerBadge: String, // officer who recorded it
    val personName: String,
    val personRole: String, // Complainant, Witness, Suspect
    val dateRecorded: Long = System.currentTimeMillis(),
    val timeRecorded: String, // HH:mm
    val statementText: String
) : Serializable

@Entity(tableName = "complainants")
data class Complainant(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val caseNumber: String,
    val name: String,
    val phone: String,
    val email: String,
    val address: String
) : Serializable

@Entity(tableName = "suspects")
data class Suspect(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val caseNumber: String,
    val name: String,
    val age: Int,
    val status: String, // At Large, In Custody, Under Investigation, Released
    val address: String,
    val description: String
) : Serializable

@Entity(tableName = "witnesses")
data class Witness(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val caseNumber: String,
    val name: String,
    val phone: String,
    val address: String
) : Serializable

@Entity(tableName = "evidence")
data class Evidence(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val caseNumber: String,
    val evidenceCode: String, // e.g. EVID-0001
    val description: String,
    val category: String, // Weapon, Document, Digital, Narcotics, Forensics, Other
    val dateCollected: Long = System.currentTimeMillis(),
    val collectingOfficerBadge: String,
    val storageLocation: String
) : Serializable

@Entity(tableName = "investigations")
data class InvestigationLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val caseNumber: String,
    val dateLogged: Long = System.currentTimeMillis(),
    val officerBadge: String,
    val logMessage: String,
    val nextSteps: String
) : Serializable
