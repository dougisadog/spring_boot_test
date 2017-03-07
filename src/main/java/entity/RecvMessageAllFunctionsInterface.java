package entity;

public interface RecvMessageAllFunctionsInterface {
	
    public String getMediaId();//媒体资源id
    
    public String getThumbMediaId(); //视频缩略图媒体id
    
    public String getMsgType();//消息类型
    
    public String getUrl();//跳转链接
    
    public String getContent();//文字内容
    
    public String getFormat();//语音格式
    
    public String getRecognition();//语音识别结果UTF8
    
    /**
     * 地图相关4属性
     * @return
     */
    public String getLocationX();
    
    public String getLocationY();
    
    public Integer getScale();
    
    public String getLabel();

}
