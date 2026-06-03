package com.example.footballclubinfo

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity representing a Football League table.
 * This will store the hardcoded initial leagues locally.
 */
@Entity(tableName = "leagues")
data class LeagueEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val leagueId: String,   // e.g., "4328"
    val leagueName: String, // e.g., "English Premier League"
    val country: String     // e.g., "England"
)

/**
 * Entity representing a Football Club table.
 * Captures all 15 details provided exactly by the API specification layout.
 */
@Entity(tableName = "clubs")
data class ClubEntity(
    @PrimaryKey val idTeam: String, // Unique Team ID from API acts as the Primary Key
    val strTeam: String,            // Name of Club
    val strTeamShort: String?,
    val strAlternate: String?,
    val intFormedYear: String?,
    val strLeague: String,          // Name of League
    val idLeague: String?,
    val strStadium: String?,
    val strKeywords: String?,
    val strStadiumThumb: String?,
    val strStadiumLocation: String?,
    val intStadiumCapacity: String?,
    val strWebsite: String?,
    val strTeamJersey: String?,     // URL to Jersey Image
    val strTeamLogo: String?        // URL to Logo Image
)