package com.example.firebase

import android.content.Context
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import java.lang.IndexOutOfBoundsException


class AdapterUser(private val context: Context) : RecyclerView.Adapter<AdapterUser.viewHolder>() {

    private var list: ArrayList<InfoUser>? = null

    fun getList(list: ArrayList<InfoUser>) {
        this.list = list
        notifyDataSetChanged()
    }

    class viewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name = view.findViewById<TextView>(R.id.name)
        var email = view.findViewById<TextView>(R.id.usernames)
        var diachi = view.findViewById<TextView>(R.id.diachi)
        var sdt = view.findViewById<TextView>(R.id.sdt)
        var mMenus = view.findViewById<ImageView>(R.id.mMenus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.item, parent, false)
        return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        var person = list!!.get(position)
        holder.name.text = person.fullname
        holder.email.text = person.email
        holder.diachi.text = person.diachi
        holder.sdt.text = person.sdt
        holder.mMenus.setOnClickListener { mPopMenu(it, person, position) }
    }


    private fun mPopMenu(it: View?, person: InfoUser, position: Int) {
        var popupMenu = PopupMenu(context, it)
        popupMenu.inflate(R.menu.show_menu)

        var mAuth = FirebaseAuth.getInstance()

        var userRef =
            FirebaseDatabase.getInstance().reference.child("users").child(mAuth.currentUser!!.uid)
        var getPq: Int? = -1;

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.editText -> {
                    userRef.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            var user = snapshot.getValue(InfoUser::class.java)
                            Log.e("check admin ", person.id.toString())
                            Log.e("check equals ", mAuth.uid.equals(person.id).toString())
                            Log.e("check admin ", (user?.pq!! == 0).toString())

                            if (mAuth.uid.equals(person.id) || user?.pq!! == 0) {
                                var layoutInflater = LayoutInflater.from(context)
                                var v = layoutInflater.inflate(R.layout.add_item, null)
                                var pq: TextView = v.findViewById(R.id.pq)
                                if (person.pq == 0) {
                                    pq.text = Editable.Factory.getInstance()
                                        .newEditable(person.pq.toString())
                                    pq.visibility = View.VISIBLE
                                } else {
                                    pq.visibility = View.GONE
                                }
                                var names: EditText = v.findViewById(R.id.name)
                                var diachi: EditText = v.findViewById(R.id.diachi)
                                var sdt: EditText = v.findViewById(R.id.sdt)

                                names.text =
                                    Editable.Factory.getInstance().newEditable(person.fullname)
                                diachi.text =
                                    Editable.Factory.getInstance().newEditable(person.diachi)
                                sdt.text = Editable.Factory.getInstance().newEditable(person.sdt)

                                MaterialAlertDialogBuilder(context)
                                    .setView(v)
                                    .setPositiveButton("Lưu") { dialog, which ->
                                        update(
                                            person.id.toString(),
                                            names.text.toString(),
                                            person.email!!,
                                            person.password!!,
                                            diachi.text.toString(),
                                            sdt.text.toString(),
                                            pq.text.toString().toInt()
                                        )
                                        list?.clear()
                                        dialog.dismiss()
                                    }.setNegativeButton("canel") { dialog, which ->
                                        dialog.dismiss()
                                    }.show()
                            } else {
                                Toast.makeText(
                                    context,
                                    "bạn không phải admin hoặc bạn không thể sửa thông tin người khác",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })
                    true
                }
                R.id.delete -> {
                    userRef.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            var user = snapshot.getValue(InfoUser::class.java)
                            if(user!!.pq == 0){
                                Toast.makeText(context,"delete",Toast.LENGTH_SHORT).show()
                                MaterialAlertDialogBuilder(context)
                                    .setMessage("ban co muon xoa")
                                    .setPositiveButton("xóa") { dialog,which ->
                                        delete(person.id.toString())
                                        notifyItemRemoved(position)
                                        list?.clear()
                                        notifyDataSetChanged()
                                    }.setNegativeButton("canel"){ dialog,which ->
                                        dialog.dismiss()
                                    }.show()
                            }else{
                                Toast.makeText(context,"Chỉ admin mới thực hiện được chức năng này",Toast.LENGTH_SHORT).show()
                            }

                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    })




                    true
                }

                else -> false
            }

        }
        popupMenu.show()
        val popup = PopupMenu::class.java.getDeclaredField("mPopup")
        popup.isAccessible = true
        val menu = popup.get(popupMenu)
        menu.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java).invoke(menu, true)
    }

    fun delete(id: String) {
        FirebaseDatabase.getInstance().reference.child("users").child(id).removeValue()
    }

    fun update(
        id: String,
        fullname: String,
        user: String,
        password: String,
        diachi: String,
        sdt: String,
        pq: Int
    ) {
        var userRef = FirebaseDatabase.getInstance().reference.child("users")
            .child(id)
        var userMap = HashMap<String, Any>()
        userMap["email"] = user
        userMap["password"] = password
        userMap["fullname"] = fullname
        userMap["diachi"] = diachi
        userMap["sdt"] = sdt
        userMap["pq"] = pq
        userRef.updateChildren(userMap)

    }

    override fun getItemCount(): Int {
        if (list != null) {
            return list!!.size
        }
        return 0
    }

}