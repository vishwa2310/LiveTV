package com.livetv;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class Activity_Video extends AppCompatActivity {
    ArrayList<String> arr_video = new ArrayList<String>();
    ArrayList<String> arr_name = new ArrayList<String>();
    ArrayList<Uri> arr_tempname = new ArrayList<Uri>();
    ArrayList<Uri> arr_temp = new ArrayList<Uri>();
    int count = 0, newcount = 0;
    private int resquestPermissionCode = 1;
    private VideoView videoView;
    private Context context;
    private String file_url = "";
    private NetworkConnection networkConnection;
    private ImageView imageView;
    private Button btn_net, btn_hw,btn_dwnload;

    public static boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        if (connectivityManager != null) {
            return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
        } else {
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        context = this;
        if (checkPermission()) {
        } else {
            requestPermission();
        }
        networkConnection = new NetworkConnection();
        arr_temp.clear();
        videoView = findViewById(R.id.videoView);
        imageView = findViewById(R.id.imageView);
        btn_net = findViewById(R.id.text_net);
        btn_hw = findViewById(R.id.text_hard);
        btn_dwnload = findViewById(R.id.text_dwnload);

        try {
            String path = Environment.getExternalStorageDirectory().toString() + "/addFront";
            // System.out.println("Files Path==:" + path);
            File directory = new File(path);
            File[] files = directory.listFiles();
            //System.out.println("Files Size: " + files.length);


            if (isNetworkAvailable(Activity_Video.this)) {
                dirClear();
                // new GetVidoList().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                try {

                    for (int i = 0; i < files.length; i++) {
                        File new_path = new File(path + "/" + files[i].getName());
                        Uri uri_path = FileProvider.getUriForFile(Activity_Video.this, BuildConfig.APPLICATION_ID + ".provider", new_path);
                        arr_temp.add(uri_path);
                        System.out.println(("Files FileName with full path no nw===" + uri_path));
                    }

                } catch (Exception e) {
                    Toast.makeText(context, "Video cant find", Toast.LENGTH_SHORT).show();
                }

         /*   //System.out.println(("Files FileName:" + files[i].getName()));
            Uri u = arr_temp.get(0);
            Uri vidurl = Uri.parse(String.valueOf(u));
            videoView.setVideoURI(vidurl);
            videoView.start();*/

                videoPlay();

            }

            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {

                    if (count != 0) {
                        System.out.println(("under completion call"));
                        videoPlay();
                    }
                }
            });
/*new Handler().postDelayed(new Runnable() {
    @Override
    public void run() {
        System.out.println("run thread");
        // your code here...
        if (isNetworkAvailable(Activity_Video.this)) {
         *//*   Intent i = new Intent(context, Activity_Video.class);
            finish();
            startActivity(i);*//*
            new GetVidoList().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }
}, TimeUnit.MINUTES.toMillis(2));*/
           // chkeSignal();

            Timer timer = new Timer();
            TimerTask hourlyTask = new TimerTask() {
                @Override
                public void run() {
                    System.out.println("run timer");
                    if (isNetworkAvailable(Activity_Video.this)) {
System.out.println("time current==="+ DateFormat.getDateTimeInstance().format(new Date()));
                        new GetVidoList().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                       // btn_net.setBackgroundColor(getResources().getColor(R.color.colorGren));
                       // btn_hw.setBackgroundColor(getResources().getColor(R.color.colorGren));
                    }else{
                       // btn_net.setBackgroundColor(getResources().getColor(R.color.colorRed));
                    }
                }
            };

