package com.example.hexa_aaronlee.nearbuy.View

import android.content.Context

public interface LoginView {

    interface View {
        fun loginToNext()
        fun regiterDirected()
        fun loginFaild()
        fun loginGoogle()
    }

    interface Model{
        var email: String
        var password: String
        var isDataVaild: Boolean
    }

    interface Presenter {
        fun clickRegisterTxt()
        fun clickLoginBtn(email: String,
                          password: String)

        fun clickGoogleBtn()
        fun saveDataProcess(email: String,
                            password: String,
                            user_id: String,
                            name: String,
                            profilePhoto: String,
                            gender : String,
                            phoneNum : String)
    }
}