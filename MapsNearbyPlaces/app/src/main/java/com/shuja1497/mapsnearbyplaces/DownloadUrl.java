package com.shuja1497.mapsnearbyplaces;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.Buffer;
import java.util.InputMismatchException;

/**
 * Created by shuja1497 on 10/31/17.
 */

public class DownloadUrl {

    public String readUrl(String myUrl) throws IOException {

        String data ="";
        InputStream inputStream = null ;
        HttpURLConnection urlConnection = null ;

        try {
            URL url = new URL(myUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            //after connecting .. we will read the data from the url

            inputStream = urlConnection.getInputStream();
            BufferedReader br  = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer sb  = new StringBuffer();

            //storing line by line in the sb

            String line  ="";
            while((line=br.readLine())!= null){
                sb.append(line);
            }

            //converting string buffer to string and storing in the data

            data = sb.toString();
            br.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //even if exception occurs , the finally block will always be executed
        finally{
            inputStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
}
