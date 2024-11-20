package com.pontoapp.pontoapp.dto;

import java.util.Date;

import com.pontoapp.pontoapp.entity.Timesheet;

public class InsertTimesheetDTO {

    private Date dot;

    private Integer timeflag;
    
    private Long user_id;

    public InsertTimesheetDTO(Timesheet timesheet) {
        this.dot = timesheet.getDot();
        this.timeflag = timesheet.getTimeflag();
        this.user_id = timesheet.getUser().getId();
    }

    public Date getDot() {
        return dot;
    }

    public void setDot(Date dot) {
        this.dot = dot;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Integer getTimeflag() {
        return timeflag;
    }

    public void setTimeflag(Integer timeflag) {
        this.timeflag = timeflag;
    }
}
