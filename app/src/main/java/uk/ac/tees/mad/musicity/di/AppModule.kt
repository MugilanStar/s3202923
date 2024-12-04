package uk.ac.tees.mad.musicity.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uk.ac.tees.mad.musicity.data.api.MusicService
import uk.ac.tees.mad.musicity.data.database.AppDatabase
import uk.ac.tees.mad.musicity.data.database.FavoriteTrackDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private const val BASE_URL = "https://deezerdevs-deezer.p.rapidapi.com/"

    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder().build()

    @Provides
    @Singleton
    fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder().client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun providesMusicApi(retrofit: Retrofit): MusicService =
        retrofit.create(MusicService::class.java)

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room
            .databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "musicity_database"
            )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideFavoriteTrackDao(appDatabase: AppDatabase): FavoriteTrackDao {
        return appDatabase.favoriteTrackDao()
    }
}