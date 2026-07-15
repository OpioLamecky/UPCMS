package com.example.data

import androidx.room.*
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PcmsDao {

    // --- OFFICERS ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOfficer(officer: Officer)

    @Query("SELECT * FROM officers WHERE badgeNumber = :badge")
    suspend fun getOfficerByBadge(badge: String): Officer?

    @Query("SELECT * FROM officers ORDER BY name ASC")
    fun getAllOfficers(): Flow<List<Officer>>


    // --- CASES ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCase(pcmsCase: Case)

    @Update
    suspend fun updateCase(pcmsCase: Case)

    @Query("SELECT * FROM cases WHERE caseNumber = :number")
    fun getCaseByNumber(number: String): Flow<Case?>

    @Query("SELECT * FROM cases ORDER BY dateCreated DESC")
    fun getAllCases(): Flow<List<Case>>

    @Query("SELECT * FROM cases WHERE assignedOfficerBadge = :badge ORDER BY dateCreated DESC")
    fun getCasesForOfficer(badge: String): Flow<List<Case>>

    @Query("DELETE FROM cases WHERE caseNumber = :number")
    suspend fun deleteCaseByNumber(number: String)


    // --- STATEMENTS ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStatement(statement: Statement)

    @Query("SELECT * FROM statements WHERE caseNumber = :caseNumber ORDER BY dateRecorded DESC")
    fun getStatementsForCase(caseNumber: String): Flow<List<Statement>>

    @Query("SELECT * FROM statements ORDER BY dateRecorded DESC")
    fun getAllStatements(): Flow<List<Statement>>


    // --- COMPLAINANTS ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComplainant(complainant: Complainant)

    @Query("SELECT * FROM complainants WHERE caseNumber = :caseNumber ORDER BY id DESC")
    fun getComplainantsForCase(caseNumber: String): Flow<List<Complainant>>

    @Query("SELECT * FROM complainants ORDER BY id DESC")
    fun getAllComplainants(): Flow<List<Complainant>>


    // --- SUSPECTS ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSuspect(suspect: Suspect)

    @Query("SELECT * FROM suspects WHERE caseNumber = :caseNumber ORDER BY id DESC")
    fun getSuspectsForCase(caseNumber: String): Flow<List<Suspect>>

    @Query("SELECT * FROM suspects ORDER BY id DESC")
    fun getAllSuspects(): Flow<List<Suspect>>


    // --- WITNESSES ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWitness(witness: Witness)

    @Query("SELECT * FROM witnesses WHERE caseNumber = :caseNumber ORDER BY id DESC")
    fun getWitnessesForCase(caseNumber: String): Flow<List<Witness>>

    @Query("SELECT * FROM witnesses ORDER BY id DESC")
    fun getAllWitnesses(): Flow<List<Witness>>


    // --- EVIDENCE ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvidence(evidence: Evidence)

    @Query("SELECT * FROM evidence WHERE caseNumber = :caseNumber ORDER BY dateCollected DESC")
    fun getEvidenceForCase(caseNumber: String): Flow<List<Evidence>>

    @Query("SELECT * FROM evidence ORDER BY dateCollected DESC")
    fun getAllEvidence(): Flow<List<Evidence>>


    // --- INVESTIGATION LOGS ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInvestigationLog(log: InvestigationLog)

    @Query("SELECT * FROM investigations WHERE caseNumber = :caseNumber ORDER BY dateLogged DESC")
    fun getInvestigationLogsForCase(caseNumber: String): Flow<List<InvestigationLog>>

    @Query("SELECT * FROM investigations ORDER BY dateLogged DESC")
    fun getAllInvestigationLogs(): Flow<List<InvestigationLog>>
}
