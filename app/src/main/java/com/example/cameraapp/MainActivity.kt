package com.example.cameraapp

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

//data creazione : 14/03/2022
//ultima modifica: 17/03/2022

private val PERMISSION_CODE = 1000;
private val IMAGE_CAPTURE_CODE = 1001;
var image_uri: Uri? = null

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //viene premuto il bottone
        capture_btn.setOnClickListener {
            //se il sistema operativo è Marshmellow o più recente dovremo richiedere i permessi
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED || checkSelfPermission(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_DENIED) {
                    //I permessi non sono stati concessi
                    val permission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    //Vengono richiesti i permessi
                    requestPermissions(permission, PERMISSION_CODE)
                }
                openCamera()
            }
            else{
                //Il sistema operativo è precedente a Marshmellow e quindi non sarà necessario chiedere i requisiti
                openCamera()
            }
        }

    }

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "Nuova Immagine")
        values.put(MediaStore.Images.Media.DESCRIPTION, "Camera")
        image_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        //camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //funzione chiamata quando l'utente accetta o rifiuta i permessi
        when(requestCode){
            PERMISSION_CODE ->{
                if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //permessi accettati
                    openCamera()
                }
                else{
                    //permessi non accettati
                    Toast.makeText(this , "Permissi non concessi", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //funzione che viene chiamata quando viene catturata un'immagine
        if(resultCode == RESULT_OK){
            //l'immagine catturata viene mostrata
            image_view.setImageURI(image_uri)
        }
    }
}
