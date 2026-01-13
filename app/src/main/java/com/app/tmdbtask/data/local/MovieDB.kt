package com.app.tmdbtask.data.local

import androidx.paging.PagingSource
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val backdropPath: String?,
    val releaseDate: String?,
    val voteAverage: Double,
    val isBookmarked: Boolean,
    val category: String, // TRENDING / NOW_PLAYING / SEARCH
    val page: Int
)

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey val id: Long,
    val addedAt: Long
)
@Dao
interface MovieDao {

    @Query("SELECT * FROM movies WHERE category = :category ORDER BY page, id")
    fun moviesByCategory(category: String): PagingSource<Int, MovieEntity>

    @Query("SELECT * FROM movies WHERE isBookmarked = 1 ORDER BY title")
    fun bookmarkedMovies(): Flow<List<MovieEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<MovieEntity>)

    @Query("UPDATE movies SET isBookmarked = CASE WHEN isBookmarked = 1 THEN 0 ELSE 1 END WHERE id = :id")
    suspend fun toggleBookmark(id: Long)

    @Query("DELETE FROM movies WHERE category = :category")
    suspend fun clearCategory(category: String)

    @Query("SELECT * FROM movies WHERE id = :id LIMIT 1")
    suspend fun getMovieById(id: Long): MovieEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(movie: MovieEntity)

    @Query("""
        SELECT * FROM movies 
        WHERE title LIKE '%' || :query || '%' 
        ORDER BY voteAverage DESC, title
    """)
    fun searchLocal(query: String): Flow<List<MovieEntity>>

    // Optional: scoped search cache category
    @Query("SELECT * FROM movies WHERE category = :category ORDER BY page, id")
    fun searchCache(category: String = "search"): Flow<List<MovieEntity>>

}

@Dao
interface BookmarkDao {
    @Query("SELECT * FROM bookmarks ORDER BY addedAt DESC")
    fun getAll(): kotlinx.coroutines.flow.Flow<List<BookmarkEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(bookmark: BookmarkEntity)

    @Query("DELETE FROM bookmarks WHERE id = :id")
    suspend fun remove(id: Long)

    @Query("SELECT EXISTS(SELECT 1 FROM bookmarks WHERE id = :id)")
    fun isBookmarked(id: Long): kotlinx.coroutines.flow.Flow<Boolean>
}

