package es.iesnervion.jmramirez.photoface;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class Main extends AppCompatActivity implements View.OnClickListener
{
    private Button camara;
    private Button galeria;
    private ImageView fotele;

    //Para startActivityForResult de la galería
    public static final int RESULT_GALLERY = 0;

    //¡OJO!!!!! EN ANDROID 6.0 HAY QUE DAR LOS PERMISOS TANTO EN EL MANIFEST.XML COMO EN EL CODIGO!!!!!!!!!!!!!!!!!!

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public void verifyStoragePermissions(Activity activity)
    {
        //Comprueba si tiene permisos de escritura
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED)
        {
            //Si no los tiene se los pide al usuario
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //LLAMAMOS AL METODO DE LOS PERMISOS
        verifyStoragePermissions(this);

        camara = (Button)findViewById(R.id.btnCamera);
        camara.setOnClickListener(this);
        galeria = (Button) findViewById(R.id.btnGallery);
        galeria.setOnClickListener(this);
        //fotele = (ImageView) findViewById(R.id.myImage);
    }

    @Override
    public void onClick(View v)
    {
        //El nombre de la foto tomará la fecha y hora, y le metemos la extensión .jpg, así podremos guardar varias fotos
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        Date now = new Date();
        String photoName = formatter.format(now) + ".jpg";

        switch(v.getId())
        {
            case R.id.btnCamera:

                //Creamos el Intent para llamar a la Camara
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                //Creamos una carpeta en la memoria del teléfono
                File imagesFolder = new File(Environment.getExternalStorageDirectory(), "PhotoFace");
                boolean b=imagesFolder.mkdir();//Crea un directorio para las imágenes de nuestra app

                //Añadimos el nombre de la imagen
                File image = new File(imagesFolder, photoName);
                Uri uriSavedImage = Uri.fromFile(image);

                //Le decimos al Intent que queremos grabar la imagen
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);

                //Lanzamos la aplicación de la cámara con retorno (forResult)
                startActivityForResult(cameraIntent, 1);

                break;

            case R.id.btnGallery:

                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent , RESULT_GALLERY);

                break;

        } //Fin switch
    } //Fin onClick

    //MÉTODO QUE RECOGE LA FOTO DEL INTENT, PERO COMO ESTA ES DE ALTA CALIDAD SE PASA DEL TAMAÑO Y NO SALE

    //Se puede poner asi o @Override OnActivity
    /*public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        //Comprobamos que se ha hecho la foto
        if (requestCode == 1 && resultCode == RESULT_OK)
        {
            //Creamos un bitmap (tipo de datos para imágenes) con la imagen recientemente almacenada en la memoria
            Bitmap bMap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/PhotoFace/photoName");

            //Añadimos el bitmap al imageView para mostrarlo por pantalla
            fotele.setImageBitmap(bMap);
        }
    } //Fin onActivityResult*/
}
