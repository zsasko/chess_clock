package com.zsasko.chessclock.di

import com.zsasko.chessclock.controllers.ChessController
import com.zsasko.chessclock.controllers.ChessControllerImpl
import com.zsasko.chessclock.viewmodel.ChessViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    @Named("Dispatchers_DEFAULT")
    fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default


    @Provides
    @Singleton
    fun provideChessController(@Named("Dispatchers_DEFAULT") coroutineDispatcher: CoroutineDispatcher): ChessController =
        ChessControllerImpl(coroutineDispatcher)


    @Provides
    fun provideChessViewModel(chessController: ChessController): ChessViewModel =
        ChessViewModel(chessController)
}