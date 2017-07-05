package com.addresspicker;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class TextAdapter extends BaseAdapter {

	Context context;
	LayoutInflater inflater;
	
	String [] texts;
	
	private int selectedPosition = -1;   
	
	public void setSelectedPosition(int position) {   
		   selectedPosition = position;   
		}   
	
	public TextAdapter(String[] texts, Context context) {
		this.texts=texts;
		this.context = context;
		inflater=LayoutInflater.from(context);
	}
	
	
	@Override
	public int getCount() {
		
		return texts.length;
	}




	@Override
	public String getItem(int position) {

		return texts[position];
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			convertView = inflater.inflate(R.layout.item_lv_text, parent,
					false);

			ViewHolder holder = new ViewHolder();
			holder.textTv=(TextView) convertView.findViewById(R.id.tv_text_ll);
	
			convertView.setTag(holder);

		}
		ViewHolder holder = (ViewHolder) convertView.getTag();

	     // 设置选中效果
			     if(selectedPosition == position)   
			    {   
			    	 holder.textTv.setTextColor(Color.RED);   

			   } else {   
				   holder.textTv.setTextColor(Color.BLACK);   
			    
			     }   
        holder.textTv.setText(texts[position]);
			 
			    
				return convertView;
	}
	
	
	
	private   class ViewHolder {

		

		public TextView textTv;

		

		

	}
	
	
	
	
	
	
	
	
	
	

}
