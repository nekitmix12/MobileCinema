package com.example.mobilecinema.data

import com.example.mobilecinema.data.datasource.remote.RemoteDataSource
import javax.inject.Inject

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepositoryImpl
    @Inject constructor(val dataSource: RemoteDataSource):RemoteDataSource {


}