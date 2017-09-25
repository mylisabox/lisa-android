package com.mylisabox.network.dashboard

import com.mylisabox.network.dashboard.models.Dashboard
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface DashboardApi {
    @GET("dashboard/room/{id}")
    fun getDashboardForRoom(@Path("id") id: Long): Single<Dashboard>

    @POST("dashboard/room/{id}")
    fun saveDashboardForRoom(@Path("id") id: Long, @Body widgets: List<Long>): Completable
}