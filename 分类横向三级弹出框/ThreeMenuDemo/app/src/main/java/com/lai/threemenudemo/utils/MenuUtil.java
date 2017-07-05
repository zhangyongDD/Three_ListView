package com.lai.threemenudemo.utils;

import android.content.Context;
import android.content.res.AssetManager;

import com.lai.threemenudemo.bean.MenuData;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by LaiYingtang on 2016/5/22.
 * 读取menu
 */
public class MenuUtil {
    //封装两个集合
    public static List<MenuData> city1, city2, city3, industry, position, major, stuMajor, practice_salary2, partTimeJob;
    public static HashMap<Integer, ArrayList<MenuData>> citys1, citys2, citys3, industrys, positions,
            majors, stuMajors, practice_salary2s, partTimeJobs;

    //获取单个menu
    public static List<MenuData> getPositions(Context context, int flag, String fileName) {
        if (position == null) {
            initPositions(context, fileName);
        }
        if (flag == 0) {
            return position;
        } else {
            return positions.get(flag);
        }
    }

    //根据fileName初始化单个menu的数据
    private static void initPositions(Context context, String fileName) {
        String industryString = readAssetsTXT(context, fileName);
        String[] strings = industryString.split(";");
        position = new ArrayList<MenuData>();
        positions = new HashMap<Integer, ArrayList<MenuData>>();
        for (int i = 0; i < strings.length; i++) {
            String[] items = strings[i].split(",");
            MenuData menuData = new MenuData();
            menuData.id = Integer.parseInt(items[0].trim());
            menuData.name = items[1];
            menuData.flag = Integer.parseInt(items[2].trim());
            if (menuData.flag == 0) {
                position.add(menuData);//将数据添加到list里面
            } else {
                if (positions.get(menuData.flag) == null) {
                    ArrayList<MenuData> menuDatas = new ArrayList<MenuData>();
                    menuDatas.add(menuData);//再将单个menu存储在ArrayList，后续滑动回来的时候menu还在
                    positions.put(menuData.flag, menuDatas);
                } else {
                    //不为空的情况下直接添加
                    positions.get(menuData.flag).add(menuData);
                }
            }
        }
    }

    private static String readAssetsTXT(Context context, String fileName) {
        try {
            AssetManager assetManager = context.getAssets();//获取assets文件下的资源
            InputStream is = assetManager.open(fileName); //打开
            byte[] bytes = new byte[1024];
            int leng;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while ((leng = is.read(bytes)) != -1) {
                baos.write(bytes, 0, leng);
            }
            return new String(baos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
