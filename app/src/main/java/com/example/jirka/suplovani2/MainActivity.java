package com.example.jirka.suplovani2;

//import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import android.widget.Spinner;



import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    //ACA = základní klasa kkterá používá prvjy podpůrné knihovny action baru, implementace do androidu,
    //extends ACA znamená že MA dedí knihovny ACA

    SharedPreferences mSharedPreferences;


    private String trida="";
    public String getTrida() {
        return trida;
    }
    public void setTrida(String value) {
        this.trida= value;
    }
    private String htmlPageUrl = "https://gjk.cz/o-studiu/rozvrh-a-suplovani/suplovani/";
    public String getHtmlPageUrl() {
        return htmlPageUrl;
    }
    public void setHtmlPageUrl(String value) {
        this.htmlPageUrl= value;
    }

    private TextView HtmlPage;

    /*public void btnSettings_onClick (View view) {
        Intent intent=new Intent(this,SettingsActivity.class);
        startActivity(intent);
    } */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Button getBtn;
        Button getBtn2;
        final Spinner Spin;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Spin = findViewById(R.id.spinner);
        Spin.post(new Runnable() {
            public void run() {
                Spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String spinnertext = parent.getItemAtPosition(position).toString();
                        //setTrida(spinnertext); //pozor na duplikaci, mozna mezera na zacatku?
                        setTitle("Suplování pro "+spinnertext);
                        setTrida(spinnertext);
                        getWebsite();
                        SharedPreferences.Editor editor = getSharedPreferences("PrefTrida", MODE_PRIVATE).edit();
                        editor.putString("Tridy", spinnertext);
                        editor.apply();


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }
        });




        mSharedPreferences = getSharedPreferences("PrefTrida", Context.MODE_PRIVATE);
        //String tridaSP = mSharedPreferences.getString("Trida", "R1.A");
        //setTrida(tridaSP);
        HtmlPage = findViewById(R.id.htmlPage);
        HtmlPage.setMovementMethod(new ScrollingMovementMethod());
        getBtn = findViewById(R.id.button);
        getBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               setHtmlPageUrl("http://178.248.252.60/~xhanj02/suplobec.htm");
               getWebsite();
           }
        });
        getBtn2 = findViewById(R.id.button2);
        getBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setHtmlPageUrl("https://gjk.cz/o-studiu/rozvrh-a-suplovani/suplovani/");
                getWebsite();
            }
        });

        getWebsite();
        setTitle("Suplování pro "+gettrida());
    }
    /*protected void storetrida(String valuetr){
        SharedPreferences.Editor editor = getSharedPreferences("PrefTrida", MODE_PRIVATE).edit();
        editor.putString("Tridy", valuetr);
        editor.apply();
    }*/

    protected String gettrida(){
        mSharedPreferences = getSharedPreferences("PrefTrida", MODE_PRIVATE);
        return mSharedPreferences.getString("Tridy", "R1.A");
    }


// Instance= objekt, atrubuty=vlastnosti objektů ve třídě
// statické metody=metody třídy, přístupné i bez existujícího objektu
// instanční m= metody instance, přístup pouze přes existující objekt

    public void getWebsite() {                  // void= nova metoda (stejna jako fce ale vztahuje se k objektu ve třídě)
        new Thread(new Runnable() {             // Thready jsou objekty ve třídě co vypracovávají kód (beží)
            @Override                           // Overriduje vlastnosti předchozí classy a umožňuje přístup do nadřazené classy
            public void run() {
                final StringBuilder builder = new StringBuilder();  //final = může být určena pouze jednou

                try {                           // Block try/catch je vyjímka, kod funguje v try dokud ne narazí vyjímku kterou vyhodí v catch (v podstatě if/else)
                    //String htmlPageUrl = "http://178.248.252.60/~xhanj02/suplobec.htm" ;
                    //String htmlPageUrl = "https://gjk.cz/o-studiu/rozvrh-a-suplovani/suplovani/";
                    Document htmlPage = Jsoup.connect(getHtmlPageUrl()).get();
                    String delim = gettrida()+"</p></td>";
                    Element table = htmlPage.getElementsByClass("tb_supltrid_3").get(0);
                    String htmlString = table.toString();
                    String[] htmlStringA =htmlString.split(delim);
                    String[] htmlStringB =htmlStringA[1].split("<p> ");
                    String htmlStringC= htmlStringB[0].replaceAll("</td>|</tr>|</tbody>|</table>|<td class.{1,40}\">|<td>|<p>", "");
                    String htmlStringD= htmlStringC.replaceAll("&nbsp;", "");
                    String htmlStringE= htmlStringD.replaceAll( " {2,}", "");
                    String htmlStringF= htmlStringE.replaceAll("\\s", " ");
                    String htmlStringG= htmlStringF.replaceAll(" {2}", "");
                    String htmlStringH= htmlStringG.replaceAll(" *<tr> *", "\n");
                    String htmlStringI= htmlStringH.replaceAll(" *</p> *", " ");
                    builder.append("Následující pracovní den:\n\n").append(htmlStringI);//po tomhle se to akorát zopakuje pro každý den- get(1,2)

                    Element table1 = htmlPage.getElementsByClass("tb_supltrid_3").get(1);
                    String htmlString1 = table1.toString();
                    String[] htmlStringA1 =htmlString1.split(delim);
                    String[] htmlStringB1 =htmlStringA1[1].split("<p> ");
                    String htmlStringC1= htmlStringB1[0].replaceAll("</td>|</tr>|</tbody>|</table>|<td class.{1,40}\">|<td>|<p>", "");
                    String htmlStringD1= htmlStringC1.replaceAll("&nbsp;", "");
                    String htmlStringE1= htmlStringD1.replaceAll( " {2,}", "");
                    String htmlStringF1= htmlStringE1.replaceAll("\\s", " ");
                    String htmlStringG1= htmlStringF1.replaceAll(" {2}", "");
                    String htmlStringH1= htmlStringG1.replaceAll(" *<tr> *", "\n");
                    String htmlStringI1= htmlStringH1.replaceAll(" *</p> *", " ");
                    builder.append("\n\nPo něm následující pracovní den:\n\n").append(htmlStringI1);

                    Element table2 = htmlPage.getElementsByClass("tb_supltrid_3").get(2);
                    String htmlString2 = table2.toString();
                    String[] htmlStringA2 =htmlString2.split(delim);
                    String[] htmlStringB2 =htmlStringA2[1].split("<p> ");
                    String htmlStringC2= htmlStringB2[0].replaceAll("</td>|</tr>|</tbody>|</table>|<td class.{1,40}\">|<td>|<p>", "");
                    String htmlStringD2= htmlStringC2.replaceAll("&nbsp;", "");
                    String htmlStringE2= htmlStringD2.replaceAll( " {2,}", "");
                    String htmlStringF2= htmlStringE2.replaceAll("\\s", " ");
                    String htmlStringG2= htmlStringF2.replaceAll(" {2}", "");
                    String htmlStringH2= htmlStringG2.replaceAll(" *<tr> *", "\n");
                    String htmlStringI2= htmlStringH2.replaceAll(" *</p> *", " ");
                    builder.append("\n\nPo něm následující pracovní den:\n\n").append(htmlStringI2);

                } catch (IOException | IndexOutOfBoundsException e) {
                    builder.append("\n\n").append("Suplování na následující dny v tuto chvíli není")/*.append(e.getMessage())*/.append("\n");
                }


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        HtmlPage.setText(builder.toString());
                    }
                });
            }
        }).start();
    }
}