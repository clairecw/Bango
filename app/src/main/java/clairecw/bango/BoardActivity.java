/**
 * Created by admin on 2/9/18.
 */

package clairecw.bango;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

public class BoardActivity extends AppCompatActivity {
    String gameTitle;
    int gameid;
    Board board = new Board();
    Button[][] buttons = new Button[5][5];
    Button bango;
    String url = "https://raw.githubusercontent.com/clairecw/BANGOBackend/0328879402e91a15f57cb3f59a575959e25a3fc6/TEST/boardDownload.json";
    ProgressDialog dialog;
    SharedPreferences mPrefs;
    boolean exists = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        bango = (Button)findViewById(R.id.buttonCheck);
        final Button button1 = (Button)findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                button1.setBackgroundResource(R.drawable.button);
            }
        });

        Intent intent = getIntent();
        gameid = intent.getIntExtra("gameid", 0);
        System.out.println(gameid);

            StringRequest request = new StringRequest(url, new Response.Listener<String>() {
                @Override
                public void onResponse(String string) {
                    System.out.println(string);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(getApplicationContext(), "Some error occurred!!", Toast.LENGTH_SHORT).show();

                }
            });


            RequestQueue rQueue = Volley.newRequestQueue(BoardActivity.this);
            rQueue.add(request);



        final RequestQueue queue = Volley.newRequestQueue(BoardActivity.this);



        bango.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /*url = "http://aried02.pythonanywhere.com/bango";
                JSONObject params = new JSONObject();
                try {
                    BoardItem[][] b = board.items;
                    HashMap<String, Object>[] tiles = new HashMap[25];
                    for (int i = 0; i < 5; i++) {
                        for (int j = 0; j < 5; j++) {
                            tiles[j] = new HashMap<String, Object>();
                            tiles[j].put("name", b[i][j].name);
                            tiles[j].put("shortname", "" + b[i][j].shortname);
                            tiles[j].put("diff", b[i][j].diff);
                        }
                    }
                    JSONArray arr = new JSONArray();
                    for (int k = 0; k < tiles.length; k++) {
                        JSONObject j = new JSONObject();

                        j.put("marked", tiles[k].get("marked"));
                        j.put("id", tiles[k].get("id"));
                        arr.put(j);
                    }

                    params.put("gameID", gameid);
                    params.put("board", arr);
                    System.out.println(params.toString());

                } catch (JSONException e) {
                    System.out.println("fuck u");
                }*/

                dialog = new ProgressDialog(BoardActivity.this);
                dialog.setMessage("Loading....");
                dialog.show();

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 5s = 5000ms
                        dialog.dismiss();

                        AlertDialog alertDialog = new AlertDialog.Builder(BoardActivity.this).create();
                        alertDialog.setTitle("BANG!");
                        alertDialog.setMessage("Congrats, that's a Bango! Your total winnings for this board were $15.");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();

                    }
                }, 1000);

                //create future request object
                /*RequestFuture future = RequestFuture.newFuture();
                //create JsonObjectRequest
                JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, params, future, future);
                queue.add(jsObjRequest);*/
            }
        });

    }

    void parseJsonData(String jsonString) {
        try {
            JSONObject object = new JSONObject(jsonString);
            for (int i = 0; i < 5; i++) {
                JSONArray barr = object.getJSONArray("row"+i);
                for (int j = 0; j < 5; j++) {
                    JSONObject o = barr.getJSONObject(j);
                    board.items[i][j].shortname = o.getString("shortname");
                    board.items[i][j].name = o.getString("name");
                    board.items[i][j].id = Integer.parseInt(o.getString("id"));

                    String res = "button" + (i * 5 + j + 1);
                    buttons[i][j] = (Button)findViewById(getResId(res, Drawable.class));
                    buttons[i][j].setText(o.getString("name"));

                }
            }

            buttons[2][2].setBackgroundColor(Color.DKGRAY);
/*
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {

                    String res = "button" + (i * 5 + j + 1);
                    buttons[i][j] = (Button)findViewById(getResId(res, Drawable.class));
                    final int a = i;
                    final int b = j;
                    buttons[i][j].setText(board.items[i][j].name);  //setup item titles
                    if (board.items[i][j].marked) buttons[i][j].setEnabled(false);  //set selected
                    buttons[i][j].setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            board.items[a][b].marked = true;
                            buttons[a][b].setEnabled(false);
                        }
                    });
                }

            }*/

        } catch (JSONException e) {
            e.printStackTrace();
        }

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