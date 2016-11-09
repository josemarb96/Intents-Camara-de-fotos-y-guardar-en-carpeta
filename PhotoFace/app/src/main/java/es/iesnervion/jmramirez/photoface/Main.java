package es.iesnervion.jmramirez.photoface;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import java.io.File;

public class Main extends AppCompatActivity implements View.OnClickListener
{
    private Button camara;
    //private Button galeria; //NO LO USAMOS DE MOMENTO
    private ImageView fotele;

    //¡OJO!!!!! EN ANDROID 6.0 HAY QUE DAR LOS PERMISOS TANTO EN EL MANIFEST.XML COMO EN EL CODIGO!!!!!!!!!!!!!!!!!!

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };



    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public void verifyStoragePermissions(Activity activity)
    {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED)
        {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //lLAMAMOS AL METODO DE LOS PERMISOS
        verifyStoragePermissions(this);

        camara = (Button)findViewById(R.id.btnCamera);
        camara.setOnClickListener(this);
        //galeria = (Button) findViewById(R.id.btnGallery);
        //galeria.setOnClickListener(this);
        fotele = (ImageView) findViewById(R.id.myImage);
    }

    @Override
    public void onClick(View v)
    {
        //Creamos el Intent para llamar a la Camara
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        //Creamos una carpeta en la memoria del teléfono
        File imagesFolder = new File(Environment.getExternalStorageDirectory(), "croqueta");
        boolean b=imagesFolder.mkdir();//Crea un directorio para las imágenes de nuestra app

        //Añadimos el nombre de la imagen
        File image = new File(imagesFolder, "myPhoto.jpg");
        Uri uriSavedImage = Uri.fromFile(image);

        //Le decimos al Intent que queremos grabar la imagen
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);

        //Lanzamos la aplicación de la cámara con retorno (forResult)
        startActivityForResult(cameraIntent, 1);
    } //Fin onClick

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        //Comprobamos que se ha hecho la foto
        if (requestCode == 1 && resultCode == RESULT_OK)
        {
            //Creamos un bitmap (tipo de datos para imágenes) con la imagen recientemente almacenada en la memoria
            Bitmap bMap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/croqueta/myPhoto.jpg");

            //Añadimos el bitmap al imageView para mostrarlo por pantalla
            fotele.setImageBitmap(bMap);
        }
    } //Fin onActivityResult
}
