package com.mylisabox.network.utils

import android.net.TrafficStats
import com.mylisabox.network.dagger.annotations.ApplicationScope
import java.io.IOException
import java.net.InetAddress
import java.net.Socket
import javax.inject.Inject
import javax.net.SocketFactory

//FIXME https://github.com/square/okhttp/issues/3537 but okHttp/retrofit this class will be removed once they fit it, impact Android O
@ApplicationScope
class TagSocketFactory @Inject constructor() : SocketFactory() {
    private val delegate: SocketFactory = SocketFactory.getDefault()

    @Throws(IOException::class)
    private fun configureSocket(socket: Socket): Socket {
        TrafficStats.tagSocket(socket)
        return socket
    }

    @Throws(IOException::class)
    override fun createSocket(): Socket {
        val socket = delegate.createSocket()
        return configureSocket(socket)
    }

    @Throws(IOException::class)
    override fun createSocket(host: String, port: Int): Socket {
        val socket = delegate.createSocket(host, port)
        return configureSocket(socket)
    }

    @Throws(IOException::class)
    override fun createSocket(host: String, port: Int, localAddress: InetAddress,
                              localPort: Int): Socket {
        val socket = delegate.createSocket(host, port, localAddress, localPort)
        return configureSocket(socket)
    }

    @Throws(IOException::class)
    override fun createSocket(host: InetAddress, port: Int): Socket {
        val socket = delegate.createSocket(host, port)
        return configureSocket(socket)
    }

    @Throws(IOException::class)
    override fun createSocket(host: InetAddress, port: Int, localAddress: InetAddress,
                              localPort: Int): Socket {
        val socket = delegate.createSocket(host, port, localAddress, localPort)
        return configureSocket(socket)
    }
}