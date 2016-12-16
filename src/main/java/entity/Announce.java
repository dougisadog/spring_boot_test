package entity;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class Announce {

    private String titleTaT;
    private String contentTaT;
    private String datelineTaT;


    public String getTitleTaT() {
		return titleTaT;
	}


	public void setTitleTaT(String titleTaT) {
		this.titleTaT = titleTaT;
	}


	public String getContentTaT() {
		return contentTaT;
	}


	public void setContentTaT(String contentTaT) {
		this.contentTaT = contentTaT;
	}


	public String getDatelineTaT() {
		return datelineTaT;
	}


	public void setDatelineTaT(String datelineTaT) {
		this.datelineTaT = datelineTaT;
	}


	@Override
    public String toString() {
        return "Announce [title=" + titleTaT + ", content=" + contentTaT
                + ", dateline=" + datelineTaT + "]";
    }

}
