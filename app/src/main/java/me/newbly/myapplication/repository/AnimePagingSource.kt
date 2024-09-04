package me.newbly.myapplication.repository

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import me.newbly.myapplication.api.JikanService
import me.newbly.myapplication.model.AnimeData
import okio.IOException
import retrofit2.HttpException

class AnimePagingSource(
    private val service: JikanService,
    private val query: String,
) : PagingSource<Int, AnimeData>() {
    override fun getRefreshKey(state: PagingState<Int, AnimeData>): Int? {
        Log.d("AnimePagingSource", "Getting refresh key for ${state.anchorPosition}")
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AnimeData> {
        val position = params.key ?: STARTING_PAGE_INDEX

        return try {
            val response = service.getAnimeList(position, query)

            val data = response.body()!!.data
            val prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1
            val nextKey =
                if (!response.body()!!.pagination.hasNextPage) null else position + (params.loadSize / JikanRepository.ANIME_PAGE_SIZE)

            Log.d("AnimePagingSource", "Loaded page $position to $nextKey")

            LoadResult.Page(data, prevKey, nextKey)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: NullPointerException) {
            LoadResult.Error(e)
        }
    }

    companion object {
        private const val STARTING_PAGE_INDEX = 1
    }
}