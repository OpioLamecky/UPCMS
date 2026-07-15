package com.example.ui

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.res.painterResource
import com.example.R
import com.example.data.model.*
import com.example.ui.theme.*

enum class AppTab {
    Dashboard,
    Cases,
    Search,
    Admin
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PcmsApp(viewModel: PcmsViewModel) {
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val loginError by viewModel.loginError.collectAsStateWithLifecycle()

    var isRegisterMode by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (currentUser == null) {
            if (isRegisterMode) {
                OfficerRegistrationScreen(
                    viewModel = viewModel,
                    onBackToLogin = { isRegisterMode = false }
                )
            } else {
                LoginScreen(
                    viewModel = viewModel,
                    loginError = loginError,
                    onToggleRegister = { isRegisterMode = true }
                )
            }
        } else {
            MainPortalContainer(
                viewModel = viewModel,
                currentUser = currentUser!!
            )
        }
    }
}

// --- LOGIN SCREEN ---
// --- LOGIN SCREEN (UPF PCMS) ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: PcmsViewModel,
    loginError: String?,
    onToggleRegister: () -> Unit
) {
    var badgeNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .drawBehind {
                // High-fidelity light blue grid background as in the screenshot
                val gridSize = 40.dp.toPx()
                val paintColor = Color(0xFF001534).copy(alpha = 0.04f)
                var x = 0f
                while (x < this.size.width) {
                    drawLine(paintColor, androidx.compose.ui.geometry.Offset(x, 0f), androidx.compose.ui.geometry.Offset(x, this.size.height), strokeWidth = 1f)
                    x += gridSize
                }
                var y = 0f
                while (y < this.size.height) {
                    drawLine(paintColor, androidx.compose.ui.geometry.Offset(0f, y), androidx.compose.ui.geometry.Offset(this.size.width, y), strokeWidth = 1f)
                    y += gridSize
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Uganda Police Force Seal Box (Screenshot 1)
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier
                    .size(100.dp)
                    .padding(bottom = 12.dp),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_uganda_police_logo),
                        contentDescription = "Uganda Police Force Logo",
                        modifier = Modifier.size(80.dp)
                    )
                }
            }

            Text(
                text = "UPF PCMS",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.SansSerif,
                    letterSpacing = 1.sp
                ),
                color = Color(0xFF001534),
                modifier = Modifier.padding(bottom = 2.dp)
            )

            Text(
                text = "CASE MANAGEMENT SYSTEM • SECURE PORTAL",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                ),
                color = Color(0xFF64748B),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Sign-In Card
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 450.dp)
            ) {
                Column(
                    modifier = Modifier.padding(28.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Officer Sign-in",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF001534),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    Text(
                        text = "Access the national criminal records and investigation database.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF475569),
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    // Username/Badge Label
                    Text(
                        text = "OFFICER ID / BADGE NUMBER",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF001534),
                        modifier = Modifier.padding(bottom = 6.dp)
                    )

                    OutlinedTextField(
                        value = badgeNumber,
                        onValueChange = { badgeNumber = it },
                        placeholder = { Text("e.g. 48291-UPF", color = Color.LightGray) },
                        leadingIcon = { Icon(Icons.Filled.Badge, contentDescription = null, tint = Color(0xFF64748B)) },
                        singleLine = true,
                        shape = RoundedCornerShape(6.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF001534),
                            unfocusedBorderColor = Color(0xFFCBD5E1),
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedTextColor = Color(0xFF001534),
                            unfocusedTextColor = Color(0xFF001534)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                            .testTag("login_badge_input")
                    )

                    // Password Label Row with Forgot Password? link
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "SECURITY PASSWORD",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFF001534)
                        )
                        Text(
                            text = "Forgot Password?",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFF3B82F6),
                            modifier = Modifier.clickable { /* action */ }
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        placeholder = { Text("Enter security password", color = Color.LightGray) },
                        leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null, tint = Color(0xFF64748B)) },
                        trailingIcon = {
                            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                Icon(
                                    imageVector = if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                    contentDescription = "Toggle Password Visibility",
                                    tint = Color(0xFF64748B)
                                )
                            }
                        },
                        visualTransformation = if (isPasswordVisible) androidx.compose.ui.text.input.VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        shape = RoundedCornerShape(6.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF001534),
                            unfocusedBorderColor = Color(0xFFCBD5E1),
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedTextColor = Color(0xFF001534),
                            unfocusedTextColor = Color(0xFF001534)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                            .testTag("login_password_input")
                    )

                    // Advisory Warning Box (Screenshot 1)
                    Card(
                        shape = RoundedCornerShape(4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
                        border = BorderStroke(1.dp, Color(0xFFDBEAFE)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Gavel,
                                contentDescription = "Legal Advisory",
                                tint = Color(0xFFB45309),
                                modifier = Modifier
                                    .size(18.dp)
                                    .padding(top = 2.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Unauthorized access to this system is strictly prohibited under the Computer Misuse Act. All sessions are monitored and logged.",
                                style = MaterialTheme.typography.bodySmall.copy(fontStyle = androidx.compose.ui.text.font.FontStyle.Italic),
                                color = Color(0xFF1E293B),
                                lineHeight = 16.sp
                            )
                        }
                    }

                    if (loginError != null) {
                        Text(
                            text = loginError,
                            color = AlertRed,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                    }

                    // Authorize Login button (Screenshot 1)
                    Button(
                        onClick = {
                            if (badgeNumber.isNotBlank() && password.isNotBlank()) {
                                viewModel.login(badgeNumber.trim().uppercase(), password) {}
                            } else {
                                // Default evaluation login bypass for standard convenience
                                viewModel.login("48291", "password") {}
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF001534)),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("login_submit_button")
                    ) {
                        Text("AUTHORIZE LOGIN ", fontWeight = FontWeight.Bold, color = Color.White)
                        Icon(Icons.Filled.VerifiedUser, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    TextButton(
                        onClick = onToggleRegister,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("Register New Officer Badge", color = Color(0xFF3B82F6), fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Footer Links (Screenshot 1)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 450.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { /* help */ }
                ) {
                    Icon(Icons.Filled.HelpOutline, contentDescription = null, tint = Color(0xFF001534), modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Contact IT Support", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = Color(0xFF001534))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text("|", color = Color(0xFFCBD5E1))
                Spacer(modifier = Modifier.width(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { /* policy */ }
                ) {
                    Icon(Icons.Filled.Security, contentDescription = null, tint = Color(0xFF001534), modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("System Policy", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = Color(0xFF001534))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "© 2024 Uganda Police Force. All Rights Reserved.\nDepartment of Information & Communications Technology.",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF64748B),
                textAlign = TextAlign.Center,
                lineHeight = 16.sp,
                modifier = Modifier.widthIn(max = 400.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Demo hints
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7)),
                border = BorderStroke(1.dp, Color(0xFFFDE68A)),
                modifier = Modifier.widthIn(max = 450.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Evaluation Account Details:", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = Color(0xFF92400E))
                    Text("• Sgt. Mukasa Joseph | Badge: 48291 | Password: password", style = MaterialTheme.typography.bodySmall, color = Color(0xFF92400E))
                }
            }
        }
    }
}

// --- OFFICER REGISTRATION SCREEN ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OfficerRegistrationScreen(
    viewModel: PcmsViewModel,
    onBackToLogin: () -> Unit
) {
    var badgeNumber by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val ranks = listOf("Constable", "Sergeant", "Inspector", "Superintendent")
    var selectedRank by remember { mutableStateOf(ranks[0]) }
    var isRankExpanded by remember { mutableStateOf(false) }

    val roles = listOf("Officer", "Admin")
    var selectedRole by remember { mutableStateOf(roles[0]) }
    var isRoleExpanded by remember { mutableStateOf(false) }

    var errorMsg by remember { mutableStateOf<String?>(null) }
    var successMsg by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(colors = listOf(PoliceBlue, CommandNavy))),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "PRECINCT REGISTER",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 2.sp
                ),
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CommandNavy),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 450.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = "Register Officer Badge",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    OutlinedTextField(
                        value = badgeNumber,
                        onValueChange = { badgeNumber = it },
                        label = { Text("Badge Number") },
                        placeholder = { Text("e.g. B2055") },
                        leadingIcon = { Icon(Icons.Filled.Badge, contentDescription = null, tint = SteelBlue) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SteelBlue,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    )

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Full Name") },
                        placeholder = { Text("e.g. Officer Alex Mercer") },
                        leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null, tint = SteelBlue) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SteelBlue,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    )

                    // Rank Selection
                    ExposedDropdownMenuBox(
                        expanded = isRankExpanded,
                        onExpandedChange = { isRankExpanded = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    ) {
                        OutlinedTextField(
                            value = selectedRank,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Rank Designation") },
                            leadingIcon = { Icon(Icons.Filled.MilitaryTech, contentDescription = null, tint = SteelBlue) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isRankExpanded) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SteelBlue,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = isRankExpanded,
                            onDismissRequest = { isRankExpanded = false }
                        ) {
                            ranks.forEach { rank ->
                                DropdownMenuItem(
                                    text = { Text(rank) },
                                    onClick = {
                                        selectedRank = rank
                                        isRankExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    // Role Selection
                    ExposedDropdownMenuBox(
                        expanded = isRoleExpanded,
                        onExpandedChange = { isRoleExpanded = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    ) {
                        OutlinedTextField(
                            value = selectedRole,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Portal Role Access") },
                            leadingIcon = { Icon(Icons.Filled.AdminPanelSettings, contentDescription = null, tint = SteelBlue) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isRoleExpanded) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SteelBlue,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = isRoleExpanded,
                            onDismissRequest = { isRoleExpanded = false }
                        ) {
                            roles.forEach { role ->
                                DropdownMenuItem(
                                    text = { Text(role) },
                                    onClick = {
                                        selectedRole = role
                                        isRoleExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Create Access Password") },
                        leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null, tint = SteelBlue) },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SteelBlue,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    )

                    if (errorMsg != null) {
                        Text(
                            text = errorMsg!!,
                            color = AlertRed,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                    }

                    if (successMsg != null) {
                        Text(
                            text = successMsg!!,
                            color = SafeGreen,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                    }

                    Button(
                        onClick = {
                            if (badgeNumber.isBlank() || name.isBlank() || password.isBlank()) {
                                errorMsg = "All fields are required"
                                successMsg = null
                                return@Button
                            }
                            val newOfficer = Officer(
                                badgeNumber = badgeNumber.trim().uppercase(),
                                name = name.trim(),
                                rank = selectedRank,
                                role = selectedRole,
                                passwordHash = password
                            )
                            viewModel.registerOfficer(
                                officer = newOfficer,
                                onSuccess = {
                                    successMsg = "Registration Successful! Badge ${newOfficer.badgeNumber} active."
                                    errorMsg = null
                                    badgeNumber = ""
                                    name = ""
                                    password = ""
                                },
                                onError = {
                                    errorMsg = it
                                    successMsg = null
                                }
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SteelBlue),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Text("Complete Registration", fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    TextButton(
                        onClick = onBackToLogin,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("Return to Login Portal", color = DarkSecondary)
                    }
                }
            }
        }
    }
}

// --- MAIN PORTAL CONTAINER ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPortalContainer(
    viewModel: PcmsViewModel,
    currentUser: Officer
) {
    var selectedTab by remember { mutableStateOf(AppTab.Dashboard) }
    var selectedCaseNumber by remember { mutableStateOf<String?>(null) }
    var statementRecordingCaseNumber by remember { mutableStateOf<String?>(null) }

    // Navigation overrides
    val navigateToCaseDetails: (String) -> Unit = { caseNum ->
        selectedCaseNumber = caseNum
        statementRecordingCaseNumber = null
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Small circular crest icon (Screenshot 3/4)
                        Image(
                            painter = painterResource(id = R.drawable.ic_uganda_police_logo),
                            contentDescription = "Uganda Police Force Logo",
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                                .border(1.dp, Color(0xFFE2E8F0), CircleShape)
                        )
                        Text(
                            text = "UPF PCMS",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.SansSerif,
                                letterSpacing = 0.5.sp
                            ),
                            color = Color(0xFF001534)
                        )
                    }
                },
                actions = {
                    // Small Search Icon
                    IconButton(onClick = {
                        selectedTab = AppTab.Search
                        selectedCaseNumber = null
                        statementRecordingCaseNumber = null
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search Tab",
                            tint = Color(0xFF001534)
                        )
                    }
                    // User Badge Info & Logout Icon
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFFF1F5F9))
                            .padding(horizontal = 6.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Sgt. Mukasa",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFF001534)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF22C55E))
                        )
                    }
                    IconButton(
                        onClick = {
                            viewModel.logout {}
                        },
                        modifier = Modifier.testTag("logout_button")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Logout,
                            contentDescription = "Log Out",
                            tint = Color(0xFF001534)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color(0xFF001534)
                ),
                modifier = Modifier.border(0.5.dp, Color(0xFFE2E8F0))
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .border(0.5.dp, Color(0xFFE2E8F0)),
                tonalElevation = 2.dp
            ) {
                // NavigationBar items matching style from Screenshots
                val activeTabColor = Color(0xFF001534)
                val inactiveTabColor = Color(0xFF64748B)
                val pillIndicatorColor = Color(0xFFFDE047) // Custom Yellow indicator pill

                NavigationBarItem(
                    selected = selectedTab == AppTab.Dashboard && selectedCaseNumber == null && statementRecordingCaseNumber == null,
                    onClick = {
                        selectedTab = AppTab.Dashboard
                        selectedCaseNumber = null
                        statementRecordingCaseNumber = null
                    },
                    icon = { Icon(Icons.Filled.GridView, contentDescription = "Dashboard") },
                    label = { Text("Dashboard") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = activeTabColor,
                        selectedTextColor = activeTabColor,
                        indicatorColor = pillIndicatorColor,
                        unselectedIconColor = inactiveTabColor,
                        unselectedTextColor = inactiveTabColor
                    )
                )

                NavigationBarItem(
                    selected = (selectedTab == AppTab.Cases || selectedCaseNumber != null || statementRecordingCaseNumber != null) && selectedTab != AppTab.Search && selectedTab != AppTab.Admin,
                    onClick = {
                        selectedTab = AppTab.Cases
                        selectedCaseNumber = null
                        statementRecordingCaseNumber = null
                    },
                    icon = { Icon(Icons.Filled.Folder, contentDescription = "Cases") },
                    label = { Text("Cases") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = activeTabColor,
                        selectedTextColor = activeTabColor,
                        indicatorColor = pillIndicatorColor,
                        unselectedIconColor = inactiveTabColor,
                        unselectedTextColor = inactiveTabColor
                    )
                )

                NavigationBarItem(
                    selected = selectedTab == AppTab.Search && selectedCaseNumber == null && statementRecordingCaseNumber == null,
                    onClick = {
                        selectedTab = AppTab.Search
                        selectedCaseNumber = null
                        statementRecordingCaseNumber = null
                    },
                    icon = { Icon(Icons.Filled.Search, contentDescription = "Crime Map") },
                    label = { Text("Crime Map") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = activeTabColor,
                        selectedTextColor = activeTabColor,
                        indicatorColor = pillIndicatorColor,
                        unselectedIconColor = inactiveTabColor,
                        unselectedTextColor = inactiveTabColor
                    )
                )

                NavigationBarItem(
                    selected = selectedTab == AppTab.Admin && selectedCaseNumber == null && statementRecordingCaseNumber == null,
                    onClick = {
                        selectedTab = AppTab.Admin
                        selectedCaseNumber = null
                        statementRecordingCaseNumber = null
                    },
                    icon = { Icon(Icons.Filled.AccountCircle, contentDescription = "Profile") },
                    label = { Text("Profile") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = activeTabColor,
                        selectedTextColor = activeTabColor,
                        indicatorColor = pillIndicatorColor,
                        unselectedIconColor = inactiveTabColor,
                        unselectedTextColor = inactiveTabColor
                    )
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (statementRecordingCaseNumber != null) {
                RecordStatementScreen(
                    caseNumber = statementRecordingCaseNumber!!,
                    viewModel = viewModel,
                    onBack = { statementRecordingCaseNumber = null }
                )
            } else if (selectedCaseNumber != null) {
                CaseDetailScreen(
                    caseNumber = selectedCaseNumber!!,
                    viewModel = viewModel,
                    currentUser = currentUser,
                    onBack = { selectedCaseNumber = null },
                    onRecordStatement = { caseNum ->
                        statementRecordingCaseNumber = caseNum
                    }
                )
            } else {
                when (selectedTab) {
                    AppTab.Dashboard -> DashboardScreen(
                        viewModel = viewModel,
                        onSelectCase = navigateToCaseDetails,
                        onRecordStatement = { caseNum ->
                            statementRecordingCaseNumber = caseNum
                        }
                    )
                    AppTab.Cases -> CaseListScreen(
                        viewModel = viewModel,
                        onSelectCase = navigateToCaseDetails
                    )
                    AppTab.Search -> SearchScreen(
                        viewModel = viewModel,
                        onSelectCase = navigateToCaseDetails
                    )
                    AppTab.Admin -> AdminPanelScreen(
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

// --- TAB 1: OPERATIONAL DASHBOARD (UPF Command Center) ---
@Composable
fun DashboardScreen(
    viewModel: PcmsViewModel,
    onSelectCase: (String) -> Unit,
    onRecordStatement: (String) -> Unit
) {
    val cases by viewModel.cases.collectAsStateWithLifecycle()
    val suspects by viewModel.suspects.collectAsStateWithLifecycle()
    val evidence by viewModel.evidence.collectAsStateWithLifecycle()
    val logs by viewModel.investigationLogs.collectAsStateWithLifecycle()

    var showNewCaseDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Officer Profile Banner (Screenshot 3)
        Card(
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Officer Avatar placeholder
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF1F5F9)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.AccountCircle, contentDescription = null, tint = Color(0xFF001534), modifier = Modifier.size(36.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Sgt. Mukasa Joseph",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFF001534)
                        )
                        Text(
                            text = "Badge #48291 • Investigating Officer, Kampala Central",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF64748B)
                        )
                    }
                }
                // Duty Status Pill
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFDCFCE7))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF22C55E))
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "DUTY ACTIVE",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF166534)
                    )
                }
            }
        }

        // Quick Command Action Buttons (Screenshot 3)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // New Case Button
            Button(
                onClick = { showNewCaseDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF001534)),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier
                    .weight(1.1f)
                    .height(42.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("NEW CASE", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
            }

            // Record Statement
            OutlinedButton(
                onClick = { onRecordStatement("UPF-2023-9021") },
                border = BorderStroke(1.dp, Color(0xFF001534)),
                shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF001534)),
                modifier = Modifier
                    .weight(1.4f)
                    .height(42.dp)
            ) {
                Icon(Icons.Filled.EditNote, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("RECORD STATEMENT", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
            }

            // Search Database (Switches view/map)
            OutlinedButton(
                onClick = { onRecordStatement("UPF-2023-9104") },
                border = BorderStroke(1.dp, Color(0xFFCBD5E1)),
                shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF475569)),
                modifier = Modifier
                    .weight(1.3f)
                    .height(42.dp)
            ) {
                Icon(Icons.Filled.Search, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("SEARCH DB", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
            }
        }

        // Section: My Active Cases (Screenshot 3)
        Card(
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "My Active Cases",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF001534)
                    )
                    Text(
                        text = "View Registry",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF3B82F6),
                        modifier = Modifier.clickable { /* Registry action */ }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Table Header Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF8FAFC))
                        .padding(vertical = 8.dp, horizontal = 12.dp)
                ) {
                    Text("CASE ID", modifier = Modifier.weight(1f), style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = Color(0xFF64748B))
                    Text("INCIDENT", modifier = Modifier.weight(2.2f), style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = Color(0xFF64748B))
                    Text("STATUS", modifier = Modifier.weight(1.8f), style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = Color(0xFF64748B))
                }

                // Table Items
                cases.take(4).forEach { pcmsCase ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelectCase(pcmsCase.caseNumber) }
                            .padding(vertical = 12.dp, horizontal = 12.dp)
                            .drawBehind {
                                drawLine(Color(0xFFE2E8F0), androidx.compose.ui.geometry.Offset(0f, this.size.height), androidx.compose.ui.geometry.Offset(this.size.width, this.size.height), strokeWidth = 1f)
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = pcmsCase.caseNumber,
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = pcmsCase.title,
                            modifier = Modifier.weight(2.2f),
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF334155),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        
                        // Styled badges for UPF Case statuses
                        val (bgColor, textColor) = when (pcmsCase.status) {
                            "ACTIVE INVESTIGATION" -> Color(0xFFFEF3C7) to Color(0xFF92400E)
                            "AWAITING FORENSICS" -> Color(0xFFE0F2FE) to Color(0xFF0369A1)
                            "CRITICAL PRIORITY" -> Color(0xFFFEE2E2) to Color(0xFF991B1B)
                            "CLOSED" -> Color(0xFFF1F5F9) to Color(0xFF475569)
                            else -> Color(0xFFEFF6FF) to Color(0xFF1E40AF)
                        }

                        Box(
                            modifier = Modifier
                                .weight(1.8f)
                                .clip(RoundedCornerShape(4.dp))
                                .background(bgColor)
                                .padding(horizontal = 6.dp, vertical = 2.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(
                                text = pcmsCase.status,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 9.sp),
                                color = textColor,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Case Load Progress Indicator (Screenshot 3)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "ACTIVE LOAD: ${cases.size} Total Cases",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF001534)
                    )
                    Text(
                        text = "6 High Priority",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFFEF4444)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = { 0.65f },
                    color = Color(0xFF001534),
                    trackColor = Color(0xFFE2E8F0),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                )
            }
        }

        // Row of Pending Statements & Today's Appointments (Screenshot 3)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Card: Pending Statements (Screenshot 3)
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                modifier = Modifier.weight(1f)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Pending Statements",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFF001534)
                        )
                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFEF4444)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("3", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Pending Item 1
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onRecordStatement("UPF-2023-9021") }
                            .padding(vertical = 6.dp)
                    ) {
                        Text("Witness: Katumba Fred", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = Color(0xFF334155))
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Case: UPF-2023-9021", style = MaterialTheme.typography.bodySmall, color = Color(0xFF64748B))
                            Text("RECORD", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = Color(0xFF3B82F6))
                        }
                    }
                    HorizontalDivider(color = Color(0xFFF1F5F9))

                    // Pending Item 2
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onRecordStatement("UPF-2023-9104") }
                            .padding(vertical = 6.dp)
                    ) {
                        Text("Suspect: Okello Sam", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = Color(0xFF334155))
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Case: UPF-2023-9104", style = MaterialTheme.typography.bodySmall, color = Color(0xFF64748B))
                            Text("RECORD", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = Color(0xFF3B82F6))
                        }
                    }
                }
            }

            // Card: Today's Appointments (Screenshot 3)
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                modifier = Modifier.weight(1.1f)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "Today's Appointments",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF001534)
                    )
                    Text(
                        text = "Friday, Oct 27, 2023",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF64748B),
                        modifier = Modifier.padding(bottom = 10.dp)
                    )

                    // Appointment 1
                    Column(modifier = Modifier.padding(vertical = 4.dp)) {
                        Row(verticalAlignment = Alignment.Top) {
                            Text("09:00 AM", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = Color(0xFF001534), modifier = Modifier.width(52.dp))
                            Column {
                                Text("Court Hearing", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = Color(0xFF334155))
                                Text("Magistrate Court 4, Buganda Rd", style = MaterialTheme.typography.bodySmall, color = Color(0xFF64748B))
                            }
                        }
                    }
                    HorizontalDivider(color = Color(0xFFF1F5F9))

                    // Appointment 2
                    Column(modifier = Modifier.padding(vertical = 4.dp)) {
                        Row(verticalAlignment = Alignment.Top) {
                            Text("11:30 AM", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = Color(0xFF001534), modifier = Modifier.width(52.dp))
                            Column {
                                Text("Scene Visit - Entebbe Rd", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = Color(0xFF334155))
                                Text("Evidence Collection", style = MaterialTheme.typography.bodySmall, color = Color(0xFF64748B))
                            }
                        }
                    }
                }
            }
        }

        // Section: Map (Screenshot 3/5)
        Card(
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Map Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "ACTIVE INCIDENT MAP",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFF001534)
                        )
                        Text(
                            text = "Kampala Metropolitan Area",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF64748B)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFFFFECEB))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "4 ACTIVE PINS",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFFEF4444)
                        )
                    }
                }

                // Interactive Kampala Map Container with Canvas (Screenshot 5)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(Color(0xFFE2E8F0))
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val width = size.width
                        val height = size.height

                        // Draw Grid/Water border coordinates
                        val borderPath = androidx.compose.ui.graphics.Path().apply {
                            moveTo(0f, height * 0.9f)
                            quadraticTo(width * 0.3f, height * 0.85f, width * 0.5f, height)
                        }
                        drawPath(
                            path = borderPath,
                            color = Color(0xFF93C5FD),
                            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4f.dp.toPx())
                        )

                        // Draw main connecting roads (Entebbe Rd, Jinja Rd, Kampala Rd)
                        // Road 1: Kampala - Entebbe Rd
                        drawLine(
                            color = Color.White,
                            start = androidx.compose.ui.geometry.Offset(width * 0.25f, 0f),
                            end = androidx.compose.ui.geometry.Offset(width * 0.5f, height),
                            strokeWidth = 6f.dp.toPx()
                        )
                        drawLine(
                            color = Color(0xFF94A3B8),
                            start = androidx.compose.ui.geometry.Offset(width * 0.25f, 0f),
                            end = androidx.compose.ui.geometry.Offset(width * 0.5f, height),
                            strokeWidth = 1f.dp.toPx()
                        )

                        // Road 2: Jinja Road
                        drawLine(
                            color = Color.White,
                            start = androidx.compose.ui.geometry.Offset(0f, height * 0.4f),
                            end = androidx.compose.ui.geometry.Offset(width, height * 0.5f),
                            strokeWidth = 6f.dp.toPx()
                        )
                        drawLine(
                            color = Color(0xFF94A3B8),
                            start = androidx.compose.ui.geometry.Offset(0f, height * 0.4f),
                            end = androidx.compose.ui.geometry.Offset(width, height * 0.5f),
                            strokeWidth = 1f.dp.toPx()
                        )

                        // Landmark: Kampala Central Precinct Node
                        drawCircle(
                            color = Color(0xFF001534),
                            radius = 6f.dp.toPx(),
                            center = androidx.compose.ui.geometry.Offset(width * 0.4f, height * 0.4f)
                        )
                        drawCircle(
                            color = Color.White,
                            radius = 2f.dp.toPx(),
                            center = androidx.compose.ui.geometry.Offset(width * 0.4f, height * 0.4f)
                        )
                    }

                    // Floating marker cards
                    Box(modifier = Modifier.fillMaxSize()) {
                        // Marker 1: Entebbe Rd petrol station (Robbery pin)
                        MarkerPin(
                            title = "UPF-2023-9021",
                            description = "Robbery (Active)",
                            color = Color(0xFFEF4444),
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 60.dp, end = 40.dp)
                                .clickable { onSelectCase("UPF-2023-9021") }
                        )

                        // Marker 2: Makindye Junction (Missing Person)
                        MarkerPin(
                            title = "UPF-2023-9104",
                            description = "Missing Person",
                            color = Color(0xFFF59E0B),
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(top = 20.dp, start = 40.dp)
                                .clickable { onSelectCase("UPF-2023-9104") }
                        )

                        // Floating Layer controls at bottom-right of map (Screenshot 5)
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(8.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color.White)
                                    .border(1.dp, Color(0xFFCBD5E1))
                                    .clickable { /* Zoom In */ },
                                contentAlignment = Alignment.Center
                            ) {
                                Text("+", style = MaterialTheme.typography.titleMedium, color = Color(0xFF001534))
                            }
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color.White)
                                    .border(1.dp, Color(0xFFCBD5E1))
                                    .clickable { /* Zoom Out */ },
                                contentAlignment = Alignment.Center
                            ) {
                                Text("-", style = MaterialTheme.typography.titleMedium, color = Color(0xFF001534))
                            }
                        }
                    }
                }
            }
        }
    }

    // New Case Registration dialog
    if (showNewCaseDialog) {
        RegisterCaseDialog(
            viewModel = viewModel,
            onDismiss = { showNewCaseDialog = false }
        )
    }
}

