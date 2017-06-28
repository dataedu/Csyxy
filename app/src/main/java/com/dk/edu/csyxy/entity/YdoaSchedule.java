package com.dk.edu.csyxy.entity;

import java.util.List;

/**
 * Created by cobb on 2017/6/27.
 */

public class YdoaSchedule {


    /**
     * title : badss
     * comment : ddadsfafs
     * startDate : 2017-06-19
     * endDate : 2017-06-25
     * publishDate : 2017-06-24
     * scheduleItems : [{"location":"dssf","host":"we","content":"casd","date":"2017-06-19","organization":"as"},{"location":"33","host":"22","content":"11","date":"2017-06-19","organization":"223"},{"location":"fsadfasd","host":"sdfq","content":"qw","date":"2017-06-20","organization":"waf"},{"location":"asdf","host":"cc","content":"asss","date":"2017-06-21","organization":"asdfasdf"},{"location":"fqwe","host":"dg","content":"dfew","date":"2017-06-22","organization":"qwasd"},{"location":"dsf","host":"ds","content":"wesadf","date":"2017-06-23","organization":"wq"},{"location":"fasdf","host":"ca","content":"adsfadfadf","date":"2017-06-24","organization":"asdf"},{"location":"qweqew","host":"fasd","content":"asd","date":"2017-06-25","organization":"wa"}]
     */

    private String title;
    private String comment;
    private String startDate;
    private String endDate;
    private String publishDate;
    private List<ScheduleItemsBean> scheduleItems;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public List<ScheduleItemsBean> getScheduleItems() {
        return scheduleItems;
    }

    public void setScheduleItems(List<ScheduleItemsBean> scheduleItems) {
        this.scheduleItems = scheduleItems;
    }

    public static class ScheduleItemsBean {
        /**
         * location : dssf
         * host : we
         * content : casd
         * date : 2017-06-19
         * organization : as
         */

        private String location;
        private String host;
        private String content;
        private String date;
        private String organization;

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getOrganization() {
            return organization;
        }

        public void setOrganization(String organization) {
            this.organization = organization;
        }
    }
}
