package com.example.taller3.Activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.taller3.Data.Usuario
import com.example.taller3.R
import com.example.taller3.databinding.ActivityFirebaseBinding
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class Firebase : AppCompatActivity() {
    lateinit var binding : ActivityFirebaseBinding
    val PATH_USERS="users/"
    private val database = FirebaseDatabase.getInstance()
    private lateinit var myRef: DatabaseReference
    private val uid = "123456778"
    private lateinit var listener1 : ValueEventListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirebaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Write a message to the database
        val database2 = Firebase.database
        val myRef2 = database2.getReference("message")

        myRef2.setValue("Hello, World!")

        binding.button.setOnClickListener {
            //Llenar info
            val usr1 = Usuario()
            usr1.nombre = binding.editTextText.text.toString()
            usr1.apellido = binding.editTextText2.text.toString()
            usr1.edad = binding.editTextText3.text.toString().toInt()

            //EScribir en la base de datos
            myRef = database.getReference(PATH_USERS+uid)
            Log.i("infoFirebase", myRef.toString())
            myRef.setValue(usr1)
            Toast.makeText(this, "Información guardada", Toast.LENGTH_SHORT).show()
        }

        loadUsers()

    }

    fun loadUsers() {
        myRef = database.getReference(PATH_USERS)
        //Esta es la segunda forma de hacerlo. Revisar diapositivas
        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (singleSnapshot in dataSnapshot.children) {
                    val myUser = singleSnapshot.getValue(Usuario::class.java)
                    Log.i("Firebase", "Encontró usuario: " + myUser?.nombre)
                    val name = myUser?.nombre
                    val age = myUser?.edad
                    Toast.makeText(baseContext, "$name: $age", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("Firebase", "error en la consulta", databaseError.toException())
            }

        })
    }

    override fun onStop(){
        super.onStop()

    }
}

