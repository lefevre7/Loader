package com.example.jeremy.loader;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;


public class MainActivity extends Activity {

    //variables
    private ListView list1;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> stringArray1 = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initializing variables
        list1 = (ListView) findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, stringArray1);
        list1.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //calls the handleCreateFilesClickAsyncTask class
    public void handleCreateFilesClick(View view) throws IOException {

        new handleCreateFilesClickAsyncTask().execute();
    }

    public class handleCreateFilesClickAsyncTask extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {

            try {

                //initializing the progressbar
                ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);

                //setting up path to write to the file
                String filename = "numbers.txt";

                File file = new File(MainActivity.this.getFilesDir(), filename);

                FileOutputStream fos = null;

                fos = new FileOutputStream(file);

                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

                //writing to the file
                for (int i = 1; i < 11; i++) {
                    String k = Integer.toString(i);
                    bw.write(k);
                    bw.newLine();
                    publishProgress(i * 10);//progress parameter (AsyncTask<params, progress, result>)
                    Thread.sleep(250);
                }

                bw.close();

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;

        }
        //update the progress
        @Override
        protected void onProgressUpdate(Integer... progress) {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
            progressBar.setProgress(progress[0]);
        }
    }

    //calls the handleLoadFilesClickAsyncTask class
    public void handleLoadFilesClick(View view) throws IOException, InterruptedException {
        adapter.clear();
        new handleLoadFilesClickAsyncTask().execute();
    }

    public class handleLoadFilesClickAsyncTask extends AsyncTask<ArrayList<String>, Integer, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(ArrayList<String>... strings) {
            try {
                //initializing variables
                ListView list1 = (ListView) findViewById(R.id.listView);

                ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this.getApplicationContext(), android.R.layout.simple_spinner_item, MainActivity.this.stringArray1);

                //setting up path to read the file
                String FilePath = MainActivity.this.getFilesDir() + "/" + "numbers.txt";

                File evensFile = new File(FilePath);

                FileInputStream fis = new FileInputStream(evensFile);

                BufferedReader br = new BufferedReader(new InputStreamReader(fis));

                String line;
                int i = 1;
                //reading the file
                while ((line = br.readLine()) != null) {
                    MainActivity.this.stringArray1.add(line);
                    publishProgress(i++ * 10);//progress parameter (AsyncTask<params, progress, result>)
                    Thread.sleep(250);
                }
                br.close();

            } catch (InterruptedException e1) {
                e1.printStackTrace();
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
          }


            return stringArray1;//returns to onPostExecute

       }
        @Override
        protected void onProgressUpdate(Integer... progress) {
            //update the progress
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
            progressBar.setProgress(progress[0]);
        }

        protected void onPostExecute(ArrayList<String> result) {
           //setting the MainActivity adapter and list
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, result);
            arrayAdapter.notifyDataSetChanged();
            list1.setAdapter(arrayAdapter);
        }
    }

    public void handleStopLoadingFilesClick(View view) {
        //clear list
        list1.setAdapter(null);
        adapter.clear();
        adapter.notifyDataSetChanged();
        list1.setAdapter(adapter);
        //clear progressbar
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setProgress(0);
    }
}