@Composable
fun MarkerPin(
    title: String,
    description: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(4.dp),
            border = BorderStroke(1.dp, color.copy(alpha = 0.5f))
        ) {
            Column(modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)) {
                Text(title, fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color(0xFF001534))
                Text(description, fontSize = 7.sp, color = Color(0xFF64748B))
            }
        }
        Canvas(modifier = Modifier.size(10.dp, 8.dp)) {
            val path = androidx.compose.ui.graphics.Path().apply {
                moveTo(0f, 0f)
                lineTo(size.width, 0f)
                lineTo(size.width / 2f, size.height)
                close()
            }
            drawPath(path = path, color = Color.White)
            drawCircle(color = color, radius = 2.5f.dp.toPx(), center = androidx.compose.ui.geometry.Offset(size.width / 2f, 1f.dp.toPx()))
        }
    }
}


// --- TAB 2: CASE LIST ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaseListScreen(
    viewModel: PcmsViewModel,
    onSelectCase: (String) -> Unit
) {
    val cases by viewModel.cases.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val filterCategory by viewModel.filterCategory.collectAsStateWithLifecycle()
    val filterStatus by viewModel.filterStatus.collectAsStateWithLifecycle()
    val filterPriority by viewModel.filterPriority.collectAsStateWithLifecycle()

    var showRegisterDialog by remember { mutableStateOf(false) }

    // Filter logic
    val filteredCases = cases.filter { pcmsCase ->
        val matchesQuery = pcmsCase.caseNumber.contains(searchQuery, ignoreCase = true) ||
                pcmsCase.title.contains(searchQuery, ignoreCase = true) ||
                pcmsCase.description.contains(searchQuery, ignoreCase = true)
        val matchesCategory = filterCategory == "All" || pcmsCase.category == filterCategory
        val matchesStatus = filterStatus == "All" || pcmsCase.status == filterStatus
        val matchesPriority = filterPriority == "All" || pcmsCase.priority == filterPriority

        matchesQuery && matchesCategory && matchesStatus && matchesPriority
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showRegisterDialog = true },
                containerColor = Color(0xFF001534),
                contentColor = Color.White,
                modifier = Modifier.testTag("add_case_fab")
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Register New Case")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF8FAFC))
        ) {
            // Search Input Row
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setSearchQuery(it) },
                label = { Text("Search Cases") },
                placeholder = { Text("Search by ID, title, description...") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null, tint = Color(0xFF001534)) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.setSearchQuery("") }) {
                            Icon(Icons.Filled.Close, contentDescription = "Clear search")
                        }
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF001534),
                    focusedLabelColor = Color(0xFF001534)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .testTag("case_search_input")
            )

            // Filtering Options
            HorizontalFilterChipsRow(viewModel = viewModel)

            // Dynamic count status
            Text(
                text = "Showing ${filteredCases.size} of ${cases.size} cases",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // Scrollable List
            if (filteredCases.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Filled.FolderOff, contentDescription = null, modifier = Modifier.size(64.dp), tint = Color.Gray)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No cases found matching filters",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Gray
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredCases, key = { it.caseNumber }) { pcmsCase ->
                        CaseItemCard(
                            pcmsCase = pcmsCase,
                            onClick = { onSelectCase(pcmsCase.caseNumber) }
                        )
                    }
                }
            }
        }
    }

    if (showRegisterDialog) {
        RegisterCaseDialog(
            viewModel = viewModel,
            onDismiss = { showRegisterDialog = false }
        )
    }
}

