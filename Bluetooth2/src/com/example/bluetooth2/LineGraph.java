package com.example.bluetooth2;
import org.achartengine.ChartFactory;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;


public class LineGraph extends Activity  {
	
	public Intent getIntent(Context context)
	{
		Bundle b = this.getIntent().getExtras();
		String data = b.getString("threshold1");
		double y2 = Double.parseDouble(data);
		
		/*Intent intent2 = getIntent();
		String message = intent2.getStringExtra(Threshold.EXTRA_MESSAGE);
		double y2 = Double.parseDouble(message);*/
		
		int[] x = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		int[] y = {30, 34, 45, 57, 77, 89, 100, 111, 123, 145};
		
		//int[] x2 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
				
		TimeSeries series = new TimeSeries("Line1");
		for( int i = 0; i < x.length; i++)
		{
			series.add(x[i],y[i]);
		}
		
		TimeSeries series2 = new TimeSeries("Threshold1");
		for(int i = 0; i < x.length; i++)
		{
			series.add(x[i], y2);
		}
		
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		dataset.addSeries(series);
		dataset.addSeries(series2);
		
		XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
		XYSeriesRenderer renderer = new XYSeriesRenderer();
		XYSeriesRenderer renderer2 = new XYSeriesRenderer();
		mRenderer.addSeriesRenderer(renderer);
		mRenderer.addSeriesRenderer(renderer2);
		
		renderer2.setColor(Color.RED);
		
		Intent intent = ChartFactory.getLineChartIntent(context, dataset, mRenderer, "Data");
		return intent;
	}
}
