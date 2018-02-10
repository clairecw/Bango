package clairecw.bango;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by admin on 2/9/18.
 */

class AsyncT extends AsyncTask<Void,Void,Void> {

    int item = 0;
    boolean bang = false;
    boolean postgame = false;
    boolean postitems = false;
    int gameid = -2;
    String gamename = "";
    Board board;
    Map<String, Object>[][] maps;

    Map<String, Object>[] tiles;

    boolean DEBUG = false;


    public void bang (Board b) {
        bang = true;
        maps = new HashMap[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                maps[i][j] = new HashMap<String, Object>();
                maps[i][j].put("id", b.items[i][j].id);
                maps[i][j].put("marked", b.items[i][j].marked);
            }
        }
        gameid = b.items[0][0].game;
    }

    public void pg (int i, String n) {
        postgame = true;
        gameid = i;
        gamename = n;
        System.out.println("pg");
    }

    public void pi(BoardItem[] wtf, int i) {
        postitems = true;
        gameid = i;
        int len = wtf.length;
        tiles = new HashMap[len];
        for (int j = 0; j < len; j++) {
            tiles[j] = new HashMap<String, Object>();
            tiles[j].put("name", wtf[j].name);
            tiles[j].put("shortname", "" + wtf[j].shortname);
            tiles[j].put("diff", wtf[j].diff);

        }
    }


    @Override
    protected Void doInBackground(Void... params) {

        if (DEBUG) {


                try {
                    if (postgame) {
                        JSONObject jo = new JSONObject();
                        jo.put("name", gamename);
                        jo.put("id", gameid);
                        System.out.println(jo.toString());
                    }

                    if (postitems) {
                        JSONArray arr = new JSONArray();
                        for (int k = 0; k < tiles.length; k++) {
                            JSONObject j = new JSONObject();

                            j.put("name", tiles[k].get("name"));
                            j.put("shortname", tiles[k].get("shortname"));
                            j.put("diff", tiles[k].get("diff"));
                            arr.put(j);
                        }

                        System.out.println("background");
                        JSONObject jo = new JSONObject();
                        jo.put("gameID", gameid);
                        jo.put("items", arr);
                        System.out.println(jo.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }



        } else {

            try {
                URL url = new URL("http://aried02.pythonanywhere.com/postgame"); //Enter URL here
                if (postgame) url = new URL("http://aried02.pythonanywhere.com/postgame");
                if (bang) url = new URL("http://aried02.pythonanywhere.com/checkwin");
                if (postitems) url = new URL("http://aried02.pythonanywhere.com/postitems");


                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod("POST"); // here you are telling that it is a POST request, which can be changed into "PUT", "GET", "DELETE" etc.
                httpURLConnection.setRequestProperty("Content-Type", "application/json"); // here you are setting the `Content-Type` for the data you are sending which is `application/json`
                httpURLConnection.connect();

                if (postgame) {

                    JSONObject jo = new JSONObject();
                    jo.put("name", gamename);
                    jo.put("id", gameid);
                    System.out.println("posting game..");

                    if (!DEBUG) {
                        DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                        wr.writeBytes(jo.toString());
                        System.out.println("written: " + jo.toString());
                        wr.flush();
                        wr.close();
                    }
                } else {

                    if (bang) {
                        JSONObject jo = new JSONObject();
                        jo.put("gameID", gameid);
                        jo.put("board", maps);

                        if (!DEBUG) {
                            DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                            wr.writeBytes(jo.toString());
                            wr.flush();
                            wr.close();
                        }
                    }

                    if (postitems) {
                        JSONObject jo = new JSONObject();
                        jo.put("" + gameid, tiles);

                        if (!DEBUG) {
                            DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                            wr.writeBytes(jo.toString());
                            wr.flush();
                            wr.close();
                        }
                    }
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

            return null;
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }

}
