package com.example.footballclubinfo

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AppDao {

    // --- League Operations (Requirement 2) ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLeagues(leagues: List<LeagueEntity>)

    @Query("SELECT * FROM leagues")
    suspend fun getAllLeagues(): List<LeagueEntity>


    // --- Club Operations (Requirement 4 & 5) ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClubs(clubs: List<ClubEntity>)

    /**
     * Requirement 5: Case-insensitive, partial match search.
     * Searches across BOTH the Name of Club or League fields.
     * SQLite's 'LIKE' operator handles case-insensitive ASCII comparison automatically.
     */
    @Query("SELECT * FROM clubs WHERE strTeam LIKE :searchQuery OR strLeague LIKE :searchQuery")
    suspend fun searchClubsByNameOrLeague(searchQuery: String): List<ClubEntity>
}