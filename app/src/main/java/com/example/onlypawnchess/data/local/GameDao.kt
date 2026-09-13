package com.example.onlypawnchess.data.local


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {

    @Insert
    suspend fun insert(game: GameEntity)

    @Update
    suspend fun update(game: GameEntity)

    @Query("SELECT * FROM games WHERE gameId = :gameId")
    suspend fun getGame(gameId: String): GameEntity?

    @Query("DELETE FROM games WHERE gameId = :gameId")
    suspend fun deleteGame(gameId: String)

    @Query("SELECT * FROM games ORDER BY updatedAt DESC")
    fun observeGames(): Flow<List<GameEntity>>
}
