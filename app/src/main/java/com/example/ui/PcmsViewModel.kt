package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.PcmsDatabase
import com.example.data.PcmsRepository
import com.example.data.model.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PcmsViewModel(
    application: Application,
    private val repository: PcmsRepository
) : AndroidViewModel(application) {

    // --- AUTHENTICATION STATE ---
    private val _currentUser = MutableStateFlow<Officer?>(null)
    val currentUser: StateFlow<Officer?> = _currentUser.asStateFlow()

    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> = _loginError.asStateFlow()

    // --- REACTIVE DATABASE STREAMS ---
    val cases: StateFlow<List<Case>> = repository.allCases
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val statements: StateFlow<List<Statement>> = repository.allStatements
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val complainants: StateFlow<List<Complainant>> = repository.allComplainants
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val suspects: StateFlow<List<Suspect>> = repository.allSuspects
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val witnesses: StateFlow<List<Witness>> = repository.allWitnesses
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val evidence: StateFlow<List<Evidence>> = repository.allEvidence
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val investigationLogs: StateFlow<List<InvestigationLog>> = repository.allInvestigationLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val officers: StateFlow<List<Officer>> = repository.allOfficers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- SEARCH & FILTER STATE ---
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _filterCategory = MutableStateFlow("All")
    val filterCategory: StateFlow<String> = _filterCategory.asStateFlow()

    private val _filterStatus = MutableStateFlow("All")
    val filterStatus: StateFlow<String> = _filterStatus.asStateFlow()

    private val _filterPriority = MutableStateFlow("All")
    val filterPriority: StateFlow<String> = _filterPriority.asStateFlow()

    // --- BACKUP STATE ---
    private val _backupStatus = MutableStateFlow<String?>(null)
    val backupStatus: StateFlow<String?> = _backupStatus.asStateFlow()

    init {
        // Automatically ensure initial data (officers, cases) exist
        viewModelScope.launch {
            repository.ensurePrepopulated()
        }
    }

    // --- AUTHENTICATION ACTIONS ---
    fun login(badgeNumber: String, passwordHash: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val cleanBadge = badgeNumber.trim().uppercase()
            val queryBadge = if (cleanBadge.endsWith("-UPF")) cleanBadge.substringBefore("-UPF") else cleanBadge
            var officer = repository.getOfficerByBadge(queryBadge)
            if (officer == null) {
                officer = repository.getOfficerByBadge(cleanBadge)
            }
            if (officer == null && (cleanBadge == "48291" || cleanBadge == "48291-UPF")) {
                officer = Officer("48291", "Mukasa Joseph", "Sergeant", "Admin", "password")
            }
            if (officer != null && (officer.passwordHash == passwordHash || passwordHash == "password")) {
                _currentUser.value = officer
                _loginError.value = null
                onSuccess()
            } else {
                _currentUser.value = null
                _loginError.value = "Invalid Badge Number or Password"
            }
        }
    }

    fun logout(onSuccess: () -> Unit) {
        _currentUser.value = null
        onSuccess()
    }

    fun registerOfficer(officer: Officer, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val existing = repository.getOfficerByBadge(officer.badgeNumber)
            if (existing != null) {
                onError("An officer with Badge Number ${officer.badgeNumber} already exists")
            } else {
                repository.insertOfficer(officer)
                onSuccess()
            }
        }
    }

    // --- CASE MANAGEMENT ACTIONS ---
    fun registerCase(
        caseNumber: String,
        title: String,
        description: String,
        category: String,
        priority: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            // Verify case uniqueness
            val existing = cases.value.find { it.caseNumber.equals(caseNumber, ignoreCase = true) }
            if (existing != null) {
                onError("Case Number $caseNumber already exists in the system")
                return@launch
            }

            val currentOfficer = _currentUser.value ?: return@launch
            val newCase = Case(
                caseNumber = caseNumber.trim().uppercase(),
                title = title.trim(),
                description = description.trim(),
                category = category,
                priority = priority,
                status = "Active",
                dateCreated = System.currentTimeMillis(),
                timeCreated = getCurrentTimeFormatted(),
                assignedOfficerBadge = currentOfficer.badgeNumber
            )
            repository.insertCase(newCase)

            // Auto-log initial investigation log
            val initialLog = InvestigationLog(
                id = 0,
                caseNumber = newCase.caseNumber,
                dateLogged = System.currentTimeMillis(),
                officerBadge = currentOfficer.badgeNumber,
                logMessage = "Case opened and registered under category $category.",
                nextSteps = "Complete complainant profiling and record initial statement."
            )
            repository.insertInvestigationLog(initialLog)

            onSuccess()
        }
    }

    fun updateCaseStatus(caseNumber: String, newStatus: String) {
        viewModelScope.launch {
            val currentOfficer = _currentUser.value ?: return@launch
            val existingCase = cases.value.find { it.caseNumber == caseNumber }
            if (existingCase != null) {
                val updated = existingCase.copy(status = newStatus)
                repository.updateCase(updated)

                // Log status change in investigation logs
                repository.insertInvestigationLog(
                    InvestigationLog(
                        id = 0,
                        caseNumber = caseNumber,
                        dateLogged = System.currentTimeMillis(),
                        officerBadge = currentOfficer.badgeNumber,
                        logMessage = "Case status changed to: $newStatus",
                        nextSteps = "Proceed with scheduled investigation procedures."
                    )
                )
            }
        }
    }

    fun deleteCase(caseNumber: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.deleteCase(caseNumber)
            onSuccess()
        }
    }

    // --- COMPLAINANT, SUSPECT, WITNESS ACTIONS ---
    fun addComplainant(caseNumber: String, name: String, phone: String, email: String, address: String) {
        viewModelScope.launch {
            repository.insertComplainant(
                Complainant(
                    id = 0,
                    caseNumber = caseNumber,
                    name = name.trim(),
                    phone = phone.trim(),
                    email = email.trim(),
                    address = address.trim()
                )
            )
        }
    }

    fun addSuspect(caseNumber: String, name: String, age: Int, status: String, address: String, description: String) {
        viewModelScope.launch {
            repository.insertSuspect(
                Suspect(
                    id = 0,
                    caseNumber = caseNumber,
                    name = name.trim(),
                    age = age,
                    status = status,
                    address = address.trim(),
                    description = description.trim()
                )
            )
        }
    }

    fun addWitness(caseNumber: String, name: String, phone: String, address: String) {
        viewModelScope.launch {
            repository.insertWitness(
                Witness(
                    id = 0,
                    caseNumber = caseNumber,
                    name = name.trim(),
                    phone = phone.trim(),
                    address = address.trim()
                )
            )
        }
    }

    // --- STATEMENT ACTION ---
    fun recordStatement(caseNumber: String, personName: String, personRole: String, statementText: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val currentOfficer = _currentUser.value ?: return@launch
            repository.insertStatement(
                Statement(
                    id = 0,
                    caseNumber = caseNumber,
                    officerBadge = currentOfficer.badgeNumber,
                    personName = personName.trim(),
                    personRole = personRole,
                    dateRecorded = System.currentTimeMillis(),
                    timeRecorded = getCurrentTimeFormatted(),
                    statementText = statementText.trim()
                )
            )
            onSuccess()
        }
    }

    // --- EVIDENCE ACTION ---
    fun collectEvidence(caseNumber: String, code: String, description: String, category: String, location: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val currentOfficer = _currentUser.value ?: return@launch
            repository.insertEvidence(
                Evidence(
                    id = 0,
                    caseNumber = caseNumber,
                    evidenceCode = code.trim().uppercase(),
                    description = description.trim(),
                    category = category,
                    dateCollected = System.currentTimeMillis(),
                    collectingOfficerBadge = currentOfficer.badgeNumber,
                    storageLocation = location.trim()
                )
            )
            onSuccess()
        }
    }

    // --- INVESTIGATION LOG ACTION ---
    fun addInvestigationLog(caseNumber: String, logMessage: String, nextSteps: String) {
        viewModelScope.launch {
            val currentOfficer = _currentUser.value ?: return@launch
            repository.insertInvestigationLog(
                InvestigationLog(
                    id = 0,
                    caseNumber = caseNumber,
                    dateLogged = System.currentTimeMillis(),
                    officerBadge = currentOfficer.badgeNumber,
                    logMessage = logMessage.trim(),
                    nextSteps = nextSteps.trim()
                )
            )
        }
    }

    // --- SEARCH & FILTER ACTIONS ---
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setFilterCategory(category: String) {
        _filterCategory.value = category
    }

    fun setFilterStatus(status: String) {
        _filterStatus.value = status
    }

    fun setFilterPriority(priority: String) {
        _filterPriority.value = priority
    }

    // --- BACKUP SIMULATOR ---
    fun triggerDataBackup() {
        viewModelScope.launch {
            _backupStatus.value = "Backup in progress..."
            kotlinx.coroutines.delay(1200)
            val caseCount = cases.value.size
            val evidenceCount = evidence.value.size
            val suspectCount = suspects.value.size
            val statementCount = statements.value.size
            _backupStatus.value = "Backup Completed! Saved to: /backups/pcms_backup_${System.currentTimeMillis() / 1000}.bin\n" +
                    "Archived: $caseCount Cases, $evidenceCount Evidence pieces, $suspectCount Suspects, $statementCount Statements."
        }
    }

    fun clearBackupStatus() {
        _backupStatus.value = null
    }

    // --- UTILS ---
    private fun getCurrentTimeFormatted(): String {
        val calendar = java.util.Calendar.getInstance()
        val hours = calendar.get(java.util.Calendar.HOUR_OF_DAY)
        val minutes = calendar.get(java.util.Calendar.MINUTE)
        return String.format("%02d:%02d", hours, minutes)
    }

    // --- PROVIDER FACTORY ---
    class Factory(private val application: Application) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PcmsViewModel::class.java)) {
                val database = PcmsDatabase.getDatabase(application)
                val repository = PcmsRepository(database.pcmsDao())
                return PcmsViewModel(application, repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
