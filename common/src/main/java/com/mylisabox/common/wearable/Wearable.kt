package com.mylisabox.common.wearable

import android.content.Context
import android.net.Uri
import android.os.Bundle
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.wearable.*
import com.google.android.gms.wearable.PutDataRequest
import com.google.android.gms.wearable.Wearable
import com.google.gson.Gson
import com.mylisabox.common.CommonApplication.Companion.KEY_DATA
import com.mylisabox.network.dagger.annotations.Qualifiers.ForApplication
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class Wearable @Inject constructor(@ForApplication context: Context, private val gson: Gson) : GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, DataApi.DataListener {

    companion object {
        val TIMEOUT = 500L
    }

    private val googleClient: GoogleApiClient = GoogleApiClient.Builder(context)
            .addApi(Wearable.API)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .build()

    var onWerableData: OnWearableDataListener<Any>? = null

    fun start() {
        googleClient.connect()
    }

    fun stop() {
        Wearable.DataApi.removeListener(googleClient, this)
        googleClient.disconnect()
    }

    fun sendData(uri: String, data: String): Completable {
        val dataMap = DataMap()
        dataMap.putString(KEY_DATA, data)
        return sendData(uri, dataMap)
    }

    fun sendData(uri: String, data: DataMap): Completable {
        return connect()
                .andThen(Completable.create({
                    val putDataMapRequest = PutDataMapRequest.create(uri)
                    putDataMapRequest.dataMap.putAll(data)
                    val dataRequest = putDataMapRequest.asPutDataRequest()
                    dataRequest.setUrgent()
                    val pendingResult: PendingResult<DataApi.DataItemResult> = Wearable.DataApi.putDataItem(googleClient, dataRequest)
                    val completableEmitter = it
                    pendingResult.setResultCallback {
                        if (it.status.isSuccess) {
                            completableEmitter.onComplete()
                        } else {
                            Completable.error(RuntimeException("Can't send the message: ${it.status.statusMessage}"))
                        }
                    }
                })).subscribeOn(Schedulers.io())
    }

    fun <T> retrieveData(resultType: Class<T>, path: String): Single<T> {
        return connect().andThen(Single.create({
            val nodesResult = Wearable.NodeApi.getConnectedNodes(googleClient).await()
            val node: Node? = nodesResult.nodes.lastOrNull()

            if (node == null) {
                it.onError(NoSuchElementException())
            } else {
                val uri = Uri.Builder()
                        .scheme(PutDataRequest.WEAR_URI_SCHEME)
                        .path(path)
                        .authority(node.id)
                        .build()

                val dataItemResult = Wearable.DataApi.getDataItem(googleClient, uri).await()

                if (dataItemResult.status.isSuccess && dataItemResult.dataItem != null) {
                    val data = DataMap.fromByteArray(dataItemResult.dataItem.data)
                    val jsonData = data.getString(KEY_DATA)
                    Timber.d(jsonData)
                    it.onSuccess(gson.fromJson(jsonData, resultType))
                } else {
                    it.onError(NoSuchElementException())
                }
            }
        }))
    }

    private fun connect(): Completable {
        return Completable.defer({
            if (!googleClient.isConnected) {
                googleClient.blockingConnect(TIMEOUT, TimeUnit.SECONDS)
            }
            if (googleClient.isConnected) {
                Completable.complete()
            } else {
                val errorMessage = "Failed to connect to GoogleApiClient within $TIMEOUT seconds"
                Timber.e(errorMessage)
                Completable.error(RuntimeException(errorMessage))
            }
        })

    }

    fun sendMessage(path: String, message: String): Completable {
        return connect().andThen(Completable.defer({
            val nodesResult = Wearable.NodeApi.getConnectedNodes(googleClient).await()
            val messageResult = Wearable.MessageApi.sendMessage(googleClient, nodesResult.nodes[0].id, path, message.toByteArray()).await()
            if (messageResult.status.isSuccess) {
                Completable.complete()
            } else {
                Completable.error(RuntimeException("Can't send the message: ${messageResult.status.statusMessage}"))
            }
            /*nodesResult.nodes.map { Wearable.MessageApi.sendMessage(googleClient, it.id, path, message.toByteArray()).await() }
                    .filterNot { it.status.isSuccess }
                    .forEach { throw RuntimeException("Can't send the message") }*/
        })).subscribeOn(Schedulers.io())
    }


    override fun onDataChanged(dataEvent: DataEventBuffer) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        //onWerableData?.onData(dataEvent.)
    }

    override fun onConnected(p0: Bundle?) {
        Wearable.DataApi.addListener(googleClient, this)
    }

    override fun onConnectionSuspended(p0: Int) {
        Wearable.DataApi.removeListener(googleClient, this)
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        Wearable.DataApi.removeListener(googleClient, this)
    }

    interface OnWearableDataListener<in T> {
        fun onData(data: T)
        fun onFailure()
    }
}
