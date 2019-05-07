package com.example.android.ip;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public String imageString;
    public ImageView MyImage;
    public RequestQueue queue;
    public Button send;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        Button OpenCamera = (Button) findViewById( R.id.TakePhoto );
        send = (Button) findViewById( R.id.Send );
        MyImage = (ImageView) findViewById( R.id.TheImage );
        OpenCamera.setOnClickListener( new View.OnClickListener() {
                                           @Override
                                           public void onClick(View view) {
                                               Intent intent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE );
                                               startActivityForResult( intent, 0 );
                                           }
                                       }
        );
        send.setOnClickListener( new View.OnClickListener() {
            public void onClick(View view) {
                String url = "http://192.168.1.6:5000/ReceiveImage";
                imageString=Encoding(MyImage);
                StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>()
                        {
                            @Override
                            public void onResponse(String response) {
                                // response
                                System.out.println( "responseeeee");

                                Log.d("Response", response);
                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // error
                                Log.d("Error.Response", "error");
                            }
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams()
                    {
                        Map<String, String>  params = new HashMap<String, String>();
                        params.put("name", imageString);
                        return params;
                    }
                };

                // add it to the RequestQueue
               queue=Volley.newRequestQueue(getApplicationContext());
               queue.add(postRequest);
            }
        } );
    }
    //@Override/
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        Bitmap bitmap = (Bitmap) data.getExtras().get( "data" );
        MyImage.setImageBitmap( bitmap );
    }
    public String Encoding(ImageView image)
    {
        BitmapDrawable drawable = (BitmapDrawable) MyImage.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return imageString;
    }
}
