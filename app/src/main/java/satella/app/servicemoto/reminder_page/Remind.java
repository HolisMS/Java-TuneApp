package satella.app.servicemoto.reminder_page;

import android.view.View;

import java.io.Serializable;

public class Remind implements Serializable {
    private static final long serialVersionUID = 1L;

    private String message;
    private String mAlarmTime;
    private String nAlarmTime;
    private String alarmDate;
    private int deleteIconVisibility;

    public Remind() {
        deleteIconVisibility = View.GONE;
    }

    public String getMessage() {
        return message;
    }

    public String getAlarmTime() {
        return nAlarmTime;
    }

    public String getMAlarmTime(){
        return mAlarmTime;
    }

    public String getAlarmDate() {
        return alarmDate;
    }

    public int getDeleteIconVisibility() {
        return deleteIconVisibility;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public void setAlarmTime(String at) {
        this.mAlarmTime = at;
        if (Integer.parseInt(at.substring(0, at.indexOf(":"))) > 12)
            nAlarmTime = Integer.parseInt(at.substring(0, at.indexOf(":"))) - 12 +
                    at.substring(at.indexOf(":")) + " " + "pm";
        else nAlarmTime = at + " " + "am";
    }

    public void setAlarmDate(String alarmDate) {
        this.alarmDate = alarmDate;
    }

    public void setDeleteIconVisibility(int deleteIconVisibility) {
        this.deleteIconVisibility = deleteIconVisibility;
    }
}