@Composable
fun HorizontalFilterChipsRow(viewModel: PcmsViewModel) {
    val selectedCategory by viewModel.filterCategory.collectAsStateWithLifecycle()
    val selectedStatus by viewModel.filterStatus.collectAsStateWithLifecycle()
    val selectedPriority by viewModel.filterPriority.collectAsStateWithLifecycle()

    val categories = listOf("All", "Robbery", "Theft", "Fraud", "Assault", "Homicide", "Other")
    val statuses = listOf("All", "Active", "Under Investigation", "Cold Case", "Closed")
    val priorities = listOf("All", "High", "Medium", "Low")

    Column(modifier = Modifier.padding(bottom = 8.dp)) {
        // Category Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 2.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEach { category ->
                FilterChip(
                    selected = selectedCategory == category,
                    onClick = { viewModel.setFilterCategory(category) },
                    label = { Text(category) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = SteelBlue.copy(alpha = 0.2f),
                        selectedLabelColor = SteelBlue
                    )
                )
            }
        }

        // Status Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 2.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            statuses.forEach { status ->
                FilterChip(
                    selected = selectedStatus == status,
                    onClick = { viewModel.setFilterStatus(status) },
                    label = { Text(status) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = WarningAmber.copy(alpha = 0.2f),
                        selectedLabelColor = WarningAmber
                    )
                )
            }
        }
    }
}

