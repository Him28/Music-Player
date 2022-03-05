package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public  class MainActivity extends AppCompatActivity {
//    insilization
    ListView ListView;
//    to store the songs name we need string varilable
    String[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        register listview to type cast
        ListView =(ListView)findViewById(R.id.listView);

//calling a method
        runtimepermission();

    }


//    creating a method
    public void runtimepermission()
    {
        Dexter.withContext(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        displaySong();

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();

                    }
                }).check();

    }


//    LIBERY DEPEDENCY for this filr
    public ArrayList<File> findSong(File file)
    {
        ArrayList<File> arrayList=new ArrayList<>();
        File[] files = file.listFiles();

        for (File singleFile: files)
        {
            if (singleFile.isDirectory()&& !singleFile.isHidden())
            {
                arrayList.addAll(findSong(singleFile));
            }
            else
            {
                if (singleFile.getName().endsWith("mp3")|| singleFile.getName().endsWith(".wav"))
                {
                    arrayList.add(singleFile);
                }
            }

        }
        return arrayList;
    }


//    this method is to fetch the from internal storage
    public void displaySong()
    {
        final ArrayList<File>mysongs = findSong(Environment.getExternalStorageDirectory());
        items = new String[mysongs.size()];
        for (int i=0;i<mysongs.size();i++)
        {
            items[i]=mysongs.get(i).getName().toString().replace(".mp3","").replace(".wav","");
        }
        customAdapter customAdapter=new customAdapter();
        ListView.setAdapter(customAdapter);

//        onclick listern
        ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                for feaching a song
                String songName=(String) ListView.getItemAtPosition(position);

                startActivity(new Intent(getApplicationContext(),PlayerActivity2.class)
                .putExtra("songs",mysongs)
                      .putExtra("songname",songName)
                        .putExtra("pos",position)
                );


            }
        });

    }
//    contact the adapter to listview
    class customAdapter extends BaseAdapter
     {

         @Override
         public int getCount() {
             return items.length;
         }

         @Override
         public Object getItem(int position) {
             return null;
         }

         @Override
         public long getItemId(int position) {
             return 0;
         }

         @Override
         public View getView(int position, View convertView, ViewGroup parent) {
             View view = getLayoutInflater().inflate(R.layout.list_iteam,null);
             TextView txtSong = view.findViewById(R.id.txtSong);
             txtSong.setSelected(true);
             txtSong.setText(items[position]);
             return view ;
         }
     }




}