// schedule the task to run starting now and then every hour...
            timer.schedule(hourlyTask, 0l, 1000 * 60*1);   // 1000*10*60 every 10 minut

   /*     Timer timer = new Timer ();
        TimerTask hourlyTask = new TimerTask () {
            @Override
            public void run () {

            }
        };
// schedule the task to run starting now and then every 15minutes...
        timer.schedule (hourlyTask, 0l, TimeUnit.MINUTES.toMillis(5));   // 1000*10*60 every 10 minut*/
        } catch (Exception e) {
            System.out.println("Error in =="+e);
        }
    }

    private void chkeSignal() {

        System.out.println("run indicatore");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isNetworkAvailable(Activity_Video.this)) {
                    btn_net.setBackgroundColor(getResources().getColor(R.color.colorGren));
                    btn_hw.setBackgroundColor(getResources().getColor(R.color.colorGren));
                } else {
                    btn_net.setBackgroundColor(getResources().getColor(R.color.colorRed));
                    // btn_hw.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                }
            }
        }, 3000);

    }
    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
    public BroadcastReceiver service_Broadcaset=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction()!=null){
                if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {

                    if (isConnected(context)){
                       // Toast.makeText(context,"Network  avilable won method", Toast.LENGTH_LONG).show();;
                        btn_net.setBackgroundColor(getResources().getColor(R.color.colorGren));

                    }else{
                        btn_net.setBackgroundColor(getResources().getColor(R.color.colorRed));
                        //Toast.makeText(context,"Network Not avilable won method", Toast.LENGTH_LONG).show();;
                    }
                }
            }

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(service_Broadcaset, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onPause() {
        unregisterReceiver(service_Broadcaset);
        super.onPause();
    }

    private void dirClear() {
        File dir = new File(Environment.getExternalStorageDirectory() + "/addFront");
        if (dir.isDirectory()) {
            System.out.println("directory find clear " + dir);
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                new File(dir, children[i]).delete();
            }
        } else {
            System.out.println("directory not  find " + dir);
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(Activity_Video.this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE, ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION}, resquestPermissionCode);
    }

    private boolean checkPermission() {
        int permission_write = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission_read = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int permission_coreloc = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_COARSE_LOCATION);
        int permission_fineloc = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        return permission_read == PackageManager.PERMISSION_GRANTED && permission_write == PackageManager.PERMISSION_GRANTED && permission_coreloc == PackageManager.PERMISSION_GRANTED && permission_fineloc == PackageManager.PERMISSION_GRANTED;

    }

    private void videoPlay() {

        if (arr_temp.size() > count) {
            Uri uri = arr_temp.get(count);
            final Uri vidurl = Uri.parse(String.valueOf(uri));
            if ((String.valueOf(vidurl).toLowerCase()).contains(".jpg") || (String.valueOf(vidurl).toLowerCase()).contains(".jpeg") || (String.valueOf(vidurl).toLowerCase()).contains(".png")) {
                videoView.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);

                //final Uri imageUri = data.getData();
                final InputStream imageStream;
                try {
                    imageStream = getContentResolver().openInputStream(vidurl);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    imageView.setImageBitmap(selectedImage);
                    System.out.println(("display image path=" + String.valueOf(vidurl)));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            count++;
                            videoPlay();
                        }
                    }, 3000);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            } else if ((String.valueOf(vidurl).toLowerCase()).contains(".mp4") || (String.valueOf(vidurl).toLowerCase()).contains(".3gp") || (String.valueOf(vidurl).toLowerCase()).contains(".avi") ||
                    (String.valueOf(vidurl).toLowerCase()).contains(".webm") || (String.valueOf(vidurl).toLowerCase()).contains(".hdv") || (String.valueOf(vidurl).toLowerCase()).contains(".mpg")) {
                imageView.setVisibility(View.GONE);
                videoView.setVisibility(View.VISIBLE);
                System.out.println(("display video path=" + String.valueOf(vidurl)));
                videoView.setVideoURI(vidurl);
                videoView.start();
            }

            System.out.println(("complete if loop" + count));
        } else {
            count = 0;
            System.out.println(("zero count" + count));
            if (arr_temp.size() > count) {
                Uri uri = arr_temp.get(count);
                Uri vidurl = Uri.parse(String.valueOf(uri));
                if ((String.valueOf(vidurl).toLowerCase()).contains(".jpg") || (String.valueOf(vidurl).toLowerCase()).contains(".jpeg") || (String.valueOf(vidurl).toLowerCase()).contains(".png")) {
                    videoView.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);

                    //final Uri imageUri = data.getData();
                    final InputStream imageStream;
                    try {
                        imageStream = getContentResolver().openInputStream(vidurl);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        imageView.setImageBitmap(selectedImage);
                        System.out.println(("display image path=" + String.valueOf(vidurl)));
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                count++;
                                videoPlay();
                            }
                        }, 3000);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                } else if ((String.valueOf(vidurl).toLowerCase()).contains(".mp4") || (String.valueOf(vidurl).toLowerCase()).contains(".3gp") || (String.valueOf(vidurl).toLowerCase()).contains(".avi")
                        || (String.valueOf(vidurl).toLowerCase()).contains(".webm") || (String.valueOf(vidurl).toLowerCase()).contains(".hdv") || (String.valueOf(vidurl).toLowerCase()).contains(".mpg") || (String.valueOf(vidurl).toLowerCase()).contains(".mov")) {
                    imageView.setVisibility(View.GONE);
                    videoView.setVisibility(View.VISIBLE);
                    System.out.println(("display video path=" + String.valueOf(vidurl)));
                    videoView.setVideoURI(vidurl);
                    videoView.start();
                }
