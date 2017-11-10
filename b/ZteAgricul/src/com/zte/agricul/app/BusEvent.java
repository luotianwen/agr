package com.zte.agricul.app;


/**
 * 基于发布,订阅模式的事件对象
 * 
 * @author wush
 */
public class BusEvent {


	// 选择地区
	public static final BusEvent AREA_SELECT_EVENT = new BusEvent();

	
	public static final BusEvent CROPS_SELECT_EVENT = new BusEvent();
	public static final BusEvent OUTPUT_SELECT_EVENT = new BusEvent();
	public static final BusEvent SIZE_SELECT_EVENT = new BusEvent();
	
	//刷新生长日志数据
	public static final BusEvent REFRESH_GROWTH_LOG_EVENT = new BusEvent();
	
	//刷新地块列表
	public static final BusEvent REFRESH_PLOT_EVENT = new BusEvent();
	
	//添加农作物 或 农作物品牌
	public static final BusEvent REFRESH_ADD_CROPS_DATA_EVENT = new BusEvent();
	
	//刷新删除地块
	public static final BusEvent REFRESH_DELETE_PLOT_EVENT = new BusEvent();
	
	//刷新删除基地
	public static final BusEvent REFRESH_DELETE_BASE_LAND_EVENT = new BusEvent();
	
	//刷新删除基地
	public static final BusEvent REFRESH_ADD_BASE_LAND_EVENT = new BusEvent();
	
	
	//刷新图标列表
	public static final BusEvent REFRESH_CHART_DATA_EVENT = new BusEvent();
	
	
	//刷新地块列表
	public static final BusEvent REFRESH_LOCATION_EVENT = new BusEvent();
	
	
	//删除生长日志
	public static final BusEvent DELETE_LOG_EVENT = new BusEvent();
	
	
	public static final BusEvent EXIT_EVENT = new BusEvent();
	// 传递事件时,可以带上相应的数据
	public Object data;

	public BusEvent() {
	}

}
