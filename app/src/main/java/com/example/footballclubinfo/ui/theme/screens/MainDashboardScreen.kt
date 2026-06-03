package com.example.footballclubinfo.ui.theme.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MainDashboardScreen() {
    // Column arranges UI elements vertically, one below the other
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center, // Centers everything vertically on the screen
        horizontalAlignment = Alignment.CenterHorizontally, // Centers everything horizontally
    ) {
        Text(
            text = "Football Info Dashboard",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 32.dp),
        )

        // Button 1: Add Leagues to DB
        Button(
            onClick = { /* We will program this database action next */ },
            modifier = Modifier.fillMaxWidth().height(55.dp)
        ) {
            Text(text = "Add Leagues to DB", fontSize = 16.sp) //
        }

        Spacer(modifier = Modifier.height(16.dp)) // Adds clean spacing between buttons

        // Button 2: Search for Clubs By League
        Button(
            onClick = { /* We will program this screen shift later */ },
            modifier = Modifier.fillMaxWidth().height(55.dp)
        ) {
            Text(text = "Search for Clubs By League", fontSize = 16.sp) //
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Button 3: Search for Clubs
        Button(
            onClick = { /* We will program this offline search later */ },
            modifier = Modifier.fillMaxWidth().height(55.dp)
        ) {
            Text(text = "Search for Clubs", fontSize = 16.sp) //
        }
    }
}