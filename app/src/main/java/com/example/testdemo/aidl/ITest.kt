package com.example.testdemo.aidl

import android.os.Binder
import android.os.IBinder
import android.os.IInterface
import android.os.Parcel

interface ITest : IInterface {
    abstract class Stub : Binder(), ITest {
        init {
            attachInterface(this, DIAL)
        }

        override fun asBinder(): IBinder = this

        override fun onTransact(code: Int, data: Parcel, reply: Parcel?, flags: Int): Boolean = when (code) {
            INTERFACE_TRANSACTION -> {
                reply?.writeString(DIAL)
                true
            }
            TRANSACTION_addTestBean -> {
                data.enforceInterface(DIAL)
                val bean: TestBean? = if (0 != data.readInt()) {
                    TestBean.CREATOR.createFromParcel(data)
                } else {
                    null
                }
                addTestBean(bean)
                reply?.writeNoException()
                true
            }
            TRANSACTION_getBean -> {
                data.enforceInterface(DIAL)
                getBean()
                reply?.writeNoException()
                true
            }
            TRANSACTION_testAidl -> {
                data.enforceInterface(DIAL)
                testAidl(data.readInt())
                reply?.writeNoException()
                true
            }
            else -> super.onTransact(code, data, reply, flags)
        }


    }

    companion object {
        private const val DIAL = "com.example.testdemo.test"

        class Proxy(private val iBinder: IBinder?) : ITest {
            override fun addTestBean(bean: TestBean?) {
                val data = Parcel.obtain()
                val reply = Parcel.obtain()

                try {
                    data.writeInterfaceToken(DIAL)
                    if (bean != null) {
                        data.writeInt(1)
                        bean.writeToParcel(data, 0)
                    } else {
                        data.writeInt(0)
                    }
                    iBinder?.transact(TRANSACTION_addTestBean,data,reply,0)
                    reply.readException()
                } finally {
                    data.recycle()
                    reply.recycle()
                }
            }

            override fun getBean(): List<TestBean>? {

                val data = Parcel.obtain()
                val reply = Parcel.obtain()
                var beans:List<TestBean>? = emptyList<TestBean>()

                try {
                    data.writeInterfaceToken(DIAL)
                    iBinder?.transact(TRANSACTION_getBean,data,reply,0)
                    reply.readException()
                    beans = reply.createTypedArrayList(TestBean.CREATOR)
                } finally {
                    data.recycle()
                    reply.recycle()
                }

                return beans
            }

            override fun testAidl(a: Int) {
                val data = Parcel.obtain()
                val reply = Parcel.obtain()

                try {
                    data.writeInterfaceToken(DIAL)
                    data.writeInt(a)
                    iBinder?.transact(TRANSACTION_testAidl, data, reply, 0)
                    reply.readException()
                } finally {
                    data.recycle()
                    reply.recycle()
                }


            }

            override fun asBinder(): IBinder? = iBinder

        }

        private const val TRANSACTION_addTestBean = IBinder.FIRST_CALL_TRANSACTION + 1
        private const val TRANSACTION_getBean = IBinder.FIRST_CALL_TRANSACTION + 2
        private const val TRANSACTION_testAidl = IBinder.FIRST_CALL_TRANSACTION + 3
    }

    fun addTestBean(bean: TestBean?)
    fun getBean(): List<TestBean>?
    fun testAidl(a: Int)
}