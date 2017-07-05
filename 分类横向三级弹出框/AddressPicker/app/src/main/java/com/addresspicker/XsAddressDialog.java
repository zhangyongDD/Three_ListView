package com.addresspicker;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by KID on 2016/12/20.
 */
public class XsAddressDialog extends AlertDialog{

    private Context context;
    /**
     * 把全国的省市区的信息以json的格式保存，解析完成后赋值为null
     */
    private JSONObject mJsonObj;
    //省的listView
    private MyListView listView1;
    private TextAdapter mAdapter1;
    //市的listView
    private MyListView listView2;
    private TextAdapter mAdapter2;
    //区的listView
    private MyListView listView3;
    private TextAdapter mAdapter3;

    /**
     * 所有省
     */
    private String[] mProvinceDatas;
    /**
     * key - 省 value - 市s
     */
    private Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
    /**
     * key - 市 values - 区s
     */
    private Map<String, String[]> mAreaDatasMap = new HashMap<String, String[]>();
    //当前的citys
    private String []citys;
    //当前的areas
    private String []areas;

    //当前省市区的选中位置
    public int provincePosition;
    public int cityPosition;
    public int areasPosition;
    //最终选中的省,市，区
    private String province;
    private String city;
    private String area;

    public XsAddressDialog(Context context, int themeResId) {
        super(context, R.style.mycustom_dialog);
        this.context = context;

    }
    private TextBack textBack;
    public interface TextBack{
        void textback(String province, String city, String area);
    }

    public void setTextBackListener(TextBack textBack) {
        this.textBack=textBack;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_address, null);
        ViewGroup.LayoutParams layoutParams =new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setContentView(view,layoutParams);

        //-------------------------------------------------------------------
        initJsonData();
        initDatas();

        listView1= (MyListView) view.findViewById(R.id.listview1);
        listView2=(MyListView) view.findViewById(R.id.listview2);
        listView3=(MyListView) view.findViewById(R.id.listview3);


        mAdapter1=new TextAdapter(mProvinceDatas,context);
        listView1.setAdapter(mAdapter1);
        //设置city的默认值
        selectCityDefult();
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


                provincePosition = position;
                mAdapter1.setSelectedPosition(provincePosition);
                mAdapter1.notifyDataSetInvalidated();

                citys = mCitisDatasMap.get(mProvinceDatas[provincePosition]);

                mAdapter2 = new TextAdapter(citys, context);

                listView2.setAdapter(mAdapter2);
                mAdapter2.setSelectedPosition(0);

                listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        cityPosition = position;
                        mAdapter2.setSelectedPosition(cityPosition);
                        mAdapter2.notifyDataSetInvalidated();

                        areas = mAreaDatasMap.get(citys[cityPosition]);
                        mAdapter3 = new TextAdapter(areas, context);
                        mAdapter3.setSelectedPosition(0);
                        listView3.setAdapter(mAdapter3);
                    }
                });
                areas = mAreaDatasMap.get(citys[0]);
                mAdapter3 = new TextAdapter(areas, context);
                mAdapter3.setSelectedPosition(0);
                listView3.setAdapter(mAdapter3);

            }
        });

    }
    private void selectCityDefult() {
        //-------------------------------------------------------------------
        provincePosition=0;
        mAdapter1.setSelectedPosition(0);
        mAdapter1.notifyDataSetInvalidated();

        citys=mCitisDatasMap.get(mProvinceDatas[0]);

        cityPosition=0;
        mAdapter2=new TextAdapter(citys,context);
        mAdapter2.setSelectedPosition(0);
        listView2.setAdapter(mAdapter2);

        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                cityPosition = position;
                mAdapter2.setSelectedPosition(cityPosition);
                mAdapter2.notifyDataSetInvalidated();

                areas = mAreaDatasMap.get(citys[cityPosition]);
                mAdapter3 = new TextAdapter(areas,context);

                listView3.setAdapter(mAdapter3);
            }
        });
        areasPosition=0;
        areas =mAreaDatasMap.get(citys[0]);
        mAdapter3 =new TextAdapter(areas,context);
        mAdapter3.setSelectedPosition(0);
        listView3.setAdapter(mAdapter3);

        listView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view, int position, long id) {
                areasPosition=position;
                province=mProvinceDatas[position];
                mAdapter3.setSelectedPosition(areasPosition);
                mAdapter3.notifyDataSetInvalidated();
                //position为最后一个数组的position
//                Toast.makeText(context,
//                        "provincePosition=" + provincePosition + "cityPosition=" + cityPosition + "areasPosition=" + areasPosition,
//                        Toast.LENGTH_LONG).show();
                        province=mProvinceDatas[provincePosition];
                        city =mCitisDatasMap.get(province)[cityPosition];
                        area =mAreaDatasMap.get(city)[areasPosition];

                 textBack.textback(province,city,area);
            }
        });
    }


    /**
     * 解析整个Json对象，完成后释放Json对象的内存
     */
    private void initDatas()
    {
        try
        {
            JSONArray jsonArray = mJsonObj.getJSONArray("citylist");
            mProvinceDatas = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonP = jsonArray.getJSONObject(i);// 每个省的json对象
                String province = jsonP.getString("p");// 省名字

                mProvinceDatas[i] = province;

                JSONArray jsonCs = null;
                try
                {
                    /**
                     * Throws JSONException if the mapping doesn't exist or is
                     * not a JSONArray.
                     */
                    jsonCs = jsonP.getJSONArray("c");
                } catch (Exception e1)
                {
                    continue;
                }
                String[] mCitiesDatas = new String[jsonCs.length()];
                for (int j = 0; j < jsonCs.length(); j++)
                {
                    JSONObject jsonCity = jsonCs.getJSONObject(j);
                    String city = jsonCity.getString("n");// 市名字
                    mCitiesDatas[j] = city;
                    JSONArray jsonAreas = null;
                    try
                    {
                        /**
                         * Throws JSONException if the mapping doesn't exist or
                         * is not a JSONArray.
                         */
                        jsonAreas = jsonCity.getJSONArray("a");
                    } catch (Exception e)
                    {
                        continue;
                    }

                    String[] mAreasDatas = new String[jsonAreas.length()];// 当前市的所有区
                    for (int k = 0; k < jsonAreas.length(); k++)
                    {
                        String area = jsonAreas.getJSONObject(k).getString("s");// 区域的名称
                        mAreasDatas[k] = area;
                    }
                    mAreaDatasMap.put(city, mAreasDatas);
                }

                mCitisDatasMap.put(province, mCitiesDatas);
            }

        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        mJsonObj = null;
    }

    /**
     * 从assert文件夹中读取省市区的json文件，然后转化为json对象
     */
    private void initJsonData()
    {
        try
        {
            StringBuffer sb = new StringBuffer();
            InputStream is = context.getAssets().open("city.json");
            int len = -1;
            byte[] buf = new byte[1024];
            while ((len = is.read(buf)) != -1)
            {
                sb.append(new String(buf, 0, len, "utf-8"));
            }
            is.close();
            mJsonObj = new JSONObject(sb.toString());
        } catch (IOException e)
        {
            e.printStackTrace();
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

}