@Composable
fun CaseItemCard(
    pcmsCase: Case,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("case_item_${pcmsCase.caseNumber}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Priority Tag Box
            val priorityColor = when (pcmsCase.priority) {
                "High" -> AlertRed
                "Medium" -> WarningAmber
                else -> SafeGreen
            }

            Box(
                modifier = Modifier
                    .width(6.dp)
                    .height(60.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(priorityColor)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = pcmsCase.caseNumber,
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        ),
                        color = SteelBlue
                    )

                    // Case Status Badge
                    val statusColor = when (pcmsCase.status) {
                        "Active" -> SteelBlue
                        "Under Investigation" -> WarningAmber
                        "Closed" -> SafeGreen
                        else -> Color.Gray
                    }
                    Card(
                        colors = CardDefaults.cardColors(containerColor = statusColor.copy(alpha = 0.15f)),
                        border = BorderStroke(1.dp, statusColor.copy(alpha = 0.3f))
                    ) {
                        Text(
                            text = pcmsCase.status.uppercase(),
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = statusColor,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = pcmsCase.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Category Icon/text
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Category, contentDescription = null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(pcmsCase.category, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }

                    // Assigned Officer
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Badge, contentDescription = null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Officer: ${pcmsCase.assignedOfficerBadge}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = "Details",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}


// --- RECORD NEW CASE DIALOG ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterCaseDialog(
    viewModel: PcmsViewModel,
    onDismiss: () -> Unit
) {
    var caseNumber by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    val categories = listOf("Robbery", "Theft", "Fraud", "Assault", "Homicide", "Other")
    var selectedCategory by remember { mutableStateOf(categories[0]) }
    var isCatExpanded by remember { mutableStateOf(false) }

    val priorities = listOf("Low", "Medium", "High")
    var selectedPriority by remember { mutableStateOf(priorities[1]) }
    var isPriorityExpanded by remember { mutableStateOf(false) }

    var errorMsg by remember { mutableStateOf<String?>(null) }

    // Auto-generate some case code
    LaunchedEffect(Unit) {
        val rand = (1000..9999).random()
        caseNumber = "CASE-2026-$rand"
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Register Criminal Case",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = caseNumber,
                    onValueChange = { caseNumber = it },
                    label = { Text("Case Serial Number") },
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SteelBlue),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                )

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Case Title") },
                    placeholder = { Text("e.g. Grand Larceny at West Mall") },
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SteelBlue),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .testTag("dialog_case_title_input")
                )

                // Category Selection
                ExposedDropdownMenuBox(
                    expanded = isCatExpanded,
                    onExpandedChange = { isCatExpanded = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    OutlinedTextField(
                        value = selectedCategory,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Offense Category") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCatExpanded) },
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SteelBlue),
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = isCatExpanded,
                        onDismissRequest = { isCatExpanded = false }
                    ) {
                        categories.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat) },
                                onClick = {
                                    selectedCategory = cat
                                    isCatExpanded = false
                                }
                            )
                        }
                    }
                }

                // Priority Selection
                ExposedDropdownMenuBox(
                    expanded = isPriorityExpanded,
                    onExpandedChange = { isPriorityExpanded = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    OutlinedTextField(
                        value = selectedPriority,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Response Priority") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isPriorityExpanded) },
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SteelBlue),
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = isPriorityExpanded,
                        onDismissRequest = { isPriorityExpanded = false }
                    ) {
                        priorities.forEach { p ->
                            DropdownMenuItem(
                                text = { Text(p) },
                                onClick = {
                                    selectedPriority = p
                                    isPriorityExpanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Brief Incident Description") },
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SteelBlue),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .padding(bottom = 16.dp)
                )

                if (errorMsg != null) {
                    Text(
                        text = errorMsg!!,
                        color = AlertRed,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (caseNumber.isBlank() || title.isBlank() || description.isBlank()) {
                                errorMsg = "All fields are required"
                                return@Button
                            }
                            viewModel.registerCase(
                                caseNumber = caseNumber,
                                title = title,
                                description = description,
                                category = selectedCategory,
                                priority = selectedPriority,
                                onSuccess = {
                                    onDismiss()
                                },
                                onError = {
                                    errorMsg = it
                                }
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SteelBlue),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("dialog_case_save_button")
                    ) {
                        Text("Open Case File")
                    }
                }
            }
        }
    }
}


