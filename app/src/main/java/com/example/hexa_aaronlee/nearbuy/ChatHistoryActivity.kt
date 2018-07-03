package com.example.hexa_aaronlee.nearbuy

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.hexa_aaronlee.nearbuy.Presenter.ChatHistoryPresenter
import com.example.hexa_aaronlee.nearbuy.R.layout.list_view_design
import com.example.hexa_aaronlee.nearbuy.View.ChatHistoryView
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_chat_history.*
import kotlin.collections.ArrayList

class ChatHistoryActivity : AppCompatActivity(), ChatHistoryView.View {

    lateinit var historyData: ArrayList<String>
    lateinit var nameData: ArrayList<String>
    lateinit var imageData: ArrayList<String>
    lateinit var titleData: ArrayList<String>
    lateinit var saleData: ArrayList<String>
    lateinit var chatDate: ArrayList<String>
    lateinit var chatTime: ArrayList<String>

    lateinit var mPresenter: ChatHistoryPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_history)

        mPresenter = ChatHistoryPresenter(this)

        historyData = ArrayList()
        imageData = ArrayList()
        nameData = ArrayList()
        titleData = ArrayList()
        saleData = ArrayList()
        chatDate = ArrayList()
        chatTime = ArrayList()

        mPresenter.checkChatHistiryData(UserDetail.user_id)
    }

    override fun setRecyclerViewAdapter(historyData: ArrayList<String>, imageData: ArrayList<String>, nameData: ArrayList<String>, titleData: ArrayList<String>, saleData: ArrayList<String>, chatDate: ArrayList<String>, chatTime: ArrayList<String>) {
        val customAdapter = CustomListView(applicationContext, nameData, historyData, imageData, titleData, saleData, chatDate, chatTime)
        listView.layoutManager = LinearLayoutManager(applicationContext)
        listView.adapter = customAdapter
    }

    override fun setEmptyViewAdapter(checkDataExits: Boolean) {
        if (!checkDataExits) {
            val arrayData = ArrayList<String>()
            arrayData.add("No Chat History!")
            val emptyAdapter = EmptyListAdapter(applicationContext, arrayData)
            listView.layoutManager = LinearLayoutManager(applicationContext)
            listView.setHasFixedSize(true)
            listView.adapter = emptyAdapter
        } else {
            mPresenter.getChatHistoryDataFromDatabase(historyData, imageData, nameData, titleData, UserDetail.user_id, saleData, chatDate, chatTime)
        }
    }


    class CustomListView(private val context: Context,
                         private val nameSource: ArrayList<String>,
                         private val IdSource: ArrayList<String>,
                         private val imageData: ArrayList<String>,
                         private val titleData: ArrayList<String>,
                         private val saleData: ArrayList<String>,
                         private val chatDate: ArrayList<String>,
                         private val chatTime: ArrayList<String>) : RecyclerView.Adapter<CustomListView.CustomViewHolder>() {

        private var mContext: Context = context

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(list_view_design, parent, false)
            return CustomViewHolder(itemView)
        }

        override fun getItemCount(): Int {
            return nameSource.size
        }

        override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

            holder.listTxt?.text = nameSource[position]

            val tmpUri = Uri.parse(imageData[position])

            Picasso.get()
                    .load(tmpUri)
                    .centerCrop()
                    .resize(700, 700)
                    .into(holder.listImage)

            holder.listTitle?.text = titleData[position]

            holder.onClickMethod(position, nameSource, IdSource, imageData, saleData)

        }


        class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            var listTxt: TextView? = null
            var listImage: ImageView? = null
            var listTitle: TextView? = null


            init {
                listTxt = itemView.findViewById(R.id.list_txt)
                listImage = itemView.findViewById(R.id.list_icon)
                listTitle = itemView.findViewById(R.id.dealTitleTxt)

            }

            fun onClickMethod(pos: Int, nameSource: ArrayList<String>,
                              IdSource: ArrayList<String>,
                              imageData: ArrayList<String>,
                              saleData: ArrayList<String>) {
                itemView.setOnClickListener {
                    Toast.makeText(itemView.context, "Position : $position", Toast.LENGTH_SHORT).show()

                    UserDetail.chatWithID = IdSource[pos]
                    UserDetail.chatWithName = nameSource[pos]
                    UserDetail.chatWithImageUri = imageData[pos]
                    UserDetail.saleSelectedId = saleData[pos]

                    Log.i("Get Data Chart Users : ", "${UserDetail.chatWithID}....${UserDetail.chatWithName}")

                    val intent = Intent(itemView.context, ChatRoomActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    itemView.context.startActivity(intent)

                    //finish the activity after clicked the list view item
                    //(itemView.context as Activity).finish()
                }
            }

        }

    }

    override fun onBackPressed() {
        //startActivity(Intent(applicationContext, MainPageActivity::class.java))
        finish()
    }
}


