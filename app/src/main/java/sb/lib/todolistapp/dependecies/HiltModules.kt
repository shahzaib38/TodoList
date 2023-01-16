package sb.lib.todolistapp.dependecies

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import sb.lib.todolistapp.database.authentication.GoogleAuthenticator
import sb.lib.todolistapp.database.authentication.WebAuthenticator
import sb.lib.todolistapp.database.network.RemoteService
import sb.lib.todolistapp.database.network.TodoRemoteService
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object HiltModules {



    @Singleton
    @Provides
    fun provideTodoRemoteService(@ApplicationContext context : Context):RemoteService{
        return TodoRemoteService.getInstance()
    }


    @Singleton
    @Provides
    fun provideGoogleSignIn(@ApplicationContext context : Context):WebAuthenticator{
        return GoogleAuthenticator.getInstance(context)
    }


}