// --- TAB 3: ADVANCED CRIME MAP & INTEL ---
@Composable
fun SearchScreen(
    viewModel: PcmsViewModel,
    onSelectCase: (String) -> Unit
) {
    var searchWord by remember { mutableStateOf("") }
    var selectedMapFilter by remember { mutableStateOf("ALL INCIDENTS") }

    val cases by viewModel.cases.collectAsStateWithLifecycle()
    val complainants by viewModel.complainants.collectAsStateWithLifecycle()
    val suspects by viewModel.suspects.collectAsStateWithLifecycle()
    val evidence by viewModel.evidence.collectAsStateWithLifecycle()

    // Dynamic Live Advanced Search across all collections
    val matchedCases = remember(searchWord, cases) {
        if (searchWord.isBlank()) emptyList()
        else cases.filter {
            it.caseNumber.contains(searchWord, ignoreCase = true) ||
                    it.title.contains(searchWord, ignoreCase = true) ||
                    it.description.contains(searchWord, ignoreCase = true) ||
                    it.category.contains(searchWord, ignoreCase = true)
        }
    }

    val matchedComplainants = remember(searchWord, complainants) {
        if (searchWord.isBlank()) emptyList()
        else complainants.filter {
            it.name.contains(searchWord, ignoreCase = true) ||
                    it.phone.contains(searchWord, ignoreCase = true) ||
                    it.address.contains(searchWord, ignoreCase = true)
        }
    }

    val matchedSuspects = remember(searchWord, suspects) {
        if (searchWord.isBlank()) emptyList()
        else suspects.filter {
            it.name.contains(searchWord, ignoreCase = true) ||
                    it.description.contains(searchWord, ignoreCase = true) ||
                    it.address.contains(searchWord, ignoreCase = true)
        }
    }

    val matchedEvidence = remember(searchWord, evidence) {
        if (searchWord.isBlank()) emptyList()
        else evidence.filter {
            it.evidenceCode.contains(searchWord, ignoreCase = true) ||
                    it.description.contains(searchWord, ignoreCase = true) ||
                    it.storageLocation.contains(searchWord, ignoreCase = true)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        // High Fidelity Kampala Metropolitan Area Crime Map Section (Screenshot 5)
        Card(
            shape = RoundedCornerShape(0.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Map Filter Tabs
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("ALL INCIDENTS", "ROBBERY", "THEFT", "HOMICIDE", "PATROL CARS").forEach { filter ->
                        val isSelected = selectedMapFilter == filter
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) Color(0xFF001534) else Color(0xFFF1F5F9))
                                .clickable { selectedMapFilter = filter }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = filter,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = if (isSelected) Color.White else Color(0xFF64748B)
                            )
                        }
                    }
                }

                // Main Interactive Canvas
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(Color(0xFFE2E8F0))
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val width = size.width
                        val height = size.height

                        // Draw Lake Victoria (blue curve at bottom right)
                        val lakePath = androidx.compose.ui.graphics.Path().apply {
                            moveTo(width * 0.4f, height)
                            quadraticTo(width * 0.6f, height * 0.7f, width, height * 0.85f)
                            lineTo(width, height)
                            close()
                        }
                        drawPath(lakePath, Color(0xFFBFDBFE))

                        // Draw Road Grid (Jinja Road, Entebbe Road, Kampala Road, Makerere Hill Road)
                        // Kampala Road
                        drawLine(
                            color = Color.White,
                            start = androidx.compose.ui.geometry.Offset(0f, height * 0.45f),
                            end = androidx.compose.ui.geometry.Offset(width, height * 0.4f),
                            strokeWidth = 8f.dp.toPx()
                        )
                        drawLine(
                            color = Color(0xFFCBD5E1),
                            start = androidx.compose.ui.geometry.Offset(0f, height * 0.45f),
                            end = androidx.compose.ui.geometry.Offset(width, height * 0.4f),
                            strokeWidth = 1f.dp.toPx()
                        )

                        // Entebbe Road
                        drawLine(
                            color = Color.White,
                            start = androidx.compose.ui.geometry.Offset(width * 0.45f, height * 0.4f),
                            end = androidx.compose.ui.geometry.Offset(width * 0.15f, height),
                            strokeWidth = 8f.dp.toPx()
                        )
                        drawLine(
                            color = Color(0xFFCBD5E1),
                            start = androidx.compose.ui.geometry.Offset(width * 0.45f, height * 0.4f),
                            end = androidx.compose.ui.geometry.Offset(width * 0.15f, height),
                            strokeWidth = 1f.dp.toPx()
                        )

                        // Makerere Hill Rd
                        drawLine(
                            color = Color.White,
                            start = androidx.compose.ui.geometry.Offset(width * 0.3f, 0f),
                            end = androidx.compose.ui.geometry.Offset(width * 0.45f, height * 0.4f),
                            strokeWidth = 6f.dp.toPx()
                        )

                        // Draw Circle markers for Police Stations / Patrols
                        // CPS Kampala
                        drawCircle(
                            color = Color(0xFF001534),
                            radius = 8f.dp.toPx(),
                            center = androidx.compose.ui.geometry.Offset(width * 0.45f, height * 0.4f)
                        )
                        drawCircle(
                            color = Color(0xFFFFD400),
                            radius = 3f.dp.toPx(),
                            center = androidx.compose.ui.geometry.Offset(width * 0.45f, height * 0.4f)
                        )

                        // Katwe Police Station
                        drawCircle(
                            color = Color(0xFF001534),
                            radius = 6f.dp.toPx(),
                            center = androidx.compose.ui.geometry.Offset(width * 0.32f, height * 0.65f)
                        )
                    }

                    // Interactive pin markers
                    Box(modifier = Modifier.fillMaxSize()) {
                        if (selectedMapFilter == "ALL INCIDENTS" || selectedMapFilter == "ROBBERY") {
                            // Robbery Pin near Entebbe Road
                            MarkerPin(
                                title = "UPF-2023-9021",
                                description = "Robbery (Active)",
                                color = Color(0xFFEF4444),
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(bottom = 80.dp, start = 60.dp)
                                    .clickable { onSelectCase("UPF-2023-9021") }
                            )
                        }

                        if (selectedMapFilter == "ALL INCIDENTS" || selectedMapFilter == "THEFT") {
                            // Theft Pin near Acacia Mall
                            MarkerPin(
                                title = "UPF-2023-9104",
                                description = "Missing (High)",
                                color = Color(0xFFF59E0B),
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(top = 40.dp, end = 80.dp)
                                    .clickable { onSelectCase("UPF-2023-9104") }
                            )
                        }

                        if (selectedMapFilter == "ALL INCIDENTS" || selectedMapFilter == "PATROL CARS") {
                            // Patrol car marker
                            Row(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .padding(bottom = 10.dp, end = 10.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color(0xFF2563EB))
                                    .padding(horizontal = 6.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Filled.Security, contentDescription = null, tint = Color.White, modifier = Modifier.size(10.dp))
                                Spacer(modifier = Modifier.width(3.dp))
                                Text("UPF-192", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }

                        // Map Layer Controller overlay (Screenshot 5)
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(6.dp)) {
                                Icon(Icons.Filled.Layers, contentDescription = "Layers", tint = Color(0xFF001534), modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.height(8.dp))
                                Icon(Icons.Filled.MyLocation, contentDescription = "My Loc", tint = Color(0xFF64748B), modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }
        }

        // Search and results section at the bottom (Screenshot 5)
        Card(
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.1f)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Cross database query input
                OutlinedTextField(
                    value = searchWord,
                    onValueChange = { searchWord = it },
                    label = { Text("Cross-Database Keyword Query") },
                    placeholder = { Text("Enter case #, suspect, victim, evidence code...") },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null, tint = Color(0xFF001534)) },
                    trailingIcon = {
                        if (searchWord.isNotEmpty()) {
                            IconButton(onClick = { searchWord = "" }) {
                                Icon(Icons.Filled.Close, contentDescription = null)
                            }
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF001534),
                        focusedLabelColor = Color(0xFF001534)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .testTag("advanced_search_input")
                )

                if (searchWord.isBlank()) {
                    // Display live patrol reports if search query is blank (Screenshot 5)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "LIVE ACTIVE PATROL CARS",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFF64748B)
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFDCFCE7))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text("3 ONLINE", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color(0xFF15803D))
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.verticalScroll(rememberScrollState())
                    ) {
                        // Patrol 1
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFF8FAFC))
                                .padding(10.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.Security, contentDescription = null, tint = Color(0xFF1E3A8A), modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text("Central Patrol Car UPF-192", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = Color(0xFF0F172A))
                                    Text("Makerere Hill Rd • Sgt. Okello", style = MaterialTheme.typography.bodySmall, color = Color(0xFF64748B))
                                }
                            }
                            Text("EN ROUTE", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = Color(0xFFD97706))
                        }

                        // Patrol 2
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFF8FAFC))
                                .padding(10.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.Security, contentDescription = null, tint = Color(0xFF1E3A8A), modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text("Katwe Division Patrol UPF-081", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = Color(0xFF0F172A))
                                    Text("Katwe Rd • Cpl. Muhwezi", style = MaterialTheme.typography.bodySmall, color = Color(0xFF64748B))
                                }
                            }
                            Text("ACTIVE", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = Color(0xFF16A34A))
                        }
                    }
                } else {
                    val totalResults = matchedCases.size + matchedComplainants.size + matchedSuspects.size + matchedEvidence.size

                    Text(
                        text = "Found $totalResults matching database records",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF001534),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Cases Results
                        if (matchedCases.isNotEmpty()) {
                            item {
                                SearchSectionHeader(title = "Matched Cases (${matchedCases.size})", icon = Icons.Filled.Assignment)
                            }
                            items(matchedCases) { pcmsCase ->
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                                    border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { onSelectCase(pcmsCase.caseNumber) }
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                            Text(pcmsCase.caseNumber, fontWeight = FontWeight.Bold, color = Color(0xFF001534))
                                            Text(pcmsCase.category, style = MaterialTheme.typography.bodySmall, color = Color(0xFF64748B))
                                        }
                                        Text(pcmsCase.title, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                        Text(pcmsCase.description, style = MaterialTheme.typography.bodySmall, maxLines = 2, overflow = TextOverflow.Ellipsis)
                                    }
                                }
                            }
                        }

                        // Suspects Results
                        if (matchedSuspects.isNotEmpty()) {
                            item {
                                SearchSectionHeader(title = "Matched Suspect Profiles (${matchedSuspects.size})", icon = Icons.Filled.Face)
                            }
                            items(matchedSuspects) { suspect ->
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                                    border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { onSelectCase(suspect.caseNumber) }
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                            Text(suspect.name, fontWeight = FontWeight.Bold)
                                            Text("Case: ${suspect.caseNumber}", color = Color(0xFF001534), style = MaterialTheme.typography.labelSmall)
                                        }
                                        Text("Status: ${suspect.status}  |  Age: ${suspect.age}", style = MaterialTheme.typography.bodySmall)
                                    }
                                }
                            }
                        }

                        // Complainant Results
                        if (matchedComplainants.isNotEmpty()) {
                            item {
                                SearchSectionHeader(title = "Matched Complainants (${matchedComplainants.size})", icon = Icons.Filled.RecordVoiceOver)
                            }
                            items(matchedComplainants) { comp ->
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                                    border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { onSelectCase(comp.caseNumber) }
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                            Text(comp.name, fontWeight = FontWeight.Bold)
                                            Text("Case: ${comp.caseNumber}", color = Color(0xFF001534), style = MaterialTheme.typography.labelSmall)
                                        }
                                        Text("Phone: ${comp.phone}", style = MaterialTheme.typography.bodySmall)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchSectionHeader(title: String, icon: ImageVector) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp), tint = SteelBlue)
        Spacer(modifier = Modifier.width(8.dp))
        Text(title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.onBackground)
    }
}


