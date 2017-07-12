package com.dk.mp.csyxy.entity;

public class YdOaEntity {

	  private int id;//拖拽界面需要唯一标示

	    private String name;//业务名称
	    private String label;//业务中文名
	    private String count;//该模块待办条数
	    private String url;//模块列表入口
	    private String title;//主标题
	    private String detailUrl;//详情入口
	    private String bussessName;//所属业务
	    private String diy;//是否在自定义首页
	    private String identity;

	    private boolean isShow;//是否展示在首页

	    private boolean selected = false;//被选中添加

	    public int getId() {
	        return id;
	    }

	    public void setId(int id) {
	        this.id = id;
	    }

	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }

	    public String getLabel() {
	        return label;
	    }

	    public void setLabel(String label) {
	        this.label = label;
	    }

	    public String getCount() {
	        return count;
	    }

	    public void setCount(String count) {
	        this.count = count;
	    }

	    public String getUrl() {
	        return url;
	    }

	    public void setUrl(String url) {
	        this.url = url;
	    }

	    public String getTitle() {
	        return title;
	    }

	    public void setTitle(String title) {
	        this.title = title;
	    }

	    public String getDetailUrl() {
	        return detailUrl;
	    }

	    public void setDetailUrl(String detailUrl) {
	        this.detailUrl = detailUrl;
	    }

	    public String getBussessName() {
	        return bussessName;
	    }

	    public void setBussessName(String bussessName) {
	        this.bussessName = bussessName;
	    }

	    public String getDiy() {
	        return diy;
	    }

	    public void setDiy(String diy) {
	        this.diy = diy;
	    }

	    public boolean isShow() {
	        return isShow;
	    }

	    public void setShow(boolean show) {
	        isShow = show;
	    }

	    public String getIdentity() {
	        return identity;
	    }

	    public void setIdentity(String identity) {
	        this.identity = identity;
	    }

	    public boolean isSelected() {
	        return selected;
	    }

	    public void setSelected(boolean selected) {
	        this.selected = selected;
	    }
}