//                videoView.setVideoURI(vidurl);
//                videoView.start();
            }

        }
        count++;
        System.out.println(("end of loop"));
        // videoPlay();
    }

   /* private class GetdeviceID extends AsyncTask<Void, Void, Void> {

        private ProgressDialog pDialog;
        String jsonStr = "";

        String url_getclientmandate = "http://adfront.in/video_api.php";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                pDialog = new ProgressDialog(Activity_Video.this);
                pDialog.setMessage("Please wait...");
                pDialog.setProgressStyle(R.style.AppCompatAlertDialogStyle);
                pDialog.setCancelable(false);
                pDialog.show();
            } catch (Exception e) {
            }
        }

        @Override
        protected Void doInBackground(Void... params) {

            System.out.println("This is device id Url====" + url_getclientmandate);
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            jsonStr = sh.makeServiceCall(url_getclientmandate).toLowerCase();
            System.out.println("This responce device id ====" + jsonStr);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (jsonStr.equals("vishwa")) {
                Toast.makeText(Activity_Video.this, "please Check Internet Connection", Toast.LENGTH_SHORT).show();

            } else {
                convertJsonData_deviceID(jsonStr);
            }
            try {
                pDialog.dismiss();
            } catch (Exception e) {
            }
        }

    }

    public void convertJsonData_deviceID(String jsonStr) {
        arr_video.clear();

        if (jsonStr != null) {
            try {
                JSONArray jsonObject = new JSONArray(jsonStr);
                if (!jsonObject.toString().equals("[]")) {
                    for (int i = 0; i < jsonObject.length(); i++) {

                        JSONObject e = jsonObject.getJSONObject(i);
                        String id = e.getString("video");
                        //if (!id.contains("jpg")) {
                        arr_video.add("http://" + id);
                        // }
                    }
                    System.out.println("json total file count(arry)=" + arr_video);
                    if (arr_video.size() > 0) {
                        arr_temp.clear();
                        for (int i = 0; i < arr_video.size(); i++) {
                            file_url = arr_video.get(i);
                            if (!file_url.equals("")) {

                                String[] temdata = file_url.split("/");
                                //new DownloadFile().execute(file_url, temdata[temdata.length - 1]);


                                new DownloadFile().execute(file_url, temdata[temdata.length - 1]);
                                System.out.println("loop for file downloading ==" + i);
                            }
                        }

                    }
                }
            } catch (final JSONException e) {

                System.out.println("Json parsing error: " + e.getMessage());
                Toast.makeText(Activity_Video.this, "No Internet Connection ", Toast.LENGTH_SHORT).show();

            }
        }
    }*/

    public void convertJsonData_clientmandate(String jsonStr) {
        arr_video.clear();
        ArrayList arrayList = new ArrayList();
        if (jsonStr != null) {
            try {
                JSONArray jsonObject = new JSONArray(jsonStr);
                if (!jsonObject.toString().equals("[]")) {
                    for (int i = 0; i < jsonObject.length(); i++) {

                        JSONObject e = jsonObject.getJSONObject(i);
                        String id = e.getString("video");
                        //if (!id.contains("jpg")) {
                        arr_video.add("http://" + id);
                        // }

                    }
                    System.out.println("json total file count(arry)=" + arr_video);
                    if (arr_video.size() > 0) {

                        // arr_temp.clear();
                        for (int i = 0; i < arr_video.size(); i++) {
                            file_url = arr_video.get(i);
                            if (!file_url.equals("")) {

                                String[] temdata = file_url.split("/");
                                System.out.println("arry name **==" + arr_name);
                                if (!arr_name.contains(temdata[temdata.length - 1])) {
                                    new DownloadFile().execute(file_url, temdata[temdata.length - 1]);
                                    arr_name.add(temdata[temdata.length - 1]);
                                    System.out.println("loop for file downloading ==" + i);
                                } else {
                                    arrayList.clear();
                                    for (int g = 0; g < arr_video.size(); g++) {

                                        String p = arr_video.get(g);
                                        if (!p.equals("")) {

                                            String[] te = p.split("/");
                                            File pdfFile = new File(Environment.getExternalStorageDirectory() + "/addFront/" + (te[temdata.length - 1]));  // -> filename = maven.pdf
                                            //Uri path = Uri.fromFile(pdfFile);
                                            Uri path = FileProvider.getUriForFile(Activity_Video.this, BuildConfig.APPLICATION_ID + ".provider", pdfFile);
                                            arrayList.add(path);

                                        }

                                    }

                                    System.out.println("****arry list vertual name" + arrayList);
                                    arr_temp.retainAll(arrayList);
                                    System.out.println("*************after retail ellement*********" + arr_temp);
                                    System.out.println("*************no new video found**********");
                                }
                            }

                       /*     try{
                                String[] temdata = file_url.split("/");
                                if (!arr_temp.contains(temdata[temdata.length - 1])){
                                    for (int k=0 ;k<arr_temp.size();k++){
                                        String g= (arr_temp.get(k).toString());
                                        String[] t = g.split("/");
                                        String f= t[t.length - 1];
                                        if (f.equals(temdata)){
                                            arr_temp.remove(k);
                                        }
                                    }

                                }}catch (Exception e){
                                System.out.println("old file checking eror" + e);
                            }*/

                            System.out.println("file names array==" + arr_name);
                        }
                    } else {
                        Toast.makeText(Activity_Video.this, "No Video Found", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (final JSONException e) {
                btn_hw.setBackgroundColor(getResources().getColor(R.color.colorRed));
                System.out.println("Json parsing error: " + e.getMessage());
                Toast.makeText(Activity_Video.this, "No Internet Connection ", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private class GetVidoList extends AsyncTask<Void, Void, Void> {

        String jsonStr = "";
        //decvice id
        //  Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID)
        String url_getclientmandate = "http://adfront.in/video_api.php?mac_address=12345";
        private ProgressDialog pDialog;
        // + Secure.getString(getApplicationContext().getContentResolver(), Secure.ANDROID_ID);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                pDialog = new ProgressDialog(Activity_Video.this);
                pDialog.setMessage("Please wait...");
                pDialog.setProgressStyle(R.style.AppCompatAlertDialogStyle);
                pDialog.setCancelable(false);
                pDialog.show();
            } catch (Exception e) {
            }
        }

        @Override
        protected Void doInBackground(Void... params) {

            System.out.println("This is Url====" + url_getclientmandate);
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            jsonStr = sh.makeServiceCall(url_getclientmandate).toLowerCase();
            System.out.println("This responce ====" + jsonStr);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (jsonStr.equals("vishwa")) {
                Toast.makeText(Activity_Video.this, "please Check Internet Connection", Toast.LENGTH_SHORT).show();

            } else {
                convertJsonData_clientmandate(jsonStr);
            }
            try {
                pDialog.dismiss();
            } catch (Exception e) {
            }
        }

    }

    private class DownloadFile extends AsyncTask<String, Void, Void> {
        String ErrorTag = "";
        String fileUrl;
        String fileName;
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            btn_dwnload.setBackgroundColor(getResources().getColor(R.color.colorRed));
     /*       try {
                pDialog = new ProgressDialog(Activity_Video.this);
                pDialog.setMessage("Please wait...");
                pDialog.setProgressStyle(R.style.AppCompatAlertDialogStyle);
                pDialog.setCancelable(false);
                pDialog.show();
            } catch (Exception e) {
            }*/
        }

        @Override
        protected Void doInBackground(String... strings) {

            fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
            fileName = strings[1];  // -> maven.pdf
            //fileName = "test.mp4";
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "addFront");
            folder.mkdir();

            File pdfFile = new File(folder, fileName);
            Activity_Video.this.runOnUiThread(new Runnable() {

                public void run() {
                    //Toast.makeText(Activity_video.this, "Downloading...", Toast.LENGTH_LONG).show();
                }
            });


            try {
                pdfFile.createNewFile();
                ErrorTag = "SUCCESS";
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("file downloading url ==" + String.valueOf(fileUrl));
            FileDownloader.downloadFile(fileUrl, pdfFile);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub

            if (ErrorTag.equals("SUCCESS")) {

                // File pdfFile = new File(Environment.getExternalStorageDirectory() + "/mandate/" + "test.mp4");
                File pdfFile = new File(Environment.getExternalStorageDirectory() + "/addFront/" + fileName);  // -> filename = maven.pdf
                //Uri path = Uri.fromFile(pdfFile);
                Uri path = FileProvider.getUriForFile(Activity_Video.this, BuildConfig.APPLICATION_ID + ".provider", pdfFile);

                arr_temp.add(path);
                System.out.println("loop of files add in directory==" + String.valueOf(arr_temp));
                newcount++;
                System.out.println("new count==" + newcount + "arraysize=" + arr_video.size());
                if (newcount == arr_video.size()) {
                    videoPlay();
                }




               /*     if (arr_video.size()>=arr_temp.size()){



                        }*/








         /*       for (int l = 0; l < arr_video.size(); l++) {

                    String d = arr_video.get(l);
                    String[] s = d.split("/");
                    for (int g = 0; g < arr_temp.size(); g++) {
                        String q = String.valueOf(arr_temp.get(l));
                        if (q.equals(s)){



                        }else{

                        }
                    }

                }*/

          /*      try{
                    String[] temdata = file_url.split("/");
                    if (!arr_temp.contains(temdata[temdata.length - 1])){
                        for (int k=0 ;k<arr_temp.size();k++){
                            String g= (arr_temp.get(k).toString());
                            String[] t = g.split("/");
                            String f= t[t.length - 1];
                            if (f.equals(temdata)){
                                arr_temp.remove(k);
                            }
                        }

                    }}catch (Exception e){
                    System.out.println("old file checking eror" + e);
                }*/

            } else {
                if (ErrorTag.contains("FileNotFoundException")) {
                    Toast.makeText(Activity_Video.this, "File Not Found", Toast.LENGTH_LONG).show();
                }
            }
            btn_dwnload.setBackgroundColor(getResources().getColor(R.color.colorGren));
            //super.onPostExecute(result);
         /*   try {
                pDialog.dismiss();
            } catch (Exception e) {
            }*/
        }
    }
}