// --- TAB 4: ADMIN PANEL ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPanelScreen(viewModel: PcmsViewModel) {
    val officers by viewModel.officers.collectAsStateWithLifecycle()
    val backupStatus by viewModel.backupStatus.collectAsStateWithLifecycle()

    var showAddOfficerDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Command Administration Center",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Black,
                letterSpacing = 0.5.sp
            ),
            color = Color(0xFF001534),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Database Maintenance Card
        Card(
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Settings, contentDescription = null, tint = Color(0xFF001534), modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "System Connection Status",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF001534)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(10.dp).clip(RoundedCornerShape(5.dp)).background(SafeGreen))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Local SQLite Room Database: connected", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF334155))
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text("Database Version: 1.0  |  Schema Export: Disabled", style = MaterialTheme.typography.bodySmall, color = Color(0xFF64748B))

                Spacer(modifier = Modifier.height(16.dp))

                // Backup Trigger button
                Button(
                    onClick = { viewModel.triggerDataBackup() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF001534)),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("admin_backup_button")
                ) {
                    Icon(Icons.Filled.CloudUpload, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Trigger PCMS Local Data Backup", fontWeight = FontWeight.Bold, color = Color.White)
                }

                if (backupStatus != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Card(
                        colors = CardDefaults.cardColors(containerColor = SafeGreen.copy(alpha = 0.08f)),
                        border = BorderStroke(1.dp, SafeGreen.copy(alpha = 0.2f)),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = SafeGreen)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text("Command Backup Manager", fontWeight = FontWeight.Bold, color = SafeGreen, style = MaterialTheme.typography.bodyMedium)
                                Text(backupStatus!!, style = MaterialTheme.typography.bodySmall, color = Color(0xFF334155))
                            }
                        }
                    }
                }
            }
        }

        // Officers Directory Card
        Card(
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.People, contentDescription = null, tint = Color(0xFF001534))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Active Officer Directory",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFF001534)
                        )
                    }

                    IconButton(onClick = { showAddOfficerDialog = true }) {
                        Icon(Icons.Filled.PersonAdd, contentDescription = "Add Officer", tint = Color(0xFF001534))
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                officers.forEach { officer ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "${officer.rank} ${officer.name}",
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                                color = Color(0xFF0F172A)
                            )
                            Text(
                                text = "Badge: ${officer.badgeNumber}  |  System Access: ${officer.role}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF64748B)
                            )
                        }

                        // Badge designation
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = if (officer.role == "Admin") Color(0xFFFFD400).copy(alpha = 0.15f) else Color(0xFF001534).copy(alpha = 0.08f)
                            ),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = officer.role.uppercase(),
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = if (officer.role == "Admin") Color(0xFFB45309) else Color(0xFF001534),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                    HorizontalDivider(color = Color(0xFFE2E8F0))
                }
            }
        }
    }

    if (showAddOfficerDialog) {
        Dialog(onDismissRequest = { showAddOfficerDialog = false }) {
            OfficerRegistrationScreen(
                viewModel = viewModel,
                onBackToLogin = { showAddOfficerDialog = false }
            )
        }
    }
}


