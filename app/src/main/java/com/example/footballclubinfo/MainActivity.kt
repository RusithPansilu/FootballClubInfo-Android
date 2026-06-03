// VIDEO DEMONSTRATION LINK: REPLACE_THIS_WITH_YOUR_ACTUAL_DRIVE_OR_YOUTUBE_URL
// I confirm that I understand what plagiarism is and have read and understood the section on Assessment Offences. The work that I have submitted is entirely my own.

package com.example.footballclubinfo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider

class MainActivity : ComponentActivity() {

    private lateinit var footballViewModel: FootballViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Instantiate our architecture controller ensuring states are safe during phone orientation pivots
        footballViewModel = ViewModelProvider(this)[FootballViewModel::class.java]

        setContent {
            // Render the root view controller wrapping all 4 screens smoothly
            AppNavigationContainer(viewModel = footballViewModel)
        }
    }
}