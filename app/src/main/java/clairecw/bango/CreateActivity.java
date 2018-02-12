/**
 * Created by admin on 2/10/18.
 */

/**
 * Created by admin on 2/9/18.
 */

package clairecw.bango;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateActivity extends AppCompatActivity {
    String gameTitle;
    int id = 0;
    Board board = new Board();
    String url = "http://aried02.pythonanywhere.com/getnextgameid";
    ProgressDialog dialog;
    int gameid;
    EditText name;
    EditText items;
    List<String> itemnames;

    boolean DEBUG = false;

/*
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

        url = "http://128.237.203.240:5000/getnextgameid";

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading....");
        dialog.show();

        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                parseJsonData(string);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), "Some error occurred!!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(CreateActivity.this);
        rQueue.add(request);
    }

    void parseJsonData(String jsonString) {
        try {
            JSONObject object = new JSONObject(jsonString);
            System.out.println(object.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Integer i = Integer.parseInt(jsonString);
        System.out.println(i);

        gameid = i;

        dialog.dismiss();
    }
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

        name = (EditText)findViewById(R.id.editText3);
        items = (EditText)findViewById(R.id.editText4);

        if(!DEBUG) {
            dialog = new ProgressDialog(this);
            dialog.setMessage("Loading....");
            dialog.show();
        }

        if (!DEBUG) {
            StringRequest request = new StringRequest(url, new Response.Listener<String>() {
                @Override
                public void onResponse(String string) {
                    parseJsonData(string);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(getApplicationContext(), "Some error occurred!!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });


            RequestQueue rQueue = Volley.newRequestQueue(CreateActivity.this);
            rQueue.add(request);
        }
        final RequestQueue queue = Volley.newRequestQueue(CreateActivity.this);

        // get board using id

        Button button = (Button)findViewById(R.id.button7);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                parseNames();
                /*AsyncT asyncT = new AsyncT();
                String nam = name.getText().toString();
                asyncT.pg(gameid, nam);
                asyncT.execute();


                AsyncT at2 = new AsyncT();
                BoardItem[] b = new BoardItem[itemnames.size()];
                for (int i = 0 ; i < itemnames.size(); i++) {
                    b[i] = new BoardItem(i, 0, itemnames.get(i), "", false, gameid);

                }
                at2.pi(b, gameid);
                at2.execute();*/

                url = "http://aried02.pythonanywhere.com/postgame";
                JSONObject params = new JSONObject();
                try {
                    params.put("name", name.getText().toString());
                    params.put("id", gameid);
                } catch (JSONException e) {
                    System.out.println("fuck u");
                }

                //create future request object
                RequestFuture<JSONObject> future = RequestFuture.newFuture();
                //create JsonObjectRequest
                JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, params, future, future);
                queue.add(jsObjRequest);




                url = "http://aried02.pythonanywhere.com/postitems";
                params = new JSONObject();
                try {
                    BoardItem[] b = new BoardItem[itemnames.size()];
                    for (int i = 0 ; i < itemnames.size(); i++) {
                        b[i] = new BoardItem(i, 0, itemnames.get(i), "", false, gameid);

                    }
                    int len = b.length;
                    HashMap<String, Object>[] tiles = new HashMap[len];
                    for (int j = 0; j < len; j++) {
                        tiles[j] = new HashMap<String, Object>();
                        tiles[j].put("name", b[j].name);
                        tiles[j].put("shortname", "" + b[j].shortname);
                        tiles[j].put("diff", b[j].diff);

                    }
                    JSONArray arr = new JSONArray();
                    for (int k = 0; k < tiles.length; k++) {
                        JSONObject j = new JSONObject();

                        j.put("name", tiles[k].get("name"));
                        j.put("shortname", tiles[k].get("shortname"));
                        j.put("diff", tiles[k].get("diff"));
                        arr.put(j);
                    }

                    params.put("gameID", gameid);
                    params.put("items", arr);
                    System.out.println(params.toString());

                } catch (JSONException e) {
                    System.out.println("fuck u");
                }

                //create future request object
                future = RequestFuture.newFuture();
                //create JsonObjectRequest
                jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, params, future, future);
                queue.add(jsObjRequest);

                Intent myIntent = new Intent(CreateActivity.this, MainActivity.class);
                startActivity(myIntent);
            }
        });


    }

    void parseNames() {
        String s = items.getText().toString();
        itemnames = Arrays.asList(s.split("\\s*,\\s*"));
    }

    void parseJsonData(String jsonString) {
        /*try {
            JSONObject object = new JSONObject(jsonString);
            System.out.println(object.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
            Integer i = Integer.parseInt(jsonString);
            System.out.println(i);

            gameid = i;

            dialog.dismiss();

    }



    public static int getResId(String resName, Class<?> c) {

        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}