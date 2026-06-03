package com.example.footballclubinfo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Global Controller Screen that switches layout structures depending on ViewModel states.
 * Preserves screen locations accurately across device rotations.
 */
@Composable
fun AppNavigationContainer(viewModel: FootballViewModel) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        when (viewModel.currentScreen.value) {
            "MainMenu" -> MainMenuScreen(viewModel)
            "LeagueSearchScreen" -> LeagueSearchScreen(viewModel)
            "LocalSearchScreen" -> LocalSearchScreen(viewModel)
            "JerseyLookupScreen" -> JerseyLookupScreen(viewModel)
        }
    }
}

// ============================================================================
// 1. MAIN MENU SCREEN (Requirement 1 & Requirement 8 Extension Button)
// ============================================================================
@Composable
fun MainMenuScreen(viewModel: FootballViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Football Information Hub",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Button 1: Requirement 2
        Button(
            onClick = { viewModel.addLeaguesToDatabase() },
            modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)
        ) {
            Text("Add Leagues to DB")
        }

        // Button 2: Requirement 3
        Button(
            onClick = { viewModel.currentScreen.value = "LeagueSearchScreen" },
            modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)
        ) {
            Text("Search for Clubs By League")
        }

        // Button 3: Requirement 5
        Button(
            onClick = { viewModel.currentScreen.value = "LocalSearchScreen" },
            modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)
        ) {
            Text("Search for Clubs")
        }

        // Button 4: Requirement 8 Extension Task
        Button(
            onClick = { viewModel.currentScreen.value = "JerseyLookupScreen" },
            modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
        ) {
            Text("Search Jerseys")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Dynamic feedback display notification text
        if (viewModel.leagueStatusMessage.value.isNotEmpty()) {
            Text(
                text = viewModel.leagueStatusMessage.value,
                fontSize = 14.sp,
                color = Color.DarkGray,
                modifier = Modifier.padding(top = 12.dp)
            )
        }
    }
}

// ============================================================================
// 2. SEARCH CLUBS BY LEAGUE SCREEN (Requirement 3 & 4)
// ============================================================================
@Composable
fun LeagueSearchScreen(viewModel: FootballViewModel) {
    val clubs by viewModel.fetchedClubs.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = { viewModel.currentScreen.value = "MainMenu" }) {
                Text("< Back")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text("Search By League", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = viewModel.leagueSearchInput.value,
            onValueChange = { viewModel.leagueSearchInput.value = it },
            label = { Text("Enter League Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(
                onClick = { viewModel.retrieveClubsByLeague() },
                modifier = Modifier.weight(1f).padding(end = 4.dp)
            ) {
                Text("Retrieve Clubs")
            }
            Button(
                onClick = { viewModel.saveFetchedClubsToDatabase() },
                modifier = Modifier.weight(1f).padding(start = 4.dp)
            ) {
                Text("Save to DB")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(text = viewModel.leagueStatusMessage.value, fontSize = 12.sp, color = Color.Blue)
        Spacer(modifier = Modifier.height(8.dp))

        if (viewModel.isLeagueSearching.value) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(clubs) { club ->
                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Row(
                            modifier = Modifier.padding(12.dp).fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Requirement 6: Render club logo thumbnail in online search results
                            AsyncImage(
                                url = club.strTeamLogo,
                                modifier = Modifier.size(80.dp).padding(end = 16.dp)
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = club.strTeam, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                Text(text = "Formed: ${club.intFormedYear ?: "N/A"}", fontSize = 14.sp)
                                Text(text = "Stadium: ${club.strStadium ?: "N/A"}", fontSize = 12.sp, color = Color.Gray)
                                Text(text = "ID: ${club.idTeam}", fontSize = 10.sp, color = Color.LightGray)
                            }
                        }
                    }
                }
            }
        }
    }
}

// ============================================================================
// 3. LOCAL DATABASE SEARCH SCREEN WITH LOGOS (Requirement 5 & Requirement 6 Extension)
// ============================================================================
@Composable
fun LocalSearchScreen(viewModel: FootballViewModel) {
    val results by viewModel.localSearchResults.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = { viewModel.currentScreen.value = "MainMenu" }) {
                Text("< Back")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text("Database Search", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = viewModel.localSearchInput.value,
                onValueChange = { viewModel.localSearchInput.value = it },
                label = { Text("Club or League Name") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { viewModel.searchClubsLocally() }) {
                Text("Search")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (viewModel.isLocalSearching.value) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(results) { club ->
                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Row(
                            modifier = Modifier.padding(12.dp).fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                url = club.strTeamLogo,
                                modifier = Modifier.size(60.dp).padding(end = 12.dp)
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = club.strTeam, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                Text(text = "League: ${club.strLeague}", fontSize = 14.sp, color = Color.Gray)
                                Text(text = "Stadium: ${club.strStadium ?: "N/A"}", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

// ============================================================================
// 4. DIRECT JERSEY LOOKUP SCREEN (Requirement 8 Extension Multi-Query Task)
// ============================================================================
@Composable
fun JerseyLookupScreen(viewModel: FootballViewModel) {
    val jerseyUrls by viewModel.jerseyUrlsList.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = { viewModel.currentScreen.value = "MainMenu" }) {
                Text("< Back")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text("Direct Jersey Lookup", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = viewModel.jerseySearchInput.value,
                onValueChange = { viewModel.jerseySearchInput.value = it },
                label = { Text("Club Substring (e.g. NAI)") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { viewModel.lookupJerseysDirectly() }) {
                Text("Fetch")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(text = viewModel.jerseyStatusMessage.value, fontSize = 12.sp, color = Color.DarkGray)
        Spacer(modifier = Modifier.height(16.dp))

        if (viewModel.isJerseySearching.value) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(4.dp)
            ) {
                items(jerseyUrls) { url ->
                    Card(
                        modifier = Modifier.padding(4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        AsyncImage(
                            url = url,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}