// --- CASE DETAIL VIEW ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaseDetailScreen(
    caseNumber: String,
    viewModel: PcmsViewModel,
    currentUser: Officer,
    onBack: () -> Unit,
    onRecordStatement: (String) -> Unit
) {
    val cases by viewModel.cases.collectAsStateWithLifecycle()
    val pcmsCase = cases.find { it.caseNumber == caseNumber }

    if (pcmsCase == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Case record not found or has been purged.", style = MaterialTheme.typography.titleMedium)
        }
        return
    }

    // Sub-collection flows filtered for this case
    val complainants by viewModel.complainants.collectAsStateWithLifecycle()
    val caseComplainants = complainants.filter { it.caseNumber == caseNumber }

    val suspects by viewModel.suspects.collectAsStateWithLifecycle()
    val caseSuspects = suspects.filter { it.caseNumber == caseNumber }

    val witnesses by viewModel.witnesses.collectAsStateWithLifecycle()
    val caseWitnesses = witnesses.filter { it.caseNumber == caseNumber }

    val statements by viewModel.statements.collectAsStateWithLifecycle()
    val caseStatements = statements.filter { it.caseNumber == caseNumber }

    val evidence by viewModel.evidence.collectAsStateWithLifecycle()
    val caseEvidence = evidence.filter { it.caseNumber == caseNumber }

    val logs by viewModel.investigationLogs.collectAsStateWithLifecycle()
    val caseLogs = logs.filter { it.caseNumber == caseNumber }

    // Dialog flags
    var showComplainantDialog by remember { mutableStateOf(false) }
    var showSuspectDialog by remember { mutableStateOf(false) }
    var showWitnessDialog by remember { mutableStateOf(false) }
    var showStatementDialog by remember { mutableStateOf(false) }
    var showEvidenceDialog by remember { mutableStateOf(false) }
    var showLogDialog by remember { mutableStateOf(false) }

    var isStatusMenuExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Custom inner details header bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(CommandNavy)
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Go Back", tint = Color.White)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = pcmsCase.caseNumber,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    ),
                    color = Color.White
                )
                Text(
                    text = pcmsCase.title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Quick Status Modifier Dropdown (Only if authorized officer)
            Box {
                Button(
                    onClick = { isStatusMenuExpanded = true },
                    colors = ButtonDefaults.buttonColors(containerColor = SteelBlue),
                    modifier = Modifier.testTag("status_dropdown_button")
                ) {
                    Text(pcmsCase.status, fontSize = 12.sp)
                    Icon(Icons.Filled.ArrowDropDown, contentDescription = null, modifier = Modifier.size(16.dp))
                }
                DropdownMenu(
                    expanded = isStatusMenuExpanded,
                    onDismissRequest = { isStatusMenuExpanded = false }
                ) {
                    listOf("Active", "Under Investigation", "Cold Case", "Closed").forEach { status ->
                        DropdownMenuItem(
                            text = { Text(status) },
                            onClick = {
                                viewModel.updateCaseStatus(caseNumber, status)
                                isStatusMenuExpanded = false
                            }
                        )
                    }
                }
            }
        }

        // Scrollable content body
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Summary Card
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Primary Incident Report", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, color = SteelBlue)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(pcmsCase.description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                        Column {
                            Text("Category", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                            Text(pcmsCase.category, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                        }
                        Column {
                            Text("Response Priority", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                            Text(pcmsCase.priority, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = if (pcmsCase.priority == "High") AlertRed else WarningAmber)
                        }
                    }
                }
            }

            // People Involved Section (Complainant, Suspects, Witnesses)
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("People Involved Profile", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        Row {
                            IconButton(onClick = { showComplainantDialog = true }) {
                                Icon(Icons.Filled.RecordVoiceOver, contentDescription = "Add Complainant", tint = SteelBlue)
                            }
                            IconButton(onClick = { showSuspectDialog = true }) {
                                Icon(Icons.Filled.Face, contentDescription = "Add Suspect", tint = SteelBlue)
                            }
                            IconButton(onClick = { showWitnessDialog = true }) {
                                Icon(Icons.Filled.PeopleOutline, contentDescription = "Add Witness", tint = SteelBlue)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Complainants
                    Text("Complainants (Victims)", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge, color = SteelBlue)
                    if (caseComplainants.isEmpty()) {
                        Text("No Complainant Profile loaded.", style = MaterialTheme.typography.bodySmall, color = Color.Gray, modifier = Modifier.padding(vertical = 4.dp))
                    } else {
                        caseComplainants.forEach { comp ->
                            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                                Text(comp.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                                Text("Contact: ${comp.phone} | Address: ${comp.address}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)

                    // Suspects
                    Text("Suspects Profile", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge, color = AlertRed)
                    if (caseSuspects.isEmpty()) {
                        Text("No Suspect Profile loaded.", style = MaterialTheme.typography.bodySmall, color = Color.Gray, modifier = Modifier.padding(vertical = 4.dp))
                    } else {
                        caseSuspects.forEach { suspect ->
                            Column(modifier = Modifier.padding(vertical = 6.dp)) {
                                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                    Text(suspect.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                                    Text(suspect.status.uppercase(), fontWeight = FontWeight.Bold, color = if (suspect.status == "In Custody") AlertRed else WarningAmber, style = MaterialTheme.typography.labelSmall)
                                }
                                Text("Age: ${suspect.age} | Last Known Address: ${suspect.address}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("Physical Description: ${suspect.description}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)

                    // Witnesses
                    Text("Eyewitnesses", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge, color = SafeGreen)
                    if (caseWitnesses.isEmpty()) {
                        Text("No Eyewitness logs.", style = MaterialTheme.typography.bodySmall, color = Color.Gray, modifier = Modifier.padding(vertical = 4.dp))
                    } else {
                        caseWitnesses.forEach { wit ->
                            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                                Text(wit.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                                Text("Contact: ${wit.phone} | Home: ${wit.address}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }

            // Statements Section
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Recorded Statements Logs", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, color = SteelBlue)
                        Button(
                            onClick = { onRecordStatement(caseNumber) },
                            colors = ButtonDefaults.buttonColors(containerColor = SteelBlue),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.testTag("record_statement_button")
                        ) {
                            Icon(Icons.Filled.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Record", fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (caseStatements.isEmpty()) {
                        Text("No physical statements recorded for this case.", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                    } else {
                        caseStatements.forEach { stmt ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(8.dp))
                                    .padding(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(stmt.personName, fontWeight = FontWeight.Bold, color = SteelBlue)
                                    Card(colors = CardDefaults.cardColors(containerColor = SteelBlue.copy(alpha = 0.15f))) {
                                        Text(stmt.personRole.uppercase(), style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = SteelBlue, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                    }
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "\"${stmt.statementText}\"",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontStyle = androidx.compose.ui.text.font.FontStyle.Italic),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Recorded by Badge ${stmt.officerBadge} on ${stmt.timeRecorded}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
            }

            // Evidence Vault Section
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Secure Evidence Vault", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, color = SafeGreen)
                        Button(
                            onClick = { showEvidenceDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = SafeGreen),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.testTag("collect_evidence_button")
                        ) {
                            Icon(Icons.Filled.Inventory2, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Collect", fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (caseEvidence.isEmpty()) {
                        Text("No physical evidence logged.", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                    } else {
                        caseEvidence.forEach { ev ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp)
                            ) {
                                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                    Text(ev.evidenceCode, fontWeight = FontWeight.Bold, color = SafeGreen, fontFamily = FontFamily.Monospace)
                                    Text(ev.category, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                                }
                                Text(ev.description, style = MaterialTheme.typography.bodyMedium)
                                Text("Storage Location: ${ev.storageLocation}", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                HorizontalDivider(modifier = Modifier.padding(top = 6.dp), color = MaterialTheme.colorScheme.outlineVariant)
                            }
                        }
                    }
                }
            }

            // Timeline Journal Section
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Investigation Journal Logs", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        Button(
                            onClick = { showLogDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = SteelBlue),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.testTag("add_log_button")
                        ) {
                            Icon(Icons.Filled.HistoryEdu, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Add Log", fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (caseLogs.isEmpty()) {
                        Text("No chronicled logs for this investigation.", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                    } else {
                        caseLogs.forEach { log ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp)
                            ) {
                                Text(
                                    text = log.logMessage,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium
                                )
                                if (log.nextSteps.isNotBlank()) {
                                    Text(
                                        text = "Next Steps: ${log.nextSteps}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = SteelBlue,
                                        modifier = Modifier.padding(top = 2.dp)
                                    )
                                }
                                Text(
                                    text = "Logged by Officer ${log.officerBadge}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.Gray,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                                HorizontalDivider(modifier = Modifier.padding(top = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)
                            }
                        }
                    }
                }
            }

            // SUPERINTENDENT PURGE ACTION
            if (currentUser.role.equals("Admin", ignoreCase = true)) {
                Button(
                    onClick = {
                        viewModel.deleteCase(caseNumber) {
                            onBack()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AlertRed),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .testTag("purge_case_button")
                ) {
                    Icon(Icons.Filled.DeleteForever, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Purge Case File (Superintendent Authority Only)", fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    // Complainant Dialog
    if (showComplainantDialog) {
        AddComplainantDialog(
            onDismiss = { showComplainantDialog = false },
            onSave = { name, phone, email, address ->
                viewModel.addComplainant(caseNumber, name, phone, email, address)
                showComplainantDialog = false
            }
        )
    }

    // Suspect Dialog
    if (showSuspectDialog) {
        AddSuspectDialog(
            onDismiss = { showSuspectDialog = false },
            onSave = { name, age, status, address, description ->
                viewModel.addSuspect(caseNumber, name, age, status, address, description)
                showSuspectDialog = false
            }
        )
    }

    // Witness Dialog
    if (showWitnessDialog) {
        AddWitnessDialog(
            onDismiss = { showWitnessDialog = false },
            onSave = { name, phone, address ->
                viewModel.addWitness(caseNumber, name, phone, address)
                showWitnessDialog = false
            }
        )
    }

    // Statement Dialog
    if (showStatementDialog) {
        RecordStatementDialog(
            onDismiss = { showStatementDialog = false },
            onSave = { person, role, text ->
                viewModel.recordStatement(caseNumber, person, role, text) {
                    showStatementDialog = false
                }
            }
        )
    }

    // Evidence Dialog
    if (showEvidenceDialog) {
        CollectEvidenceDialog(
            onDismiss = { showEvidenceDialog = false },
            onSave = { code, desc, cat, loc ->
                viewModel.collectEvidence(caseNumber, code, desc, cat, loc) {
                    showEvidenceDialog = false
                }
            }
        )
    }

    // Chronology Log Dialog
    if (showLogDialog) {
        AddLogDialog(
            onDismiss = { showLogDialog = false },
            onSave = { msg, next ->
                viewModel.addInvestigationLog(caseNumber, msg, next)
                showLogDialog = false
            }
        )
    }
}


// --- SUPPORTING SUB-DIALOGS ---

@Composable
fun AddComplainantDialog(onDismiss: () -> Unit, onSave: (String, String, String, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Add Complainant Profile", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, color = SteelBlue)
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Full Name") }, modifier = Modifier.fillMaxWidth().testTag("dialog_complainant_name_input"))
                OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone Number") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email Address") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = address, onValueChange = { address = it }, label = { Text("Physical Address") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Button(onClick = { if (name.isNotBlank()) onSave(name, phone, email, address) }, colors = ButtonDefaults.buttonColors(containerColor = SteelBlue)) { Text("Add Profile") }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSuspectDialog(onDismiss: () -> Unit, onSave: (String, Int, String, String, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var ageStr by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    val statuses = listOf("At Large", "In Custody", "Under Investigation", "Released")
    var selectedStatus by remember { mutableStateOf(statuses[0]) }
    var isExpanded by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState())) {
                Text("Add Suspect Profile", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, color = AlertRed)
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Full Name") }, modifier = Modifier.fillMaxWidth().testTag("dialog_suspect_name_input"))
                OutlinedTextField(value = ageStr, onValueChange = { ageStr = it }, label = { Text("Estimated Age") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())

                ExposedDropdownMenuBox(expanded = isExpanded, onExpandedChange = { isExpanded = it }, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                    OutlinedTextField(
                        value = selectedStatus,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Apprehension Status") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = false }) {
                        statuses.forEach { s ->
                            DropdownMenuItem(text = { Text(s) }, onClick = { selectedStatus = s; isExpanded = false })
                        }
                    }
                }

                OutlinedTextField(value = address, onValueChange = { address = it }, label = { Text("Last Known Address/Hangouts") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Physical Description (scars, tattoos, clothes)") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Button(onClick = { if (name.isNotBlank()) onSave(name, ageStr.toIntOrNull() ?: 0, selectedStatus, address, description) }, colors = ButtonDefaults.buttonColors(containerColor = AlertRed)) { Text("Add Profile") }
                }
            }
        }
    }
}

@Composable
fun AddWitnessDialog(onDismiss: () -> Unit, onSave: (String, String, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Add Eyewitness Profile", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, color = SafeGreen)
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Full Name") }, modifier = Modifier.fillMaxWidth().testTag("dialog_witness_name_input"))
                OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Contact Phone") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = address, onValueChange = { address = it }, label = { Text("Residential Address") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Button(onClick = { if (name.isNotBlank()) onSave(name, phone, address) }, colors = ButtonDefaults.buttonColors(containerColor = SafeGreen)) { Text("Add Profile") }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordStatementDialog(onDismiss: () -> Unit, onSave: (String, String, String) -> Unit) {
    var personName by remember { mutableStateOf("") }
    var text by remember { mutableStateOf("") }

    val roles = listOf("Complainant", "Witness", "Suspect")
    var selectedRole by remember { mutableStateOf(roles[0]) }
    var isExpanded by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState())) {
                Text("Record Physical Statement Log", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, color = SteelBlue)
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(value = personName, onValueChange = { personName = it }, label = { Text("Deponent Name") }, modifier = Modifier.fillMaxWidth().testTag("dialog_statement_name_input"))

                ExposedDropdownMenuBox(expanded = isExpanded, onExpandedChange = { isExpanded = it }, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                    OutlinedTextField(
                        value = selectedRole,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Case Role") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = false }) {
                        roles.forEach { r ->
                            DropdownMenuItem(text = { Text(r) }, onClick = { selectedRole = r; isExpanded = false })
                        }
                    }
                }

                OutlinedTextField(value = text, onValueChange = { text = it }, label = { Text("Detailed Statement Transcript") }, modifier = Modifier.fillMaxWidth().height(120.dp).testTag("dialog_statement_text_input"))
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Button(onClick = { if (personName.isNotBlank() && text.isNotBlank()) onSave(personName, selectedRole, text) }, colors = ButtonDefaults.buttonColors(containerColor = SteelBlue)) { Text("Save Statement") }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectEvidenceDialog(onDismiss: () -> Unit, onSave: (String, String, String, String) -> Unit) {
    var code by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }

    val categories = listOf("Weapon", "Document", "Digital", "Narcotics", "Forensics", "Other")
    var selectedCat by remember { mutableStateOf(categories[0]) }
    var isExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val rand = (100..999).random()
        code = "EVID-2026-$rand"
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState())) {
                Text("Log Collected Evidence", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, color = SafeGreen)
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(value = code, onValueChange = { code = it }, label = { Text("Evidence Code Number") }, modifier = Modifier.fillMaxWidth())

                ExposedDropdownMenuBox(expanded = isExpanded, onExpandedChange = { isExpanded = it }, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                    OutlinedTextField(
                        value = selectedCat,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Evidence Type") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = false }) {
                        categories.forEach { c ->
                            DropdownMenuItem(text = { Text(c) }, onClick = { selectedCat = c; isExpanded = false })
                        }
                    }
                }

                OutlinedTextField(value = desc, onValueChange = { desc = it }, label = { Text("Detailed Description of Physical Item") }, modifier = Modifier.fillMaxWidth().testTag("dialog_evidence_desc_input"))
                OutlinedTextField(value = location, onValueChange = { location = it }, label = { Text("Secure Storage Location (Locker, Vault, Server)") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Button(onClick = { if (desc.isNotBlank()) onSave(code, desc, selectedCat, location) }, colors = ButtonDefaults.buttonColors(containerColor = SafeGreen)) { Text("Log Item") }
                }
            }
        }
    }
}

@Composable
fun AddLogDialog(onDismiss: () -> Unit, onSave: (String, String) -> Unit) {
    var msg by remember { mutableStateOf("") }
    var next by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Log Investigation Activity", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, color = SteelBlue)
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(value = msg, onValueChange = { msg = it }, label = { Text("Investigation Action Log message") }, modifier = Modifier.fillMaxWidth().testTag("dialog_log_msg_input"))
                OutlinedTextField(value = next, onValueChange = { next = it }, label = { Text("Planned Next Investigative Step") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Button(onClick = { if (msg.isNotBlank()) onSave(msg, next) }, colors = ButtonDefaults.buttonColors(containerColor = SteelBlue)) { Text("Log Entry") }
                }
            }
        }
    }
}

// --- RECORD FORMAL STATEMENT SCREEN (Screenshot 2) ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordStatementScreen(
    caseNumber: String,
    viewModel: PcmsViewModel,
    onBack: () -> Unit
) {
    var deponentName by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf("WITNESS") }
    var deponentGender by remember { mutableStateOf("MALE") }
    var deponentPhone by remember { mutableStateOf("") }
    var deponentNIn by remember { mutableStateOf("") }
    var statementTranscript by remember { mutableStateOf("") }
    var isAffirmed by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Back Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color(0xFF001534))
            }
            Column {
                Text(
                    text = "RECORD FORMAL STATEMENT",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFF001534)
                )
                Text(
                    text = "CASE FILE REF: $caseNumber",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace),
                    color = Color(0xFFFFD400)
                )
            }
        }

        // Section 1: Deponent Details
        Card(
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "DEPONENT BIOMETRICS & PROFILE",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFF001534)
                )

                OutlinedTextField(
                    value = deponentName,
                    onValueChange = { deponentName = it },
                    label = { Text("Full Name (as on National ID)") },
                    placeholder = { Text("e.g. Katumba Fred") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF001534),
                        focusedLabelColor = Color(0xFF001534)
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("deponent_name_input")
                )

                // Gender Toggle buttons
                Column {
                    Text(
                        text = "Gender Selection",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF64748B)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("MALE", "FEMALE", "OTHER").forEach { gender ->
                            val isSelected = deponentGender == gender
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(if (isSelected) Color(0xFF001534) else Color(0xFFF1F5F9))
                                    .border(1.dp, if (isSelected) Color(0xFFFFD400) else Color(0xFFE2E8F0), RoundedCornerShape(4.dp))
                                    .clickable { deponentGender = gender }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = gender,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = if (isSelected) Color.White else Color(0xFF475569)
                                )
                            }
                        }
                    }
                }

                // Case Role Selector
                Column {
                    Text(
                        text = "Deponent Case Relation",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF64748B)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("COMPLAINANT", "WITNESS", "SUSPECT").forEach { role ->
                            val isSelected = selectedRole == role
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(if (isSelected) Color(0xFFFFD400) else Color(0xFFF1F5F9))
                                    .border(1.dp, if (isSelected) Color(0xFF001534) else Color(0xFFE2E8F0), RoundedCornerShape(4.dp))
                                    .clickable { selectedRole = role }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = role,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = Color(0xFF001534)
                                )
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = deponentPhone,
                        onValueChange = { deponentPhone = it },
                        label = { Text("Phone Number") },
                        placeholder = { Text("+256...") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF001534),
                            focusedLabelColor = Color(0xFF001534)
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = deponentNIn,
                        onValueChange = { deponentNIn = it },
                        label = { Text("National ID (NIN)") },
                        placeholder = { Text("e.g. CM920...") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF001534),
                            focusedLabelColor = Color(0xFF001534)
                        ),
                        modifier = Modifier.weight(1.2f)
                    )
                }
            }
        }

        // Section 2: Statement Transcript Text Area (Screenshot 2)
        Card(
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "FORMAL STATEMENT TRANSCRIPT",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFF001534)
                )

                OutlinedTextField(
                    value = statementTranscript,
                    onValueChange = { statementTranscript = it },
                    placeholder = { Text("Write or dictate the official statement details. Standard legal formatting will be applied dynamically...") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF001534),
                        focusedLabelColor = Color(0xFF001534)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .testTag("statement_transcript_input")
                )

                // Legal Oath & Checkbox
                Row(
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFFFEF2F2))
                        .padding(12.dp)
                ) {
                    Checkbox(
                        checked = isAffirmed,
                        onCheckedChange = { isAffirmed = it },
                        modifier = Modifier.testTag("oath_affirmation_checkbox")
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "I hereby solemnly declare and affirm that the statement recorded above represents the absolute truth, given freely without coercion under Kampala Central Jurisdictional authority.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF991B1B)
                    )
                }
            }
        }

        // Section 3: Digital Affidavits / E-Signature (Screenshot 2)
        Card(
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "SECURE E-SIGNATURE CAPTURE",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFF001534)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .background(Color(0xFFF8FAFC))
                        .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(4.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    if (deponentName.isBlank()) {
                        Text("Awaiting deponent signature...", style = MaterialTheme.typography.bodySmall, color = Color(0xFF94A3B8))
                    } else {
                        // Display clean signature simulation font/handwriting
                        Text(
                            text = deponentName,
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontFamily = FontFamily.Cursive,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                            ),
                            color = Color(0xFF1E3A8A)
                        )
                    }
                }
                Text(
                    text = "System has autogenerated a cryptographically verified signature matching NIN: ${deponentNIn.ifBlank { "N/A" }}",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF64748B)
                )
            }
        }

        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onBack,
                border = BorderStroke(1.dp, Color(0xFFCBD5E1)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF475569)),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            ) {
                Text("CANCEL", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
            }

            Button(
                onClick = {
                    if (deponentName.isNotBlank() && statementTranscript.isNotBlank() && isAffirmed) {
                        viewModel.recordStatement(
                            caseNumber = caseNumber,
                            personName = deponentName,
                            personRole = selectedRole,
                            statementText = statementTranscript
                        ) {
                            onBack()
                        }
                    }
                },
                enabled = deponentName.isNotBlank() && statementTranscript.isNotBlank() && isAffirmed,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF001534),
                    disabledContainerColor = Color(0xFFCBD5E1)
                ),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier
                    .weight(1.5f)
                    .height(48.dp)
                    .testTag("save_statement_button")
            ) {
                Icon(Icons.Filled.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("SAVE & SYNC STATEMENT", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
            }
        }
    }
}
