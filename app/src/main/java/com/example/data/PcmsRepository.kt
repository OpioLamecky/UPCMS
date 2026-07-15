package com.example.data

import com.example.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class PcmsRepository(private val pcmsDao: PcmsDao) {

    // --- OFFICERS ---
    val allOfficers: Flow<List<Officer>> = pcmsDao.getAllOfficers()

    suspend fun getOfficerByBadge(badge: String): Officer? {
        return pcmsDao.getOfficerByBadge(badge)
    }

    suspend fun insertOfficer(officer: Officer) {
        pcmsDao.insertOfficer(officer)
    }

    // --- CASES ---
    val allCases: Flow<List<Case>> = pcmsDao.getAllCases()

    fun getCasesForOfficer(badge: String): Flow<List<Case>> {
        return pcmsDao.getCasesForOfficer(badge)
    }

    fun getCaseByNumber(number: String): Flow<Case?> {
        return pcmsDao.getCaseByNumber(number)
    }

    suspend fun insertCase(pcmsCase: Case) {
        pcmsDao.insertCase(pcmsCase)
    }

    suspend fun updateCase(pcmsCase: Case) {
        pcmsDao.updateCase(pcmsCase)
    }

    suspend fun deleteCase(number: String) {
        pcmsDao.deleteCaseByNumber(number)
    }

    // --- STATEMENTS ---
    val allStatements: Flow<List<Statement>> = pcmsDao.getAllStatements()

    fun getStatementsForCase(caseNumber: String): Flow<List<Statement>> {
        return pcmsDao.getStatementsForCase(caseNumber)
    }

    suspend fun insertStatement(statement: Statement) {
        pcmsDao.insertStatement(statement)
    }

    // --- COMPLAINANTS ---
    val allComplainants: Flow<List<Complainant>> = pcmsDao.getAllComplainants()

    fun getComplainantsForCase(caseNumber: String): Flow<List<Complainant>> {
        return pcmsDao.getComplainantsForCase(caseNumber)
    }

    suspend fun insertComplainant(complainant: Complainant) {
        pcmsDao.insertComplainant(complainant)
    }

    // --- SUSPECTS ---
    val allSuspects: Flow<List<Suspect>> = pcmsDao.getAllSuspects()

    fun getSuspectsForCase(caseNumber: String): Flow<List<Suspect>> {
        return pcmsDao.getSuspectsForCase(caseNumber)
    }

    suspend fun insertSuspect(suspect: Suspect) {
        pcmsDao.insertSuspect(suspect)
    }

    // --- WITNESSES ---
    val allWitnesses: Flow<List<Witness>> = pcmsDao.getAllWitnesses()

    fun getWitnessesForCase(caseNumber: String): Flow<List<Witness>> {
        return pcmsDao.getWitnessesForCase(caseNumber)
    }

    suspend fun insertWitness(witness: Witness) {
        pcmsDao.insertWitness(witness)
    }

    // --- EVIDENCE ---
    val allEvidence: Flow<List<Evidence>> = pcmsDao.getAllEvidence()

    fun getEvidenceForCase(caseNumber: String): Flow<List<Evidence>> {
        return pcmsDao.getEvidenceForCase(caseNumber)
    }

    suspend fun insertEvidence(evidence: Evidence) {
        pcmsDao.insertEvidence(evidence)
    }

    // --- INVESTIGATION LOGS ---
    val allInvestigationLogs: Flow<List<InvestigationLog>> = pcmsDao.getAllInvestigationLogs()

    fun getInvestigationLogsForCase(caseNumber: String): Flow<List<InvestigationLog>> {
        return pcmsDao.getInvestigationLogsForCase(caseNumber)
    }

    suspend fun insertInvestigationLog(log: InvestigationLog) {
        pcmsDao.insertInvestigationLog(log)
    }

    // --- PRE-POPULATION LOGIC ---
    suspend fun ensurePrepopulated() {
        // We use first() to take a snapshot of the current state of officers list
        val officers = pcmsDao.getAllOfficers().first()
        val hasMukasa = officers.any { it.badgeNumber == "48291" || it.badgeNumber == "48291-UPF" }
        if (!hasMukasa) {
            // Seed Uganda Police Force officers
            val seedOfficers = listOf(
                Officer("48291", "Mukasa Joseph", "Sergeant", "Admin", "password"),
                Officer("B1001", "James Smith", "Superintendent", "Admin", "admin"),
                Officer("B1002", "Sarah Jones", "Inspector", "Officer", "officer"),
                Officer("B1003", "John Doe", "Sergeant", "Officer", "password")
            )
            seedOfficers.forEach { pcmsDao.insertOfficer(it) }

            // Seed exact cases from screenshots
            val now = System.currentTimeMillis()
            val seedCases = listOf(
                Case(
                    caseNumber = "UPF-2023-9021",
                    title = "Aggravated Robbery - Entebbe Rd",
                    description = "Armed robbery incident reported on Entebbe Road near the petrol station. Suspects armed with SMG took cash and valuables.",
                    category = "Robbery",
                    priority = "High",
                    status = "ACTIVE INVESTIGATION",
                    dateCreated = now - (86400000 * 10),
                    timeCreated = "02:15",
                    assignedOfficerBadge = "48291"
                ),
                Case(
                    caseNumber = "UPF-2023-8842",
                    title = "Fraud Case - Diamond Bank",
                    description = "Unauthorized vault access and digital transactions reported by Diamond Bank Kampala branch audit team.",
                    category = "Fraud",
                    priority = "Medium",
                    status = "AWAITING FORENSICS",
                    dateCreated = now - (86400000 * 5),
                    timeCreated = "10:30",
                    assignedOfficerBadge = "48291"
                ),
                Case(
                    caseNumber = "UPF-2023-9104",
                    title = "Missing Person - Makindye",
                    description = "Report of a missing teenager last seen boarding a local taxi at Makindye junction. Immediate priority search initiated.",
                    category = "Other",
                    priority = "High",
                    status = "CRITICAL PRIORITY",
                    dateCreated = now - (86400000 * 1),
                    timeCreated = "16:40",
                    assignedOfficerBadge = "48291"
                ),
                Case(
                    caseNumber = "CRIM-2023-0842",
                    title = "Theft at Central Market",
                    description = "Snatched wallet and electronic goods from a vendor stall inside Kampala Central Market. CCTV footage recovered.",
                    category = "Theft",
                    priority = "Medium",
                    status = "OPEN",
                    dateCreated = now - (86400000 * 3),
                    timeCreated = "11:20",
                    assignedOfficerBadge = "48291"
                ),
                Case(
                    caseNumber = "CRIM-2023-0791",
                    title = "Illegal Possession of Firearms",
                    description = "Raided property at Plot 12, Katwe. Recovered unlicensed assault rifle. Suspect currently in police custody.",
                    category = "Weapon",
                    priority = "High",
                    status = "REFERRED TO PROSECUTION",
                    dateCreated = now - (86400000 * 6),
                    timeCreated = "22:15",
                    assignedOfficerBadge = "48291"
                ),
                Case(
                    caseNumber = "CRIM-2023-0755",
                    title = "Digital Fraud - Mobile Money",
                    description = "Cyber Crime Unit investigation into linked mobile money accounts and unauthorized SIM card swaps in Entebbe.",
                    category = "Fraud",
                    priority = "Medium",
                    status = "CLOSED",
                    dateCreated = now - (86400000 * 12),
                    timeCreated = "09:00",
                    assignedOfficerBadge = "48291"
                ),
                Case(
                    caseNumber = "CRIM-2023-0722",
                    title = "Traffic Incident: Hit and Run",
                    description = "Hit and run collision at Jinja Road junction. White Sedan identified from CCTV logs driving towards Nakawa.",
                    category = "Other",
                    priority = "Low",
                    status = "OPEN",
                    dateCreated = now - (86400000 * 15),
                    timeCreated = "17:35",
                    assignedOfficerBadge = "48291"
                )
            )
            seedCases.forEach { pcmsDao.insertCase(it) }

            // Seed Complainants, Suspects, and Witnesses matching screenshots
            pcmsDao.insertComplainant(Complainant(0, "UPF-2023-9021", "Katumba Fred", "+256 772 101010", "kfred@gmail.com", "Entebbe Rd, Kampala"))
            pcmsDao.insertComplainant(Complainant(0, "UPF-2023-9104", "Namubiru Joyce", "+256 701 223344", "jnamu@hotmail.com", "Makindye Hill, Block 3"))
            pcmsDao.insertComplainant(Complainant(0, "CRIM-2023-0842", "Mary Atwine", "+256 752 908070", "matwine@yahoo.com", "Kisenyi Lane 4"))

            pcmsDao.insertSuspect(Suspect(0, "UPF-2023-9104", "Okello Sam", 22, "Under Investigation", "Makindye Area", "Last seen with missing person. Suspect in custody."))
            pcmsDao.insertSuspect(Suspect(0, "CRIM-2023-0791", "Kigozi Bruno", 31, "In Custody", "Katwe Plot 12", "Found in possession of unregistered firearm."))

            pcmsDao.insertWitness(Witness(0, "UPF-2023-9021", "Katumba Fred", "+256 772 101010", "Entebbe Rd Shop"))
            pcmsDao.insertWitness(Witness(0, "CRIM-2023-0722", "Kassim Ssekandi (Taxi Driver)", "+256 781 112233", "Jinja Road Stage"))

            // Seed statements
            pcmsDao.insertStatement(Statement(
                id = 0,
                caseNumber = "UPF-2023-9021",
                officerBadge = "48291",
                personName = "Witness: Katumba Fred",
                personRole = "Witness",
                dateRecorded = now - (86400000 * 2),
                timeRecorded = "14:15",
                statementText = "I saw two armed boys on a boda boda with masked faces stop right in front of the petrol station. They fired once in the air and demanded all the cash box contents. It was terrifying."
            ))
            pcmsDao.insertStatement(Statement(
                id = 0,
                caseNumber = "UPF-2023-9104",
                officerBadge = "48291",
                personName = "Suspect: Okello Sam",
                personRole = "Suspect",
                dateRecorded = now - (86400000 * 1),
                timeRecorded = "09:30",
                statementText = "I don't know where she went. We were at the junction but she boarded a different taxi going towards the city center."
            ))

            // Seed Evidence
            pcmsDao.insertEvidence(Evidence(
                id = 0,
                caseNumber = "UPF-2023-9021",
                evidenceCode = "EVID-2023-0912-882",
                description = "7.62mm spent shell casing recovered from Entebbe Rd petrol station heist site.",
                category = "Weapon",
                dateCollected = now - (86400000 * 10),
                collectingOfficerBadge = "48291",
                storageLocation = "Kampala Central Evidence Vault Locker B"
            ))

            // Seed Investigation Logs
            pcmsDao.insertInvestigationLog(InvestigationLog(
                id = 0,
                caseNumber = "UPF-2023-9021",
                dateLogged = now - (86400000 * 10),
                officerBadge = "48291",
                logMessage = "Case opened and active patrol team dispatched to petrol station on Entebbe Road.",
                nextSteps = "Secure crime scene, record statements from staff, and request traffic CCTV surveillance logs."
            ))
        }
    }
}
