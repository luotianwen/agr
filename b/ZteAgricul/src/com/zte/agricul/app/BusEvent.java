package com.zte.agricul.app;


/**
 * ���ڷ���,����ģʽ���¼�����
 * 
 * @author wush
 */
public class BusEvent {


	// ѡ�����
	public static final BusEvent AREA_SELECT_EVENT = new BusEvent();

	
	public static final BusEvent CROPS_SELECT_EVENT = new BusEvent();
	public static final BusEvent OUTPUT_SELECT_EVENT = new BusEvent();
	public static final BusEvent SIZE_SELECT_EVENT = new BusEvent();
	
	//ˢ��������־����
	public static final BusEvent REFRESH_GROWTH_LOG_EVENT = new BusEvent();
	
	//ˢ�µؿ��б�
	public static final BusEvent REFRESH_PLOT_EVENT = new BusEvent();
	
	//���ũ���� �� ũ����Ʒ��
	public static final BusEvent REFRESH_ADD_CROPS_DATA_EVENT = new BusEvent();
	
	//ˢ��ɾ���ؿ�
	public static final BusEvent REFRESH_DELETE_PLOT_EVENT = new BusEvent();
	
	//ˢ��ɾ������
	public static final BusEvent REFRESH_DELETE_BASE_LAND_EVENT = new BusEvent();
	
	//ˢ��ɾ������
	public static final BusEvent REFRESH_ADD_BASE_LAND_EVENT = new BusEvent();
	
	
	//ˢ��ͼ���б�
	public static final BusEvent REFRESH_CHART_DATA_EVENT = new BusEvent();
	
	
	//ˢ�µؿ��б�
	public static final BusEvent REFRESH_LOCATION_EVENT = new BusEvent();
	
	
	//ɾ��������־
	public static final BusEvent DELETE_LOG_EVENT = new BusEvent();
	
	
	public static final BusEvent EXIT_EVENT = new BusEvent();
	// �����¼�ʱ,���Դ�����Ӧ������
	public Object data;

	public BusEvent() {
	}

}
