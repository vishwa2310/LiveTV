package com.livetv;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class Activity_Video extends AppCompatActivity {
    private VideoView videoView;
    private Context context;
    ArrayList<String> arr_video = new ArrayList<String>();
    private String file_url = "";
    ArrayList<Uri> arr_temp = new ArrayList<Uri>();
    int count = 0, newcount = 0;
    private NetworkConnection networkConnection;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        context = this;
        networkConnection = new NetworkConnection();
        arr_temp.clear();
        videoView = findViewById(R.id.videoView);
        imageView = findViewById(R.id.imageView);

        String path = Environment.getExternalStorageDirectory().toString() + "/addFront";
        // System.out.println("Files Path==:" + path);
        File directory = new File(path);
        File[] files = directory.listFiles();
        //System.out.println("Files Size: " + files.length);


        if (isNetworkAvailable(Activity_Video.this)) {
            dirClear();
            new Getclientmandate().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(networkConnection, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onPause() {
        unregisterReceiver(networkConnection);
        super.onPause();
    }

    private void dirClear() {
        File dir = new File(Environment.getExternalStorageDirectory() + "/addFront");
        if (dir.isDirectory()) {
            System.out.println("directory find " + dir);
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                new File(dir, children[i]).delete();
            }
        } else {
            System.out.println("directory not  find " + dir);
        }
    }

    private void videoPlay() {

        if (arr_temp.size() > count) {
            Uri uri = arr_temp.get(count);
            final Uri vidurl = Uri.parse(String.valueOf(uri));

                videoView.setVideoURI(vidurl);
                videoView.start();

            System.out.println(("complete" + count));
        } else {
            count = 0;
            if (arr_temp.size() > count) {
                Uri uri = arr_temp.get(count);
                Uri vidurl = Uri.parse(String.valueOf(uri));
                videoView.setVideoURI(vidurl);
                videoView.start();
            }
            System.out.println(("zero count" + count));
        }
        count++;
    }

    public static boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        if (connectivityManager != null) {
            return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
        } else {
            return false;
        }
    }

    private class Getclientmandate extends AsyncTask<Void, Void, Void> {

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

    public void convertJsonData_clientmandate(String jsonStr) {
        arr_video.clear();
        if (jsonStr != null) {
            try {
                JSONArray jsonObject = new JSONArray(jsonStr);
                if (!jsonObject.toString().equals("[]")) {
                    for (int i = 0; i < jsonObject.length(); i++) {

                        JSONObject e = jsonObject.getJSONObject(i);
                        String id = e.getString("video");
                        //if (!id.contains("")) {
                        arr_video.add("http://" + id);
                        //}
                    }
                    System.out.println("array video==" + arr_video);
                    if (arr_video.size() > 0) {
                        arr_temp.clear();
                        for (int i = 0; i < arr_video.size(); i++) {
                            file_url = arr_video.get(i);
                            if (!file_url.equals("")) {

                                String[] temdata = file_url.split("/");
                                //new DownloadFile().execute(file_url, temdata[temdata.length - 1]);
                                new DownloadFile().execute(file_url, temdata[temdata.length - 1]);
                                System.out.println("loop==" + i);
                            }
                        }
                    }
                }
            } catch (final JSONException e) {

                System.out.println("Json parsing error: " + e.getMessage());
                Toast.makeText(Activity_Video.this, "No Internet Connection ", Toast.LENGTH_SHORT).show();

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
            System.out.println("url hit ==" + String.valueOf(fileUrl));
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
                System.out.println("path array addintions==" + String.valueOf(arr_temp));
                newcount++;
                System.out.println("new count==" + newcount + "arraysize=" + arr_video.size());
                if (newcount == arr_video.size()) {
                    videoPlay();
                }

            } else {
                if (ErrorTag.contains("FileNotFoundException")) {
                    Toast.makeText(Activity_Video.this, "File Not Found", Toast.LENGTH_LONG).show();
                }
            }
            //super.onPostExecute(result);
            try {
                pDialog.dismiss();
            } catch (Exception e) {
            }
        }
    }
}
